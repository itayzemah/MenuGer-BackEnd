package mg.data.dal;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import mg.data.entities.joinentities.MenuRecipe;
import mg.data.entities.joinentities.MenuRecipeId;

public interface MenuRecipeDataAccessRepo extends JpaRepository<MenuRecipe, MenuRecipeId> {
	
	public List<MenuRecipe> findAllById_MenuId(Long menuId);
}
