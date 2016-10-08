package pl.pg.eti.kio.skroom.model;

/**
 * Project model class to manage in app.
 *
 * @author Wojciech Stanisławski
 * @since 17.08.16
 */
public class Project {

	private int id;
	private String name;

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
}
