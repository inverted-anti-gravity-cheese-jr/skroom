package pl.pg.eti.kio.skroom.model;

import pl.pg.eti.kio.skroom.exception.NoSuchTaskStatusException;
import pl.pg.eti.kio.skroom.model.dao.TaskStatusDao;
import pl.pg.eti.kio.skroom.model.dba.tables.TaskStatuses;
import pl.pg.eti.kio.skroom.model.dba.tables.records.TasksRecord;

import java.sql.Connection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

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
	private Project project;
	private String color;
	private String description;
	private int estimatedTime;

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

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public int getEstimatedTime() {
		return estimatedTime;
	}

	public void setEstimatedTime(int estimatedTime) {
		this.estimatedTime = estimatedTime;
	}

	public Project getProject() {
		return project;
	}

	public void setProject(Project project) {
		this.project = project;
	}

	@Override
	public String toString() {
		return "Task{" +
				"id=" + id +
				", name='" + name + '\'' +
				", assignee=" + assignee +
				", status=" + status +
				", project=" + project +
				", color='" + color + '\'' +
				", description='" + description + '\'' +
				", estimatedTime=" + estimatedTime +
				'}';
	}

	/**
	 * Method for converting database records into model classes.
	 *
	 * @param record Database record fetched by jOOQ
	 * @param user User assigned to this task
	 * @return Converted task
	 * @throws NoSuchTaskStatusException Thrown if wrong task status supplied (check your database)
	 */
	public static Task fromDba(TasksRecord record, User user, Project project, List<TaskStatus> taskStatusesList) throws NoSuchTaskStatusException {
		Task task = new Task();

		task.setId(record.getId());
		task.setAssignee(user);
		task.setName(record.getName());
		task.setProject(project);
		Optional<TaskStatus> taskStatus = taskStatusesList.stream()
				.filter(status -> status.getId() == record.getId())
				.findFirst();
		if(taskStatus.isPresent()) {
			task.setStatus(taskStatus.get());
		}
		task.setEstimatedTime(record.getEstimatedTime());
		task.setDescription(record.getDescription());
		task.setColor(record.getColor());

		return task;
	}

}
