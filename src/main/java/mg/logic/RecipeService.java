package mg.logic;

import java.util.List;

import mg.boundaries.RecipeBoundary;
import mg.boundaries.Response;
import mg.boundaries.helper.MenuFeedbackEnum;
import mg.data.entities.IngredientEntity;

public interface RecipeService {

	public Response<RecipeBoundary> create(RecipeBoundary recipe);

	public Response<RecipeBoundary[]> getAll(int page, int size);

	public Response<RecipeBoundary[]> getByTitle(String name, int page, int size);

	public List<RecipeBoundary> getAllRecipesWithIngredientNotIn(List<IngredientEntity> uiArr);

	public RecipeBoundary getById(Long rId);

	public RecipeBoundary[] getAllBestRecipesForUser(String userEmail);

	public void feedbackRecipe(long recipeId, String userEmail, MenuFeedbackEnum feedback);

}
