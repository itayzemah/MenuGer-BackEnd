package mg.logic;

import java.util.List;
import java.util.Map;

import mg.boundaries.IngredientBoundary;
import mg.boundaries.Response;
import mg.data.entities.joinentities.UserIngredient;

public interface UserIngredientService {

	public UserIngredient update(String userEmail, Long ingredientId,String type,Double weight);

	public Response<List<IngredientBoundary>> getAllByType(String userEmail, String type, int size, int page);

	public void update(String userEmail, Long[] ingredients, String type);

	public Response<Map<String,IngredientBoundary[]>> getAll(String userEmail, int size, int page);

	public void unbind(String userEmail, Long[] ingredients);

	public Response<List<UserIngredient>> getAllUserIngredientByType(String userEmail, String type, int size, int page);

	public UserIngredient getOne(String user, Long ingredientId);	
	
	public double goodScore(String userEmail, Long ingredientId);
	
	public double badScore(String userEmail, Long ingredientId);

	public void removeAll(String userEmail);
}
