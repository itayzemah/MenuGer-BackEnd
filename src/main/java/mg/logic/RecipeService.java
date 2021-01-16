package mg.logic;

import mg.boundaries.RecipeBoundary;
import mg.boundaries.Response;

public interface RecipeService {

	Response<RecipeBoundary> create(RecipeBoundary recipe);

	Response<RecipeBoundary[]> getAll(int page, int size);

	Response<RecipeBoundary[]> getByName(String name, int page, int size);

}
