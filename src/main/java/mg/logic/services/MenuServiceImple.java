package mg.logic.services;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import mg.boundaries.IngredientBoundary;
import mg.boundaries.MenuBoundary;
import mg.boundaries.RecipeBoundary;
import mg.boundaries.helper.MenuFeedbackEnum;
import mg.boundaries.helper.RecipeWithRate;
import mg.boundaries.helper.RecipeWithRateComparator;
import mg.data.converters.IngredientConverter;
import mg.data.converters.MenuConverter;
import mg.data.dal.MenuDataAccessLayer;
import mg.data.entities.IngredientEntity;
import mg.data.entities.IngredientTypeEnum;
import mg.data.entities.MenuEntity;
import mg.data.entities.joinentities.MenuRecipe;
import mg.data.entities.joinentities.UserIngredient;
import mg.logic.MenuRecipeService;
import mg.logic.MenuService;
import mg.logic.RecipeService;
import mg.logic.UserIngredientService;
import mg.logic.UserService;
import mg.logic.exceptions.MenuNotFoundExcetion;

@Service
@NoArgsConstructor
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class MenuServiceImple implements MenuService {
	private MenuDataAccessLayer menuDAL;
	private UserIngredientService userIngrdientsService;
	private IngredientConverter ingredientConverter;
	private RecipeService recipeService;
	private MenuRecipeService menuRecipeService;
	private MenuConverter menuConverter;

	@Transactional
	public MenuBoundary createMenu(String userEmail, int numOfRecipes) {
		// create menu in DB
		MenuEntity menu = new MenuEntity();
		menu.setTimestamp(new Date());

		// get menu id
		menu = menuDAL.save(menu);

		IngredientBoundary[] allForbiddenIngredients = userIngrdientsService
				.getAllByType(userEmail, IngredientTypeEnum.FORBIDDEN.name(), 1000, 0).getData();

		// get All user FORBIDDEN ingredients
		List<IngredientEntity> forbiddenUserIngredients = new ArrayList<IngredientBoundary>(
				Arrays.asList(allForbiddenIngredients)).stream().map(this.ingredientConverter::fromBoundary)
						.collect(Collectors.toList());
		// get All user PREFERRED ingredients
		// TODO change to get user ingredients
		List<UserIngredient> preferredUserIngredients = userIngrdientsService
				.getAllUserIngredientByType(userEmail, IngredientTypeEnum.PREFERRED.name(), 1000, 0).getData();

		// get all appropriate recipes
		List<RecipeWithRate> recipesWithRate = getListOfRatedRecipes(preferredUserIngredients,
				recipeService.getAllRecipesWithIngredientNotIn(forbiddenUserIngredients));

		recipesWithRate.sort(Comparator.comparingDouble(RecipeWithRate::getRate).reversed());

		for (int i = 0; i < numOfRecipes; i++) {
//			RecipeBoundary recipe = recipeEnityties.remove(new Random().nextInt(recipeEnityties.size())); 
			// for dev only
			// TODO remove
			RecipeBoundary recipe = recipesWithRate.get(new Random().nextInt(recipesWithRate.size()));
			menuRecipeService.create(menu.getId(), recipe.getId());
		}

		MenuBoundary rv = this.menuConverter.toBoundary(menu);
		rv.setRecipes(this.menuRecipeService.getAllForMenu(menu.getId()));

		menu = menuDAL.save(menu);
		return rv;
	}

	private List<RecipeWithRate> getListOfRatedRecipes(List<UserIngredient> preferredUserIngredients,
			List<RecipeBoundary> allAllowRecipes) {
		ArrayList<RecipeWithRate> rv = new ArrayList<RecipeWithRate>();

		allAllowRecipes.forEach(recipe -> rv.add(calcRate(recipe, preferredUserIngredients)));

		return rv;
	}

	private RecipeWithRate calcRate(RecipeBoundary recipe, List<UserIngredient> preferredUserIngredients) {
		return new RecipeWithRate(recipe, preferredUserIngredients);
	}

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
