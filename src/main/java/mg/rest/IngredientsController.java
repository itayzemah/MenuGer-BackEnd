package mg.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.microsoft.sqlserver.jdbc.SQLServerException;

import lombok.AllArgsConstructor;
import mg.boundaries.IngredientBoundary;
import mg.boundaries.Response;
import mg.logic.IngredientService;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping(path="/ingredient")
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class IngredientsController {
	private IngredientService ingredientService;
	
	@RequestMapping
	(path="id/{id}", method = RequestMethod.GET,
	produces = MediaType.APPLICATION_JSON_VALUE)
	public Response<IngredientBoundary> findById(@PathVariable("id") long ingredientId){
		return ingredientService.findById(ingredientId);
	}

	@RequestMapping
	(path="name/{name}", method = RequestMethod.GET,
	produces = MediaType.APPLICATION_JSON_VALUE)
	public Response<IngredientBoundary[]> findByName(@PathVariable String name,
			@RequestParam(name = "page", required = false, defaultValue = "0") int page,
			@RequestParam(name = "size", required = false, defaultValue = "1000") int size) throws JsonMappingException, JsonProcessingException, UnirestException{
		return ingredientService.findByName(name,size, page);
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
	(method = RequestMethod.POST,
	consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public Response<IngredientBoundary> create(@RequestBody IngredientBoundary ingredientBoundary){
		return ingredientService.create(ingredientBoundary);
	}
	
	
	@RequestMapping
	(method = RequestMethod.PUT,
	consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response<Void> update(@RequestBody IngredientBoundary ingredientBoundary){
		return ingredientService.update(ingredientBoundary);
	}
	
	/*@RequestMapping
	(path = "{ingredientId}", method = RequestMethod.DELETE,
	consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response<Void> remove(@PathVariable Long ingredientId){
		return ingredientService.remove(ingredientId);
	}
	
	@RequestMapping
	(method = RequestMethod.DELETE,
	consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response<Void> removeAll(){
		return ingredientService.removeAll();
	}
	*/
	@ExceptionHandler
	@ResponseStatus(code = HttpStatus.NOT_FOUND)
	public Response<String> handleException(SQLServerException e) {
		Response<String> response = new Response<String>();
		String error = e.getMessage();
		if (error == null) {
			error = "Not found";
		}
		response.setData(error);
		response.setSuccess(false);
		response.setMessage(error);
		return response;
	}
	
	@ExceptionHandler
	@ResponseStatus(code = HttpStatus.NOT_FOUND)
	public Response<String> handleException(RuntimeException e) {
		Response<String> response = new Response<String>();
		String error = e.getMessage();
		if (error == null) {
			error = "Not found";
		}
		response.setData(error);
		response.setSuccess(false);
		response.setMessage(error);
		return response;
	}
	
	
}
