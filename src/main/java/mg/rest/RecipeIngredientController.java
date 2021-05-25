package mg.rest;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.AllArgsConstructor;
import mg.logic.RecipeIngredientService;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping(path="/recipe_ingredient")
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class RecipeIngredientController {

	private RecipeIngredientService recipeIngredientService;
	
	@RequestMapping(path ="/update/{recipeId}/{ingredientId}", method=RequestMethod.PUT)
	public void update(@PathVariable long recipeId, @PathVariable String ingredientName,
			@RequestParam(required = true, defaultValue = "0.0") double ammount) {
		recipeIngredientService.update(recipeId, ingredientName);
	}
	
	@RequestMapping(path = "/bind/{recipeId}", method = RequestMethod.PUT)
	public void bind(@PathVariable long recipeId,
			@RequestBody  List<String> ingredients) {
		recipeIngredientService.bind(recipeId, ingredients);
	}
	
	@RequestMapping(path="/{recipeId}", method = RequestMethod.GET)
	public String[] getAll(@PathVariable long recipeId){
		return recipeIngredientService.getAllForRecipe(recipeId);
	}
	
	@RequestMapping(path="/{recipeId}/{ingredientName}", method = RequestMethod.DELETE)
	public void remove(@PathVariable long recipeId, @PathVariable String ingredientName) {
		this.recipeIngredientService.remove(recipeId, ingredientName);
	}

}
