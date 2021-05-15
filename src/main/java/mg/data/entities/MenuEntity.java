package mg.data.entities;

import java.util.Date;
import java.util.Set;

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
import lombok.Setter;
import lombok.ToString;
import mg.data.entities.joinentities.MenuRecipe;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@EqualsAndHashCode
@Entity
@Table(name= "MENUS")
public class MenuEntity {

	@GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id private Long id;
	
	private Date timestamp;
	private short numOfErrors;
	private String user;
	
	@OneToMany(mappedBy = "recipe", fetch = FetchType.LAZY)
	private Set<MenuRecipe> menuRecipes;

   
    
}
