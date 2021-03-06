package pl.pg.eti.kio.skroom;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

/**
 * Bundles resources for bundle JSP tag.
 *
 * @author Wojciech Stanisławski
 * @since 22.05.16
 */
@Service
public class ResourcesBundlesService {
	
	private Map<String, List<String>> bundles = new HashMap<>();
	
	public ResourcesBundlesService() {
		bundles.put("jquery-ui", getJqueryUiBundle());
		bundles.put("jquery", getJqueryBundle());
		bundles.put("main", getMainBundle());
		bundles.put("bootstrap", getBootstrapBundle());
		bundles.put("font-awesome", getFontAwesomeBundle());
		bundles.put("full-calendar", getFullCalendarBundle());
	}

	public List<String> getBundle(String bundleName) {
		return bundles.get(bundleName);
	}
	
	private List<String> getMainBundle() {
		return Arrays.asList("resources/css/app/site.css",
				"resources/css/app/menu.css",
				"resources/css/app/kanbanBoard.css",
				"resources/css/app/dashboard.css",
				"resources/css/app/login.css",
				"resources/css/app/productBacklog.css",
				"resources/js/app/skroom.js");
	}

	private List<String> getJqueryBundle() {
		return Arrays.asList("resources/js/jquery/jquery-1.9.1.min.js");
	}

	private List<String> getFullCalendarBundle() {
		return Arrays.asList("resources/js/moment.min.js",
				"resources/js/fullcalendar.min.js",
				"resources/js/full-calendar-locale-all.js",
				"resources/css/fullcalendar.min.css");
	}
	
	private List<String> getJqueryUiBundle() {
		return Arrays.asList("resources/js/jqueryui/jquery-ui-1.9.1.min.js",
				"resources/css/jqueryui/jquery.ui.core.css");
	}

	private List<String> getBootstrapBundle() {
		return Arrays.asList("resources/js/bootstrap/bootstrap.min.js",
				"resources/css/bootstrap/bootstrap.min.css",
				"resources/css/bootstrap/bootstrap-theme.min.css",
				"resources/css/bootstrap-colorpicker.min.css",
				"resources/js/bootstrap-colorpicker.min.js");
	}

	private List<String> getFontAwesomeBundle() {
		return Arrays.asList("resources/css/font-awesome.css");
	}

}
