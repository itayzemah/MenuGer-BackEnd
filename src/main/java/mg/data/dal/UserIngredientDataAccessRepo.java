package mg.data.dal;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import mg.data.entities.joinentities.UserIngredient;
import mg.data.entities.joinentities.UserIngredientKey;

public interface UserIngredientDataAccessRepo extends JpaRepository<UserIngredient, UserIngredientKey> {

	public List<UserIngredient> findAllByUser_Email(@Param("user_email") String userEmail, Pageable pageRequest);

	public List<UserIngredient> findAllByUser_EmailAndType(@Param("user_email") String userEmail,
			@Param("type") String type, Pageable pageRequest);
}
