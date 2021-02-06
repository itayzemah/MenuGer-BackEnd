package mg.logic.services;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.AllArgsConstructor;
import mg.boundaries.RecipeBoundary;
import mg.data.converters.RecipeConverter;
import mg.data.dal.MenuRecipeDataAccessRepo;
import mg.logic.MenuRecipeService;

@Service
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class MenuRecipeServiceImple implements MenuRecipeService{
	private RecipeConverter recipeConverter;
	private MenuRecipeDataAccessRepo menuRecipeDAL;
	
	@Override
	public List<RecipeBoundary> getAllForMenu(long menuId) {
		return menuRecipeDAL.findAllByMenu_Id(menuId).stream().map(mr -> mr.getRecipe()).map(this.recipeConverter::toBoundary).collect(Collectors.toList());	
	}

}
