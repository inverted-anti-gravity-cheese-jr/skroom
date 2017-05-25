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
		bundles.put("jquery-ui-js", getJqueryUiBundleJs());
		bundles.put("jquery-ui-css", getJqueryUiBundleCss());
		bundles.put("jquery-js", getJqueryBundleJs());
		bundles.put("main-js", getMainBundleJs());
		bundles.put("main-css", getMainBundleCss());
		bundles.put("bootstrap-js", getBootstrapBundleJs());
		bundles.put("bootstrap-css", getBootstrapBundleCss());
		bundles.put("font-awesome-css", getFontAwesomeBundleCss());
		bundles.put("full-calendar-js", getFullCalendarBundleJs());
		bundles.put("full-calendar-css", getFullCalendarBundleCss());
	}

	public Map<String, List<String>> getBundles() {
		return bundles;
	}

	public List<String> getBundle(String bundleName) {
		return bundles.get(bundleName);
	}
	
	private List<String> getMainBundleCss() {
		return Arrays.asList("resources/css/app/site.css",
				"resources/css/app/menu.css",
				"resources/css/app/kanbanBoard.css",
				"resources/css/app/dashboard.css",
				"resources/css/app/login.css",
				"resources/css/app/productBacklog.css");
	}

	private List<String> getMainBundleJs() {
		return Arrays.asList("resources/js/app/skroom.js");
	}

	private List<String> getJqueryBundleJs() {
		return Arrays.asList("resources/js/jquery/jquery-1.9.1.min.js");
	}

	private List<String> getFullCalendarBundleJs() {
		return Arrays.asList("resources/js/moment.min.js",
				"resources/js/fullcalendar.min.js",
				"resources/js/full-calendar-locale-all.js");
	}

	private List<String> getFullCalendarBundleCss() {
		return Arrays.asList("resources/css/fullcalendar.min.css");
	}
	
	private List<String> getJqueryUiBundleJs() {
		return Arrays.asList("resources/js/jqueryui/jquery-ui-1.9.1.min.js");
	}

	private List<String> getJqueryUiBundleCss() {
		return Arrays.asList("resources/css/jqueryui/jquery.ui.core.css");
	}

	private List<String> getBootstrapBundleJs() {
		return Arrays.asList("resources/js/bootstrap/bootstrap.min.js",
				"resources/js/bootstrap-colorpicker.min.js");
	}

	private List<String> getBootstrapBundleCss() {
		return Arrays.asList("resources/css/bootstrap/bootstrap.min.css",
				"resources/css/bootstrap/bootstrap-theme.min.css",
				"resources/css/bootstrap-colorpicker.min.css");
	}

	private List<String> getFontAwesomeBundleCss() {
		return Arrays.asList("resources/css/font-awesome.css");
	}

}
