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
		entity.setName(boundary.getName());
		entity.setPrepartion(boundary.getPrepartion());
		entity.setRecipeId(boundary.getRecipeId());
		return entity;
	}
	public RecipeBoundary toBoundary(RecipeEntity entity) {
		RecipeBoundary boundary = new RecipeBoundary();
		boundary.setCreatedBy(entity.getCreatedBy());
		boundary.setName(entity.getName());
		boundary.setPrepartion(entity.getPrepartion());
		boundary.setRecipeId(entity.getRecipeId());
		return boundary;
	}
	
}
