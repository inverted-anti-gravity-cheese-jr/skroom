package pl.pg.eti.kio.skroom;

import java.io.IOException;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.webapp.WebAppContext;
import org.springframework.core.io.ClassPathResource;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

import pl.pg.eti.kio.skroom.tags.MenuElementTag;
import pl.pg.eti.kio.skroom.tags.ResourceBundleTag;

public class Main {

	private static final String PORT_PARAMETER_NAME = "-p";
	private static final int DEFAULT_PORT = 8080;
	private static final String CONTEXT_PATH = "/";
	private static final String COMPONENT_SCAN_PACKAGE = "pl.pg.eti.kio.skroom";
	private static final String MAPPING_URL = "/";
	private static final boolean DEBUG = true;

	public static void main(String[] args) throws Exception {
		int port = getPort(args);

		Server server = new Server(port);
		server.setHandler(getServletContextHandler());
		
		server.start();
		server.join();
	}

	private static int getPort(String[] args) {
		for(int i = 1; i < args.length -1; i++) {
			if(PORT_PARAMETER_NAME.equals(args[i])) {
				try {
					return Integer.parseInt(args[i+1]);
				}
				catch (Exception e) {}
			}
		}
		return DEFAULT_PORT;
	}

	private static WebAppContext getServletContextHandler() throws IOException {
		WebAppContext contextHandler = new WebAppContext();
		if(DEBUG) {
			contextHandler.setErrorHandler(null);
		}
		// app starts from "localhost/"
		contextHandler.setContextPath(CONTEXT_PATH);
		
		contextHandler.setInitParameter("org.eclipse.jetty.servlet.SessionCookie", "XSESSIONID");
		contextHandler.setInitParameter("org.eclipse.jetty.servlet.SessionIdPathParameterName", "xsessionid");

		// create context
		AnnotationConfigWebApplicationContext context = new AnnotationConfigWebApplicationContext();
		context.setConfigLocation(COMPONENT_SCAN_PACKAGE);
		context.register(SpringConfig.class);

		// add dispatcher servlet
		contextHandler.addServlet(new ServletHolder(new DispatcherServlet(context)), MAPPING_URL);
		contextHandler.addEventListener(new ContextLoaderListener(context));
		contextHandler.setResourceBase(new ClassPathResource("webapp").getURI().toString());
		
		return contextHandler;
	}

}
