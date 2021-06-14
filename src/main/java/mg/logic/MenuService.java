package mg.logic;

import java.util.Date;

import mg.boundaries.MenuBoundary;
import mg.boundaries.RecipeBoundary;

public interface MenuService {

//	public MenuBoundary createMenu(String userEmail, int days);

	public MenuBoundary buildMenu(String userEmail, RecipeBoundary[] recipes);

	public MenuBoundary[] searchMenu(Date fromDate, Date toDate);

	public MenuBoundary[] getAll(int page, int size);

	public MenuBoundary[] getAllForUser(String userEmail, int page, int size);
}
