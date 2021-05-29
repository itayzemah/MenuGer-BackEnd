package mg.logic.services.api;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
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
import mg.boundaries.IngredientBoundary;
import mg.boundaries.RecipeBoundary;
import mg.boundaries.Response;
import mg.boundaries.helper.MenuFeedbackEnum;
import mg.boundaries.helper.RecipeWithRate;
import mg.data.converters.IngredientConverter;
import mg.data.entities.IngredientEntity;
import mg.data.entities.IngredientTypeEnum;
import mg.data.entities.MenuEntity;
import mg.data.entities.joinentities.MenuRecipe;
import mg.data.entities.joinentities.UserIngredient;
import mg.logic.RecipeIngredientService;
import mg.logic.RecipeService;
import mg.logic.UserIngredientService;
import mg.logic.exceptions.MenuNotFoundExcetion;
import mg.logic.exceptions.RecipeNotFoundException;

@Service
@NoArgsConstructor
//@AllArgsConstructor(onConstructor = @__(@Autowired))
public class RecipeApiService implements RecipeService {

	private UserIngredientService userIngrdientsService;
	@Value("${spoonacular.base.url}")
	private String baseUrl;
	@Value("${spoonacular.recipes.url}")
	private String recipeUrl;
	@Value("${x-rapidapi-key}")
	private String rapidapiKey;
	@Value("${x-rapidapi-host}")
	private String rapidapiHost;
	private IngredientConverter ingredientConverter;

	@Autowired
	public RecipeApiService(UserIngredientService userIngrdientsService, IngredientConverter ingredientConverter) {
		this.userIngrdientsService = userIngrdientsService;
		this.ingredientConverter = ingredientConverter;
	}

	@Override
	public Response<RecipeBoundary[]> getAll(int page, int size) {
		Response<RecipeBoundary[]> retval = new Response<RecipeBoundary[]>();
		/*
		 * HttpResponse<String> response = Unirest.get(
		 * "https://spoonacular-recipe-food-nutrition-v1.p.rapidapi.com/recipes/complexSearch?number=30&excludeIngredients=peanuts")
		 * .header("x-rapidapi-key",
		 * "65bc01e644msh253ff15fa2688c0p1fe83djsn741ff5c1ded8")
		 * .header("x-rapidapi-host",
		 * "spoonacular-recipe-food-nutrition-v1.p.rapidapi.com") .asString();
		 */
		return retval;
	}

