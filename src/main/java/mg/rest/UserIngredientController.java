package mg.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.AllArgsConstructor;
import mg.data.entities.IngredientTypeEnum;
import mg.logic.UserIngredientService;

@RestController
@RequestMapping(path = "/useringredient")
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class UserIngredientController {
	private UserIngredientService userIngreService;

	@RequestMapping(path = "/bind/{userEmail}/{ingredientId}", method = RequestMethod.PUT)
	public void bind(@PathVariable("userEmail") String userEmail,
			@RequestParam(name = "type", required = true, defaultValue = "preferred") IngredientTypeEnum type,
			@PathVariable("ingredientId") long ingredientId) {
		userIngreService.bind(userEmail, ingredientId,type.toString());
	}
}
