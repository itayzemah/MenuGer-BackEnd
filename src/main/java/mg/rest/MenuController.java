package mg.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.AllArgsConstructor;
import mg.boundaries.MenuBoundary;
import mg.boundaries.helper.MenuBuilderBoundary;
import mg.boundaries.helper.MenuSearchBoundary;
import mg.logic.MenuService;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping(path = "/menu")
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class MenuController {
	private MenuService menuService;

	@RequestMapping(path = "/{userEmail}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public MenuBoundary createMenu(@PathVariable String userEmail,
			@RequestParam(defaultValue = "6", required = false) int days) {
		return menuService.createMenu(userEmail, days);
	}

	@RequestMapping(path = "/all/{userEmail}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public MenuBoundary[] getAll(@PathVariable String userEmail,
			@RequestParam(name = "page", required = false, defaultValue = "0") int page,
			@RequestParam(name = "size", required = false, defaultValue = "10") int size) {
		return menuService.getAll(userEmail, page, size);
	}

	@RequestMapping(path = "/build", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public MenuBoundary buildMenu(@RequestBody MenuBuilderBoundary menubuilderBoundary) {
		return menuService.buildMenu(menubuilderBoundary.getUserEmail(), menubuilderBoundary.getRecipeId());
	}
	
	@RequestMapping(path = "/search", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public MenuBoundary searchBetweenDatesCreated(@RequestBody MenuSearchBoundary menuSearchBoundary) {
		return menuService.searchMenu(menuSearchBoundary.getFromDate(), menuSearchBoundary.getToDate());
	}

}
