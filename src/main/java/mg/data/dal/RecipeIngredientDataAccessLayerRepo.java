package mg.data.dal;

import org.springframework.data.jpa.repository.JpaRepository;

import mg.data.entities.joinentities.RecipeIngredient;
import mg.data.entities.joinentities.RecipeIngredientId;

public interface RecipeIngredientDataAccessLayerRepo extends JpaRepository<RecipeIngredient, RecipeIngredientId> {

}
