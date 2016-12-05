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
	private String asA;
	private String iWantTo;
	private String soThat;
	private String description;
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
		userStory.setAsA(record.getAsAStory());
		userStory.setiWantTo(record.getIWantToStory());
		userStory.setSoThat(record.getSoThatStory());
		userStory.setDescription(record.getDescription());
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

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
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

	public String getAsA() {
		return asA;
	}

	public void setAsA(String asA) {
		this.asA = asA;
	}

	public String getiWantTo() {
		return iWantTo;
	}

	public void setiWantTo(String iWantTo) {
		this.iWantTo = iWantTo;
	}

	public String getSoThat() {
		return soThat;
	}

	public void setSoThat(String soThat) {
		this.soThat = soThat;
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
