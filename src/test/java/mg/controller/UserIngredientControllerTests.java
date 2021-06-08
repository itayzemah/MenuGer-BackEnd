package mg.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;

import javax.annotation.PostConstruct;

import org.aspectj.lang.annotation.Before;
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
import mg.boundaries.Response;
import mg.boundaries.UserBoundary;
import mg.data.entities.IngredientTypeEnum;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class UserIngredientControllerTests {

	private RestTemplate restTemplate;
	private String userUrl;
	private String userIngredientUrl;
	private int port;
	private final UserBoundary baseUser = new UserBoundary("test@test.com", "test user", "Male");
	private Long[] ingreLst;

	@LocalServerPort
	public void setPort(int port) {
		this.port = port;
	}

	@PostConstruct
	public void init() {
		ingreLst = new Long[] { (long) 1033, (long) 1056, (long) 1145, (long) 2047 };
		restTemplate = new RestTemplate();
		final String url = "http://localhost:" + port;
		userUrl = url + "/user";
		userIngredientUrl = url + "/useringredient";
	}

	@BeforeEach
	void setUp() throws Exception {
		this.restTemplate.postForObject(this.userUrl + "/subscribe", this.baseUser, Response.class);
	}

	@AfterEach
	void tearDown() throws Exception {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < this.ingreLst.length - 1; i++) {
			sb.append(this.ingreLst[i]);
			sb.append(",");
		}
		sb.append(this.ingreLst[this.ingreLst.length - 1]);
		this.restTemplate.delete(this.userIngredientUrl + "/" + this.baseUser.getEmail() + "?ingredients=" + sb.toString());
		this.restTemplate.delete(this.userUrl + "/" + this.baseUser.getEmail());

	}

	@Test
	void test_Add_UserIngredients() {
		// set preferred UserIngredient
		String addingreURL = (userIngredientUrl + "/update/" + this.baseUser.getEmail() + "?type="
				+ IngredientTypeEnum.PREFERRED.name());
		restTemplate.put(addingreURL, ingreLst);

		// Get user ingredients
		String getIngreURL = userIngredientUrl + "/by/type/" + this.baseUser.getEmail() + "?type="
				+ IngredientTypeEnum.PREFERRED.name();
		Response<List<IngredientBoundary>> res = this.restTemplate.exchange(getIngreURL, HttpMethod.GET, null,
				new ParameterizedTypeReference<Response<List<IngredientBoundary>>>() {
				}).getBody();
		// check query success
		assertThat(res.getSuccess());

		// check all ingredients submitted
		IntStream.range(0, res.getData().size()).forEach(idx -> {
			assertThat(new ArrayList<Long>(Arrays.asList(ingreLst)).contains(res.getData().get(idx).getId()));
		});
	}
}
