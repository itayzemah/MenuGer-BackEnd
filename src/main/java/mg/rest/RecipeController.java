package mg.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.AllArgsConstructor;
import mg.boundaries.IngredientBoundary;
import mg.boundaries.RecipeBoundary;
import mg.boundaries.Response;
import mg.boundaries.helper.MenuFeedbackEnum;
import mg.logic.RecipeService;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping(path = "/recipe")
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class RecipeController {
	private RecipeService recipeService;

	@RequestMapping(method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public Response<RecipeBoundary> create(@RequestBody RecipeBoundary recipe) {
		return this.recipeService.create(recipe);
	}

	@RequestMapping(path="/{userEmail}",method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public Response<RecipeBoundary[]> getAll(@PathVariable("userEmail") String userEmail,
			@RequestParam(name = "page", required = false, defaultValue = "0") int page,
			@RequestParam(name = "size", required = false, defaultValue = "40") int size) {
		return this.recipeService.getAll(userEmail, page, size);
	}

	@RequestMapping(path = "/by/name/{userEmail}/{name}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public Response<RecipeBoundary[]> getByName(@PathVariable("userEmail") String userEmail,
			@PathVariable("name") String name,
			@RequestParam(name = "page", required = false, defaultValue = "0") int page,
			@RequestParam(name = "size", required = false, defaultValue = "20") int size) {
		return this.recipeService.getByTitle(userEmail, name, page, size);
	}

	@RequestMapping(path = "/by/id/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public RecipeBoundary getById(@PathVariable("id") Long id) {
		return this.recipeService.getById(id);
	}

	@RequestMapping(path = "/bests/{userEmail}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public RecipeBoundary[] getAllBestRecipes(@PathVariable("userEmail") String userEmail,
			@RequestParam(name = "count", required = false, defaultValue = "30") int count) {
		return this.recipeService.getBestRecipesForUser(userEmail,count);
	}

	@RequestMapping(path = "/feedback/{userEmail}", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public void feedbackMenuResults(@PathVariable("userEmail") String userEmail,
			@RequestParam(name = "feedback", required = true, defaultValue = "GOOD") MenuFeedbackEnum feedback,
			@RequestBody(required = true) IngredientBoundary[] ingredients) {
		this.recipeService.feedbackRecipe(ingredients, userEmail, feedback);
	}
}
