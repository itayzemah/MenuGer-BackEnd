package mg.controller;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import javax.annotation.PostConstruct;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.client.RestTemplate;

import mg.boundaries.IngredientBoundary;
import mg.boundaries.MenuBoundary;
import mg.boundaries.RecipeBoundary;
import mg.boundaries.Response;
import mg.boundaries.UserBoundary;
import mg.boundaries.helper.MenuBuilderBoundary;
import mg.data.entities.IngredientTypeEnum;
import mg.data.entities.joinentities.UserIngredient;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class MenuControllerTests {
	private RestTemplate restTemplate;
	private String userUrl;
	private int port;
	private final UserBoundary baseUser = new UserBoundary("test@test.com", "test user", "Male");
	private String userIngredientUrl;
	private String recipeUrl;
	private String menuUrl;
	private Long[] PrefLst;
	private Long[] forbLst;

	@LocalServerPort
	public void setPort(int port) {
		this.port = port;
	}

	@PostConstruct
	public void init() {
		PrefLst = new Long[] { (long) 1033, (long) 1056, (long) 1145, (long) 2047, (long) 4053 };
		forbLst = new Long[] { (long) 11529, (long) 18363, (long) 1001009, (long) 1001026, (long) 1002044,
				(long) 1009037, (long) 1019037, (long) 1029037, (long) 1055062, (long) 1062047, (long) 1102047 };
		this.restTemplate = new RestTemplate();
		final String url = "http://localhost:" + this.port;
		this.userUrl = url + "/user";
		this.userIngredientUrl = url + "/useringredient";
		this.recipeUrl = url + "/recipe";
		this.menuUrl = url + "/menu";
	}

	@BeforeAll
	void setUpBeforeClass() {
		this.restTemplate.postForObject(userUrl + "/subscribe", baseUser, Response.class);

		// set PREFERRED UserIngredient
		String addingreURL = (userIngredientUrl + "/update/" + this.baseUser.getEmail() + "?type="
				+ IngredientTypeEnum.PREFERRED.name());
		this.restTemplate.put(addingreURL, PrefLst);

		// set FORBIDDEN UserIngredient
		addingreURL = (userIngredientUrl + "/update/" + this.baseUser.getEmail() + "?type="
				+ IngredientTypeEnum.FORBIDDEN.name());

		this.restTemplate.put(addingreURL, forbLst);

	}

	@AfterAll
	void tearDownAfterClass() {
		this.restTemplate.delete(userUrl + "/" + this.baseUser.getEmail());

	}

	@Test
	void test_build_menu_add_ingredients_to_pref_list() {
		MenuBuilderBoundary mb = new MenuBuilderBoundary();
		// Get some recipes
		RecipeBoundary[] recipes = getBestRecipes();
		mb.setUserEmail(this.baseUser.getEmail());
		mb.setRecipes(IntStream.range(0, 7).boxed().map(i -> recipes[i]).collect(Collectors.toList())
				.toArray(new RecipeBoundary[0]));

		this.restTemplate.postForObject(menuUrl + "/build", mb, MenuBoundary.class);

		// Get user ingredients
		List<IngredientBoundary> preferredIngredients = getIngredientOfUserByType(IngredientTypeEnum.PREFERRED);

		Arrays.stream(recipes).forEach(
				r -> Arrays.stream(r.getIngredients()).forEach(i -> assertThat(preferredIngredients.contains(i))));

	}

	@Test
	void test_build_menu_add_rate_to_ingredients_in_pref_list() {
		List<UserIngredient> beforeAddPreferredIngredients = getUserIngredientOfUserByType(
				IngredientTypeEnum.PREFERRED);

		RecipeBoundary[] recipes = getBestRecipes();
		RecipeBoundary[] selectedRecipes = IntStream.range(0, 5).boxed().map(i -> recipes[i])
				.collect(Collectors.toList()).toArray(new RecipeBoundary[0]);

		MenuBuilderBoundary mb = new MenuBuilderBoundary(selectedRecipes, this.baseUser.getEmail());

		this.restTemplate.postForObject(menuUrl + "/build", mb, MenuBoundary.class);

		List<UserIngredient> afterAddPreferredIngredients = getUserIngredientOfUserByType(IngredientTypeEnum.PREFERRED);

		assertRateOfIngredientIncreasedOrAdded(beforeAddPreferredIngredients, afterAddPreferredIngredients,
				selectedRecipes);
	}

	private RecipeBoundary[] getBestRecipes() {
		RecipeBoundary[] recipes = this.restTemplate
				.getForObject(recipeUrl + "/bests/" + this.baseUser.getEmail() + "?count=10", RecipeBoundary[].class);
		return recipes;
	}

	private List<UserIngredient> getUserIngredientOfUserByType(IngredientTypeEnum type) {
		String getIngreURL = userIngredientUrl + "/ui/by/type/" + this.baseUser.getEmail() + "?type=" + type.name();
		List<UserIngredient> preferredUserIngredients = this.restTemplate.exchange(getIngreURL, HttpMethod.GET, null,
				new ParameterizedTypeReference<Response<List<UserIngredient>>>() {
				}).getBody().getData();
		return preferredUserIngredients;
	}

	private List<IngredientBoundary> getIngredientOfUserByType(IngredientTypeEnum type) {
		String getIngreURL = userIngredientUrl + "/by/type/" + this.baseUser.getEmail() + "?type=" + type.name();
		List<IngredientBoundary> preferredUserIngredients = this.restTemplate.exchange(getIngreURL, HttpMethod.GET,
				null, new ParameterizedTypeReference<Response<List<IngredientBoundary>>>() {
				}).getBody().getData();
		return preferredUserIngredients;
	}

	private void assertRateOfIngredientIncreasedOrAdded(List<UserIngredient> beforeAddPreferredIngredients,
			List<UserIngredient> afterAddPreferredIngredients, RecipeBoundary[] selectedRecipes) {
		HashMap<Long, Integer> ingredientsInstances = new HashMap<Long, Integer>();
		Arrays.stream(selectedRecipes).forEach(r -> {
			Arrays.stream(r.getIngredients()).forEach(ing -> {
				Long ingId = ing.getId();
				int val = ingredientsInstances.getOrDefault(ingId, 0);
				ingredientsInstances.put(ingId, val + 1);
			});
		});
		afterAddPreferredIngredients.forEach(i -> {

			int indexBefore = beforeAddPreferredIngredients.indexOf(i);
			if (indexBefore == -1) {
				assertThat(i.getRate()).isEqualTo(ingredientsInstances.get(i.getId().getIngredientId()) * 0.5);
			} else {
				System.err.println("---afterAddPreferredIngredients:" + i);
				System.err.println("---beforeAddPreferredIngredients" + beforeAddPreferredIngredients.get(indexBefore));
				int numOfIngredientsInstances = ingredientsInstances.getOrDefault(i.getId().getIngredientId(), 0);

				assertThat(i.getRate()).isEqualTo(beforeAddPreferredIngredients.get(indexBefore).getRate()
						+ numOfIngredientsInstances * 0.5);
			}
		});
	}

}
