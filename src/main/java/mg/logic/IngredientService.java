package mg.logic;

import mg.boundaries.IngredientBoundary;
import mg.boundaries.Response;

public interface IngredientService {

	public Response<IngredientBoundary> create(IngredientBoundary ingredientBoundary);

	public Response<Void> remove(Long ingredientId);

	public Response<Void> update(IngredientBoundary ingredientBoundary);

	public Response<IngredientBoundary> findById(Long ingredientId);

	public Response<IngredientBoundary[]> findByName(String name);

	public Response<IngredientBoundary[]> getAll(int size, int page);


}
