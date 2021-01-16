package mg.data.dal;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import mg.data.entities.joinentities.RecipeIngredient;
import mg.data.entities.joinentities.RecipeIngredientId;

public interface RecipeIngredientDataAccessLayerRepo extends JpaRepository<RecipeIngredient, RecipeIngredientId> {

	public List<RecipeIngredient> findByRecipe_RecipeId(long id);
}
