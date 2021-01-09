package mg.data.dal;

import org.springframework.data.jpa.repository.JpaRepository;

import mg.data.entities.joinentities.UserIngredient;
import mg.data.entities.joinentities.UserIngredientKey;

public interface UserIngredientDataAccessRepo extends JpaRepository<UserIngredient, UserIngredientKey> {

}
