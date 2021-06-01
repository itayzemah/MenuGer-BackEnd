package mg.boundaries;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

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
@JsonIgnoreProperties(ignoreUnknown = true)
public class IngredientBoundary {
	private Long id;
	@NonNull
	private String name;
	private String image;
	private String original;
	private String imageUrl;

	public String getImageUrl() {
		if (image != null)
			return "https://spoonacular.com/cdn/ingredients_250x250/" + image;
		return null;
	}

}
