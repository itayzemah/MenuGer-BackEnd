package mg.data.entities;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
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
import mg.data.entities.joinentities.UserIngredient;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@EqualsAndHashCode
@Table(name = "USERS")
@Entity
public class UserEntity {
	private @NonNull @Id String email;
	private String fullName;
	private String gender;
	private Boolean isActive = true;
    @OneToMany(mappedBy = "user",fetch = FetchType.LAZY)
	private Set<UserIngredient> userIngredients;
	
}
