package mg.logic.services;

import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import mg.boundaries.IngredientBoundary;
import mg.boundaries.Response;
import mg.data.converters.IngredientConverter;
import mg.data.dal.IngredientDataAccessRepo;
import mg.data.entities.IngredientTypeEnum;
import mg.logic.IngredientService;

@Service
@NoArgsConstructor
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class IngredientServiceImpl implements IngredientService {

	private IngredientDataAccessRepo ingredientDAL;
	private IngredientConverter converter;
	@Override
	public Response<IngredientBoundary> create(IngredientBoundary ingredientBoundary) {
		Response<IngredientBoundary> rv = new Response<IngredientBoundary>();
		rv.setData(this.converter.toBoundary(this.ingredientDAL.save(this.converter.fromBoundary(ingredientBoundary))));
		return rv;
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
		Response<IngredientBoundary> rv = new Response<IngredientBoundary>();
		rv.setData(this.converter.toBoundary(this.ingredientDAL.findById(ingredientId).orElseGet(() -> {
			rv.setSuccess(false);
			rv.setMessage("No Ingredient with this ID");
			return null;
		})));
		return rv;
	}

	@Override
	public Response<IngredientBoundary[]> findByName(String name) {
		Response<IngredientBoundary[]> rv = new Response<>();
		
		rv.setData(this.ingredientDAL.findAllByName(name)
				.stream()
				.map(this.converter::toBoundary)
				.collect(Collectors.toList())
				.toArray(new IngredientBoundary[0]));
		return rv;
	}

	@Override
	public Response<IngredientBoundary[]> getAll(int size, int page) {
		Response<IngredientBoundary[]> rv = new Response<>();
		rv.setData(this.ingredientDAL.findAll(PageRequest.of(page, size))
				.stream()
				.map(this.converter::toBoundary)
				.collect(Collectors.toList())
				.toArray(new IngredientBoundary[0]));
		return rv;
	}

	@Override
	public Response<IngredientBoundary[]> getAllByType(String userEmail, IngredientTypeEnum type, int size, int page) {
		// TODO Auto-generated method stub
		return null;
	}

}
