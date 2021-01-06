package mg.logic;

import java.util.List;

import mg.boundaries.Response;
import mg.boundaries.UserBoundary;
import mg.boundaries.helper.UserLoginBoundary;

public interface UserService {

	public Boolean isUserExist(String userEmail);

	public Response<UserBoundary> create(UserBoundary user);

	public Response<UserBoundary> unsubscribe(UserLoginBoundary user);

	public Response<UserBoundary> login(UserLoginBoundary user);

	public void updateUser(UserBoundary user);

	public Response<UserBoundary[]> getAll();

}