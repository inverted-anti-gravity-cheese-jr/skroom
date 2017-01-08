package pl.pg.eti.kio.skroom;


import java.net.InetSocketAddress;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.webapp.WebAppContext;

import pl.pg.eti.kio.skroom.tags.ResourceBundleTag;

public class SkroomServer {

	public static void main(String[] args) throws Exception {
		ResourceBundleTag.changeRootPath("/");
		
		Server server = new Server(new InetSocketAddress(8080));

		WebAppContext webapp = new WebAppContext();
	    webapp.setContextPath("/");
	    webapp.setWar("D:/skroom/Source/build/libs/skroom.war");
	    webapp.setExtractWAR(true);
	    server.setHandler(webapp);

        server.start();
        System.out.println("STARTED SKROOM");
        System.out.println("Go to http://localhost:8080");
        server.join();
	}
}
