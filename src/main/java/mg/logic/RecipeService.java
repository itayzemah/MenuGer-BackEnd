package mg.logic;

import mg.boundaries.RecipeBoundary;
import mg.boundaries.Response;

public interface RecipeService {

	Response<RecipeBoundary> create(RecipeBoundary recipe);

}
