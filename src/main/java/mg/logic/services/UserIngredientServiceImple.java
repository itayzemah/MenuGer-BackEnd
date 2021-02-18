package mg.logic.services;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
import mg.data.dal.UserDataAccessRepo;
import mg.data.dal.UserIngredientDataAccessRepo;
import mg.data.entities.IngredientEntity;
import mg.data.entities.IngredientTypeEnum;
import mg.data.entities.UserEntity;
import mg.data.entities.joinentities.UserIngredient;
import mg.data.entities.joinentities.UserIngredientKey;
import mg.logic.UserIngredientService;
import mg.logic.exceptions.IngredientNotFoundException;
import mg.logic.exceptions.UserNotFoundException;

@Service
@NoArgsConstructor
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class UserIngredientServiceImple implements UserIngredientService {
	private UserDataAccessRepo userDAL;
	private IngredientDataAccessRepo ingreDAL;
	private UserIngredientDataAccessRepo userIngreDAL;
	private IngredientConverter ingredientConverter;

	@Override
	public void update(String userEmail, long ingredientId, String type) {
		IngredientEntity ingreEntity = this.ingreDAL.findById(ingredientId)
				.orElseThrow(() -> new IngredientNotFoundException("Ingredient " + ingredientId + " not found"));
		UserEntity userEntity = this.userDAL.findById(userEmail)
				.orElseThrow(() -> new UserNotFoundException("User " + userEmail + " not found"));
		UserIngredient userIngredient = new UserIngredient();
		userIngredient.setIngredient(ingreEntity);
		userIngredient.setUser(userEntity);
		userIngredient.setType(type);
		UserIngredientKey key = new UserIngredientKey(userEntity.getEmail(), ingreEntity.getId());
		userIngredient.setId(key);
		this.userIngreDAL.save(userIngredient);
	}

	@Override
	public Response<IngredientBoundary[]> getAllByType(String userEmail, String type, int size, int page) {
		Response<IngredientBoundary[]> rv = new Response<IngredientBoundary[]>();
		if (type.isEmpty() || type == null) {
			rv.setSuccess(false);
			rv.setMessage("type can not be empty or null");
			return rv;
		}

		List<UserIngredient> lst = this.userIngreDAL.findAllByUser_EmailAndType(userEmail, type,
				PageRequest.of(page, size));
		rv.setData(lst.stream().map((ui) -> ui.getIngredient()).map(this.ingredientConverter::toBoundary)
				.collect(Collectors.toList()).toArray(new IngredientBoundary[0]));
		return rv;
	}

	@Override
	public Response<Map<String, IngredientBoundary[]>> getAll(String userEmail, int size, int page) {
		Response<Map<String, IngredientBoundary[]>> rv = new Response<Map<String, IngredientBoundary[]>>();
		Map<String, IngredientBoundary[]> map = new HashMap<String, IngredientBoundary[]>();
		List<UserIngredient> lst = this.userIngreDAL.findAllByUser_Email(userEmail, PageRequest.of(page, size));

		List<IngredientBoundary> lstF = new ArrayList<>();
		List<IngredientBoundary> lstP = new ArrayList<>();

		lst.forEach(i -> {
			if (i.getType().equals(IngredientTypeEnum.PREFERRED.name())) {
				lstP.add(this.ingredientConverter.toBoundary(i.getIngredient()));
			} else {
				lstF.add(this.ingredientConverter.toBoundary(i.getIngredient()));
			}
		});
		map.put(IngredientTypeEnum.PREFERRED.name(), lstP.toArray(new IngredientBoundary[0]));
		map.put(IngredientTypeEnum.FORBIDDEN.name(), lstF.toArray(new IngredientBoundary[0]));
		rv.setData(map);
		return rv;
	}

	@Override
	public void bind(String userEmail, Long[] ingredients, String type) {
		for (int i = 0; i < ingredients.length; i++) {
			this.update(userEmail, ingredients[i], type);
		}

	}
	
	@Override
	public void unbind(String userEmail, Long[] ingredients) {
		for (int i = 0; i < ingredients.length; i++) {
			this.userIngreDAL.deleteById(new UserIngredientKey(userEmail,ingredients[i]));
		}
	}

}
