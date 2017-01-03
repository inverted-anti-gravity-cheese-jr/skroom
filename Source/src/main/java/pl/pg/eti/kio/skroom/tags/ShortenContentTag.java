package pl.pg.eti.kio.skroom.tags;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.BodyTagSupport;
import java.io.IOException;

/**
 * @author Wojciech Stanisławski
 * @since 03.01.17
 */
public class ShortenContentTag extends BodyTagSupport {
	private int max;

	public int getMax() {
		return max;
	}

	public void setMax(int max) {
		this.max = max;
	}

	@Override
	public int doEndTag() throws JspException {

		JspWriter out = pageContext.getOut();
		String body = bodyContent.getString();

		if(body.length() > max) {
			if(max < 4) {
				body = body.substring(0, max);
			}
			else {
				body = body.substring(0, max - 3) + "...";
			}
		}

		try {
			out.print(body);
		} catch (IOException e) {
			e.printStackTrace();
		}

		return EVAL_PAGE;
	}
}
