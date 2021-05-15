package mg.logic;

import java.util.List;
import java.util.Map;

import mg.boundaries.IngredientBoundary;
import mg.boundaries.Response;
import mg.data.entities.joinentities.UserIngredient;

public interface UserIngredientService {

	public UserIngredient update(String userEmail, long ingredientId,String type,Double weight);

	public Response<IngredientBoundary[]> getAllByType(String userEmail, String type, int size, int page);

	public void update(String userEmail, Long[] ingredients, String type);

	public Response<Map<String,IngredientBoundary[]>> getAll(String userEmail, int size, int page);

	public void unbind(String userEmail, Long[] ingredients);

	public Response<List<UserIngredient>> getAllUserIngredientByType(String userEmail, String type, int size, int page);

	public UserIngredient getOne(String user, long recipeId);	
	
	public double goodScore(String userEmail, long recipeId);
	
	public double badScore(String userEmail, long recipeId);
}