	@Override
	public Response<RecipeBoundary[]> getByTitle(String name, int page, int size) {
		Response<RecipeBoundary[]> retval = new Response<RecipeBoundary[]>();
		String search = "/complexSearch?addRecipeInformation=true&instructionsRequired=true&query=" + name + "&offset="
				+ page * size + "&number=" + size;

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
				values = results.stream().map(r -> this.getById(r.getId())).collect(Collectors.toList())
						.toArray(new RecipeBoundary[0]);
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
	public List<RecipeBoundary> getAllRecipesWithIngredientNotIn(List<IngredientEntity> forbiddenIngredients) {
		List<RecipeBoundary> retval = new ArrayList<RecipeBoundary>();
		StringBuilder excludeIngredientsSB = new StringBuilder();
		for (int j = 0; j < forbiddenIngredients.size() -1; j++) {
			excludeIngredientsSB.append(forbiddenIngredients.get(j).getName());	
			excludeIngredientsSB.append(",");	
		}
		excludeIngredientsSB.append(forbiddenIngredients.get(forbiddenIngredients.size() -1).getName());
		String search = "/complexSearch?addRecipeInformation=true&instructionsRequired=true&number=500&excludeIngredients=" +
		forbiddenIngredients.stream().map(i -> i.getName()+",");

		String body = "";
		HttpResponse<String> response = httpCall(search);
		body = response.getBody();
		System.err.println(body);

		if (response.getStatus() != 200) {
			throw new RecipeNotFoundException("Bad request with url: " + baseUrl + recipeUrl + search);

		} else {
			ObjectMapper mapper = new ObjectMapper();
			try {
				JsonNode jsonNode = mapper.readTree(response.getBody());
				String resultsJson = jsonNode.path("results").toString();
				retval = mapper.readValue(resultsJson, new TypeReference<List<RecipeBoundary>>() {
				});
				retval = retval.stream().map(r -> this.getById(r.getId())).collect(Collectors.toList());
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
				retval.setCreatedBy(jsonNode.path("creditsText").toString().replace("\"", ""));
				SetIngredients(retval, jsonNode.path("extendedIngredients"));
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

	private void SetIngredients(RecipeBoundary recipe, JsonNode extendedIngredients)
			throws JsonMappingException, JsonProcessingException {
		ObjectMapper mapper = new ObjectMapper();
		ArrayList<IngredientBoundary> ingredients = new ArrayList<>();
		if (extendedIngredients.isArray()) {
			for (final JsonNode objNode : extendedIngredients) {
				ingredients.add(mapper.readValue(objNode.toString(), IngredientBoundary.class));
			}
		}
		recipe.setIngredients(ingredients.toArray(new IngredientBoundary[0]));
	}

	private HttpResponse<String> httpCall(String search) {
		HttpResponse<String> response = null;
		System.err.println(baseUrl + recipeUrl + search);
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

	@Override
	public RecipeBoundary[] getAllBestRecipesForUser(String userEmail) {
		IngredientBoundary[] allForbiddenIngredients = userIngrdientsService
				.getAllByType(userEmail, IngredientTypeEnum.FORBIDDEN.name(), 1000, 0).getData();

		// get All user FORBIDDEN ingredients
		List<IngredientEntity> forbiddenUserIngredients = new ArrayList<IngredientBoundary>(
				Arrays.asList(allForbiddenIngredients)).stream().map(this.ingredientConverter::fromBoundary)
						.collect(Collectors.toList());
		// get All user PREFERRED ingredients
		// TODO change to get user ingredients
		List<UserIngredient> preferredUserIngredients = userIngrdientsService
				.getAllUserIngredientByType(userEmail, IngredientTypeEnum.PREFERRED.name(), 1000, 0).getData();

		// get all appropriate recipes
		List<RecipeWithRate> recipesWithRate = getListOfRatedRecipes(preferredUserIngredients,
				this.getAllRecipesWithIngredientNotIn(forbiddenUserIngredients));

		recipesWithRate.sort(Comparator.comparingDouble(RecipeWithRate::getRate).reversed());
		return recipesWithRate.toArray(new RecipeWithRate[0]);
	}

	@Override
	public void feedbackRecipe(long recipeId, String userEmail, MenuFeedbackEnum feedback) {
		IngredientBoundary[] ingredients = this.getById(recipeId).getIngredients();
		if (feedback.equals(MenuFeedbackEnum.GOOD)) {
			for (IngredientBoundary ingredient : ingredients) {
				this.userIngrdientsService.goodScore(userEmail, ingredient.getId());
			}
		} else {
			for (IngredientBoundary ingredient : ingredients) {
				this.userIngrdientsService.badScore(userEmail, ingredient.getId());
			}
		}
	}

	private List<RecipeWithRate> getListOfRatedRecipes(List<UserIngredient> preferredUserIngredients,
			List<RecipeBoundary> allAllowRecipes) {
		ArrayList<RecipeWithRate> rv = new ArrayList<RecipeWithRate>();

		allAllowRecipes.forEach(recipe -> rv.add(calcRate(recipe, preferredUserIngredients)));

		return rv;
	}

	private RecipeWithRate calcRate(RecipeBoundary recipe, List<UserIngredient> preferredUserIngredients) {
		return new RecipeWithRate(recipe, preferredUserIngredients);
	}
}
