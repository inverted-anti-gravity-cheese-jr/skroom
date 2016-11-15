package pl.pg.eti.kio.skroom.enumeration;

/**
 * Basic text colors in HTML.
 *
 * @author Wojciech Stanisławski
 * @since 15.11.16
 */
public enum TextColor {
	WHITE("white"), BLACK("black");

	private String value;

	TextColor(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}
}
