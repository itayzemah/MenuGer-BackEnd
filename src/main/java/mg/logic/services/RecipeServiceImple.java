package mg.logic.services;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import mg.boundaries.IngredientBoundary;
import mg.boundaries.RecipeBoundary;
import mg.boundaries.Response;
import mg.data.converters.RecipeConverter;
import mg.data.dal.IngredientDataAccessRepo;
import mg.data.dal.RecipeDataAccessLayerRepo;
import mg.data.dal.RecipeIngredientDataAccessLayerRepo;
import mg.data.entities.IngredientEntity;
import mg.data.entities.RecipeEntity;
import mg.data.entities.joinentities.RecipeIngredient;
import mg.data.entities.joinentities.RecipeIngredientId;
import mg.logic.RecipeService;

@Service
@NoArgsConstructor
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class RecipeServiceImple implements RecipeService {
	private RecipeIngredientDataAccessLayerRepo recipeIngreDal;
	private RecipeDataAccessLayerRepo recipeDal;
	private IngredientDataAccessRepo ingredientDAL;
	
	private RecipeConverter recipeConverter;
	
	@Override
	@Transactional
	public Response<RecipeBoundary> create(RecipeBoundary recipe) {
		RecipeEntity entity = recipeConverter.fromBoundary(recipe);
		entity = recipeDal.save(entity);
		for (Map.Entry<String, Double> entry : recipe.getIngredients().entrySet()) {
		    System.err.println(entry.getKey() + "/" + entry.getValue());
		    
		    RecipeIngredient ri = new RecipeIngredient();
		    IngredientEntity ingredient = ingredientDAL.findAllByName(entry.getKey()).get(0);
		    ri.setId(new RecipeIngredientId(entity.getRecipeId(),
		    					ingredient.getId()));
		    ri.setAmmount(entry.getValue());
		    try {
		    	ri.setIngredient(ingredient);
		    	ri.setRecipe(entity);
		    	
		    }catch (Exception e) {
		    	System.err.println(e);
		    	System.err.println(entity);
		    	System.err.println(ingredient);
			}
		    System.err.println(ri.toString());
//		    recipeIngreDal.save(ri);
		}	
		System.err.println(entity.toString());
		System.err.println(recipe.getIngredients().toString());
		return null;
	}

}
