package pl.pg.eti.kio.skroom.tags;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.SimpleTagSupport;
import java.io.IOException;

/**
 * Created by Krzysztof Świeczkowski on 12.11.16.
 */
public class PageTitleTag extends SimpleTagSupport {

    @Override
    public void doTag() throws JspException, IOException {

        JspWriter out = getJspContext().getOut();
        String currentSiteName = (String) getJspContext().getAttribute("pageTitle", PageContext.REQUEST_SCOPE);

        out.print("<title>"+currentSiteName+"</title>");
    }
}
