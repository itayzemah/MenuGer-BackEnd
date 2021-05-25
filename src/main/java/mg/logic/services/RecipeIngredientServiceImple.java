package mg.logic.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.AllArgsConstructor;
import mg.data.dal.IngredientDataAccessRepo;
import mg.data.dal.RecipeDataAccessLayerRepo;
import mg.data.dal.RecipeIngredientDataAccessLayerRepo;
import mg.data.entities.joinentities.RecipeIngredient;
import mg.data.entities.joinentities.RecipeIngredientId;
import mg.logic.RecipeIngredientService;
import mg.logic.exceptions.IngredientNotFoundException;
import mg.logic.exceptions.RecipeNotFoundException;

@Service
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class RecipeIngredientServiceImple implements RecipeIngredientService {
	private IngredientDataAccessRepo ingreDAL;
	private RecipeIngredientDataAccessLayerRepo recipeIngreDAL;
	private RecipeDataAccessLayerRepo recipeDAL;

	@Transactional
	@Override
	public void update(long recipeId, String ingredientName) {
		RecipeIngredient ri = new RecipeIngredient();
		ri.setRecipe(recipeDAL.findById(recipeId)
				.orElseThrow(() -> new RecipeNotFoundException("recipe with id " + recipeId + " not found")));
		ri.setIngredient(ingreDAL.findById(ingredientName).orElseThrow(
				() -> new IngredientNotFoundException("ingredient with id: " + ingredientName + " not found")));
		ri.setId(new RecipeIngredientId(recipeId, ingredientName));
		recipeIngreDAL.save(ri);
	}

	@Transactional
	@Override
	public void bind(long recipeId, List<String> ingredients) {
		if(!this.recipeDAL.existsById(recipeId)) {
			throw new RecipeNotFoundException("recipe with id "+recipeId+" not found");
		}
		ingredients.forEach(i -> this.update(recipeId, i));

	}

	@Transactional(readOnly = true)
	@Override
	public String[] getAllForRecipe(long recipeId) {
		List<String> rv = new ArrayList<>();
		this.recipeIngreDAL.findByRecipe_RecipeId(recipeId)
				.forEach(ri -> rv.add(ri.getIngredient().getName()));
		return rv.toArray(new String[0]);
	}

	@Transactional
	@Override
	public void remove(long recipeId, String ingredientName) {
		this.recipeIngreDAL.deleteById(new RecipeIngredientId(recipeId, ingredientName));
	}

}
