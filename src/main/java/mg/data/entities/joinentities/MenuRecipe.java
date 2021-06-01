package mg.data.entities.joinentities;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@EqualsAndHashCode
@Entity
@Table(name = "MenuRecipe")
public class MenuRecipe {
	
	@EmbeddedId
	private MenuRecipeId id;
	
//	@ManyToOne(fetch = FetchType.LAZY)
//	//@MapsId("recipeId")
//	@JoinColumn(name = "recipe_id")
//	private Long recipe;
//
//	@ManyToOne(fetch = FetchType.LAZY)
//    @MapsId("menuId")
//    @JoinColumn(name = "menu_id")
//	private MenuEntity menu;
//	
//	private Boolean match;

}
