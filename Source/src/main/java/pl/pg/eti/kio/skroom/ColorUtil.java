package pl.pg.eti.kio.skroom;

import java.awt.Color;

import org.springframework.stereotype.Service;

/**
 * 
 * @author Wojciech Stanisławski
 * @since 26.12.2016
 */
@Service
public class ColorUtil {
	
	public String interpolateColors(float value, String c1, String c2) {
		return colorToHex(interpolateColors(value, hexToColor(c1), hexToColor(c2)));
	}
	
	public String colorToHex(Color color) {
		String hex = Integer.toHexString(color.getRGB());
		if(hex.length() > 6) {
			hex = hex.substring(2, hex.length());
		}
		return "#" + hex;
	}
	
	public Color hexToColor(String hex) {
		return Color.decode(hex);
	}
	
	public Color interpolateColors(float value, Color c1, Color c2) {
		value = (float)Math.min(1.0, Math.max((double)value, 0.0));
		
		int diffR = c2.getRed() - c1.getRed();
		int diffG = c2.getGreen() - c1.getGreen();
		int diffB = c2.getBlue() - c1.getBlue();
		
		return new Color(c1.getRed() + (int)(value * diffR),
				c1.getGreen() + (int)(value * diffG),
				c1.getBlue() + (int)(value * diffB));
	}
	
}
