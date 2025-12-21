package pl.pg.eti.kio.skroom.model;

import java.util.List;
import java.util.Optional;

import pl.pg.eti.kio.skroom.exception.NoSuchTaskStatusException;
import pl.pg.eti.kio.skroom.model.dba.tables.records.TasksRecord;

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
	private UserStory userStory;
	private String color;
	private String description;
	private Sprint sprint;
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

	public UserStory getUserStory() {
		return userStory;
	}

	public void setUserStory(UserStory userStory) {
		this.userStory = userStory;
	}
	
	public Sprint getSprint() {
		return sprint;
	}

	public void setSprint(Sprint sprint) {
		this.sprint = sprint;
	}

	@Override
	public String toString() {
		return "Task [id=" + id + ", name=" + name + ", assignee=" + assignee + ", status=" + status + ", project="
				+ project + ", userStory=" + userStory + ", color=" + color + ", description=" + description
				+ ", sprint=" + sprint + ", estimatedTime=" + estimatedTime + "]";
	}

	/**
	 * Method for converting database records into model classes.
	 *
	 * @param record Database record fetched by jOOQ
	 * @param user User assigned to this task
	 * @param sprints 
	 * @return Converted task
	 * @throws NoSuchTaskStatusException Thrown if wrong task status supplied (check your database)
	 */
	public static Task fromDba(TasksRecord record, User user, Project project, List<TaskStatus> taskStatusesList, List<UserStory> userStories, List<Sprint> sprints) throws NoSuchTaskStatusException {
		Task task = new Task();

		task.setId(record.getId());
		task.setAssignee(user);
		task.setName(record.getName());
		task.setProject(project);
		if(record.getTaskStatusId() != null) {
			Optional<TaskStatus> taskStatus = taskStatusesList.stream()
					.filter(status -> status.getId() == record.getTaskStatusId())
					.findAny();
			
			if(taskStatus.isPresent()) {
				task.setStatus(taskStatus.get());
			}
		}
		
		task.setEstimatedTime(record.getEstimatedTime());
		task.setDescription(record.getDescription());
		task.setColor(record.getColor());
		
		Optional<UserStory> userStory = userStories.stream()
				.filter(story -> story.getId() == record.getUserStoryId())
				.findAny();
		
		if(userStory.isPresent()) {
			task.setUserStory(userStory.get());
		}
		
		Optional<Sprint> sprint = sprints.stream()
				.filter(sprt -> sprt.getId() == record.getSprintId())
				.findAny();
		
		if(sprint.isPresent()) {
			task.setSprint(sprint.get());
		}

		return task;
	}

}
