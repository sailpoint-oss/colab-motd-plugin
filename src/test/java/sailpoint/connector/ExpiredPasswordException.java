package sailpoint.connector;

/**
 * Dummy exception class to make the compilation of Unit Tests possible.
 */
@SuppressWarnings("unused")
public class ExpiredPasswordException extends Exception {
	public ExpiredPasswordException() {
		super();
	}
	
	public ExpiredPasswordException(String message) {
		super(message);
	}
	
	public ExpiredPasswordException(String message, Throwable cause) {
		super(message, cause);
	}
	
	public ExpiredPasswordException(Throwable cause) {
		super(cause);
	}
	
	protected ExpiredPasswordException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}
}
