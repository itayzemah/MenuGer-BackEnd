package mg.data.entities.joinentities;

import javax.persistence.EmbeddedId;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
//@EqualsAndHashCode
//@Entity(name="recipe_ingredient")
public class RecipeIngredient {
	@EmbeddedId
	private RecipeIngredientId id;
	
//	@ManyToOne(fetch = FetchType.LAZY)
//	@MapsId("recipeId")
//	@JoinColumn(name = "recipe_id")
//	private RecipeEntity recipe;
//
//	@ManyToOne(fetch = FetchType.LAZY)
//    @MapsId("ingredientId")
//    @JoinColumn(name = "ingredient_id")
//	private IngredientEntity ingredient;

	
}
