package mg.rest;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.AllArgsConstructor;
import mg.boundaries.IngredientBoundary;
import mg.boundaries.Response;
import mg.data.entities.IngredientTypeEnum;
import mg.logic.UserIngredientService;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping(path = "/useringredient")
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class UserIngredientController {
	private UserIngredientService userIngreService;

	@RequestMapping(path = "/update/{userEmail}/{ingredientId}", method = RequestMethod.PUT)
	public void update(
			@PathVariable("userEmail") String userEmail,
			@RequestParam(name = "type", required = true, defaultValue = "preferred") IngredientTypeEnum type,
			@PathVariable("ingredientId") long ingredientId) {
		userIngreService.update(userEmail, ingredientId, type.toString());
	}
	@RequestMapping(path = "/bind/{userEmail}", method = RequestMethod.PUT)
	public void bind(@PathVariable("userEmail") String userEmail,
			@RequestParam(name = "type", required = true, defaultValue = "preferred") IngredientTypeEnum type,
			@RequestBody Long[] ingredients) {
		userIngreService.bind(userEmail, ingredients, type.toString());
	}

	@RequestMapping(path = "by/type/{userEmail}", method = RequestMethod.GET)
	public Response<IngredientBoundary[]> getAllIngreOfUserByType(
			@PathVariable("userEmail") String userEmail,
			@RequestParam(name = "type", required = false, defaultValue = "") IngredientTypeEnum type,
			@RequestParam(name = "page", required = false, defaultValue = "0") int page,
			@RequestParam(name = "size", required = false, defaultValue = "1000") int size
			) {
		return userIngreService.getAllByType(userEmail,type==null?"": type.toString(), size, page);
	}
	@RequestMapping(path = "/{userEmail}", method = RequestMethod.GET)
	public Response<Map<String, IngredientBoundary[]>> getAllIngreOfUser(
			@PathVariable("userEmail") String userEmail,
			@RequestParam(name = "page", required = false, defaultValue = "0") int page,
			@RequestParam(name = "size", required = false, defaultValue = "1000") int size
			) {
		return userIngreService.getAll(userEmail, size, page);
	}
}
