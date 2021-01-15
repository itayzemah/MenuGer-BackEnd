package mg.rest;

import java.util.Collections;
import java.util.Map;

import javax.validation.constraints.Email;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import mg.boundaries.Response;
import mg.boundaries.UserBoundary;
import mg.boundaries.helper.UserLoginBoundary;
import mg.logic.UserService;
import mg.logic.exceptions.UserAlreadyExistException;
import mg.logic.exceptions.UserNotFoundException;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping(path="/user")
public class UserController {

	private UserService userService;
	
	@Autowired
	public UserController(UserService userService) {
		super();
		this.userService = userService;
	}

	@RequestMapping
			(path="/subscribe", method = RequestMethod.POST,
			consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public Response<UserBoundary> subscribe(@RequestBody UserBoundary user) {
		return this.userService.create(user);
	}
	
	@RequestMapping
	(path="/unsubscribe", method = RequestMethod.DELETE,
	consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public Response<UserBoundary> unsubscribe(@RequestBody UserLoginBoundary user) {
		return this.userService.unsubscribe(user);
	} 
	
	@RequestMapping
	(path="/login", method = RequestMethod.POST,
	consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public Response<UserBoundary> login(@RequestBody UserLoginBoundary user) {
		return this.userService.login(user);
	}
	@RequestMapping
	(path="/update", method = RequestMethod.POST,
	consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public void updateUser(@RequestBody UserBoundary user) {
		this.userService.updateUser(user);
	}
	@RequestMapping
	(path="/all", method = RequestMethod.GET,
	 produces = MediaType.APPLICATION_JSON_VALUE)
	public Response<UserBoundary[]> getAllUsers() {
		return this.userService.getAll();
	}
	
	@RequestMapping(path = "/{email}/exist", method = RequestMethod.GET)
	@ResponseBody
	public Boolean isUserExist(@Email @PathVariable("email") String email) {
		return this.userService.isUserExist(email);
	}
	
	@ExceptionHandler
	@ResponseStatus(code = HttpStatus.NOT_FOUND)
	public Response<String> handleException(UserNotFoundException e) {
		Response<String> response = new Response<String>();
		String error = e.getMessage();
		if (error == null) {
			error = "Not found";
		}
		response.setData(error);
		response.setSuccess(false);
		response.setMessage(error);
		return response;
	}
	
	@ExceptionHandler
	@ResponseStatus(code = HttpStatus.BAD_REQUEST)
	public Map<String, String> handleException(UserAlreadyExistException e) {
		String error = e.getMessage();
		if (error == null) {
			error = "User Aleady Exist!";
		}
		return Collections.singletonMap("error", error);
	}
}
