package mg.logic;

import java.util.List;

import mg.boundaries.IngredientBoundary;
import mg.boundaries.RecipeBoundary;
import mg.boundaries.Response;
import mg.boundaries.helper.MenuFeedbackEnum;
import mg.data.entities.IngredientEntity;

public interface RecipeService {

	public Response<RecipeBoundary> create(RecipeBoundary recipe);

	public Response<RecipeBoundary[]> getAll(String userEmail, int page, int size);

	public Response<RecipeBoundary[]> getByTitle(String userEmail, String name, int page, int size);

	public List<RecipeBoundary> getRecipesWithIngredientNotIn(List<IngredientEntity> uiArr,int count);

	public RecipeBoundary getById(Long rId);

	public RecipeBoundary[] getBestRecipesForUser(String userEmail, int count);

	public void feedbackRecipe(IngredientBoundary[] recipeId, String userEmail, MenuFeedbackEnum feedback);

}
