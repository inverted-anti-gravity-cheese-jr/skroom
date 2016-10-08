package pl.pg.eti.kio.skroom;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.web.servlet.ModelAndView;

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
	}

	public List<String> getBundle(String bundleName) {
		return bundles.get(bundleName);
	}
	
	private List<String> getMainBundle() {
		return Arrays.asList("resources/css/app/site.css",
				"resources/css/app/menu.css",
				"resources/css/app/kanbanBoard.css",
				"resources/css/app/dashboard.css",
				"resources/css/app/login.css");
	}

	private List<String> getJqueryBundle() {
		return Arrays.asList("resources/js/jquery/jquery-1.8.2.min.js");
	}
	
	private List<String> getJqueryUiBundle() {
		return Arrays.asList("resources/js/jqueryui/jquery-ui-1.8.24.min.js",
				"resources/css/jqueryui/jquery.ui.core.css");
	}

	private List<String> getBootstrapBundle() {
		return Arrays.asList("resources/js/bootstrap/bootstrap.min.js",
				"resources/css/bootstrap/bootstrap.min.css",
				"resources/css/bootstrap/bootstrap-theme.min.css");
	}

}
