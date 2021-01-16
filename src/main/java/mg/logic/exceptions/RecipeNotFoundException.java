package mg.logic.exceptions;

public class RecipeNotFoundException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public RecipeNotFoundException() {
	}

	public RecipeNotFoundException(String message) {
		super(message);
	}

	public RecipeNotFoundException(Throwable cause) {
		super(cause);
	}

	public RecipeNotFoundException(String message, Throwable cause) {
		super(message, cause);
	}

	public RecipeNotFoundException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
