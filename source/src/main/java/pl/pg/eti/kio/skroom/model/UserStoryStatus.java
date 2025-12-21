package pl.pg.eti.kio.skroom.model;

import pl.pg.eti.kio.skroom.model.dba.tables.records.UserStoryStatusRecord;

/**
 * @author Wojciech Stanisławski
 * @since 15.11.16
 */
public class UserStoryStatus {
	private int id;
	private String name;
	private String color;
	private boolean archive;

	/**
	 * Method for converting database records into model classes.
	 *
	 * @param record	Database record fetched by jOOQ
	 * @return			Converted user
	 */
	public static UserStoryStatus fromDba(UserStoryStatusRecord record) {
		if(record == null) {
			return null;
		}

		UserStoryStatus type = new UserStoryStatus();
		type.setId(record.getId());
		type.setName(record.getStatusName());
		type.setArchive(record.getIsArchive() == 1);
		type.setColor(record.getColor());

		return type;
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

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	public boolean isArchive() {
		return archive;
	}

	public void setArchive(boolean archive) {
		this.archive = archive;
	}

	@Override
	public String toString() {
		return "UserStoryStatus{" +
				"id=" + id +
				", name='" + name + '\'' +
				", color='" + color + '\'' +
				", archive=" + archive +
				'}';
	}
}
