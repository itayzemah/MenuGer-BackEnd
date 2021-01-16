package mg.logic.services;

import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import mg.boundaries.Response;
import mg.boundaries.UserBoundary;
import mg.boundaries.helper.UserLoginBoundary;
import mg.data.converters.UserConverter;
import mg.data.dal.UserDataAccessRepo;
import mg.data.entities.UserEntity;
import mg.logic.UserService;
import mg.logic.exceptions.UserAlreadyExistException;
import mg.logic.exceptions.UserNotFoundException;

@Service
@NoArgsConstructor
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class UserServiceImpl implements UserService {
	private UserDataAccessRepo userDAL;
	private UserConverter userConverter;

	@Override
	public Boolean isUserExist(String userEmail) {

		return userDAL.existsById(userEmail);
	}

	@Transactional
	@Override
	public Response<UserBoundary> create(UserBoundary user) {
		if (isUserExist(user.getEmail())) {
			throw new UserAlreadyExistException(user.getEmail());
		}
		emailValidationCheck(user.getEmail());
		Response<UserBoundary> rv = new Response<>();
		rv.setData(this.userConverter.toBoundary(this.userDAL.save(this.userConverter.fromBoundary(user))));
		return rv;
	}

	@Transactional
	@Override
	public Response<UserBoundary> unsubscribe(UserLoginBoundary user) {
		Response<UserBoundary> rv = new Response<>();
		Optional<UserEntity> opUser = this.userDAL.findById(user.getEmail());
		if (!opUser.isPresent()) {
			rv.setMessage("User with the id: " + user.getEmail() + " is not in registered");
			rv.setSuccess(false);
			return rv;
		}
		try {
			this.userDAL.delete(opUser.get());
			rv.setMessage("User" + user.getEmail() + "Unsubscribed successfully");
		} catch (IllegalArgumentException ex) {
			rv.setMessage("Wrong User");
			rv.setSuccess(false);
		}

		return rv;
	}

	@Transactional(readOnly = true)
	@Override
	public Response<UserBoundary> login(UserLoginBoundary user) {
		Response<UserBoundary> rv = new Response<>();
		Optional<UserEntity> opUser = this.userDAL.findById(user.getEmail());
		if (!opUser.isPresent()) {
			throw new UserNotFoundException("User with the id: " + user.getEmail() + " is not in registered");
		}
		rv.setData(this.userConverter.toBoundary(opUser.get()));
		return rv;
	}

	@Transactional
	@Override
	public void updateUser(UserBoundary user) {
		UserBoundary userFromDB = userConverter.toBoundary(
				userDAL.findById(user.getEmail()).orElseThrow(() -> new UserNotFoundException(user.getEmail())));
		this.userDAL.save(userConverter.fromBoundary(userFromDB));
	}

	@Transactional(readOnly = true)
	@Override
	public Response<UserBoundary[]> getAll() {
		Response<UserBoundary[]> rv = new Response<>();
		List<UserBoundary> lst = this.userDAL.findAll().stream().map(this.userConverter::toBoundary)
				.collect(Collectors.toList());
		rv.setData(lst.toArray(new UserBoundary[0]));
		return rv;
	}
	
	private void emailValidationCheck(String email) {
		if (email == null) {
			throw new RuntimeException("invalid Email");
		}
		String regex = "^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$";
		Pattern pattern = Pattern.compile(regex);

		Matcher matcher = pattern.matcher(email);
		if (!matcher.matches()) {
			throw new RuntimeException("invalid Email");
		}
	}

}
