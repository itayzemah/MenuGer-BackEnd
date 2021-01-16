package mg.rest;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.AllArgsConstructor;
import mg.data.entities.joinentities.helpers.RecipeIngreHelper;
import mg.logic.RecipeIngredientService;

@RestController
@RequestMapping(path="/recipe_ingredient")
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class RecipeIngredientController {

	private RecipeIngredientService recipeIngredientService;
	
	@RequestMapping(path ="/update/{recipeId}/{ingredientId}", method=RequestMethod.PUT)
	public void update(@PathVariable long recipeId, @PathVariable long ingredientId,
			@RequestParam(required = true, defaultValue = "0.0") double ammount) {
		recipeIngredientService.update(recipeId, ingredientId, ammount);
	}
	
	@RequestMapping(path = "/bind/{recipeId}", method = RequestMethod.PUT)
	public void bind(@PathVariable long recipeId,
			@RequestBody  List<RecipeIngreHelper> recipeIngreHelper) {
		recipeIngredientService.bind(recipeId, recipeIngreHelper);
	}
	
	@RequestMapping(path="/{recipeId}", method = RequestMethod.GET)
	public Map<String, Double> getAll(@PathVariable long recipeId){
		return recipeIngredientService.getAllForRecipe(recipeId);
	}
	
	@RequestMapping(path="/{recipeId}/{ingredientId}", method = RequestMethod.DELETE)
	public void remove(@PathVariable long recipeId, @PathVariable long ingredientId) {
		this.recipeIngredientService.remove(recipeId, ingredientId);
	}

}
