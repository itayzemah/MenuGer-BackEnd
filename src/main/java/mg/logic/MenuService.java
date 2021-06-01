package mg.logic;

import java.util.Date;

import mg.boundaries.MenuBoundary;

public interface MenuService {

//	public MenuBoundary createMenu(String userEmail, int days);

	public MenuBoundary buildMenu(String userEmail, Long[] recipeId);

	public MenuBoundary[] searchMenu(Date fromDate, Date toDate);

	public MenuBoundary[] getAll(int page, int size);

	public MenuBoundary[] getAllForUser(String userEmail, int page, int size);
}
