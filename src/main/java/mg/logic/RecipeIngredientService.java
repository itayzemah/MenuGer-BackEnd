package mg.logic;

import java.util.List;
import java.util.Map;

import mg.data.entities.joinentities.helpers.RecipeIngreHelper;

public interface RecipeIngredientService {

	public void update(long recipeId, long ingredientId,double ammount);

	public void bind(long recipeId,List<RecipeIngreHelper> recipeIngreHelper);
	
	public void remove(long recipeId, long ingredientId);

	public Map<String,Double> getAllForRecipe(long recipeId);	
	
}
