package pl.pg.eti.kio.skroom.model;

import pl.pg.eti.kio.skroom.model.dba.tables.records.ProjectsRecord;

/**
 * Project model class to manage in app.
 *
 * @author Wojciech Stanisławski
 * @since 17.08.16
 */
public class Project {

	private int id = -1;
	private String name;
	private String description;

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

	/**
	 * Method for converting database records into model classes.
	 *
	 * @param record Database project record
	 * @return Model project class
	 */
	public static Project fromDba(ProjectsRecord record) {
		if (record == null) {
			return null;
		}

		Project project = new Project();

		project.setId(record.getId());
		project.setName(record.getName());
		project.setDescription(record.getDescription());

		return project;
	}
}
