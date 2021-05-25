package mg.data.entities.joinentities;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

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
@Embeddable
public class UserIngredientKey implements Serializable {
	private static final long serialVersionUID = 1L;

	@Column(name = "user_email")
	private @NonNull String useremail;
    
	@Column(name = "ingredient_id")
	private @NonNull String ingredientName;
}
