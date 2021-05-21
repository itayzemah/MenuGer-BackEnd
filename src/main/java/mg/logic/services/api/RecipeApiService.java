package mg.logic.services.api;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import mg.boundaries.IngredientBoundary;
import mg.boundaries.RecipeBoundary;
import mg.boundaries.Response;
import mg.boundaries.helper.SpoonacularSearchResult;
import mg.data.converters.RecipeConverter;
import mg.data.dal.RecipeDataAccessLayerRepo;
import mg.data.entities.IngredientEntity;
import mg.data.entities.RecipeEntity;
import mg.logic.RecipeIngredientService;
import mg.logic.RecipeService;
import mg.logic.exceptions.RecipeNotFoundException;

public class RecipeApiService implements RecipeService {

	private RecipeIngredientService recipeIngreService;
	private RestTemplate client;
	@Value("${spoonacular.base.url}")
	private String baseUrl;
	@Value("${spoonacular.recipes.url}")
	private String recipeUrl;

	public RecipeApiService(RecipeIngredientService recipeIngreService) {
		client = new RestTemplate();
		this.recipeIngreService = recipeIngreService;
	}

	@Override
	public Response<RecipeBoundary[]> getAll(int page, int size) {
		Response<RecipeBoundary[]> retval = new Response<RecipeBoundary[]>();

		return retval;
	}

	@Override
	public Response<RecipeBoundary[]> getByName(String name, int page, int size) {
		String search = "/" + name;
		Response<RecipeBoundary[]> retval = new Response<RecipeBoundary[]>();
		ResponseEntity<SpoonacularSearchResult<RecipeBoundary>> response = client.exchange(baseUrl + recipeUrl + search,
				HttpMethod.GET, null, new ParameterizedTypeReference<SpoonacularSearchResult<RecipeBoundary>>() {
				});
		if (response.getStatusCode().isError()) {
			retval.setMessage("Bad request with url: " + baseUrl + recipeUrl + search);
			retval.setSuccess(false);
		} else {
			retval.setData(response.getBody().getResults());
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
		return null;
	}

	@Override
	public Response<RecipeBoundary> create(RecipeBoundary recipe) {
		throw new RuntimeException("In this app version there is not option to add recipe.");

	}

	private void setIngredients(RecipeBoundary[] recipeArr) {
		throw new RuntimeException("In this app version there is not option to setIngredients.");

	}

}
