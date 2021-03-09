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
import mg.boundaries.RecipeBoundary;
import mg.boundaries.Response;
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

	@RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public Response<RecipeBoundary[]> getAll(
			@RequestParam(name = "page", required = false, defaultValue = "0") int page,
			@RequestParam(name = "size", required = false, defaultValue = "1000") int size
		) {
		return this.recipeService.getAll(page,size);
	}

	@RequestMapping(path="/not-forb/{userEmail}",method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public Response<RecipeBoundary[]> getAllNotForb(@PathVariable("userEmail") String userEmail
			) {
		return this.recipeService.getRecipeWIthoutForbIngredients(userEmail);
	}
	
	@RequestMapping(path="/by/name/{name}",method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public Response<RecipeBoundary[]> getByName(
			@PathVariable("name") String name,
			@RequestParam(name = "page", required = false, defaultValue = "0") int page,
			@RequestParam(name = "size", required = false, defaultValue = "1000") int size
			) {
		return this.recipeService.getByName(name,page,size);
	}
	
	@RequestMapping(path="/by/id/{id}",method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public Response<RecipeBoundary> getById(
			@PathVariable("id") Long id) {
		return this.recipeService.getById(id);
	}
}
