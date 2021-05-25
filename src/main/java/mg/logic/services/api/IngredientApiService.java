package mg.logic.services.api;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

import mg.boundaries.IngredientBoundary;
import mg.boundaries.Response;
import mg.boundaries.helper.SpoonacularSearchResult;
import mg.logic.IngredientService;

@Service
public class IngredientApiService implements IngredientService {

	private RestTemplate client;
	@Value("${spoonacular.base.url}")
	private String baseUrl;
	@Value("${spoonacular.ingredient.url}")
	private String ingredientUrl;
	@Value("${x-rapidapi-key}")
	private String rapidapiKey;
	@Value("${x-rapidapi-host}")
	private String rapidapiHost;

	public IngredientApiService() {
		client = new RestTemplate();
	}

	@Override
	public Response<IngredientBoundary> findById(Long ingredientId) {
		return null;
	}

	@Override
	public Response<IngredientBoundary[]> findByName(String name) throws JsonMappingException, JsonProcessingException, UnirestException {
		Response<IngredientBoundary[]> retval = new Response<IngredientBoundary[]>();
		String search = "/autocomplete?query=" + name;
		// SpoonacularSearchResult<IngredientBoundary> result =
		// client.getForObject(baseUrl + ingredientUrl+search,
		// SpoonacularSearchResult.class);
		String body = "";
		HttpResponse<String> response = Unirest.get(baseUrl + ingredientUrl + search)
				.header("x-rapidapi-key", rapidapiKey).header("x-rapidapi-host", rapidapiHost).asString();
		body = response.getBody();
		System.err.println(body);

		/*
		 * HttpHeaders headers = new HttpHeaders(); headers.set("x-rapidapi-host",
		 * rapidapiHost); headers.set("x-rapidapi-key", rapidapiKey); HttpEntity<?>
		 * entity = new HttpEntity<Object>(headers);
		 * 
		 * ResponseEntity<SpoonacularSearchResult<IngredientBoundary>> response =
		 * client.exchange( baseUrl + ingredientUrl + search, HttpMethod.GET, entity,
		 * SpoonacularSearchResult<IngredientBoundary>.class);
		 */
		if (response.getStatus()!= 200) {
			retval.setMessage("Bad request with url: " + baseUrl + ingredientUrl + search);
			retval.setSuccess(false);
		} else {
			ObjectMapper mapper = new ObjectMapper();
			IngredientBoundary[] values = mapper.readValue(response.getBody(), IngredientBoundary[].class);
			retval.setData(values);
		}
		return retval;
	}

	@Override
	public Response<IngredientBoundary[]> getAll(int size, int page) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Response<IngredientBoundary> create(IngredientBoundary ingredientBoundary) {
		throw new RuntimeException("In this app version there is not option to add ingredient.");
	}

	@Override
	public Response<Void> remove(Long ingredientId) {
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
