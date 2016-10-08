package pl.pg.eti.kio.skroom.model.enumeration;

import pl.pg.eti.kio.skroom.exception.NoSuchUserRoleException;

/**
 * Enumeration of all possible user roles in the base system (not project-specific)
 *
 * @author Wojciech Stanisławski
 * @since 21.08.16
 */
public enum UserRole {
	ADMIN(0), PROJECT_MANAGER(1), PEASANT(2);

	private int code;

	UserRole(int code) {
		this.code = code;
	}

	public int getCode() {
		return code;
	}

	public static UserRole getByCode(int code) throws NoSuchUserRoleException {
		for (UserRole userRole :
				UserRole.values()) {
			if(userRole.code == code) {
				return userRole;
			}
		}
		throw new NoSuchUserRoleException();
	}
}
