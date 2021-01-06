package mg.data.dal;

import org.springframework.data.jpa.repository.JpaRepository;

import mg.data.entities.IngredientEntity;

public interface IngredientDataAccessRepo extends JpaRepository<IngredientEntity, Long> {

}
