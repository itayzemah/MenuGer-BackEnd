package mg.logic;

import mg.boundaries.MenuBoundary;

public interface MenuService {

	public MenuBoundary createMenu(String userEmail, int days);

	public MenuBoundary[] searchMenu(String userEmail, int page, int size);

	public MenuBoundary[] buildMenu(String userEmail, Long[] recipeId);

}
