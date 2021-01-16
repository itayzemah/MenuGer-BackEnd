package mg.logic.services;

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
import mg.data.entities.RecipeEntity;
import mg.logic.RecipeService;

@Service
@NoArgsConstructor
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class RecipeServiceImple implements RecipeService {
	private RecipeIngredientDataAccessLayerRepo recipeIngreDal;
	private RecipeDataAccessLayerRepo recipeDal;
	private IngredientDataAccessRepo ingredientDAL;

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

	@Override
	public Response<RecipeBoundary[]> getAll(int page, int size) {
		Response<RecipeBoundary[]> rv = new Response<RecipeBoundary[]>();
		RecipeBoundary[] RecipeArr = recipeDal.findAll(PageRequest.of(page, size)).stream()
				.map(this.recipeConverter::toBoundary).collect(Collectors.toList()).toArray(new RecipeBoundary[0]);
		rv.setData(RecipeArr);
		return rv;
	}

	@Override
	public Response<RecipeBoundary> getByName(int page, int size) {
		// TODO Auto-generated method stub
		return null;
	}

}
