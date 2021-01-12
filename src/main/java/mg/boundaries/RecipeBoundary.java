package mg.boundaries;

import java.util.Map;

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
	 private long RecipeId ;
	 private @NonNull String Name;
	 private Map<String, Double> Ingredients;
	 private String Prepartion;
	 private String CreatedBy ;
}
