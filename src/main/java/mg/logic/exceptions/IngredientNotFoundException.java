package mg.logic.exceptions;

public class IngredientNotFoundException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public IngredientNotFoundException() {
	}

	public IngredientNotFoundException(String message) {
		super(message);
	}

	public IngredientNotFoundException(Throwable cause) {
		super(cause);
	}

	public IngredientNotFoundException(String message, Throwable cause) {
		super(message, cause);
	}

	public IngredientNotFoundException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
