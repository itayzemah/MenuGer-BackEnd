package mg.boundaries;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

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
	private Set<IngredientBoundary> forbiddenIngredients = new HashSet<>();
	private Set<IngredientBoundary> favoriteIngredients = new HashSet<>();
	
}
