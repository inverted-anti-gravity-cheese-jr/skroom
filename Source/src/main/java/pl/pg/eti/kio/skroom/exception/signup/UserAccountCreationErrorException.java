package pl.pg.eti.kio.skroom.exception.signup;

/**
 * Exception that is thrown when user account creation error occured.
 *
 * @author Wojciech Stanisławski
 * @since 26.08.16
 */
public class UserAccountCreationErrorException extends Exception {
	public UserAccountCreationErrorException() {
		super("User account creation error occured.");
	}
}
