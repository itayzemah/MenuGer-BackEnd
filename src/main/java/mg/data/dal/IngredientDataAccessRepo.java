package mg.data.dal;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import mg.data.entities.IngredientEntity;

public interface IngredientDataAccessRepo extends JpaRepository<IngredientEntity, Long> {
	
	public IngredientEntity findAllByuserIngredients_user(String name);

	public List<IngredientEntity> findAllByName(String name);
	
}
