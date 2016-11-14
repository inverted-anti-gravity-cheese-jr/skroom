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
 * @author Wojciech Stanisławski
 * @since 10.11.16
 */
@Service
public class DefaultTemplateDataInjector {

	@Autowired
	private ProjectDao projectDao;

	public ModelAndView getIndexForSiteName(String viewName, String pageTitle, Project selectedProject, User user, WebRequest request) {
		ModelAndView modelAndView = new ModelAndView(viewName);
		modelAndView.addObject("pageTitle", pageTitle);
		modelAndView.addObject("siteName", request.getDescription(false).substring(4));
		if(selectedProject != null) {
			modelAndView.addObject("selectedProject", selectedProject);
		}
		modelAndView.addObject("canCreateProjects", UserRole.ADMIN.equals(user.getRole()) || UserRole.PROJECT_MANAGER.equals(user.getRole()));
		modelAndView.addObject("isAdmin", UserRole.ADMIN.equals(user.getRole()));
		modelAndView.addObject("isProjectSelected", selectedProject != null);

		modelAndView.addObject("availableProjects", projectDao.getProjectsForUser(DatabaseSettings.getDatabaseConnection(), user));
		return modelAndView;
	}

}
