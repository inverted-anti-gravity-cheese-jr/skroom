package pl.pg.eti.kio.skroom.model;

import pl.pg.eti.kio.skroom.model.dba.tables.records.SprintsRecord;

import java.util.Date;

/**
 * Model representation of a sprint.
 *
 * @author Wojciech Stanisławski
 * @since 05.12.16
 */
public class Sprint {
	private int id;
	private String name;
	private Date startDate;
	private Date endDate;
	private Project project;

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

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public Project getProject() {
		return project;
	}

	public void setProject(Project project) {
		this.project = project;
	}

	public static Sprint fromDba(SprintsRecord record, Project project) {
		Sprint sprint = new Sprint();

		sprint.setId(record.getId());
		sprint.setName(record.getName());
		sprint.setProject(project);
		sprint.setStartDate(record.getStartDay());
		sprint.setEndDate(record.getEndDay());

		return sprint;
	}

	@Override
	public String toString() {
		return "Sprint{" +
				"id=" + id +
				", name='" + name + '\'' +
				", startDate=" + startDate +
				", endDate=" + endDate +
				", project=" + project +
				'}';
	}
}
