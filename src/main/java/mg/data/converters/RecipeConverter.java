package mg.data.converters;

import org.springframework.stereotype.Component;

import lombok.NoArgsConstructor;
import mg.boundaries.RecipeBoundary;
import mg.data.entities.RecipeEntity;

@NoArgsConstructor
@Component
public class RecipeConverter {

	public RecipeEntity fromBoundary(RecipeBoundary boundary) {
		RecipeEntity entity = new RecipeEntity();
		entity.setCreatedBy(boundary.getCreatedBy());
		entity.setTitle(boundary.getTitle());
		entity.setInstructions(boundary.getInstructions());
		entity.setRecipeId(boundary.getId());
		entity.setImage(boundary.getImage());
		return entity;
	}
	public RecipeBoundary toBoundary(RecipeEntity entity) {
		RecipeBoundary boundary = new RecipeBoundary();
		boundary.setCreatedBy(entity.getCreatedBy());
		boundary.setTitle(entity.getTitle());
		boundary.setInstructions(entity.getInstructions());
		boundary.setId(entity.getRecipeId());
		boundary.setImage(entity.getImage());
		return boundary;
	}
	
}
