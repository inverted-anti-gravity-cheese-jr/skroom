package pl.pg.eti.kio.skroom.model.dao;

import org.jooq.DSLContext;
import org.jooq.Result;
import org.jooq.impl.DSL;
import org.springframework.stereotype.Service;
import pl.pg.eti.kio.skroom.model.Project;
import pl.pg.eti.kio.skroom.model.UserStory;
import pl.pg.eti.kio.skroom.model.dba.Tables;
import pl.pg.eti.kio.skroom.model.dba.tables.records.UserStoriesRecord;
import pl.pg.eti.kio.skroom.settings.DatabaseSettings;

import java.sql.Connection;
import java.util.ArrayList;

/**
 * Class for accessing user stories in the database.
 *
 * @author Wojciech Stanisławski
 * @since 15.11.16
 */
@Service
public class UserStoryDao {

	/**
	 * Fetches all user stories that are assigned for supplied project.
	 *
	 * @param connection	Connection to a database.
	 * @param project		Project for which database engine will search.
	 * @return				All user stories for supplied project. If none found, then list will be empty.
	 */
	public ArrayList<UserStory> fetchUserStoriesForProject(Connection connection, Project project) {
		DSLContext query = DSL.using(connection, DatabaseSettings.getCurrentSqlDialect());
		ArrayList<UserStory> list = new ArrayList<UserStory>();

		Result<UserStoriesRecord> userStoriesRecords = query.selectFrom(Tables.USER_STORIES)
				.where(Tables.USER_STORIES.PROJECT_ID.eq(project.getId())).fetch();

		for(UserStoriesRecord record: userStoriesRecords) {
			list.add(UserStory.fromDba(record, query));
		}

		return list;
	}

	/**
	 * Finds user story with id in the database and returns it.
	 *
	 * @param connection	Connection to a database.
	 * @param id			Id of a user story to search for.
	 * @return				User story with supplied id.
	 */
	public UserStory fetchUserStoryById(Connection connection, int id) {
		DSLContext query = DSL.using(connection, DatabaseSettings.getCurrentSqlDialect());

		UserStoriesRecord userStoryRecord = query.selectFrom(Tables.USER_STORIES).where(Tables.USER_STORIES.ID.eq(id)).fetchOne();
		return UserStory.fromDba(userStoryRecord, query);
	}
}
