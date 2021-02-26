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
public class RecipeBoundary {
	 private long recipeId ;
	 private @NonNull String name;
	 private String[] ingredients;
	 private String prepartion;
	 private String createdBy ;
}
