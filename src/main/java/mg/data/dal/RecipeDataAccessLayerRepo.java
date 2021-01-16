package mg.data.dal;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import mg.data.entities.RecipeEntity;

public interface RecipeDataAccessLayerRepo extends JpaRepository<RecipeEntity, Long> {
	
	public List<RecipeEntity> findAllByName(String name, Pageable pageRequest );

}
