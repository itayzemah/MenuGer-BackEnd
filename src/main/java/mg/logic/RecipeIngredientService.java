package mg.logic;

import java.util.List;


public interface RecipeIngredientService {

	public void update(long recipeId, long ingredientId);

	public void bind(long recipeId,List<Long> ingredients);
	
	public void remove(long recipeId, long ingredientId);

	public String[] getAllForRecipe(long recipeId);
	
}
