package pl.pg.eti.kio.skroom.model.enumeration;


import pl.pg.eti.kio.skroom.enumeration.TextColor;

import static pl.pg.eti.kio.skroom.enumeration.TextColor.BLACK;
import static pl.pg.eti.kio.skroom.enumeration.TextColor.WHITE;

/**
 * List of all available story points for user stories.
 *
 * @author Wojciech Stanisławski
 * @since 15.11.16
 */
public enum StoryPoints {
	SP_1("1", "#8bc34a" , BLACK),
	SP_2("2", "#689f38", WHITE),
	SP_3("3", "#558b2f", WHITE),
	SP_5("5", "#388e3c", WHITE),
	SP_8("8", "#cddc39", BLACK),
	SP_13("13", "#ffc107", BLACK),
	SP_20("20", "#ff9800", BLACK),
	SP_40("40", "#ff5722", WHITE),
	SP_100("100", "#f44336", WHITE),
	SP_INF("∞", "#b71c1c", WHITE);

	private static final String INF_LABEL = "1000";
	private String displayName;
	private String color;
	private TextColor textColor;

	StoryPoints(String displayName, String color, TextColor textColor) {
		this.displayName = displayName;
		this.color = color;
		this.textColor = textColor;
	}

	public static StoryPoints fromValue(int storyPoints) {
		String storyPointsString = Integer.toString(storyPoints);
		if (SP_2.displayName.equals(storyPointsString))
			return SP_2;
		if (SP_3.displayName.equals(storyPointsString))
			return SP_3;
		if (SP_5.displayName.equals(storyPointsString))
			return SP_5;
		if (SP_8.displayName.equals(storyPointsString))
			return SP_8;
		if (SP_13.displayName.equals(storyPointsString))
			return SP_13;
		if (SP_20.displayName.equals(storyPointsString))
			return SP_20;
		if (SP_40.displayName.equals(storyPointsString))
			return SP_40;
		if (SP_100.displayName.equals(storyPointsString))
			return SP_100;
		if (INF_LABEL.equals(storyPointsString))
			return SP_INF;
		return SP_1;
	}

	public String getColor() {
		return color;
	}

	public String getDisplayName() {
		return displayName;
	}

	public String getTextColor() {
		return textColor.getValue();
	}

	@Override
	public String toString() {
		return "StoryPoints{" +
				"displayName='" + displayName + '\'' +
				", color='" + color + '\'' +
				", textColor=" + textColor +
				'}';
	}
}
