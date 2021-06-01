package mg.logic.services;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import mg.boundaries.MenuBoundary;
import mg.boundaries.RecipeBoundary;
import mg.boundaries.helper.MenuFeedbackEnum;
import mg.data.converters.MenuConverter;
import mg.data.dal.MenuDataAccessLayer;
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
	private RecipeService recipeService;
	private MenuRecipeService menuRecipeService;
	private MenuConverter menuConverter;

	@Override
	public MenuBoundary[] getAll(int page, int size) {
		List<MenuBoundary> lst = this.menuDAL.findAll(PageRequest.of(page, size)).stream()
				.map(this.menuConverter::toBoundary).collect(Collectors.toList());
		return linkRecipesAndConvertToArr(lst);
	}

	@Override
	public MenuBoundary buildMenu(String userEmail, Long[] recipeId) {
		MenuEntity menu = new MenuEntity();
		menu.setUserEmail(userEmail);
		menu.setTimestamp(new Date());
		menu = this.menuDAL.save(menu);
		List<RecipeBoundary> recipes = new ArrayList<>();
		List<MenuRecipe> menuRecipes = new ArrayList<>();
		for (int i = 0; i < recipeId.length; i++) {

			recipes.add(this.recipeService.getById(recipeId[i]));
			menuRecipes.add(this.menuRecipeService.create(menu.getId(), recipes.get(i).getId()));
		}
//		menu.setMenuRecipes(new HashSet<>(menuRecipes));
		menuDAL.save(menu);
		recipes.forEach(r -> this.recipeService.feedbackRecipe(r.getId(), userEmail, MenuFeedbackEnum.GOOD));
		MenuBoundary rv = this.menuConverter.toBoundary(menu);
		rv.setRecipes(this.menuRecipeService.getAllForMenu(menu.getId()));
		return rv;
	}

	@Override
	public MenuBoundary[] searchMenu(Date fromDate, Date toDate) {
		List<MenuBoundary> entities = this.menuDAL.findAllByTimestampBetween(fromDate, toDate).stream()
				.map(this.menuConverter::toBoundary).collect(Collectors.toList());
		return linkRecipesAndConvertToArr(entities);
	}

	private MenuBoundary[] linkRecipesAndConvertToArr(List<MenuBoundary> lst) {
		lst.forEach(m -> m.setRecipes(this.menuRecipeService.getAllForMenu(m.getId())));
		return lst.toArray(new MenuBoundary[0]);
	}

	@Override
	public MenuBoundary[] getAllForUser(String userEmail, int page, int size) {
		List<MenuBoundary> entities = this.menuDAL.findAllByUserEmail(userEmail, PageRequest.of(page, size)).stream()
				.map(this.menuConverter::toBoundary).collect(Collectors.toList());
		return linkRecipesAndConvertToArr(entities);
	}
}
