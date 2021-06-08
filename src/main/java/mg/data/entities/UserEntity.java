package mg.data.entities;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.MapsId;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
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
@Table(name = "USERS")
@Entity
public class UserEntity {
	private @NonNull @Id String email;
	private String fullName;
	private String gender;
	private boolean isActive;

	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinColumn(name = "user_email")
	@MapsId("id.user_email")
	private Set<UserIngredient> userIngredients;

	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
//	@MapsId("userEmail")
	@JoinColumn(name = "userEmail")
	private Set<MenuEntity> userMenus;
}
