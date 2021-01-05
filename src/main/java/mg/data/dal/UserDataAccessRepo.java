package mg.data.dal;

import org.springframework.data.jpa.repository.JpaRepository;

import mg.data.entities.UserEntity;

public interface UserDataAccessRepo  extends JpaRepository<UserEntity, String>{
	
	public UserEntity findByEmail(String email);

}
