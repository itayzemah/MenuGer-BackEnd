package mg.logic.services;

import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import mg.boundaries.IngredientBoundary;
import mg.boundaries.Response;
import mg.data.converters.IngredientConverter;
import mg.data.dal.IngredientDataAccessRepo;
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
		if (ingredientBoundary.getName() == null || ingredientBoundary.getName().isEmpty()) {
			rv.setSuccess(false);
			rv.setMessage("Name of Ingredient can not be empty");
		} else if (this.ingredientDAL.findAllByName(ingredientBoundary.getName()).size() > 0) {
			rv.setSuccess(false);
			rv.setMessage("Ingredient with this name is already exist: " + ingredientBoundary.getName());
		} else {
			rv.setData(this.converter
					.toBoundary(this.ingredientDAL.save(this.converter.fromBoundary(ingredientBoundary))));

		}
		return rv;
	}

	@Override
	public Response<Void> remove(Long ingredientId) {
		Response<Void> rv = new Response<Void>();
		if (!this.ingredientDAL.existsById(ingredientId)) {
			rv.setSuccess(false);
			rv.setMessage("No Ingredient with this Id");
		} else {
			this.ingredientDAL.deleteById(ingredientId);
		}
		return rv;
	}

	@Override
	public Response<Void> update(IngredientBoundary ingredientBoundary) {
		Response<Void> rv = new Response<Void>();
		if (!this.ingredientDAL.existsById(ingredientBoundary.getId())) {
			rv.setSuccess(false);
			rv.setMessage("No Ingredient with this Id");
		} else if (ingredientBoundary.getName() == null || ingredientBoundary.getName().isEmpty()) {
			rv.setSuccess(false);
			rv.setMessage("Name of Ingredient can not be empty");
		} else {
			this.ingredientDAL.save(this.converter.fromBoundary(ingredientBoundary));
		}
		return rv;
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

		rv.setData(this.ingredientDAL.findAllByName(name).stream().map(this.converter::toBoundary)
				.collect(Collectors.toList()).toArray(new IngredientBoundary[0]));
		return rv;
	}

	@Override
	public Response<IngredientBoundary[]> getAll(int size, int page) {
		Response<IngredientBoundary[]> rv = new Response<>();
		rv.setData(this.ingredientDAL.findAll(PageRequest.of(page, size)).stream().map(this.converter::toBoundary)
				.collect(Collectors.toList()).toArray(new IngredientBoundary[0]));
		return rv;
	}

	@Override
	public Response<Void> removeAll() {
		Response<Void> rv = new Response<Void>();
		this.ingredientDAL.deleteAll();
		return rv;
	}

}
