package mg.logic;

import java.util.List;

import mg.boundaries.RecipeBoundary;
import mg.boundaries.Response;
import mg.data.entities.IngredientEntity;

public interface RecipeService {

	Response<RecipeBoundary> create(RecipeBoundary recipe);

	Response<RecipeBoundary[]> getAll(int page, int size);

	Response<RecipeBoundary[]> getByName(String name, int page, int size);

	List<RecipeBoundary> getRecipeIWIthngredientNotIn(List<IngredientEntity> uiArr);

	RecipeBoundary getById(Long rId);

}
