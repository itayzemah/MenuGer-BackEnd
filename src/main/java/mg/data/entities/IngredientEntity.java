package mg.data.entities;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import mg.data.entities.joinentities.RecipeIngredient;
import mg.data.entities.joinentities.UserIngredient;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@EqualsAndHashCode
@Entity
@Table(name= "INGREDIENTS",uniqueConstraints= {@UniqueConstraint(columnNames = {"name"})})
public class IngredientEntity {
   
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id private Long id;
    @Column(nullable =false)
    private String name;

    private String ingredientGroup;
    
    @OneToMany( mappedBy = "ingredient",fetch = FetchType.LAZY)
    private Set<UserIngredient> userIngredients;
    
    @OneToMany(mappedBy = "recipe",fetch = FetchType.LAZY)
	 private Set<RecipeIngredient> recipeIngredients;
}
