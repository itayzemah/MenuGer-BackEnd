package mg.logic.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import mg.data.converters.IngredientConverter;
import mg.data.converters.UserConverter;
import mg.data.dal.IngredientDataAccessRepo;
import mg.data.dal.UserDataAccessRepo;
import mg.data.dal.UserIngredientDataAccessRepo;
import mg.data.entities.IngredientEntity;
import mg.data.entities.UserEntity;
import mg.data.entities.joinentities.UserIngredient;
import mg.data.entities.joinentities.UserIngredientKey;
import mg.logic.UserIngredientService;

@Service
@NoArgsConstructor
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class UserIngredientServiceImple implements UserIngredientService {
	private UserDataAccessRepo userDAL;
	private IngredientDataAccessRepo ingreDAL;
	private UserIngredientDataAccessRepo userIngreDAL;

	@Override
	public void bind(String userEmail, long ingredientId,String type) {
		IngredientEntity ingreEntity = this.ingreDAL.findById(ingredientId).orElseGet(() -> {
			// TODO check ingre exist
			return null;
		});
		UserEntity userEntity = this.userDAL.findById(userEmail).orElseGet(() -> {
			// TODO check user exist
			return null;
		});
		UserIngredient userIngredient = new UserIngredient();
		userIngredient.setIngredient(ingreEntity);
		userIngredient.setUser(userEntity);
		userIngredient.setType(type);
		UserIngredientKey key = new UserIngredientKey(userEntity.getEmail(), ingreEntity.getId());
		userIngredient.setId(key);
		this.userIngreDAL.save(userIngredient);
	}

}
