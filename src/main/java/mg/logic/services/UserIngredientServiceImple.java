package mg.logic.services;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import mg.boundaries.IngredientBoundary;
import mg.boundaries.Response;
import mg.data.dal.UserDataAccessRepo;
import mg.data.dal.UserIngredientDataAccessRepo;
import mg.data.entities.IngredientTypeEnum;
import mg.data.entities.UserEntity;
import mg.data.entities.joinentities.UserIngredient;
import mg.data.entities.joinentities.UserIngredientKey;
import mg.logic.IngredientService;
import mg.logic.UserIngredientService;
import mg.logic.exceptions.IngredientNotFoundException;
import mg.logic.exceptions.UserIngredientNotFound;
import mg.logic.exceptions.UserNotFoundException;

@Service
@NoArgsConstructor
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class UserIngredientServiceImple implements UserIngredientService {
	private UserDataAccessRepo userDAL;
	private IngredientService ingredientService;
	private UserIngredientDataAccessRepo userIngreDAL;

	@Override
	public UserIngredient update(String userEmail, Long ingredientId, String type, Double rate) {
		Response<IngredientBoundary> ingredientRes = this.ingredientService.findById(ingredientId);
		if (!ingredientRes.getSuccess()) {
			throw new IngredientNotFoundException(ingredientRes.getMessage());
		}

		UserEntity userEntity = this.userDAL.findById(userEmail)
				.orElseThrow(() -> new UserNotFoundException("User " + userEmail + " not found"));
		
		if (rate != null && (rate > 10 || rate < 0)) {
			throw new RuntimeException("rate out of range 0-10");
		}
		// For ORM loading
		Set<UserIngredient> userUserIngredients = new HashSet<UserIngredient>(
				this.userIngreDAL.findAllById_UserEmail(userEmail, PageRequest.of(0, 1000)));
		
		UserIngredient userIngredient = new UserIngredient();
		userIngredient.setName(ingredientRes.getData().getName());
		userIngredient.setType(type);
		userIngredient.setRate(type.equals(IngredientTypeEnum.PREFERRED.name()) ? rate : null);
		UserIngredientKey key = new UserIngredientKey(userEntity.getEmail(), ingredientRes.getData().getId());
		userIngredient.setId(key);
		
		return this.userIngreDAL.save(userIngredient);
	}

	@Override
	public Response<List<IngredientBoundary>> getAllByType(String userEmail, String type, int size, int page) {
		Response<List<IngredientBoundary>> rv = new Response<List<IngredientBoundary>>();
		if (type.isEmpty() || type == null) {
			rv.setSuccess(false);
			rv.setMessage("type can not be empty or null");
			return rv;
		}

		List<UserIngredient> lst = this.userIngreDAL.findAllByTypeAndId_UserEmail(type, userEmail,
				PageRequest.of(page, size));
		
		rv.setData(lst.stream().map((ui) -> {
			IngredientBoundary ingredient = new IngredientBoundary();
			ingredient.setId(ui.getId().getIngredientId());
			ingredient.setName(ui.getName());
			return ingredient;
		})// this.ingredientService.findById(ui.getId().getIngredientId()).getData()
			// .map(this.ingredientConverter::toBoundary)
				.collect(Collectors.toList()));
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

		List<UserIngredient> lst = this.userIngreDAL.findAllByTypeAndId_UserEmail(type,userEmail, 
				PageRequest.of(page, size));
		rv.setData(lst);
		return rv;
	}

	@Override
	public Response<Map<String, IngredientBoundary[]>> getAll(String userEmail, int size, int page) {
		Response<Map<String, IngredientBoundary[]>> rv = new Response<Map<String, IngredientBoundary[]>>();
		Map<String, IngredientBoundary[]> map = new HashMap<String, IngredientBoundary[]>();

		List<UserIngredient> lst = this.userIngreDAL.findAllById_UserEmail(userEmail, PageRequest.of(page, size));

		List<IngredientBoundary> lstF = new ArrayList<>();
		List<IngredientBoundary> lstP = new ArrayList<>();

		lst.forEach(i -> {
			if (i.getType().equals(IngredientTypeEnum.PREFERRED.name())) {
				lstP.add(this.ingredientService.findById(i.getId().getIngredientId()).getData());
//				lstP.add(this.ingredientConverter.toBoundary(i.getIngredient()));
			} else {
				lstF.add(this.ingredientService.findById(i.getId().getIngredientId()).getData());
//				lstF.add(/*this.ingredientConverter.toBoundary(*/i.getIngredient()/*)*/);
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
		List<UserIngredient> removedUI = this.userIngreDAL.deleteByTypeAndId_UserEmail(type, userEmail);

		if (type == IngredientTypeEnum.PREFERRED.name()) {
			for (UserIngredient userIngredient : removedUI) {
				int idx = ingredientsLst.indexOf(userIngredient.getId().getIngredientId());
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
	public UserIngredient getOne(String user, Long ingredientId) {
		return this.userIngreDAL.findById(new UserIngredientKey(user, ingredientId))
				.orElseThrow(() -> new UserIngredientNotFound(user, ingredientId));
//				(new UserIngredient(new UserIngredientKey(user, ingredientId),, IngredientTypeEnum.PREFERRED.name(), 0.0));

	}

	@Override
	public double goodScore(String userEmail, Long ingredientId) {
		return changeRate(userEmail, ingredientId, 0.5);
	}

	@Override
	public double badScore(String userEmail, Long ingredientId) {
		return changeRate(userEmail, ingredientId, -0.5);

	}

	private double changeRate(String userEmail, Long ingredientId, double delta) {
		UserIngredient userIngredient = this.userIngreDAL.findById(new UserIngredientKey(userEmail, ingredientId))
				.orElse(new UserIngredient(new UserIngredientKey(userEmail, ingredientId),
						this.ingredientService.findById(ingredientId).getData().getName(),
						IngredientTypeEnum.PREFERRED.name(), 0.0));
		userIngredient.setRate(userIngredient.getRate() + delta);
		this.userIngreDAL.save(userIngredient);
		return userIngredient.getRate();
	}

}
