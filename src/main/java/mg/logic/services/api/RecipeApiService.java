package mg.logic.services.api;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

import lombok.NoArgsConstructor;
import mg.boundaries.IngredientBoundary;
import mg.boundaries.RecipeBoundary;
import mg.boundaries.Response;
import mg.boundaries.helper.MenuFeedbackEnum;
import mg.boundaries.helper.RecipeWithRate;
import mg.boundaries.helper.UserLoginBoundary;
import mg.data.converters.IngredientConverter;
import mg.data.entities.IngredientEntity;
import mg.data.entities.IngredientTypeEnum;
import mg.data.entities.joinentities.UserIngredient;
import mg.logic.RecipeService;
import mg.logic.UserIngredientService;
import mg.logic.UserService;
import mg.logic.exceptions.RecipeNotFoundException;

@Service
@NoArgsConstructor
//@AllArgsConstructor(onConstructor = @__(@Autowired))
public class RecipeApiService implements RecipeService {

	private UserIngredientService userIngrdientsService;
	private UserService userSercive;
	@Value("${spoonacular.base.url}")
	private String baseUrl;
	@Value("${spoonacular.recipes.url}")
	private String recipeUrl;
	@Value("${x-rapidapi-key}")
	private String rapidapiKey;
	@Value("${x-rapidapi-host}")
	private String rapidapiHost;
	private IngredientConverter ingredientConverter;
	private ObjectMapper mapper;

	@Autowired
	public RecipeApiService(UserIngredientService userIngrdientsService, IngredientConverter ingredientConverter,
			UserService userSercive) {
		this.userIngrdientsService = userIngrdientsService;
		this.ingredientConverter = ingredientConverter;
		this.userSercive = userSercive;
		mapper = new ObjectMapper();
	}

	@Override
	public Response<RecipeBoundary[]> getAll(String userEmail, int page, int size) {
		Response<RecipeBoundary[]> retval = new Response<RecipeBoundary[]>();
		retval.setSuccess(userSercive.login(new UserLoginBoundary(userEmail)).getSuccess());

		if (retval.getSuccess()) {
			List<IngredientEntity> forbiddenUserIngredients = getUserForbiddenIndredients(userEmail);
			retval.setData(
					this.getRecipesWithIngredientNotIn(forbiddenUserIngredients, size).toArray(new RecipeBoundary[0]));
		}
		return retval;
	}

	@Override
	public Response<RecipeBoundary[]> getByTitle(String userEmail, String name, int page, int size) {
		Response<RecipeBoundary[]> retval = new Response<RecipeBoundary[]>();
		retval.setSuccess(userSercive.login(new UserLoginBoundary(userEmail)).getSuccess());

		List<IngredientEntity> forbiddenIngredients = getUserForbiddenIndredients(userEmail);
		String excludeListString = getForbiddenIngredientsAsFlatString(forbiddenIngredients);

		String search = "/complexSearch?addRecipeInformation=true&instructionsRequired=true&fillIngredients=true&query="
				+ name + "&offset=" + page * size + "&number=" + size + "&excludeIngredients="
				+ excludeListString;
		;

		String body = "";
		HttpResponse<String> response = httpCall(search);
		body = response.getBody();
		System.err.println(body);

		if (response.getStatus() != 200) {
			retval.setMessage("Bad request with url: " + baseUrl + recipeUrl + search);
			retval.setSuccess(false);
		} else {
			RecipeBoundary[] values = null;

			List<RecipeBoundary> results = extractListOfRecipes(body);

			values = results.toArray(new RecipeBoundary[0]);
			retval.setData(values);
		}
		return retval;
	}

