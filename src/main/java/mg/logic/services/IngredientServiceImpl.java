package mg.logic.services;

import org.springframework.stereotype.Service;

import mg.boundaries.IngredientBoundary;
import mg.boundaries.Response;
import mg.logic.IngredientService;

@Service
public class IngredientServiceImpl implements IngredientService {

	@Override
	public Response<IngredientBoundary> create(IngredientBoundary ingredientBoundary) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Response<Void> remove(Long ingredientId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Response<Void> update(IngredientBoundary ingredientBoundary) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Response<IngredientBoundary> findById(Long ingredientId) {
		// TODO Auto-generated method stub
		return null;
	}

}
