package mg.logic;

import java.util.List;

import mg.boundaries.RecipeBoundary;

public interface MenuRecipeService {
	
	public List<RecipeBoundary> getAllForMenu(long menuId);
	

}
