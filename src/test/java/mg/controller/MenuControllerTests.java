package mg.controller;

import static org.junit.jupiter.api.Assertions.*;

import javax.annotation.PostConstruct;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.web.client.RestTemplate;

import mg.boundaries.Response;
import mg.boundaries.UserBoundary;
import mg.data.entities.IngredientTypeEnum;

class MenuControllerTests {
	private RestTemplate restTemplate;
	private String url;
	private int port;
	private final UserBoundary baseUser = new UserBoundary("test@test.com", "test user", "Male");
	private String userIngredientUrl;
	private String menuUrl;

	@LocalServerPort
	public void setPort(int port) {
		this.port = port;
	}

	@PostConstruct
	public void init() {
		this.restTemplate = new RestTemplate();
		final String url = "http://localhost:" + this.port;
		this.url = url + "/user";
		this.userIngredientUrl = url + "/useringredient";
		this.menuUrl = url + "/menu";
	}

	@BeforeAll
	void setUp() throws Exception {
		this.restTemplate.postForObject(url + "/subscribe", baseUser, Response.class);
		// set preferred UserIngredient
		String addingreURL = (userIngredientUrl + "/update/" + this.baseUser.getEmail() + "?type="
				+ IngredientTypeEnum.PREFERRED.name());
		Long[] ingreLst = new Long[] { (long) 1033, (long) 1056, (long) 1145, (long) 2047,(long) 4053 };
		this.restTemplate.put(addingreURL, ingreLst);

	}

	@AfterAll
	void tearDown() throws Exception {
		this.restTemplate.delete(url + "/" + this.baseUser.getEmail());
//		this.restTemplate.put(url, this.baseUser);

	}

	@Test
	void test_build_menu_manually() {
		fail("Not yet implemented");
	}

}
