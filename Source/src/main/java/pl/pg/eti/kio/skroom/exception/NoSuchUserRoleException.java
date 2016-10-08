package pl.pg.eti.kio.skroom.exception;

/**
 * Exception that is thrown when there is no user role with supplied code.
 *
 * @author Wojciech Stanisławski
 * @since 18.08.16
 */
public class NoSuchUserRoleException extends Exception {

	public NoSuchUserRoleException() {
		super("No such user role exception");
	}
}
