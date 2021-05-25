package mg.logic;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.mashape.unirest.http.exceptions.UnirestException;

import mg.boundaries.IngredientBoundary;
import mg.boundaries.Response;

public interface IngredientService {

	public Response<IngredientBoundary> create(IngredientBoundary ingredientBoundary);

	public Response<Void> remove(Long ingredientId);

	public Response<Void> update(IngredientBoundary ingredientBoundary);

	public Response<IngredientBoundary> findById(Long ingredientId);

	public Response<IngredientBoundary[]> findByName(String name) throws JsonMappingException, JsonProcessingException, UnirestException;

	public Response<IngredientBoundary[]> getAll(int size, int page);

	public Response<Void> removeAll();


}
