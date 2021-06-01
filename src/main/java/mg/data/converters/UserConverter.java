package mg.data.converters;

import org.springframework.stereotype.Component;

import lombok.NoArgsConstructor;
import mg.boundaries.UserBoundary;
import mg.data.entities.UserEntity;

@NoArgsConstructor
@Component
public class UserConverter {

	public UserEntity fromBoundary(UserBoundary boundary) {
		UserEntity rv = new UserEntity();
		rv.setEmail(boundary.getEmail());
		rv.setFullName(boundary.getFullName());
		rv.setGender(boundary.getGender());
		rv.setActive(true);
		return rv;
	}
	
	public UserBoundary toBoundary(UserEntity entity) {
		UserBoundary rv = new UserBoundary();
		rv.setEmail(entity.getEmail());
		rv.setFullName(entity.getFullName());
		rv.setGender(entity.getGender());

		return rv;
	}
	
}
