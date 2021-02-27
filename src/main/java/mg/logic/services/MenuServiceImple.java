package mg.logic.services;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import mg.boundaries.MenuBoundary;
import mg.boundaries.RecipeBoundary;
import mg.data.converters.MenuConverter;
import mg.data.dal.MenuDataAccessLayer;
import mg.data.dal.UserIngredientDataAccessRepo;
import mg.data.entities.IngredientEntity;
import mg.data.entities.IngredientTypeEnum;
import mg.data.entities.MenuEntity;
import mg.data.entities.joinentities.MenuRecipe;
import mg.logic.MenuRecipeService;
import mg.logic.MenuService;
import mg.logic.RecipeService;

@Service
@NoArgsConstructor
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class MenuServiceImple implements MenuService {
	private MenuDataAccessLayer menuDAL;
	private UserIngredientDataAccessRepo userIngrdientsDAL;
	private RecipeService recipeService;
	private MenuRecipeService menuRecipeService;
	private MenuConverter menuConverter;

	@Override
	@Transactional
	public MenuBoundary createMenu(String userEmail, int days) {
		// create menu in DB
		MenuEntity menu = new MenuEntity();
		menu.setTimestamp(new Date());

		// get All user FORBIDDEN ingredients
		List<IngredientEntity> uiArr = userIngrdientsDAL
				.findAllByUser_EmailAndType(userEmail, IngredientTypeEnum.FORBIDDEN.name(), PageRequest.of(0, 1000))
				.stream().map(ui -> ui.getIngredient()).collect(Collectors.toList());

		// get all appropriate recipes
		List<RecipeBoundary> recipeEnityties = recipeService.getRecipeIWIthngredientNotIn(uiArr);

		for (int i = 0; i < days; i++) {
//			RecipeBoundary recipe = recipeEnityties.remove(new Random().nextInt(recipeEnityties.size())); 
			// for dev only
			// TODO remove
			RecipeBoundary recipe = recipeEnityties.get(new Random().nextInt(recipeEnityties.size()));
			menuRecipeService.create(menu.getId(), recipe.getRecipeId());
		}
		
		MenuBoundary rv = this.menuConverter.toBoundary(menu);
		rv.setRecipes(this.menuRecipeService.getAllForMenu(menu.getId()));
		
		menu = menuDAL.save(menu);
		return rv;
	}

	

	@Override
	public MenuBoundary[] getAll(String userEmail, int page, int size) {
		List<MenuBoundary> lst = this.menuDAL.findAll(PageRequest.of(page, size)).stream()
				.map(this.menuConverter::toBoundary).collect(Collectors.toList());
		return linkRecipesAndConvertToArr(lst);
	}



	@Override
	public MenuBoundary buildMenu(String userEmail, Long[] recipeId) {
		MenuEntity menu = new MenuEntity();
		menu.setTimestamp(new Date());
		menu = this.menuDAL.save(menu);
		List<RecipeBoundary> recipes = new ArrayList<>();
		List<MenuRecipe> menuRecipes = new ArrayList<>();
		for (int i = 0; i < recipeId.length; i++) {
			
			recipes.add(this.recipeService.getById(recipeId[i]).getData());
			menuRecipes.add(this.menuRecipeService.create(menu.getId(), recipes.get(i).getRecipeId()));
		}
		menu.setMenuRecipes(new HashSet<>(menuRecipes));
		menuDAL.save(menu);
		
		MenuBoundary rv = this.menuConverter.toBoundary(menu);
		rv.setRecipes(this.menuRecipeService.getAllForMenu(menu.getId()));
		return rv;
	}



	@Override
	public MenuBoundary[] searchMenu(Date fromDate, Date toDate) {
		List<MenuBoundary> entities = this.menuDAL.findAllByTimestampBetween(fromDate,toDate)
				.stream()
				.map(this.menuConverter::toBoundary)
				.collect(Collectors.toList());
		return linkRecipesAndConvertToArr(entities);
	}
	


	private MenuBoundary[] linkRecipesAndConvertToArr(List<MenuBoundary> lst) {
		lst.forEach(m -> m.setRecipes(this.menuRecipeService.getAllForMenu(m.getId())));
		return lst.toArray(new MenuBoundary[0]);
	}
}
