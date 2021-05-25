package mg.logic.services.api;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import mg.boundaries.RecipeBoundary;
import mg.boundaries.Response;
import mg.data.entities.IngredientEntity;
import mg.logic.RecipeIngredientService;
import mg.logic.RecipeService;
import mg.logic.exceptions.RecipeNotFoundException;

@Service
@NoArgsConstructor
//@AllArgsConstructor(onConstructor = @__(@Autowired))
public class RecipeApiService implements RecipeService {

	private RecipeIngredientService recipeIngreService;
	@Value("${spoonacular.base.url}")
	private String baseUrl;
	@Value("${spoonacular.recipes.url}")
	private String recipeUrl;
	@Value("${x-rapidapi-key}")
	private String rapidapiKey;
	@Value("${x-rapidapi-host}")
	private String rapidapiHost;

	@Autowired
	public RecipeApiService(RecipeIngredientService recipeIngreService) {
		this.recipeIngreService = recipeIngreService;
	}

	@Override
	public Response<RecipeBoundary[]> getAll(int page, int size) {
		Response<RecipeBoundary[]> retval = new Response<RecipeBoundary[]>();

		return retval;
	}

	@Override
	public Response<RecipeBoundary[]> getByTitle(String name, int page, int size) {
		Response<RecipeBoundary[]> retval = new Response<RecipeBoundary[]>();
		String search = "/complexSearch?addRecipeInformation=true&query=" + name + "&offset=" + page * size + "&number="
				+ size;

		String body = "";
		HttpResponse<String> response = httpCall(search);
		body = response.getBody();
		System.err.println(body);

		if (response.getStatus() != 200) {
			retval.setMessage("Bad request with url: " + baseUrl + recipeUrl + search);
			retval.setSuccess(false);
		} else {
			ObjectMapper mapper = new ObjectMapper();
			RecipeBoundary[] values = null;
			try {
				JsonNode jsonNode = mapper.readTree(response.getBody());
				String resultsJson = jsonNode.path("results").toString();
				List<RecipeBoundary> results = mapper.readValue(resultsJson, new TypeReference<List<RecipeBoundary>>() {
				});
				values = results.stream().map(r-> 
					this.getById(r.getId())).collect(Collectors.toList()).toArray(new RecipeBoundary[0]);
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
	public List<RecipeBoundary> getAllRecipesWithIngredientNotIn(List<IngredientEntity> uiArr) {
		List<RecipeBoundary> retval = new ArrayList<RecipeBoundary>();

		return retval;
	}

	@Override
	public RecipeBoundary getById(Long rId) {
		RecipeBoundary retval = new RecipeBoundary();
		String search = "/" + rId + "/information?includeNutrition=false";

		String body = "";
		HttpResponse<String> response = httpCall(search);
		body = response.getBody();
		System.err.println(body);

		if (response.getStatus() != 200) {
			throw new RecipeNotFoundException("Bad request with url: " + baseUrl + recipeUrl + search);
		} else {
			ObjectMapper mapper = new ObjectMapper();
			try {
				retval = mapper.readValue(response.getBody(), RecipeBoundary.class);
				JsonNode jsonNode = mapper.readTree(response.getBody());
				JsonNode extendedIngredients = jsonNode.path("extendedIngredients");
				retval.setCreatedBy(jsonNode.path("creditsText").toString().replace("\"", ""));
				ArrayList<String> ingredients = new ArrayList<>();
				if (extendedIngredients.isArray()) {
					for (final JsonNode objNode : extendedIngredients) {
						ingredients.add(objNode.path("name").toString().replace("\"", ""));
					}
				}
				retval.setIngredients(ingredients.toArray(new String[0]));
			} catch (JsonMappingException e) {
				e.printStackTrace();
				throw new RuntimeException("Error parsing " + response.getBody());
			} catch (JsonProcessingException e) {
				e.printStackTrace();
				throw new RuntimeException("Error parsing " + response.getBody());
			}
		}
		return retval;
	}

	private HttpResponse<String> httpCall(String search) {
		HttpResponse<String> response = null;
		try {
			response = Unirest.get(baseUrl + recipeUrl + search).header("x-rapidapi-key", rapidapiKey)
					.header("x-rapidapi-host", rapidapiHost).asString();
		} catch (UnirestException e) {
			e.printStackTrace();
			throw new RuntimeException("Error connect " + baseUrl + recipeUrl + search);
		}
		return response;
	}

	@Override
	public Response<RecipeBoundary> create(RecipeBoundary recipe) {
		throw new RuntimeException("In this app version there is not option to add recipe.");

	}

	private void setIngredients(RecipeBoundary[] recipeArr) {
		throw new RuntimeException("In this app version there is not option to setIngredients.");

	}

}
