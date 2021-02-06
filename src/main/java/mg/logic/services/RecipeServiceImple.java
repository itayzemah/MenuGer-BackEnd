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
import mg.data.dal.IngredientDataAccessRepo;
import mg.data.dal.RecipeDataAccessLayerRepo;
import mg.data.dal.RecipeIngredientDataAccessLayerRepo;
import mg.data.entities.IngredientEntity;
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
	public List<RecipeBoundary> getRecipeIWIthngredientNotIn(List<IngredientEntity> uiArr) {
		return this.recipeDal.findDistinctByRecipeIngredients_IngredientNotIn(uiArr).stream()
				.map(this.recipeConverter::toBoundary).collect(Collectors.toList());
	}

	@Override
	public RecipeBoundary getById(Long rId) {
		return this.recipeConverter.toBoundary(this.recipeDal.findById(rId)
				.orElseThrow(() -> new RecipeNotFoundException("Recipe with ID " + rId + " not found")));

	}

}
