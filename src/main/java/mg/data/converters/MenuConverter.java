package mg.data.converters;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import lombok.NoArgsConstructor;
import mg.boundaries.MenuBoundary;
import mg.data.entities.MenuEntity;

@Component
//@AllArgsConstructor
@NoArgsConstructor(onConstructor = @__(@Autowired))
public class MenuConverter {
	
	public MenuBoundary toBoundary(MenuEntity menu) {
		MenuBoundary rv = new MenuBoundary();
		rv.setId(menu.getId());
//		rv.setRecipes(menu.getMenuRecipes().stream().map(mr -> mr.getRecipe())
//				.map(this.recipeConverter::toBoundary).collect(Collectors.toList()));
		rv.setTimestampe(menu.getTimestamp());
		return rv;
	}

}
