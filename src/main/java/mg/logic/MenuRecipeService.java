package mg.logic;

import java.util.List;

import mg.boundaries.RecipeBoundary;
import mg.data.entities.joinentities.MenuRecipe;

public interface MenuRecipeService {
	
	public List<RecipeBoundary> getAllForMenu(long menuId);
	
	public MenuRecipe create(long menuId, long recipeId);
	
	public MenuRecipe findOne(long menuId, long recipeId);
}
