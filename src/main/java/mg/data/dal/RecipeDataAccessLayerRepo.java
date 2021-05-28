//package mg.data.dal;
//
//import java.util.Collection;
//import java.util.List;
//
//import org.springframework.data.domain.Pageable;
//import org.springframework.data.jpa.repository.JpaRepository;
//
//import mg.data.entities.IngredientEntity;
//import mg.data.entities.RecipeEntity;
//
//public interface RecipeDataAccessLayerRepo extends JpaRepository<RecipeEntity, Long> {
//	
//	public List<RecipeEntity> findAllByTitle(String title, Pageable pageRequest);
//	
//	public List<RecipeEntity> findDistinctByRecipeIngredients_IngredientNotIn(Collection<IngredientEntity> ingredients);
//
//}
