package mg.data.entities.joinentities;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Entity
@Table(name="user_ingredient")
public class UserIngredient {

	@EmbeddedId
	private UserIngredientKey id;

//	@ManyToOne(fetch = FetchType.LAZY)
//	@MapsId("userEmail")
////	@JoinColumn(name = "user_email")
//	private UserEntity user;
////
////	@ManyToOne(fetch = FetchType.LAZY)
////    @JoinColumn(name = "ingredient_name")
//	@MapsId("ingredient_id")
//	private Long ingredientId;
	private String Name;
	private String type;
	
	private Double rate;

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		UserIngredient other = (UserIngredient) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
	
	
}
