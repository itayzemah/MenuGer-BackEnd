package mg.logic;

import java.util.List;

import mg.boundaries.IngredientBoundary;


public interface RecipeIngredientService {

	public void update(long recipeId, String ingredientName);

	public void bind(long recipeId,List<String> ingredients);
	
	public void remove(long recipeId, String ingredientName);

	public IngredientBoundary[] getAllForRecipe(long recipeId);
	
}
