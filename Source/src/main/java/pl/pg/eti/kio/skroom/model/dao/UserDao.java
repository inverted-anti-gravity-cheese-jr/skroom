package pl.pg.eti.kio.skroom.model.dao;

import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.Record1;
import org.jooq.Result;
import org.jooq.impl.DSL;
import org.springframework.stereotype.Service;
import pl.pg.eti.kio.skroom.exception.NoSuchUserRoleException;
import pl.pg.eti.kio.skroom.exception.signup.UserAccountCreationErrorException;
import pl.pg.eti.kio.skroom.exception.signup.UserAlreadyExistsException;
import pl.pg.eti.kio.skroom.model.Project;
import pl.pg.eti.kio.skroom.model.User;
import pl.pg.eti.kio.skroom.model.UserSecurity;
import pl.pg.eti.kio.skroom.model.UserSettings;
import pl.pg.eti.kio.skroom.model.dba.Tables;
import pl.pg.eti.kio.skroom.model.dba.tables.records.UsersRecord;
import pl.pg.eti.kio.skroom.model.dba.tables.records.UsersSecurityRecord;
import pl.pg.eti.kio.skroom.model.dba.tables.records.UsersSettingsRecord;
import pl.pg.eti.kio.skroom.settings.DatabaseSettings;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import static pl.pg.eti.kio.skroom.model.dba.Tables.USERS_SETTINGS;

/**
 * Class to access model's User from database.
 *
 * @author Wojciech Stanisławski
 * @since 19.08.16
 */
@Service
public class UserDao {

	private static final int USER_PRIVILAGE_TO_EDIT_PROJECT = 1;


	public boolean saveUserSettings(Connection connection, UserSettings userSettings) {
		DSLContext query = DSL.using(connection, DatabaseSettings.getCurrentSqlDialect());

		int updatedRows = query.update(USERS_SETTINGS)
				.set(USERS_SETTINGS.RECENT_PROJECT_ID, userSettings.getRecentProject().getId())
				.set(USERS_SETTINGS.USER_STORIES_PER_PAGE, userSettings.getUserStoriesPerPage())
				.execute();

		return updatedRows > 0;
	}

	/**
	 * Lists all users in database, proceed with caution
	 *
	 * @param connection Connection to a database
	 * @return List of all users
	 */
	public List<UserContainer> listAllUsers(Connection connection) {
		DSLContext query = DSL.using(connection, DatabaseSettings.getCurrentSqlDialect());

		List<UserContainer> users = new ArrayList<>();

		Result<Record> records = query.select(Tables.USERS.fields()).select(Tables.USERS_SECURITY.ACCEPTED)
				.from(Tables.USERS)
				.join(Tables.USERS_SECURITY).onKey()
				.orderBy(Tables.USERS_SECURITY.ACCEPTED)
				.fetch();

		try {
			for (Record record : records) {
				UsersRecord userRecord = record.into(Tables.USERS);
				Record1<Integer> accepted = record.into(Tables.USERS_SECURITY.ACCEPTED);
				users.add(new UserContainer(User.fromDba(userRecord), accepted.value1() == 1));
			}
		} catch (NoSuchUserRoleException e) {
			users.clear();
		}
		return users;
	}

	/**
	 * Method check if user has any privileges that allow him to edit project.
	 *
	 * @param connection Connection to a database
	 * @param user User class from model
	 * @param project Project class from model
	 * @return Returns true if user has any privileges that allow him to edit project, and false otherwise
	 */
	public boolean checkIfHasProjectEditPermissions(Connection connection, User user, Project project) {
		if (project == null) {
			return false;
		}

		DSLContext query = DSL.using(connection, DatabaseSettings.getCurrentSqlDialect());

		Record1<Integer> rolesWithPrivilages = query.selectCount()
				.from(Tables.USERS_PROJECTS, Tables.USER_ROLES_IN_PROJECT)
				.where(Tables.USERS_PROJECTS.USER_ID.eq(user.getId())
						.and(Tables.USERS_PROJECTS.PROJECT_ID.eq(project.getId())
								.and(Tables.USER_ROLES_IN_PROJECT.ID.eq(Tables.USERS_PROJECTS.USER_ROLE_ID)))
						.and(Tables.USER_ROLES_IN_PROJECT.PRIVILIGES.eq(USER_PRIVILAGE_TO_EDIT_PROJECT))).fetchOne();

		if (rolesWithPrivilages != null && rolesWithPrivilages.value1() > 0) {
			return true;
		}

		return false;
	}

