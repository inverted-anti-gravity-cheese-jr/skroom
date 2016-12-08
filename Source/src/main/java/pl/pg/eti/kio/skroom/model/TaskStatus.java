package pl.pg.eti.kio.skroom.model;

import pl.pg.eti.kio.skroom.model.dba.tables.records.TaskStatusesRecord;

/**
 * Model representation of a task status.
 *
 * @author Wojciech Stanisławski
 * @since 08.12.16
 */
public class TaskStatus {
	private int id;
	private String name;
	private boolean staysInSprint;
	private Project project;

	public static TaskStatus fromDba(TaskStatusesRecord record, Project project) {
		if(record == null) {
			return null;
		}

		TaskStatus status = new TaskStatus();

		status.setId(record.getId());
		status.setName(record.getName());
		status.setStaysInSprint(record.getStaysInSprint() != 0);
		status.setProject(project);

		return status;
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

	public boolean isStaysInSprint() {
		return staysInSprint;
	}

	public void setStaysInSprint(boolean staysInSprint) {
		this.staysInSprint = staysInSprint;
	}

	public Project getProject() {
		return project;
	}

	public void setProject(Project project) {
		this.project = project;
	}

	@Override
	public String toString() {
		return "TaskStatus{" +
				"id=" + id +
				", name='" + name + '\'' +
				", staysInSprint=" + staysInSprint +
				", project=" + project +
				'}';
	}
}
