package mg.data.converters;

import org.springframework.stereotype.Component;

import lombok.NoArgsConstructor;
import mg.boundaries.UserBoundary;
import mg.data.entities.UserEntity;
import mg.data.entities.joinentities.UserIngredient;

@NoArgsConstructor
@Component
public class UserConverter {

	private IngredientConverter ingredientConverter;
	

	public UserEntity fromBoundary(UserBoundary boundary) {
		UserEntity rv = new UserEntity();
		rv.setEmail(boundary.getEmail());
		rv.setFullName(boundary.getFullName());
		rv.setGender(boundary.getGender());
		for (int i = 0; i < boundary.getForbiddenIngredients().size(); i++) {
			rv.setUserIngredients(new UserIngredient(null,rv,));
		}
		return rv;
	}
	
}
