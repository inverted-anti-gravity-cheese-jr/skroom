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
			hex = hex.substring(0, hex.length() - 2);
		}
		return "#" + hex;
	}
	
	public Color hexToColor(String hex) {
		return Color.decode(hex);
	}
	
	public Color interpolateColors(float value, Color c1, Color c2) {
		value = Math.min(1, Math.max(value, 0));
		
		int maxR = Math.max(c1.getRed(), c2.getRed());
		int minR = Math.min(c1.getRed(), c2.getRed());

		int maxB = Math.max(c1.getBlue(), c2.getBlue());
		int minB = Math.min(c1.getBlue(), c2.getBlue());
		
		int maxG = Math.max(c1.getGreen(), c2.getGreen());
		int minG = Math.min(c1.getGreen(), c2.getGreen());
		
		return new Color( minR + (int)(value * (maxR - minR)), 
				minG + (int)(value * (maxG - minG)),
				minB + (int)(value * (maxB - minB)));
	}
	
}
