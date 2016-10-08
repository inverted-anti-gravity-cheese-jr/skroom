package pl.pg.eti.kio.skroom.model;

import pl.pg.eti.kio.skroom.exception.NoSuchTaskStatusException;
import pl.pg.eti.kio.skroom.model.dba.tables.records.TasksRecord;
import pl.pg.eti.kio.skroom.model.enumeration.TaskStatus;

/**
 * Task model class to manage in app.
 *
 * @author Wojciech Stanisławski
 * @since 17.08.16
 */
public class Task {

	private int id;
	private String name;
	private User assignee;
	private TaskStatus status;
	private int storyPoints;

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

	public User getAssignee() {
		return assignee;
	}

	public void setAssignee(User assignee) {
		this.assignee = assignee;
	}

	public TaskStatus getStatus() {
		return status;
	}

	public void setStatus(TaskStatus status) {
		this.status = status;
	}

	public int getStoryPoints() {
		return storyPoints;
	}

	@Override
	public String toString() {
		return "Task{" +
				"id=" + id +
				", name='" + name + '\'' +
				", assignee=" + assignee +
				", status=" + status +
				", storyPoints=" + storyPoints +
				'}';
	}

	public void setStoryPoints(int storyPoints) {
		this.storyPoints = storyPoints;
	}

	/**
	 * Method for converting database records into model classes.
	 *
	 * @param record Database record fetched by jOOQ
	 * @param user User assigned to this task
	 * @return Converted task
	 * @throws NoSuchTaskStatusException Thrown if wrong task status supplied (check your database)
	 */
	public static Task fromDba(TasksRecord record, User user) throws NoSuchTaskStatusException {
		Task task = new Task();

		task.setId(record.getId());
		task.setAssignee(user);
		task.setName(record.getName());
		task.setStatus(TaskStatus.getByCode(record.getTaskStatus()));
		task.setStoryPoints(record.getStoryPoints());

		return task;
	}

}
