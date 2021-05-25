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
public class RecipeBoundary {
	 private long id ;
	 private @NonNull String title;
	 private IngredientBoundary[] ingredients;
	 private String instructions;
	 private String createdBy ;
	 private String image ;
}
