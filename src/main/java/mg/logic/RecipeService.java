package mg.logic;

import java.util.List;

import mg.boundaries.RecipeBoundary;
import mg.boundaries.Response;
import mg.data.entities.IngredientEntity;

public interface RecipeService {

	public Response<RecipeBoundary> create(RecipeBoundary recipe);

	public Response<RecipeBoundary[]> getAll(int page, int size);

	public Response<RecipeBoundary[]> getByName(String name, int page, int size);

	public List<RecipeBoundary> getRecipeIWIthngredientNotIn(List<IngredientEntity> uiArr);

	public Response<RecipeBoundary> getById(Long rId);

}
