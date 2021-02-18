package mg.logic.services;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.AllArgsConstructor;
import mg.boundaries.RecipeBoundary;
import mg.data.converters.RecipeConverter;
import mg.data.dal.MenuDataAccessLayer;
import mg.data.dal.MenuRecipeDataAccessRepo;
import mg.data.dal.RecipeDataAccessLayerRepo;
import mg.data.entities.joinentities.MenuRecipe;
import mg.data.entities.joinentities.MenuRecipeId;
import mg.logic.MenuRecipeService;

@Service
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class MenuRecipeServiceImple implements MenuRecipeService{
	private RecipeConverter recipeConverter;
	private MenuRecipeDataAccessRepo menuRecipeDAL;
	private MenuDataAccessLayer menuDAL;
	private RecipeDataAccessLayerRepo recipeDAL;
	
	@Override
	public List<RecipeBoundary> getAllForMenu(long menuId) {
		return menuRecipeDAL.findAllByMenu_Id(menuId).stream().map(mr -> mr.getRecipe()).map(this.recipeConverter::toBoundary).collect(Collectors.toList());	
	}

	@Override
	public MenuRecipe create(long menuId, long recipeId) {
		MenuRecipe mr = new MenuRecipe();
		mr.setMenu(this.menuDAL.findById(menuId).orElseThrow(() -> new RuntimeException("Error on Menu creation")));
		mr.setRecipe(
				this.recipeDAL.findById(recipeId).orElseThrow(() -> new RuntimeException("Error on Menu creation")));
		mr.setId(new MenuRecipeId(recipeId, menuId));
		return menuRecipeDAL.save(mr);
	}

}