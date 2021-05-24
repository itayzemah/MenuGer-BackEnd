package mg.logic.services.api;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

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
	public Response<IngredientBoundary[]> findByName(String name) {
		Response<IngredientBoundary[]> retval = new Response<IngredientBoundary[]>();
		String search = "/autocomplete?query=" + name;
		// SpoonacularSearchResult<IngredientBoundary> result =
		// client.getForObject(baseUrl + ingredientUrl+search,
		// SpoonacularSearchResult.class);
		
		try {
			HttpResponse<String> response1 = Unirest.get(baseUrl + ingredientUrl + search)
					.header("x-rapidapi-key", "65bc01e644msh253ff15fa2688c0p1fe83djsn741ff5c1ded8")
					.header("x-rapidapi-host", "spoonacular-recipe-food-nutrition-v1.p.rapidapi.com")
					.asString();
		} catch (UnirestException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
		
		ResponseEntity<SpoonacularSearchResult<IngredientBoundary>> response = client.exchange(
				baseUrl + ingredientUrl + search, HttpMethod.GET, null,
				new ParameterizedTypeReference<SpoonacularSearchResult<IngredientBoundary>>() {
				});
		if (response.getStatusCode().isError()) {
			retval.setMessage("Bad request with url: " + baseUrl + ingredientUrl + search);
			retval.setSuccess(false);
		} else {
			retval.setData(response.getBody().getResults());
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
