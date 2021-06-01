package mg.logic.exceptions;

public class UserIngredientNotFound extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public UserIngredientNotFound() {
		// TODO Auto-generated constructor stub
	}
	public UserIngredientNotFound(String user, Long ingredientId) {
		this("UserIngredient for " + user+ " with ingredient ID- " + ingredientId+" not found");
	}

	public UserIngredientNotFound(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

	public UserIngredientNotFound(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}

	public UserIngredientNotFound(String message, Throwable cause) {
		super(message, cause);
		// TODO Auto-generated constructor stub
	}

	public UserIngredientNotFound(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
		// TODO Auto-generated constructor stub
	}

}
