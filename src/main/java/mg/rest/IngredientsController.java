package mg.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import lombok.AllArgsConstructor;
import mg.boundaries.IngredientBoundary;
import mg.boundaries.Response;
import mg.logic.IngredientService;

@RestController
@RequestMapping(path="/ingredient")
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class IngredientsController {
	private IngredientService ingredientService;
	
	@RequestMapping
	(path="/{id}", method = RequestMethod.POST,
	consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public Response<IngredientBoundary> findById(@PathVariable("id") Long ingredientId){
		return ingredientService.findById(ingredientId);
	}
	
	@RequestMapping
	(method = RequestMethod.POST,
	consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public Response<IngredientBoundary> create(IngredientBoundary ingredientBoundary){
		return ingredientService.create(ingredientBoundary);
	}
	
	@RequestMapping
	(method = RequestMethod.PUT,
	consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response<Void> remove(IngredientBoundary ingredientBoundary){
		return ingredientService.update(ingredientBoundary);
	}
	
	@RequestMapping
	(method = RequestMethod.DELETE,
	consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response<Void> remove(Long ingredientId){
		return ingredientService.remove(ingredientId);
	}
	
	
	
	
	
	
	
	
}
