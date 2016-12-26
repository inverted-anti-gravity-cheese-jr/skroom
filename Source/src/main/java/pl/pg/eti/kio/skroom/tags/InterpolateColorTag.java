package pl.pg.eti.kio.skroom.tags;

import java.io.IOException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.SimpleTagSupport;

import pl.pg.eti.kio.skroom.ColorUtil;

public class InterpolateColorTag extends SimpleTagSupport {
	
	private String from;
	private String to;
	private int value;
	private int min;
	private int max;

	public String getFrom() {
		return from;
	}

	public void setFrom(String from) {
		this.from = from;
	}

	public String getTo() {
		return to;
	}

	public void setTo(String to) {
		this.to = to;
	}

	public int getValue() {
		return value;
	}
	
	public void setValue(int value) {
		this.value = value;
	}

	public int getMin() {
		return min;
	}

	public void setMin(int min) {
		this.min = min;
	}

	public int getMax() {
		return max;
	}

	public void setMax(int max) {
		this.max = max;
	}

	@Override
	public void doTag() throws JspException, IOException {
		float val = (float)(min + value) / (float)max;
		
		JspWriter out = getJspContext().getOut();
		try {
			out.print(new ColorUtil().interpolateColors(val, from, to));
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
