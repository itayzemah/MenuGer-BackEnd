package mg.logic.services.api;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

import lombok.NoArgsConstructor;
import mg.boundaries.IngredientBoundary;
import mg.boundaries.Response;
import mg.logic.IngredientService;

@Service
@NoArgsConstructor
public class IngredientApiService implements IngredientService {

	@Value("${spoonacular.base.url}")
	private String baseUrl;
	@Value("${spoonacular.ingredient.url}")
	private String ingredientUrl;
	@Value("${x-rapidapi-key}")
	private String rapidapiKey;
	@Value("${x-rapidapi-host}")
	private String rapidapiHost;


	@Override
	public Response<IngredientBoundary[]> findByName(String name) {
		Response<IngredientBoundary[]> retval = new Response<IngredientBoundary[]>();
		String search = "/autocomplete?query=" + name;

		String body = "";
		HttpResponse<String> response = null;
		try {
			response = Unirest.get(baseUrl + ingredientUrl + search).header("x-rapidapi-key", rapidapiKey)
					.header("x-rapidapi-host", rapidapiHost).asString();
		} catch (UnirestException e) {
			e.printStackTrace();
			throw new RuntimeException("Error connect " + baseUrl + ingredientUrl + search);
		}
		body = response.getBody();
		System.err.println(body);

		
		if (response.getStatus() != 200) {
			retval.setMessage("Bad request with url: " + baseUrl + ingredientUrl + search);
			retval.setSuccess(false);
		} else {
			ObjectMapper mapper = new ObjectMapper();
			IngredientBoundary[] values = null;
			try {
				values = mapper.readValue(response.getBody(), IngredientBoundary[].class);
			} catch (JsonMappingException e) {
				e.printStackTrace();
				throw new RuntimeException("Error parsing " + response.getBody());
			} catch (JsonProcessingException e) {
				e.printStackTrace();
				throw new RuntimeException("Error parsing " + response.getBody());
			}
			retval.setData(values);
		}
		return retval;
	}

	@Override
	public Response<IngredientBoundary[]> getAll(int size, int page) {
		throw new RuntimeException("In this app version there is not option to get all ingredient.");
	}

	@Override
	public Response<IngredientBoundary> create(IngredientBoundary ingredientBoundary) {
		throw new RuntimeException("In this app version there is not option to add ingredient.");
	}

	@Override
	public Response<Void> remove(String ingredientName) {
		throw new RuntimeException("In this app version there is not option to remove ingredient.");

	}

	@Override
	public Response<Void> update(IngredientBoundary ingredientBoundary) {
		throw new RuntimeException("In this app version there is not option to update ingredient.");

	}

	@Override
	public Response<Void> removeAll() {
		throw new RuntimeException("In this app version there is not option to remove all ingredients.");

	}

}
