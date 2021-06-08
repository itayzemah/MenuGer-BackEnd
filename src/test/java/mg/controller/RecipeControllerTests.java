package mg.controller;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.annotation.PostConstruct;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.client.RestTemplate;

import mg.boundaries.IngredientBoundary;
import mg.boundaries.RecipeBoundary;
import mg.boundaries.Response;
import mg.boundaries.UserBoundary;
import mg.data.entities.IngredientTypeEnum;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class RecipeControllerTests {
	private RestTemplate restTemplate;
	private String userUrl;
	private int port;
	private final UserBoundary baseUser = new UserBoundary("test@test.com", "test user", "Male");
	private String userIngredientUrl;
	private String recipeUrl;
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
	}

	@BeforeEach
	 void setUp() throws Exception {
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

	@AfterEach
	void tearDown() throws Exception {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < PrefLst.length-1; i++) {
			sb.append(PrefLst[i]);
			sb.append(",");
		}
		for (int i = 0; i < forbLst.length; i++) {
			sb.append(forbLst[i]);
			sb.append(",");
		}
		sb.append(PrefLst[PrefLst.length-1]);
		this.restTemplate.delete(userIngredientUrl + "/" + this.baseUser.getEmail() + "?ingredients=" + sb.toString());
		this.restTemplate.delete(userUrl + "/" + this.baseUser.getEmail());
	}

	@Test
	void test_getBest_Recipes_for_user_not_contains_forbidden_ingredietns() {
		RecipeBoundary[] result = this.restTemplate.getForObject(recipeUrl+"/bests/"+this.baseUser.getEmail(), RecipeBoundary[].class);
		
		assertIngredientNotInForbiddenLst(result);
	}

	@Test
	void test_get_search_Recipes_with_name_not_contains_forbidden_ingredietns() {
//		RecipeBoundary[] result = this.restTemplate.getForObject(recipeUrl+"/by/name/"+this.baseUser.getEmail()+"/spaghetti?size=10", RecipeBoundary[].class);
		Response<List<RecipeBoundary>> result = this.restTemplate.exchange(recipeUrl+"/by/name/"+this.baseUser.getEmail()+"/spaghetti?size=10", HttpMethod.GET, null,
				new ParameterizedTypeReference<Response<List<RecipeBoundary>>>() {
				}).getBody();
		assertIngredientNotInForbiddenLst(result.getData().toArray(new RecipeBoundary[0]));
	}

	private void assertIngredientNotInForbiddenLst(RecipeBoundary[] result) {
		ArrayList<IngredientBoundary> resultIngredients = new ArrayList<IngredientBoundary>();
		for (RecipeBoundary recipeBoundary : result) {
			resultIngredients.addAll(Arrays.asList(recipeBoundary.getIngredients()));
		}
		resultIngredients.forEach(i -> {
			assertThat(Arrays.stream(forbLst).noneMatch(i.getId()::equals));
		});
	}
}
