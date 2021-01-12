package mg.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import lombok.AllArgsConstructor;
import mg.boundaries.RecipeBoundary;
import mg.boundaries.Response;
import mg.logic.RecipeService;

@RestController
@RequestMapping(path = "/recipe")
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class RecipeController {

	private RecipeService recipeService;

	@RequestMapping(method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public Response<RecipeBoundary> create(@RequestBody RecipeBoundary recipe) {
		return this.recipeService.create(recipe);
	}
}
