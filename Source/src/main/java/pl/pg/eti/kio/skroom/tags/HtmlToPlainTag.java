package pl.pg.eti.kio.skroom.tags;

import pl.pg.eti.kio.skroom.PlainTextUtil;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.SimpleTagSupport;
import java.io.IOException;
import java.io.StringWriter;

import static pl.pg.eti.kio.skroom.PlainTextUtil.HTML_ENDLINE_STRING;
import static pl.pg.eti.kio.skroom.PlainTextUtil.PLAIN_TEXT_ENDLINE_STRING;

/**
 * @author Wojciech Stanisławski
 * @since 03.01.17
 */
public class HtmlToPlainTag extends SimpleTagSupport {

	@Override
	public void doTag() throws JspException, IOException {
		JspWriter out = getJspContext().getOut();

		StringWriter stringWriter = new StringWriter();
		getJspBody().invoke(stringWriter);
		String body = stringWriter.toString();

		out.print(body.replace(HTML_ENDLINE_STRING, PLAIN_TEXT_ENDLINE_STRING));
	}
}
