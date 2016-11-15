package pl.pg.eti.kio.skroom.model;

import org.jooq.DSLContext;
import pl.pg.eti.kio.skroom.model.dba.Tables;
import pl.pg.eti.kio.skroom.model.dba.tables.records.UserStoriesRecord;
import pl.pg.eti.kio.skroom.model.dba.tables.records.UserStoryStatusRecord;
import pl.pg.eti.kio.skroom.model.enumeration.StoryPoints;

/**
 * @author Wojciech Stanisławski
 * @since 15.11.16
 */
public class UserStory {

	private int id;
	private String name;
	private int priority;
	private StoryPoints storyPoints;
	private UserStoryStatus status;


	/**
	 * Method for converting database records into model classes.
	 *
	 * @param record	Database record fetched by jOOQ
	 * @return			Converted user
	 */
	public static UserStory fromDba(UserStoriesRecord record, DSLContext query) {
		if(record == null) {
			return null;
		}

		UserStory userStory = new UserStory();
		userStory.setId(record.getId());
		userStory.setName(record.getName());
		userStory.setPriority(record.getPriority());
		userStory.setStoryPoints(StoryPoints.fromValue(record.getStoryPoints()));

		UserStoryStatusRecord userStoryStatusRecord = query.selectFrom(Tables.USER_STORY_STATUS)
				.where(Tables.USER_STORY_STATUS.ID.eq(record.getStatusId())).fetchOne();

		userStory.setStatus(UserStoryStatus.fromDba(userStoryStatusRecord));
		return userStory;
	}

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

	public int getPriority() {
		return priority;
	}

	public void setPriority(int priority) {
		this.priority = priority;
	}

	public StoryPoints getStoryPoints() {
		return storyPoints;
	}

	public void setStoryPoints(StoryPoints storyPoints) {
		this.storyPoints = storyPoints;
	}

	public UserStoryStatus getStatus() {
		return status;
	}

	public void setStatus(UserStoryStatus status) {
		this.status = status;
	}

	@Override
	public String toString() {
		return "UserStory{" +
				"id=" + id +
				", name='" + name + '\'' +
				", priority=" + priority +
				", storyPoints=" + storyPoints +
				", status=" + status +
				'}';
	}
}