	@Override
	public List<RecipeBoundary> getRecipesWithIngredientNotIn(List<IngredientEntity> forbiddenIngredients, int count) {
		List<RecipeBoundary> retval = new ArrayList<RecipeBoundary>();

		String excludeListString = getForbiddenIngredientsAsFlatString(forbiddenIngredients);
		String search = "/complexSearch?addRecipeInformation=true&fillIngredients=true&instructionsRequired=true&number="
				+ count + "&excludeIngredients=" + excludeListString;

		HttpResponse<String> response = httpCall(search);
		String body = response.getBody();
		System.err.println(body);

		if (response.getStatus() != 200) {
			throw new RecipeNotFoundException("Bad request with url: " + baseUrl + recipeUrl + search);

		} else {
			retval = extractListOfRecipes(body);

		}
		return retval;
	}

	private String getForbiddenIngredientsAsFlatString(List<IngredientEntity> forbiddenIngredients) {
		String excludeListString = forbiddenIngredients.stream().map(IngredientEntity::getName)
                .collect(Collectors.joining(","));		
		
		try {
			excludeListString = URLEncoder.encode(excludeListString, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			throw new RuntimeException("Unsupported URL encoding");
		}
		return excludeListString;
	}

	private List<RecipeBoundary> extractListOfRecipes(String responseBody) {
		List<RecipeBoundary> retval = new ArrayList<RecipeBoundary>();
		try {
			JsonNode jsonNode = mapper.readTree(responseBody);
			JsonNode resultsJson = jsonNode.path("results");

			if (resultsJson.isArray()) {
				for (final JsonNode objNode : resultsJson) {
					RecipeBoundary r = this.mapper.readValue(objNode.toString(), RecipeBoundary.class);
					r.setCreatedBy(objNode.path("creditsText").toString().replace("\"", ""));
					this.SetIngredients(r, objNode.path("extendedIngredients"));
					retval.add(r);
				}
			}
			return retval;

		} catch (JsonMappingException e) {
			e.printStackTrace();
			throw new RuntimeException("Error parsing " + responseBody);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
			throw new RuntimeException("Error parsing " + responseBody);
		}
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

	private void SetIngredients(RecipeBoundary recipe, JsonNode extendedIngredients) {
		ArrayList<IngredientBoundary> ingredients = new ArrayList<>();
		if (extendedIngredients.isArray()) {
			for (final JsonNode objNode : extendedIngredients) {
				try {
					ingredients.add(mapper.readValue(objNode.toString(), IngredientBoundary.class));
				} catch (JsonProcessingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
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
	public RecipeBoundary[] getBestRecipesForUser(String userEmail, int count) {
		userSercive.login(new UserLoginBoundary(userEmail));

		List<IngredientEntity> forbiddenUserIngredients = getUserForbiddenIndredients(userEmail);
		// get All user PREFERRED ingredients
		List<UserIngredient> preferredAgreedUserIngredients = userIngrdientsService
				.getUserIngredientsNotForb(userEmail);
		System.err.println(preferredAgreedUserIngredients);
		// get all appropriate recipes
		List<RecipeWithRate> recipesWithRate = getListOfRatedRecipes(preferredAgreedUserIngredients,
				this.getRecipesWithIngredientNotIn(forbiddenUserIngredients, count));

		recipesWithRate.sort(Comparator.comparingDouble(RecipeWithRate::getRate).reversed());
		return recipesWithRate.toArray(new RecipeWithRate[0]);
	}

	private List<IngredientEntity> getUserForbiddenIndredients(String userEmail) {
		userSercive.login(new UserLoginBoundary(userEmail));

		List<IngredientBoundary> allForbiddenIngredients = userIngrdientsService
				.getAllByType(userEmail, IngredientTypeEnum.FORBIDDEN.name(), 1000, 0).getData();

		// get All user FORBIDDEN ingredients
		List<IngredientEntity> forbiddenUserIngredients = allForbiddenIngredients.stream()
				.map(this.ingredientConverter::fromBoundary).collect(Collectors.toList());
		return forbiddenUserIngredients;
	}

	@Override
	public void feedbackRecipe(IngredientBoundary[] ingredients, String userEmail, MenuFeedbackEnum feedback) {
		userSercive.login(new UserLoginBoundary(userEmail));

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
