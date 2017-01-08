package pl.pg.eti.kio.skroom.model;

import java.io.Serializable;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import pl.pg.eti.kio.skroom.exception.NoSuchUserRoleException;
import pl.pg.eti.kio.skroom.model.dba.tables.records.UsersRecord;
import pl.pg.eti.kio.skroom.model.enumeration.UserRole;

/**
 * User model class to manage in app.
 *
 * @author Wojciech Stanisławski
 * @since 17.08.16
 */
@Component
@Scope("session")
public class User implements Serializable {

	private int id = -1;
	private String name;
	private String email;
	private String avatar;
	private UserRole role;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getAvatar() {
		return avatar;
	}

	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}

	public UserRole getRole() {
		return role;
	}

	public void setRole(UserRole role) {
		this.role = role;
	}

	@Override
	public String toString() {
		return "User{" +
				"id=" + id +
				", name='" + name + '\'' +
				", email='" + email + '\'' +
				", avatar='" + avatar + '\'' +
				", role=" + role +
				'}';
	}

	/**
	 * Method for converting database records into model classes.
	 *
	 * @param record Database record fetched by jOOQ
	 * @return Converted user
	 * @throws NoSuchUserRoleException Thrown if wrong user role supplied (check your database)
	 */
	public static User fromDba(UsersRecord record) throws NoSuchUserRoleException {
		User user = new User();

		user.setId(record.getId());
		user.setName(record.getName());
		user.setEmail(record.getEmail());
		user.setAvatar(record.getAvatar());
		user.setRole(UserRole.getByCode(record.getRole()));

		return user;
	}
}
