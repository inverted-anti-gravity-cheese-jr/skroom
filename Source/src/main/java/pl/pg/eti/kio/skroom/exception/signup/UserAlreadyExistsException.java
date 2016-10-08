package pl.pg.eti.kio.skroom.exception.signup;

/**
 * Exception that is thrown when there already exists user with that name or email.
 *
 * @author Wojciech Stanisławski
 * @since 26.08.16
 */
public class UserAlreadyExistsException extends Exception {

	public UserAlreadyExistsException() {
		super("User with that name or email already exists!");
	}
}
