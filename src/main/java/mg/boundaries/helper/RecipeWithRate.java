package mg.boundaries.helper;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import lombok.ToString;
import mg.boundaries.IngredientBoundary;
import mg.boundaries.RecipeBoundary;
import mg.data.entities.joinentities.UserIngredient;

@Getter
@Setter
@ToString
@EqualsAndHashCode(callSuper = true)

public class RecipeWithRate extends RecipeBoundary implements Comparator<RecipeWithRate> {

	private Double rate;

	public RecipeWithRate(long recipeId, @NonNull String name, IngredientBoundary[] ingredients, String instructions,
			String image, String createdBy, Double rate) {
		super(recipeId, name, ingredients, instructions, createdBy, image);
		this.rate = rate;
	}

	public RecipeWithRate(RecipeBoundary recipeBoundary, Double rate) {
		this(recipeBoundary.getId(), recipeBoundary.getTitle(), recipeBoundary.getIngredients(),
				recipeBoundary.getInstructions(), recipeBoundary.getImage(), recipeBoundary.getCreatedBy(), rate);
	}

	public RecipeWithRate(RecipeBoundary recipeBoundary, List<UserIngredient> preferredUserIngredients) {
		this(recipeBoundary.getId(), recipeBoundary.getTitle(), recipeBoundary.getIngredients(),
				recipeBoundary.getInstructions(), recipeBoundary.getImage(), recipeBoundary.getCreatedBy(), 0.0);
		double rate = 0;
		for (IngredientBoundary ingredient : recipeBoundary.getIngredients()) {
			Optional<UserIngredient> userIngredient = preferredUserIngredients.stream()
//					.peek(
//					ui -> System.out.println("IngredientId(): " + ui.getId().getIngredientId() + " ingredient.getId() "
//							+ ingredient.getId() + (ui.getId().getIngredientId() == ingredient.getId()) + " "
//							+ ui.getId().getIngredientId().equals(ingredient.getId())))
					.filter(ui -> ui.getId().getIngredientId().equals(ingredient.getId())).findFirst();
			if (userIngredient.isPresent()) {
				rate += userIngredient.get().getRate();
			}
		}
		this.setRate(rate);
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