	/**
	 * Finds in database user by name and convers it to app model.
	 *
	 * @param connection Connection to a database
	 * @param name       Name of a user
	 * @return Model representation of a user
	 */
	public User fetchByName(Connection connection, String name) {
		DSLContext query = DSL.using(connection, DatabaseSettings.getCurrentSqlDialect());

		Result<UsersRecord> usersRecordResult = query.selectFrom(Tables.USERS).where(Tables.USERS.NAME.eq(name)).fetch();

		if (usersRecordResult.isEmpty()) {
			return null;
		}

		User user = null;
		try {
			user = User.fromDba(usersRecordResult.get(0));
		} catch (NoSuchUserRoleException e) {
			e.printStackTrace();
			return null;
		}

		return user;
	}

	/**
	 * Fetches from database security data about a user.
	 *
	 * @param connection Connection to a database
	 * @param user       User from app model
	 * @return User security data
	 */
	public UserSecurity fetchUserSecurityData(Connection connection, User user) {
		DSLContext query = DSL.using(connection, DatabaseSettings.getCurrentSqlDialect());

		UsersSecurityRecord usersSecurityRecord = query.selectFrom(Tables.USERS_SECURITY).where(Tables.USERS_SECURITY.USER_ID.eq(user.getId())).fetchOne();

		return UserSecurity.fromDba(usersSecurityRecord);
	}


	/**
	 * Fetches user settings for supplied user
	 *
	 * @param connection	Connection to a database
	 * @param user			User from app model
	 * @return
	 */
	public UserSettings fetchUserSettingsData(Connection connection, User user) {
		DSLContext query = DSL.using(connection, DatabaseSettings.getCurrentSqlDialect());

		UsersSettingsRecord usersSettingsRecord = query.selectFrom(USERS_SETTINGS).where(USERS_SETTINGS.USER_ID.eq(user.getId())).fetchOne();

		return UserSettings.fromDba(usersSettingsRecord, query);
	}

	/**
	 * Registers user in the database. Throw exception when operation whas unsuccessfull.
	 *
	 * @param connection   Connection to a database
	 * @param user         Basic user information
	 * @param userSecurity Security settings for user (password, security question, etc.)
	 * @throws UserAlreadyExistsException        Thrown when user with that name or email already exists
	 * @throws UserAccountCreationErrorException Thrown when an error occured
	 */
	public void registerUser(Connection connection, User user, UserSecurity userSecurity) throws UserAlreadyExistsException, UserAccountCreationErrorException {
		DSLContext query = DSL.using(connection, DatabaseSettings.getCurrentSqlDialect());

		int usersWithTheSameNameOrEmailCount = query.selectCount().from(Tables.USERS)
				.where(Tables.USERS.NAME.eq(user.getName())
						.or(Tables.USERS.EMAIL.eq(user.getEmail()))).fetchOne(0, int.class);

		if (usersWithTheSameNameOrEmailCount > 0) {
			throw new UserAlreadyExistsException();
		}

		try {
			query.transaction(configuration -> {
				Integer userId = (Integer) DSL.using(configuration).fetchOne("SELECT seq FROM sqlite_sequence WHERE name='" + Tables.USERS.getName() + "'").get(0);

				if (userId == null) {
					// rollback
					throw new Exception();
				}

				int usersCreatedRows = DSL.using(configuration).insertInto(Tables.USERS)
						.values(null, user.getName(), user.getEmail(), user.getAvatar(), user.getRole().getCode()).execute();

				int usersSecurityCreatedRows = query.insertInto(Tables.USERS_SECURITY).values(null, userId.intValue() + 1,
						userSecurity.getPassword(), userSecurity.getSalt(), userSecurity.getSecureQuestion(),
						userSecurity.getSecureAnswer(), 0).execute();

				int usersSettingsCreateRows = query.insertInto(USERS_SETTINGS).values(null, -1, 10).execute();

				if (usersCreatedRows != 1 || usersSecurityCreatedRows != 1 || usersSettingsCreateRows != 1) {
					// rollback
					throw new Exception();
				}

				userSecurity.setId(userId);
			});
		} catch (Exception e) {
			e.printStackTrace();
			throw new UserAccountCreationErrorException();
		}
	}

	public class UserContainer {
		private User user;
		private boolean accepted;

		UserContainer(User user, boolean accepted) {
			this.user = user;
			this.accepted = accepted;
		}

		public User getUser() {
			return user;
		}

		public boolean isAccepted() {
			return accepted;
		}
	}

}
