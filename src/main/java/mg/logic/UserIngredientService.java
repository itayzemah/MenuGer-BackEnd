package mg.logic;

import java.util.Map;

import mg.boundaries.IngredientBoundary;
import mg.boundaries.Response;
import mg.data.entities.joinentities.UserIngredient;

public interface UserIngredientService {

	public void bind(String userEmail, long ingredientId,String type);

	public Response<IngredientBoundary[]> getAllByType(String userEmail, String type, int size, int page);

	public void update(String userEmail, Long[] ingredients, String type);

	public Response<Map<String,IngredientBoundary[]>> getAll(String userEmail, int size, int page);

	void unbind(String userEmail, Long[] ingredients);

	public Response<UserIngredient> remove(String userEmail, Long ingredientId);	
	
}
