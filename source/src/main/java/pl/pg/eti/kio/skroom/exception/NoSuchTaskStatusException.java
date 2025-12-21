package pl.pg.eti.kio.skroom.exception;

/**
 * Exception that is thrown when there is no task status with supplied code.
 *
 * @author Wojciech Stanisławski
 * @since 18.08.16
 */
@SuppressWarnings("serial")
public class NoSuchTaskStatusException extends Exception {
	public NoSuchTaskStatusException() {
		super("No such task status exception");
	}
}
