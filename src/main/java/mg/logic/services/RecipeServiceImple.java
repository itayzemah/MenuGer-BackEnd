package mg.logic.services;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import mg.boundaries.RecipeBoundary;
import mg.boundaries.Response;
import mg.data.converters.RecipeConverter;
import mg.data.dal.RecipeDataAccessLayerRepo;
import mg.data.dal.UserIngredientDataAccessRepo;
import mg.data.entities.IngredientEntity;
import mg.data.entities.IngredientTypeEnum;
import mg.data.entities.RecipeEntity;
import mg.logic.RecipeIngredientService;
import mg.logic.RecipeService;
import mg.logic.exceptions.RecipeNotFoundException;

@Service
@NoArgsConstructor
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class RecipeServiceImple implements RecipeService {
	private RecipeDataAccessLayerRepo recipeDal;
	private RecipeIngredientService recipeIngreService;
	private RecipeConverter recipeConverter;
	private UserIngredientDataAccessRepo userIngrdientsDAL;

	@Override
	@Transactional
	public Response<RecipeBoundary> create(RecipeBoundary recipe) {
		Response<RecipeBoundary> rv = new Response<RecipeBoundary>();
		RecipeEntity entity = recipeConverter.fromBoundary(recipe);
		entity = recipeDal.save(entity);
		rv.setData(recipeConverter.toBoundary(entity));
		return rv;
	}

	@Transactional(readOnly = true)
	@Override
	public Response<RecipeBoundary[]> getAll(int page, int size) {
		Response<RecipeBoundary[]> rv = new Response<RecipeBoundary[]>();
		RecipeBoundary[] recipeArr = recipeDal.findAll(PageRequest.of(page, size)).stream()
				.map(this.recipeConverter::toBoundary).collect(Collectors.toList()).toArray(new RecipeBoundary[0]);
		for (int i = 0; i < recipeArr.length; i++) {
			recipeArr[i].setIngredients(recipeIngreService.getAllForRecipe(recipeArr[i].getRecipeId()));
		}
		rv.setData(recipeArr);
		return rv;
	}

	@Transactional(readOnly = true)
	@Override
	public Response<RecipeBoundary[]> getByName(String name, int page, int size) {
		Response<RecipeBoundary[]> rv = new Response<RecipeBoundary[]>();
		RecipeBoundary[] recipeArr = this.recipeDal.findAllByName(name, PageRequest.of(page, size)).stream()
				.map(this.recipeConverter::toBoundary).collect(Collectors.toList()).toArray(new RecipeBoundary[0]);
		for (int i = 0; i < recipeArr.length; i++) {
			recipeArr[i].setIngredients(recipeIngreService.getAllForRecipe(recipeArr[i].getRecipeId()));
		}
		rv.setData(recipeArr);
		return rv;
	}


	@Override
	public Response<RecipeBoundary[]> getRecipeWIthoutForbIngredients(String userEmail) {
		Response<RecipeBoundary[]> rv = new Response<RecipeBoundary[]>();
		try {		
		// get All user FORBIDDEN ingredients
				List<IngredientEntity> uiArr = userIngrdientsDAL
						.findAllByUser_EmailAndType(userEmail, IngredientTypeEnum.FORBIDDEN.name(), PageRequest.of(0, 1000))
						.stream().map(ui -> ui.getIngredient()).collect(Collectors.toList());
				rv.setData(this.recipeDal.findDistinctByRecipeIngredients_IngredientNotIn(uiArr).stream()
						.map(this.recipeConverter::toBoundary).collect(Collectors.toList()).toArray(new RecipeBoundary[0]));
		}catch (Exception e) {
			rv.setMessage("Error in user details");
			rv.setSuccess(false);
		}	
		return rv;
}
	
	@Transactional(readOnly = true)
	@Override
	public Response<RecipeBoundary> getById(Long rId) {
		Response<RecipeBoundary> rv = new Response<RecipeBoundary>();
		RecipeBoundary recipeArr = this.recipeConverter.toBoundary(this.recipeDal.findById(rId)
				.orElseThrow(() -> new RecipeNotFoundException("Recipe with ID " + rId + " not found")));

		recipeArr.setIngredients(recipeIngreService.getAllForRecipe(recipeArr.getRecipeId()));
		
		rv.setData(recipeArr);
		return rv;
	}

}
