package mg.controller;

import static org.assertj.core.api.Assertions.assertThat;

import javax.annotation.PostConstruct;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.client.RestTemplate;

import mg.boundaries.Response;
import mg.boundaries.UserBoundary;
import mg.boundaries.helper.UserLoginBoundary;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class UserControllerTests {
	private RestTemplate restTemplate;
	private String url;
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
		this.url = url + "/user";
	}
	
	@BeforeEach
	void setUp() throws Exception {
		this.restTemplate.postForObject(url+"/subscribe",baseUser, Response.class);
	}

	@AfterEach
	void tearDown() throws Exception {
		this.restTemplate.delete(url+"/"+this.baseUser.getEmail());
//		this.restTemplate.put(url, this.baseUser);

	}

	@Test
	void test_subscribe() {
	}

	@Test
	void test_login() {
		Response<UserBoundary> response = new Response<UserBoundary>();
        HttpEntity<UserLoginBoundary> requestEntity = new HttpEntity<>(new UserLoginBoundary(baseUser.getEmail()));
        response = restTemplate.exchange(url+"/login",HttpMethod.POST,requestEntity,new ParameterizedTypeReference<Response<UserBoundary>>() {}).getBody();
		assertThat(response.getSuccess());
		assertThat(response.getData().equals(this.baseUser));
	}
	@Test
	void test_update_user_name_and_check_changed_successed() {
		String newName = "new name";
		UserBoundary changedUser = this.baseUser;
		changedUser.setFullName(newName);
		this.restTemplate.put(url, changedUser);
		
//		Response<UserBoundary> userLogedIn = this.restTemplate.postForObject(url+"/login", new UserLoginBoundary(baseUser.getEmail()), Response.class);
        HttpEntity<UserLoginBoundary> requestEntity = new HttpEntity<>(new UserLoginBoundary(baseUser.getEmail()));
		Response<UserBoundary> userLogedIn = restTemplate.exchange(url + "/login", HttpMethod.POST, requestEntity,
				new ParameterizedTypeReference<Response<UserBoundary>>() {
				}).getBody();
		assertThat(userLogedIn.getSuccess());
		assertThat(userLogedIn.getData().getFullName().equals(newName));
	}

}
