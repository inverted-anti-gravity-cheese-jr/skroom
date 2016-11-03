package pl.pg.eti.kio.skroom.model.dao;

import org.jooq.*;
import org.jooq.exception.DataAccessException;
import org.jooq.impl.DSL;
import org.jooq.util.derby.sys.Sys;
import org.jooq.util.xml.jaxb.Table;
import org.springframework.stereotype.Service;
import pl.pg.eti.kio.skroom.exception.NoSuchUserRoleException;
import pl.pg.eti.kio.skroom.exception.signup.UserAccountCreationErrorException;
import pl.pg.eti.kio.skroom.exception.signup.UserAlreadyExistsException;
import pl.pg.eti.kio.skroom.model.User;
import pl.pg.eti.kio.skroom.model.UserSecurity;
import pl.pg.eti.kio.skroom.model.dba.Tables;
import pl.pg.eti.kio.skroom.model.dba.tables.records.UsersRecord;
import pl.pg.eti.kio.skroom.model.dba.tables.records.UsersSecurityRecord;
import pl.pg.eti.kio.skroom.settings.DatabaseSettings;

import java.sql.Connection;

import static org.jooq.impl.DSL.field;
import static org.jooq.impl.DSL.table;

/**
 * Class to access model's User from database.
 *
 * @author Wojciech Stanisławski
 * @since 19.08.16
 */
@Service
public class UserDao {

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

		Result<UsersSecurityRecord> securityRecords = query.selectFrom(Tables.USERS_SECURITY).where(Tables.USERS_SECURITY.USER_ID.eq(user.getId())).fetch();

		if (securityRecords.isEmpty()) {
			return null;
		}

		return UserSecurity.fromDba(securityRecords.get(0));
	}

	/**
	 * Registers user in the database. Throw exception when operation whas unsuccessfull.
	 *
	 * @param connection Connection to a database
	 * @param user Basic user information
	 * @param userSecurity Security settings for user (password, security question, etc.)
	 * @throws UserAlreadyExistsException Thrown when user with that name or email already exists
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
				Integer userId = (Integer) DSL.using(configuration).fetchOne("SELECT seq FROM sqlite_sequence WHERE name='" + Tables.USERS.getName() +"'").get(0);

				if (userId == null) {
					// rollback
					throw new Exception();
				}

				int usersCreatedRows = DSL.using(configuration).insertInto(Tables.USERS)
						.values(null, user.getName(), user.getEmail(), user.getAvatar(), user.getRole().getCode()).execute();

				int usersSecurityCreatedRows = query.insertInto(Tables.USERS_SECURITY).values(null, userId.intValue() + 1,
						userSecurity.getPassword(), userSecurity.getSalt(), userSecurity.getSecureQuestion(),
						userSecurity.getSecureAnswer(), 0).execute();

				if (usersCreatedRows != 1 || usersSecurityCreatedRows != 1) {
					// rollback
					throw new Exception();
				}

				userSecurity.setId(userId);
			});
		}
		catch (Exception e) {
			e.printStackTrace();
			throw new UserAccountCreationErrorException();
		}
	}

}
