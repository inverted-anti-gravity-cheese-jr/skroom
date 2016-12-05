package pl.pg.eti.kio.skroom.tags;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.SimpleTagSupport;
import java.io.IOException;
import java.io.StringWriter;

/**
 * Tag for managing menu elements in JSP.
 *
 * @author Wojciech Stanisławski
 * @since 03.11.16
 */
public class MenuElementTag extends SimpleTagSupport {
	private static final String ROOT_PATH = "/skroom/";

	private String href;
	private String siteName;
	private String fa;
	private boolean disabled;

	public String getHref() {
		return href;
	}

	public void setHref(String href) {
		this.href = href;
	}


	public String getFa() {
		return fa;
	}

	public void setFa(String fa) {
		this.fa = fa;
	}

	public String getSiteName() {
		return siteName;
	}

	public void setSiteName(String siteName) {
		this.siteName = siteName;
	}

	public boolean isDisabled() {
		return disabled;
	}

	public void setDisabled(boolean disable) {
		this.disabled = disable;
	}

	@Override
	public void doTag() throws JspException, IOException {
		if(disabled) {
			return;
		}

		JspWriter out = getJspContext().getOut();
		String currentSiteName = (String) getJspContext().getAttribute("siteName", PageContext.REQUEST_SCOPE);

		if(siteName == null || siteName.isEmpty()) {
			siteName = href;
		}

		if(currentSiteName.equals(ROOT_PATH + siteName)) {
			out.print("<li class=\"active\">");
		}
		else {
			out.print("<li>");
		}

		out.print("<a href=\"" + ROOT_PATH + href + "\">");
		if(fa != null && !fa.isEmpty()) {
			out.print("<i class=\"" + fa + "\" aria-hidden=\"true\"></i>");
		}

		StringWriter stringWriter = new StringWriter();
		getJspBody().invoke(stringWriter);
		out.print(stringWriter.toString());
		out.print("</a>");
		out.print("</li>");
	}
}
