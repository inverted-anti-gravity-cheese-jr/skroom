package pl.pg.eti.kio.skroom.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.ModelAndView;
import pl.pg.eti.kio.skroom.model.Project;
import pl.pg.eti.kio.skroom.model.User;
import pl.pg.eti.kio.skroom.model.dao.ProjectDao;
import pl.pg.eti.kio.skroom.model.enumeration.UserRole;
import pl.pg.eti.kio.skroom.settings.DatabaseSettings;

/**
 * Class for injecting default data inside a JSP page based on index
 *
 * @author Wojciech Stanisławski
 * @since 10.11.16
 */
@Service
public class DefaultTemplateDataInjector {

	private static final String REDIRECT_TO_LOGIN = "redirect:/login";

	@Autowired
	private ProjectDao projectDao;

	/**
	 * Gets index site and injects important variables into it. If user is not logged in, then
	 * he will be redirected to login page.
	 *
	 * @param viewName			Name of a view, used for menu options.
	 * @param pageTitle			Page title, displayed in title bar.
	 * @param selectedProject	Currently selected project or null if there is none selected.
	 * @param user				Logged in user (from the session)
	 * @param request			?
	 * @return					View with injected all necessary variables.
	 */
	public ModelAndView getIndexForSiteName(String viewName, String pageTitle, Project selectedProject, User user, WebRequest request) {
		return getIndexForSiteName(viewName, pageTitle, selectedProject, user, request, true);
	}

	/**
	 * Gets index site and injects important variables into it.
	 *
	 * @param viewName			Name of a view, used for menu options.
	 * @param pageTitle			Page title, displayed in title bar.
	 * @param selectedProject	Currently selected project or null if there is none selected.
	 * @param user				Logged in user (from the session)
	 * @param request			?
	 * @param checkForLogin		If true then method redirects to login page if user is not logged in.
	 * @return					View with injected all necessary variables.
	 */
	public ModelAndView getIndexForSiteName(String viewName, String pageTitle, Project selectedProject, User user, WebRequest request, boolean checkForLogin) {
		if(checkForLogin && (user == null || user.getId() < 0)) {
			return new ModelAndView(REDIRECT_TO_LOGIN);
		}
		ModelAndView modelAndView = new ModelAndView(viewName);
		modelAndView.addObject("pageTitle", pageTitle);
		modelAndView.addObject("siteName", request.getDescription(false).substring(4));
		if(selectedProject != null) {
			modelAndView.addObject("selectedProject", selectedProject);
		}
		modelAndView.addObject("canCreateProjects", UserRole.ADMIN.equals(user.getRole()) || UserRole.PROJECT_MANAGER.equals(user.getRole()));
		modelAndView.addObject("isAdmin", UserRole.ADMIN.equals(user.getRole()));
		modelAndView.addObject("isProjectSelected", selectedProject != null);

		modelAndView.addObject("menuAvailableProjects", projectDao.getProjectsForUser(DatabaseSettings.getDatabaseConnection(), user));
		return modelAndView;
	}

}
