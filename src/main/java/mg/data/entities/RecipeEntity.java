package mg.data.entities;

import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;
import lombok.ToString;
import mg.data.entities.joinentities.RecipeIngredient;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@EqualsAndHashCode
@Entity
@Table(name = "RECIPES")
public class RecipeEntity {
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Id
	private long recipeId;
	@Column(unique = true)
	private @NonNull String Name;
	private String prepartion;
	private String createdBy;

	@OneToMany(mappedBy = "recipe", fetch = FetchType.LAZY)
	private Set<RecipeIngredient> recipeIngredients;

}
