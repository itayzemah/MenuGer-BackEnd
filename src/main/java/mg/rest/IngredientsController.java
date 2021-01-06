package mg.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.AllArgsConstructor;
import mg.boundaries.IngredientBoundary;
import mg.boundaries.Response;
import mg.data.entities.IngredientTypeEnum;
import mg.logic.IngredientService;

@RestController
@RequestMapping(path="/ingredient")
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class IngredientsController {
	private IngredientService ingredientService;
	
	@RequestMapping
	(path="/{id}", method = RequestMethod.GET,
	consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public Response<IngredientBoundary> findById(@PathVariable("id") long ingredientId){
		return ingredientService.findById(ingredientId);
	}

	@RequestMapping
	(method = RequestMethod.GET,
	produces = MediaType.APPLICATION_JSON_VALUE)
	public Response<IngredientBoundary[]> getAll(
			@RequestParam(name = "page", required = false, defaultValue = "0") int page,
			@RequestParam(name = "size", required = false, defaultValue = "1000") int size){
		return ingredientService.getAll(size, page);
	}
	
	@RequestMapping
	(path="/by/type", method = RequestMethod.GET,
	consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public Response<IngredientBoundary[]> getAllForbOf(
			@RequestParam(name = "user", required = true, defaultValue = "") String userEmail,
			@RequestParam(name = "type", required = true, defaultValue = "preferred") IngredientTypeEnum type,
			@RequestParam(name = "page", required = false, defaultValue = "0") int page,
			@RequestParam(name = "size", required = false, defaultValue = "1000") int size){
		return ingredientService.getAllByType(userEmail,type,size, page);
	}
	
	@RequestMapping
	(path="/{name}", method = RequestMethod.GET,
	consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public Response<IngredientBoundary[]> findByName(@PathVariable String name){
		return ingredientService.findByName(name);
	}
	
	@RequestMapping
	(method = RequestMethod.POST,
	consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public Response<IngredientBoundary> create(@RequestBody IngredientBoundary ingredientBoundary){
		return ingredientService.create(ingredientBoundary);
	}
	
	@RequestMapping
	(method = RequestMethod.PUT,
	consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response<Void> remove(@RequestBody IngredientBoundary ingredientBoundary){
		return ingredientService.update(ingredientBoundary);
	}
	
	@RequestMapping
	(method = RequestMethod.DELETE,
	consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response<Void> remove(Long ingredientId){
		return ingredientService.remove(ingredientId);
	}
	
	
	
	
	
	
	
	
}
