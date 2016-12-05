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

import static org.jooq.impl.DSL.row;
import static pl.pg.eti.kio.skroom.model.dba.Tables.USER_STORIES;

/**
 * Class for accessing user stories in the database.
 *
 * @author Wojciech Stanisławski
 * @since 15.11.16
 */
@Service
public class UserStoryDao {

	/**
	 * Removes user story with supplied id from a database.
	 *
	 * @param connection	Connection to a database.
	 * @param userStoryId	Identifier of a user story to be deleted.
	 * @return				Returns true if user story was deleted successfully.
	 */
	public boolean removeUserStoryWithId(Connection connection, int userStoryId) {
		DSLContext query = DSL.using(connection, DatabaseSettings.getCurrentSqlDialect());

		int removedRows = query.deleteFrom(USER_STORIES).where(USER_STORIES.ID.eq(userStoryId)).execute();

		return removedRows > 0;
	}

	/**
	 * Inserts a user story to a database.
	 *
	 * @param connection	Connection to a database.
	 * @param userStory		User story to insert to a database.
	 * @param project		Project to assign the user story to.
	 * @return				Returns true if successfully inserted a user story.
	 */
	public boolean insertNewUserStory(Connection connection, UserStory userStory, Project project) {
		DSLContext query = DSL.using(connection, DatabaseSettings.getCurrentSqlDialect());

		int insertedRows = query.insertInto(USER_STORIES).values(null, userStory.getName(),
				userStory.getAsA(), userStory.getiWantTo(), userStory.getSoThat(),
				userStory.getDescription(), userStory.getPriority(),
				userStory.getStoryPoints().getValue(), userStory.getStatus().getId(), project.getId()).execute();

		return insertedRows > 0;
	}

	/**
	 * Updates selected user story in the database.
	 *
	 * @param connection	Connection to a database.
	 * @param userStory		User story to be updated.
	 * @return				Returns true if successfully updated a user story.
	 */
	public boolean updateUserStory(Connection connection, UserStory userStory) {
		DSLContext query = DSL.using(connection, DatabaseSettings.getCurrentSqlDialect());

		int updatedRows = query.update(USER_STORIES)
				.set(USER_STORIES.NAME, userStory.getName())
				.set(USER_STORIES.DESCRIPTION, userStory.getDescription())
				.set(USER_STORIES.PRIORITY, userStory.getPriority())
				.set(USER_STORIES.STORY_POINTS, userStory.getStoryPoints().getValue())
				.set(USER_STORIES.STATUS_ID, userStory.getStatus().getId())
				.set(USER_STORIES.AS_A_STORY, userStory.getAsA())
				.set(USER_STORIES.I_WANT_TO_STORY, userStory.getiWantTo())
				.set(USER_STORIES.SO_THAT_STORY, userStory.getSoThat())
				.where(USER_STORIES.ID.eq(userStory.getId())).execute();

		return updatedRows > 0;
	}

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

		Result<UserStoriesRecord> userStoriesRecords = query.selectFrom(USER_STORIES)
				.where(USER_STORIES.PROJECT_ID.eq(project.getId())).fetch();

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

		UserStoriesRecord userStoryRecord = query.selectFrom(USER_STORIES).where(USER_STORIES.ID.eq(id)).fetchOne();
		return UserStory.fromDba(userStoryRecord, query);
	}

	/**
	 * Removes all user stories for project.
	 *
	 * @param connection	Connection to a database.
	 * @param project		Project from which all user stories will be deleted.
	 * @return				Returns true if deleted any rows.
	 */
	public boolean removeUserStoriesForProject(Connection connection, Project project) {
		DSLContext query = DSL.using(connection, DatabaseSettings.getCurrentSqlDialect());

		int deletedRows = query.deleteFrom(USER_STORIES).where(USER_STORIES.PROJECT_ID.eq(project.getId())).execute();

		return deletedRows > 0;
	}
}
