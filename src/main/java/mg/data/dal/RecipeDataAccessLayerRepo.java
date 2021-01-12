package mg.data.dal;

import org.springframework.data.jpa.repository.JpaRepository;

import mg.data.entities.RecipeEntity;

public interface RecipeDataAccessLayerRepo extends JpaRepository<RecipeEntity, Long> {
	

}
