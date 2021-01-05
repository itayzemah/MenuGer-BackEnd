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
		return rv;
	}
}
