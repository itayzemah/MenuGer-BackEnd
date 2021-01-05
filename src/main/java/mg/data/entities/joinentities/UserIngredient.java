package mg.data.entities.joinentities;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import mg.data.entities.IngredientEntity;
import mg.data.entities.UserEntity;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@EqualsAndHashCode
@Entity
public class UserIngredient {

	@EmbeddedId
	private UserIngredientKey id;

	@ManyToOne
	@MapsId("userEmail")
	@JoinColumn(name = "user_email")
	private UserEntity user;

	@ManyToOne
    @MapsId("ingredientID")
    @JoinColumn(name = "ingredient_id")
	private IngredientEntity ingredient;

	private String type;
}
