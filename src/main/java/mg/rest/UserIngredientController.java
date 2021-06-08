package mg.rest;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
import mg.data.entities.joinentities.UserIngredient;
import mg.logic.UserIngredientService;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping(path = "/useringredient")
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class UserIngredientController {
	private UserIngredientService userIngreService;

	@RequestMapping(path = "/update1/{userEmail}/{ingredientId}", method = RequestMethod.PUT)
	public void update(@PathVariable("userEmail") String userEmail,
			@RequestParam(name = "type", required = true, defaultValue = "preferred") IngredientTypeEnum type,
			@RequestParam(name = "weight", required = false, defaultValue = "null") Double weight,
			@PathVariable("ingredientId") Long ingredientId) {
		userIngreService.update(userEmail, ingredientId, type.toString(), weight);
	}

	// PUT /update/{userEmail}?type=???<IngredientTypeEnum>
	@RequestMapping(path = "/update/{userEmail}", method = RequestMethod.PUT)
	public void bind(@PathVariable("userEmail") String userEmail,
			@RequestParam(name = "type", required = true, defaultValue = "preferred") IngredientTypeEnum type,
			@RequestBody Long[] ingredients) {
		userIngreService.update(userEmail, ingredients, type.toString());
	}

	@RequestMapping(path = "by/type/{userEmail}", method = RequestMethod.GET)
	public Response<List<IngredientBoundary>> getAllIngreOfUserByType(@PathVariable("userEmail") String userEmail,
			@RequestParam(name = "type", required = false, defaultValue = "preferred") IngredientTypeEnum type,
			@RequestParam(name = "page", required = false, defaultValue = "0") int page,
			@RequestParam(name = "size", required = false, defaultValue = "1000") int size) {
		return userIngreService.getAllByType(userEmail, type == null ? "" : type.toString(), size, page);
	}

	@RequestMapping(path = "/{userEmail}", method = RequestMethod.GET)
	public Response<Map<String, IngredientBoundary[]>> getAllIngreOfUser(@PathVariable("userEmail") String userEmail,
			@RequestParam(name = "page", required = false, defaultValue = "0") int page,
			@RequestParam(name = "size", required = false, defaultValue = "1000") int size) {
		return userIngreService.getAll(userEmail, size, page);
	}

	@RequestMapping(path = "/{userEmail}", method = RequestMethod.DELETE)
	public void removeUserIngredient(@PathVariable("userEmail") String userEmail,
			@RequestParam(name = "ingredients", required = true) String ingredients) {
		Long[] ingredientsArr = Arrays.stream(ingredients.split(",")).map(i -> Long.parseLong(i))
				.collect(Collectors.toList()).toArray(new Long[0]);
		userIngreService.unbind(userEmail, ingredientsArr);
	}
	
	@RequestMapping(path = "/{userEmail}/all", method = RequestMethod.DELETE)
	public void removeAllUserIngredient(@PathVariable("userEmail") String userEmail) {
		userIngreService.removeAll(userEmail);
	}
}
