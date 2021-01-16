package mg.logic.services;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.AllArgsConstructor;
import mg.data.dal.IngredientDataAccessRepo;
import mg.data.dal.RecipeDataAccessLayerRepo;
import mg.data.dal.RecipeIngredientDataAccessLayerRepo;
import mg.data.entities.joinentities.RecipeIngredient;
import mg.data.entities.joinentities.RecipeIngredientId;
import mg.data.entities.joinentities.helpers.RecipeIngreHelper;
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
	public void update(long recipeId, long ingredientId, double ammount) {
		RecipeIngredient ri = new RecipeIngredient();
		ri.setRecipe(recipeDAL.findById(recipeId)
				.orElseThrow(() -> new RecipeNotFoundException("recipe with id " + recipeId + " not found")));
		ri.setIngredient(ingreDAL.findById(ingredientId).orElseThrow(
				() -> new IngredientNotFoundException("ingredient with id: " + ingredientId + " not found")));
		ri.setAmmount(ammount);
		ri.setId(new RecipeIngredientId(recipeId, ingredientId));
		recipeIngreDAL.save(ri);
	}

	@Transactional
	@Override
	public void bind(long recipeId, List<RecipeIngreHelper> recipeIngreHelper) {
		recipeIngreHelper.forEach(i -> this.update(recipeId, i.getIngredientId(), i.getAmmount()));

	}

	@Transactional(readOnly = true)
	@Override
	public Map<String, Double> getAllForRecipe(long recipeId) {
		Map<String, Double> rv = new HashMap<>();
		this.recipeIngreDAL.findByRecipe_RecipeId(recipeId)
				.forEach(ri -> rv.put(ri.getIngredient().getName(), ri.getAmmount()));
		return rv;
	}

	@Transactional
	@Override
	public void remove(long recipeId, long ingredientId) {
		this.recipeIngreDAL.deleteById(new RecipeIngredientId(recipeId, ingredientId));
	}

}
