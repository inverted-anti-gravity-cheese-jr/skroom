package pl.pg.eti.kio.skroom.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.ModelAndView;
import pl.pg.eti.kio.skroom.model.Project;
import pl.pg.eti.kio.skroom.model.User;
import pl.pg.eti.kio.skroom.model.UserSettings;
import pl.pg.eti.kio.skroom.model.dao.ProjectDao;
import pl.pg.eti.kio.skroom.model.dao.SprintDao;
import pl.pg.eti.kio.skroom.model.dao.UserDao;
import pl.pg.eti.kio.skroom.model.dao.UserStoryDao;
import pl.pg.eti.kio.skroom.settings.DatabaseSettings;

import javax.annotation.PostConstruct;
import java.sql.Connection;

/**
 * Main controller for managing projects.
 *
 * @author Wojciech Stanisławski, Krzysztof Świeczkowski
 * @since 09.11.16
 */
@Controller
@SessionAttributes({"loggedUser", "userSettings"})
public class ProjectManagementController {

	@Autowired private ProjectDao projectDao;
	@Autowired private SprintDao sprintDao;
	@Autowired private UserDao userDao;
	@Autowired private UserStoryDao userStoryDao;
	@Autowired private DefaultTemplateDataInjector injector;
	@Autowired private WebRequest request;

	@ModelAttribute("loggedUser")
	public User defaultNullUser() {
		return new User();
	}

	@ModelAttribute("userSettings")
	public UserSettings defaultNullUserSettings() {
		return new UserSettings();
	}

	@RequestMapping(value = "/addProject", method = RequestMethod.GET)
	public ModelAndView addProjectForm(@ModelAttribute("loggedUser") User user, @ModelAttribute("userSettings") UserSettings userSettings) {
		ModelAndView modelAndView = injector.getIndexForSiteName(Views.PROJECT_FORM_JSP_LOCATION, "Add Project", userSettings.getRecentProject(), user, request);

		modelAndView.addObject("submitButtonText", "Add Project");
		modelAndView.addObject("displayDeleteButton", "false");

		return modelAndView;
	}

	@RequestMapping(value = "/addProject", method = RequestMethod.POST)
	public ModelAndView addProject(@ModelAttribute("loggedUser") User user, @RequestParam String name,
								   @RequestParam String description, @RequestParam("sprint-length") String sprintLengthString,
								   @RequestParam("first-sprint-name") String firstSprintName) {
		int sprintLength = 1;
		try {
			sprintLength = Integer.parseInt(sprintLengthString);
		}
		catch (Exception e) {
			e.printStackTrace();
		}

		Project project = new Project();

		project.setName(name);
		project.setDescription(description);
		project.setDefaultSprintLength(sprintLength);

		Connection dbConnection = DatabaseSettings.getDatabaseConnection();

		projectDao.addProject(dbConnection, project);
		sprintDao.createFirstSprint(dbConnection, project, firstSprintName);

		return new ModelAndView("redirect:/");
	}

	@RequestMapping(value = "/editProject", method = RequestMethod.GET)
	public ModelAndView editProjectRedirect() {
		return new ModelAndView("redirect:/");
	}

	@RequestMapping(value = "/editProject/{projectId}", method = RequestMethod.GET)
	public ModelAndView editProjectForm(@ModelAttribute("loggedUser") User user, @ModelAttribute("userSettings") UserSettings userSettings, @PathVariable Integer projectId) {
		Connection dbConnection = DatabaseSettings.getDatabaseConnection();

		Project project = projectDao.getProjectForUser(dbConnection,projectId,user);
		if(!projectDao.checkUserEditPermissionsForProject(dbConnection, project, user)) {
			return new ModelAndView("redirect:/");
		}
		ModelAndView modelAndView = injector.getIndexForSiteName(Views.PROJECT_FORM_JSP_LOCATION, "Edit Project", userSettings.getRecentProject(), user, request);
		modelAndView.addObject("project", project);
		modelAndView.addObject("submitButtonText", "Update Project");
		modelAndView.addObject("displayDeleteButton", "true");

		return modelAndView;
	}

	@RequestMapping(value = "/editProject/{projectId}", method = RequestMethod.POST)
	public ModelAndView editProject(@ModelAttribute("loggedUser") User user, @PathVariable Integer projectId, @RequestParam String name, @RequestParam String description, @RequestParam("sprint-length") String sprintLengthString) {
		Connection dbConnection = DatabaseSettings.getDatabaseConnection();

		Project project = projectDao.getProjectForUser(dbConnection,projectId,user);
		if(!projectDao.checkUserEditPermissionsForProject(dbConnection, project, user)) {
			return new ModelAndView("redirect:/");
		}

		int sprintLength = 1;
		try {
			sprintLength = Integer.parseInt(sprintLengthString);
		}
		catch (Exception e) {
			e.printStackTrace();
		}

		project.setName(name);
		project.setDescription(description);
		project.setDefaultSprintLength(sprintLength);

		projectDao.updateProject(dbConnection, project);

		return new ModelAndView("redirect:/");
	}

	@RequestMapping(value = "/removeProject/{projectId}")
	public ModelAndView removeProject(@ModelAttribute("loggedUser") User user, @ModelAttribute("userSettings") UserSettings userSettings, @PathVariable Integer projectId) {
		Connection dbConnection = DatabaseSettings.getDatabaseConnection();

		Project project = projectDao.getProjectForUser(dbConnection,projectId,user);
		if(!projectDao.checkUserEditPermissionsForProject(dbConnection, project, user)) {
			return new ModelAndView("redirect:/");
		}

		userStoryDao.removeUserStoriesForProject(dbConnection, project);
		sprintDao.removeSprintsForProject(dbConnection, project);
		projectDao.removeProject(dbConnection, projectId);

		userSettings.setRecentProject(null);
		userDao.saveUserSettings(dbConnection, userSettings);

		return new ModelAndView("redirect:/");
	}
}