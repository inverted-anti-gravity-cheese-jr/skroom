package pl.pg.eti.kio.skroom.tags;

import java.io.IOException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.SimpleTagSupport;

import pl.pg.eti.kio.skroom.PathUtils;
import pl.pg.eti.kio.skroom.ResourcesBundlesService;

/**
 * JSP tag to manage bundles of resources in JSP file.
 *
 * @author Wojciech Stanisławski
 * @since 22.05.16
 */

public class ResourceBundleTag extends SimpleTagSupport {
	
	private static final ResourcesBundlesService bundlesService = new ResourcesBundlesService();
	
	private String name;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public void doTag() throws JspException, IOException {
		if (name == null || name.isEmpty()) {
			return;
		}

		JspWriter jspWriter = getJspContext().getOut();
		
		for (String resource : bundlesService.getBundle(name)) {
			resource = PathUtils.getRootPath() + resource;
			//resource = 
			if (resource.trim().endsWith(".js")) {
				jspWriter.println("<script type=\"text/javascript\" src=\"" + resource + "\"></script>");
			} else {
				jspWriter.println("<link href=\"" + resource + "\" rel=\"stylesheet\" />");
			}
		}
	}

}
