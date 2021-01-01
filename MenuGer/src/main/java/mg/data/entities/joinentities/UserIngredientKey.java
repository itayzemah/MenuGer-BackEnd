package mg.data.entities.joinentities;

import java.io.Serializable;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Entity;

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
@Entity
@Embeddable
public class UserIngredientKey implements Serializable {
	private static final long serialVersionUID = -2216018707601921086L;
    @Column(name = "user_email")
	public @NonNull String userEmail;
    @Column(name = "ingredient_id")
    public @NonNull String ingredientID;
}
