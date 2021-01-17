package mg.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.AllArgsConstructor;
import mg.boundaries.MenuBoundary;
import mg.logic.MenuService;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping(path="/menu")
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class MenuController {
	private MenuService menuService;
	
	@RequestMapping(path ="/{userEmail}")
	public MenuBoundary createMenu(@PathVariable String userEmail,
			@RequestParam(defaultValue = "6", required = false) int days) {
		return null;
	}

}
