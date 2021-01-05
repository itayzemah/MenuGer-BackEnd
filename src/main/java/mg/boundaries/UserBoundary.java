package mg.boundaries;

import javax.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@EqualsAndHashCode
public class UserBoundary {
	private @NonNull @Id String email;
	private String fullName;
	private String gender;
//	private Set<IngredientBoundary> forbiddenIngredients = new HashSet<>();
//	private Set<IngredientBoundary> favoriteIngredients = new HashSet<>();

}
