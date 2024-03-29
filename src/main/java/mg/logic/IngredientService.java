package mg.logic;

import mg.boundaries.IngredientBoundary;
import mg.boundaries.Response;

public interface IngredientService {

	public Response<IngredientBoundary> create(IngredientBoundary ingredientBoundary);

	public Response<Void> remove(String ingredientName);

	public Response<Void> update(IngredientBoundary ingredientBoundary);

	public Response<IngredientBoundary> findById(Long ingredientId);

	public Response<IngredientBoundary[]> findByName(String name,int size, int page);

	public Response<IngredientBoundary[]> getAll(int size, int page);

	public Response<Void> removeAll();


}
