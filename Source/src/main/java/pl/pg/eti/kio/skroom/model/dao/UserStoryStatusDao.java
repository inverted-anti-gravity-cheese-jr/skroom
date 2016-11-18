package pl.pg.eti.kio.skroom.model.dao;

import org.jooq.DSLContext;
import org.jooq.Result;
import org.jooq.impl.DSL;
import org.springframework.stereotype.Service;
import pl.pg.eti.kio.skroom.model.UserStoryStatus;
import pl.pg.eti.kio.skroom.model.dba.Tables;
import pl.pg.eti.kio.skroom.model.dba.tables.records.UserStoryStatusRecord;
import pl.pg.eti.kio.skroom.settings.DatabaseSettings;

import java.sql.Connection;
import java.util.ArrayList;

/**
 * Class for accessing user story statuses from database.
 *
 * @author Wojciech Stanisławski
 * @since 18.11.16
 */
@Service
public class UserStoryStatusDao {

	public UserStoryStatus getUserStoryStatusForName(Connection connection, String name) {
		DSLContext query = DSL.using(connection, DatabaseSettings.getCurrentSqlDialect());

		UserStoryStatusRecord storyStatusRecord = query.selectFrom(Tables.USER_STORY_STATUS).where(Tables.USER_STORY_STATUS.STATUS_NAME.eq(name)).fetchOne();
		return UserStoryStatus.fromDba(storyStatusRecord);
	}

	/**
	 * Lists all available user story statuses.
	 *
	 * @param connection	Connection to a database.
	 * @return				List of user story statuses.
	 */
	public ArrayList<UserStoryStatus> listAllStatuses(Connection connection) {
		DSLContext query = DSL.using(connection, DatabaseSettings.getCurrentSqlDialect());

		ArrayList<UserStoryStatus> statuses = new ArrayList<UserStoryStatus>();

		Result<UserStoryStatusRecord> storyStatusRecords = query.selectFrom(Tables.USER_STORY_STATUS).fetch();
		for(UserStoryStatusRecord userStoryStatusRecord : storyStatusRecords) {
			statuses.add(UserStoryStatus.fromDba(userStoryStatusRecord));
		}

		return statuses;
	}

}
