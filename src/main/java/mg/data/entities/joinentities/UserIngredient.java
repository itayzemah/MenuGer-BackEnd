package mg.data.entities.joinentities;

import javax.persistence.CascadeType;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
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
@Entity(name="user_ingredient")
public class UserIngredient {
	@EmbeddedId
	private UserIngredientKey id;

	@ManyToOne(fetch = FetchType.LAZY)
	@MapsId("userEmail")
	@JoinColumn(name = "user_email")
	private UserEntity user;

	@ManyToOne(fetch = FetchType.LAZY)
    @MapsId("ingredientId")
    @JoinColumn(name = "ingredient_id")
	private IngredientEntity ingredient;

	private String type;
}
