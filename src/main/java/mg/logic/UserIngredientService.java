package mg.logic;

import java.util.Map;

import mg.boundaries.IngredientBoundary;
import mg.boundaries.Response;
import mg.data.entities.IngredientTypeEnum;

public interface UserIngredientService {

	public void update(String userEmail, long ingredientId,String type);

	public Response<IngredientBoundary[]> getAllByType(String userEmail, String type, int size, int page);

	public void bind(String userEmail, Long[] ingredients, String type);

	public Response<Map<String,IngredientBoundary[]>> getAll(String userEmail, int size, int page);	
	
}
