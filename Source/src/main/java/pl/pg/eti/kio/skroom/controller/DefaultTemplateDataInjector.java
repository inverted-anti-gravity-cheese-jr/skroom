package pl.pg.eti.kio.skroom.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
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

	public ModelAndView getIndexForSiteName(String siteName, Project selectedProject, User user) {
		ModelAndView modelAndView = new ModelAndView(Views.INDEX_JSP_LOCATION);
		modelAndView.addObject("siteName", siteName);
		if(selectedProject != null) {
			modelAndView.addObject("selectedProject", selectedProject);
		}

		modelAndView.addObject("canCreateProjects", UserRole.ADMIN.equals(user.getRole()) || UserRole.PROJECT_MANAGER.equals(user.getRole()));
		modelAndView.addObject("isAdmin", UserRole.ADMIN.equals(user.getRole()));
		modelAndView.addObject("isProjectSelected", selectedProject != null && selectedProject.getId() > 0 );

		modelAndView.addObject("availableProjects", projectDao.getProjectsForUser(DatabaseSettings.getDatabaseConnection(), user));
		return modelAndView;
	}

}
