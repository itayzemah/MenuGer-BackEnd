package mg.boundaries;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@EqualsAndHashCode
public class IngredientBoundary {
	private Long id;
	@NonNull
	private String name;
	private String image;
	private String imageUrl;
	public String getImageUrl() {
		return "https://spoonacular.com/cdn/ingredients_250x250/" + image;
	}

}
