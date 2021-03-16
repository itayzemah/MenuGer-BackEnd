package mg.logic;


import mg.boundaries.RecipeBoundary;
import mg.boundaries.Response;

public interface RecipeService {

	public Response<RecipeBoundary> create(RecipeBoundary recipe);

	public Response<RecipeBoundary[]> getAll(int page, int size);

	public Response<RecipeBoundary[]> getByName(String name, int page, int size);

	public Response<RecipeBoundary> getById(Long rId);

	public Response<RecipeBoundary[]> getRecipeWIthoutForbIngredients(String userEmail);

}
