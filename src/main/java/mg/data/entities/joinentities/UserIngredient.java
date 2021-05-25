package mg.data.entities.joinentities;

import java.io.Serializable;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import mg.data.entities.UserEntity;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@EqualsAndHashCode
@Entity(name="user_ingredient")
public class UserIngredient implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@EmbeddedId
	private UserIngredientKey id;

	@ManyToOne(fetch = FetchType.LAZY)
	@MapsId("userEmail")
	@JoinColumn(name = "user_email")
	private UserEntity user;
////
////	@ManyToOne(fetch = FetchType.LAZY)
////    @JoinColumn(name = "ingredient_name")
////	@MapsId("ingredient_id")
//	private Long ingredientId;

	private String type;
	
	private Double rate;
}
