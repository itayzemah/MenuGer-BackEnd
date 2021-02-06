package mg.logic.services;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import mg.boundaries.MenuBoundary;
import mg.data.converters.MenuConverter;
import mg.data.dal.IngredientDataAccessRepo;
import mg.data.dal.MenuDataAccessLayer;
import mg.data.dal.MenuRecipeDataAccessRepo;
import mg.data.dal.RecipeDataAccessLayerRepo;
import mg.data.dal.UserIngredientDataAccessRepo;
import mg.data.entities.IngredientEntity;
import mg.data.entities.IngredientTypeEnum;
import mg.data.entities.MenuEntity;
import mg.data.entities.RecipeEntity;
import mg.data.entities.joinentities.MenuRecipe;
import mg.data.entities.joinentities.MenuRecipeId;
import mg.logic.MenuRecipeService;
import mg.logic.MenuService;

@Service
@NoArgsConstructor
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class MenuServiceImple implements MenuService {
	private MenuRecipeDataAccessRepo menuRecipeDAL;
	private MenuDataAccessLayer menuDAL;
	private RecipeDataAccessLayerRepo recipeDAL;
	private IngredientDataAccessRepo ingrdientsDAL;
	private UserIngredientDataAccessRepo userIngrdientsDAL;
	private MenuRecipeService menuRecipeService;
	private MenuConverter menuConverter;
	@Override
	@Transactional
	public MenuBoundary createMenu(String userEmail, int days) {
		//create menu in DB
		MenuEntity menu = new MenuEntity();
		menu.setTimestamp(new Date());
		menu = menuDAL.save(menu);
		
		// get All user FORBIDDEN ingredients
		List<IngredientEntity> uiArr = userIngrdientsDAL
				.findAllByUser_EmailAndType(userEmail, IngredientTypeEnum.FORBIDDEN.name(), PageRequest.of(0, 1000))
				.stream().map(ui -> ui.getIngredient())			
				.collect(Collectors.toList());
		
		// get all appropriate recipes
		List<RecipeEntity> recipeEnityties = recipeDAL.findDistinctByRecipeIngredients_IngredientNotIn(uiArr);
		
		for (int i = 0; i < days; i++) {
//			RecipeEntity recipe =recipeEnityties.remove(new Random().nextInt(recipeEnityties.size())); 
			//for dev only
			//TODO remove
			RecipeEntity recipe =recipeEnityties.get(new Random().nextInt(recipeEnityties.size())); 
			createMenuRecipe(menu.getId(), recipe.getRecipeId());
		}
		MenuBoundary rv = this.menuConverter.toBoundary(menu);
		rv.setRecipes(this.menuRecipeService.getAllForMenu(menu.getId()));
		return rv;
	}

	private MenuRecipe createMenuRecipe(Long menuId, long recipeId) {
		MenuRecipe mr = new MenuRecipe();
		mr.setMenu(this.menuDAL.findById(menuId).orElseThrow(() -> new RuntimeException("Error on Menu creation")));
		mr.setRecipe(this.recipeDAL.findById(recipeId).orElseThrow(() -> new RuntimeException("Error on Menu creation")));
		mr.setId(new MenuRecipeId(recipeId, menuId));
		return menuRecipeDAL.save(mr);
	}

	@Override
	public MenuBoundary[] searchMenu(String userEmail, int page, int size) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public MenuBoundary[] buildMenu(String userEmail, Long[] recipeId) {
		// TODO Auto-generated method stub
		return null;
	}
}
