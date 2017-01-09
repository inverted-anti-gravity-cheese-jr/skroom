package pl.pg.eti.kio.skroom;

import org.apache.tomcat.util.scan.StandardJarScanner;
import org.eclipse.jetty.apache.jsp.JettyJasperInitializer;
import org.eclipse.jetty.servlet.ServletContextHandler.ServletContainerInitializerCaller;
import org.eclipse.jetty.util.component.AbstractLifeCycle;
import org.eclipse.jetty.webapp.WebAppContext;

public class ApacheJspConfig extends AbstractLifeCycle implements ServletContainerInitializerCaller {
	private JettyJasperInitializer initializer = new JettyJasperInitializer();
	private WebAppContext contextHandler;
	
	public ApacheJspConfig(WebAppContext contextHandler) {
		this.contextHandler = contextHandler;
		contextHandler.setAttribute("org.apache.tomcat.JarScanner", new StandardJarScanner());
	}
	
	@Override
	protected void doStart() throws Exception {
		ClassLoader oldClassLoader = Thread.currentThread().getContextClassLoader();;
		try {
            Thread.currentThread().setContextClassLoader(contextHandler.getClassLoader());
            initializer.onStartup(null, contextHandler.getServletContext());   
            super.doStart();
		}
		catch (Exception e) { /* ignore */ }
		Thread.currentThread().setContextClassLoader(oldClassLoader);
	}
}