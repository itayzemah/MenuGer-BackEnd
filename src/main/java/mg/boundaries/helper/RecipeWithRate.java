package mg.boundaries.helper;

import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;
import lombok.ToString;
import mg.boundaries.RecipeBoundary;
import mg.data.entities.joinentities.UserIngredient;

@Getter
@Setter
@ToString
@EqualsAndHashCode(callSuper=true)

public class RecipeWithRate extends RecipeBoundary implements Comparator<RecipeWithRate> {

	private Double rate;

	public RecipeWithRate(long recipeId, @NonNull String name, String[] ingredients, String prepartion,
			String createdBy,Double rate) {
		super(recipeId, name, ingredients, prepartion, createdBy);
		this.rate = rate;
	}
	public RecipeWithRate(RecipeBoundary recipeBoundary, Double rate) {
		this(recipeBoundary.getRecipeId(), recipeBoundary.getName(), recipeBoundary.getIngredients()
				, recipeBoundary.getPrepartion(), recipeBoundary.getCreatedBy(),rate);
	}
	public RecipeWithRate(RecipeBoundary recipeBoundary, List<UserIngredient> preferredUserIngredients) {
		this(recipeBoundary.getRecipeId(), recipeBoundary.getName(), recipeBoundary.getIngredients()
				, recipeBoundary.getPrepartion(), recipeBoundary.getCreatedBy(),0.0);
		double rate= 0.0;
		String[] ingredients = recipeBoundary.getIngredients();
		for (int i = 0; i < ingredients.length; i++) {
			for (Iterator iterator = preferredUserIngredients.iterator(); iterator.hasNext();) {
				UserIngredient userIngredient = (UserIngredient) iterator.next();
				
			}
		}
	}
	public Double addToRate(double d) {
		this.rate += d;
		return rate;
	}
	
	@Override
	public int compare(RecipeWithRate r1, RecipeWithRate r2) {
		return Double.compare(r1.getRate(), r2.getRate());

	}

	

}
