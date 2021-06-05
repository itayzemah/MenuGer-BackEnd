package mg.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.stream.IntStream;

import javax.annotation.PostConstruct;

import org.junit.jupiter.api.AfterEach;
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

	@LocalServerPort
	public void setPort(int port) {
		this.port = port;
	}

	@PostConstruct
	public void init() {
		this.restTemplate = new RestTemplate();
		final String url = "http://localhost:" + this.port;
		this.userUrl = url + "/user";
		this.userIngredientUrl = url + "/useringredient";
	}

	@BeforeEach
	void setUp() throws Exception {
		this.restTemplate.postForObject(userUrl + "/subscribe", baseUser, Response.class);
	}

	@AfterEach
	void tearDown() throws Exception {
		this.restTemplate.delete(userUrl + "/" + this.baseUser.getEmail());

	}

	@Test
	void test_Add_UserIngredients() {
		String addingreURL = (userIngredientUrl + "/update/" + this.baseUser.getEmail() + "?type="
				+ IngredientTypeEnum.PREFERRED.name());
		Long[] ingreLst = new Long[] { (long) 1033, (long) 1056, (long) 1145, (long) 2047 };
		this.restTemplate.put(addingreURL, ingreLst);
		String getIngreURL = userIngredientUrl + "/by/type/" + this.baseUser.getEmail() + "?type="
				+ IngredientTypeEnum.PREFERRED.name();
		System.err.println("-------" + getIngreURL);
		Response<List<IngredientBoundary>> res = this.restTemplate.exchange(getIngreURL, HttpMethod.GET, null,
				new ParameterizedTypeReference<Response<List<IngredientBoundary>>>() {
				}).getBody();
		assertThat(res.getSuccess());
		IntStream.range(0, res.getData().size()).forEach(idx -> {
			assertThat(res.getData().contains(ingreLst[idx]));
			
		});
		System.err.println("-------" +res);
	}
}
