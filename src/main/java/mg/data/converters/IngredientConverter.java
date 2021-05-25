package mg.data.converters;


import org.springframework.stereotype.Component;

import lombok.NoArgsConstructor;
import mg.boundaries.IngredientBoundary;
import mg.data.entities.IngredientEntity;

@NoArgsConstructor
@Component
public class IngredientConverter {

	
	public IngredientEntity fromBoundary(IngredientBoundary ingredientBoundary){
		IngredientEntity rv = new IngredientEntity();
		//rv.setId(ingredientBoundary.getId());
		rv.setName(ingredientBoundary.getName());
		return rv;
	}
	
	public IngredientBoundary toBoundary(IngredientEntity ingredientEntity){
		IngredientBoundary rv = new IngredientBoundary();
		//rv.setId(ingredientEntity.getId());
		rv.setName(ingredientEntity.getName());
		rv.setImageUrl(rv.getImageUrl());
		return rv;
	}
}
