package pl.pg.eti.kio.skroom;

import java.util.Properties;

public class PathUtils {
	
	private static String rootPath;
	
	public static String getRootPath() {
		if(rootPath == null) {
			try {
				final Properties prop = new Properties();
				prop.load(PathUtils.class.getClassLoader().getResourceAsStream("webapp/resources/app.properties"));
				rootPath = (String) prop.get("pl.pg.eti.kio.rootPath");
			} catch (Exception e) {
				e.printStackTrace();
				rootPath = "/skroom/";
			}
		}
		return rootPath;
	}
}
