package mg.data.entities;

import javax.persistence.Column;
import javax.persistence.Id;
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
//@Entity
//@Table(name= "INGREDIENTS",uniqueConstraints= {@UniqueConstraint(columnNames = {"name"})})
public class IngredientEntity {
   
    //@GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id private Long id;
    @Column(nullable =false)
    private String name;
    private String image;
    
//    @OneToMany( mappedBy = "id.ingredientId",fetch = FetchType.LAZY)
//    private Set<UserIngredient> userIngredients;
//    
//    @OneToMany(mappedBy = "recipe",fetch = FetchType.LAZY)
//	 private Set<RecipeIngredient> recipeIngredients;
}
