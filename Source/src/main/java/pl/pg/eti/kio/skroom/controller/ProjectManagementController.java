package pl.pg.eti.kio.skroom.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.ModelAndView;
import pl.pg.eti.kio.skroom.model.Project;
import pl.pg.eti.kio.skroom.model.User;
import pl.pg.eti.kio.skroom.model.dao.ProjectDao;
import pl.pg.eti.kio.skroom.settings.DatabaseSettings;

import java.sql.Connection;

/**
 * @author Wojciech Stanisławski, Krzysztof Świeczkowski
 * @since 09.11.16
 */
@Controller
@SessionAttributes({"loggedUser", "selectedProject"})
public class ProjectManagementController {

	@Autowired private ProjectDao projectDao;
	@Autowired private DefaultTemplateDataInjector injector;
	@Autowired private WebRequest request;

	@RequestMapping(value = "/addProject", method = RequestMethod.GET)
	public ModelAndView addProjectForm(@ModelAttribute("loggedUser") User user) {
		ModelAndView modelAndView = injector.getIndexForSiteName(Views.PROJECT_FORM_JSP_LOCATION, "Add Project", null, user, request);

		modelAndView.addObject("submitButtonText", "Add Project");
		modelAndView.addObject("displayDeleteButton", "false");

		return modelAndView;
	}

	@RequestMapping(value = "/addProject", method = RequestMethod.POST)
	public ModelAndView addProject(@ModelAttribute("loggedUser") User user, @RequestParam String name, @RequestParam String description) {
		Project project = new Project();

		project.setName(name);
		project.setDescription(description);

		Connection dbConnection = DatabaseSettings.getDatabaseConnection();

		projectDao.addProject(dbConnection, project);

		return new ModelAndView("redirect:/");
	}

	@RequestMapping(value = "/editProject", method = RequestMethod.GET)
	public ModelAndView editProjectRedirect() {
		return new ModelAndView("redirect:/");
	}

	@RequestMapping(value = "/editProject/{projectId}", method = RequestMethod.GET)
	public ModelAndView editProjectForm(@ModelAttribute("loggedUser") User user, @PathVariable Integer projectId) {
		Connection dbConnection = DatabaseSettings.getDatabaseConnection();

		Project project = projectDao.getProjectForUser(dbConnection,projectId,user);
		if(project == null) {
			return new ModelAndView("redirect:/");
		}
		ModelAndView modelAndView = injector.getIndexForSiteName(Views.PROJECT_FORM_JSP_LOCATION, "Edit Project", null, user, request);
		modelAndView.addObject("name", project.getName());
		modelAndView.addObject("description", project.getDescription());
		modelAndView.addObject("submitButtonText", "Update Project");
		modelAndView.addObject("displayDeleteButton", "true");

		return modelAndView;
	}

	@RequestMapping(value = "/editProject/{projectId}", method = RequestMethod.POST)
	public ModelAndView editProject(@ModelAttribute("loggedUser") User user, @PathVariable Integer projectId, @RequestParam String name, @RequestParam String description) {
		Connection dbConnection = DatabaseSettings.getDatabaseConnection();

		Project project = projectDao.getProjectForUser(dbConnection,projectId,user);
		if(project == null) {
			return new ModelAndView("redirect:/");
		}

		project.setName(name);
		project.setDescription(description);

		projectDao.updateProject(dbConnection, project);

		return new ModelAndView("redirect:/");
	}

	@RequestMapping(value = "/removeProject/{projectId}")
	public ModelAndView removeProject(@ModelAttribute("loggedUser") User user, @PathVariable Integer projectId) {
		return new ModelAndView("redirect:/");
	}
}