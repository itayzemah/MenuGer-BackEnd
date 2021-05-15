package mg.logic.services;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

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
	public UserIngredient update(String userEmail, long ingredientId, String type, Double rate) {
		IngredientEntity ingreEntity = this.ingreDAL.findById(ingredientId)
				.orElseThrow(() -> new IngredientNotFoundException("Ingredient " + ingredientId + " not found"));

		UserEntity userEntity = this.userDAL.findById(userEmail)
				.orElseThrow(() -> new UserNotFoundException("User " + userEmail + " not found"));

		UserIngredient userIngredient = new UserIngredient();
		userIngredient.setIngredient(ingreEntity);
		userIngredient.setUser(userEntity);
		userIngredient.setType(type);
		userIngredient.setRate(rate);

		UserIngredientKey key = new UserIngredientKey(userEntity.getEmail(), ingreEntity.getId());
		userIngredient.setId(key);

		return this.userIngreDAL.save(userIngredient);
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
	public Response<List<UserIngredient>> getAllUserIngredientByType(String userEmail, String type, int size,
			int page) {
		Response<List<UserIngredient>> rv = new Response<List<UserIngredient>>();
		if (type.isEmpty() || type == null) {
			rv.setSuccess(false);
			rv.setMessage("type can not be empty or null");
			return rv;
		}

		List<UserIngredient> lst = this.userIngreDAL.findAllByUser_EmailAndType(userEmail, type,
				PageRequest.of(page, size));
		rv.setData(lst);
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
	@Transactional
	public void update(String userEmail, Long[] ingredients, String type) {
		ArrayList<Long> ingredientsLst = new ArrayList<>(Arrays.asList(ingredients));
		List<UserIngredient> removedUI = this.userIngreDAL.deleteByUser_EmailAndType(userEmail, type);

		if (type == IngredientTypeEnum.PREFERRED.name()) {
			for (UserIngredient userIngredient : removedUI) {
				int idx = ingredientsLst.indexOf(userIngredient.getId().getIngredientid());
				if (idx != -1) {
					this.update(userEmail, ingredientsLst.get(idx), IngredientTypeEnum.PREFERRED.name(),
							userIngredient.getRate());
					ingredientsLst.remove(idx);
				}
			}

		}
		for (int i = 0; i < ingredientsLst.size(); i++) {

			this.update(userEmail, ingredientsLst.get(i), type,
					type == IngredientTypeEnum.PREFERRED.name() ? 5.0 : null);
		}
	}

	@Override
	public void unbind(String userEmail, Long[] ingredients) {
		for (int i = 0; i < ingredients.length; i++) {
			this.userIngreDAL.deleteById(new UserIngredientKey(userEmail, ingredients[i]));
		}
	}

	@Override
	public UserIngredient getOne(String user, long ingredientsId) {
		return this.userIngreDAL.findById(new UserIngredientKey(user, ingredientsId))
				.orElseThrow(() -> new RuntimeException("Not Found"));

	}

	@Override
	public double goodScore(String userEmail, long ingredientId) {
		return changeRate(userEmail, ingredientId, -0.5);
	}

	@Override
	public double badScore(String userEmail, long ingredientId) {
		return changeRate(userEmail, ingredientId, 0.5);

	}

	private double changeRate(String userEmail, long ingredientId, double delta) {
		UserIngredient userIngredient = this.getOne(userEmail, ingredientId);
		userIngredient.setRate(userIngredient.getRate() + delta);
		return userIngredient.getRate();
	}

}
