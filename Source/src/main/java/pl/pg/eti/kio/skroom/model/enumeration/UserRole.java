package pl.pg.eti.kio.skroom.model.enumeration;

import pl.pg.eti.kio.skroom.exception.NoSuchUserRoleException;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

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

	public String getDisplayName() {
		return this.toString().replace('_', ' ');
	}

	public static List<UserRole> getAvailableDisplayNames() {
		return Arrays.stream(values()).collect(Collectors.toList());
	}

	public static UserRole getByDisplayName(String displayName) {
		return Arrays.stream(values())
				.filter(r -> r.getDisplayName().equals(displayName))
				.findFirst().orElse(PEASANT);
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
