package pl.pg.eti.kio.skroom.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import pl.pg.eti.kio.skroom.model.Project;
import pl.pg.eti.kio.skroom.model.Task;
import pl.pg.eti.kio.skroom.model.User;
import pl.pg.eti.kio.skroom.model.dao.ProjectDao;
import pl.pg.eti.kio.skroom.model.dao.TaskDao;
import pl.pg.eti.kio.skroom.model.dao.UserDao;
import pl.pg.eti.kio.skroom.model.enumeration.UserRole;
import pl.pg.eti.kio.skroom.settings.DatabaseSettings;

import javax.annotation.PostConstruct;
import java.sql.Connection;
import java.util.List;

import static pl.pg.eti.kio.skroom.controller.Views.INDEX_JSP_LOCATION;
import static pl.pg.eti.kio.skroom.controller.Views.LOGIN_JSP_LOCATION;

/**
 * Main model and view controller.
 *
 * @author Wojciech Stanisławski
 * @since 22.05.16
 */
@Controller
@SessionAttributes({"loggedUser", "selectedProject"})
public class MainController {

	@Autowired private TaskDao taskDao;
	@Autowired private UserDao userDao;
	@Autowired private DefaultTemplateDataInjector injector;

	@PostConstruct
	public void initDatabase() {
		DatabaseSettings.initConnection("jdbc:sqlite:data.db");
	}

	@ModelAttribute("loggedUser")
	public User defaultNullUser() {
		return new User();
	}

	@ModelAttribute("selectedProject")
	public Project defaultNullProject() {
		return new Project();
	}

	@RequestMapping(value = { "/", "/dashboard" }, method = RequestMethod.GET)
	public ModelAndView showDashboard(@ModelAttribute("loggedUser") User user, @ModelAttribute("selectedProject") Project project) {
		ModelAndView check = checkSessionAttributes(user, project);
		if(check != null) {
			return check;
		}

		Connection dbConnection = DatabaseSettings.getDatabaseConnection();

		List<Task> taskList = taskDao.fetchTasks(dbConnection);

		ModelAndView model = injector.getIndexForSiteName("dashboard", project, user);
		model.addObject("list", taskList);

		return model;
	}

	@RequestMapping(value = "/productbacklog", method = RequestMethod.GET)
	public ModelAndView showProductBacklog(@ModelAttribute("loggedUser") User user, @ModelAttribute("selectedProject") Project project) {
		ModelAndView check = checkSessionAttributes(user, project);
		if(check != null) {
			return check;
		}

		ModelAndView model = injector.getIndexForSiteName("productBacklog", project, user);
		return model;
	}

	@RequestMapping(value = "/sprintbacklog", method = RequestMethod.GET)
	public ModelAndView showSprintBacklog(@ModelAttribute("loggedUser") User user, @ModelAttribute("selectedProject") Project project) {
		ModelAndView check = checkSessionAttributes(user, project);
		if(check != null) {
			return check;
		}

		ModelAndView model = injector.getIndexForSiteName("sprintBacklog", project, user);
		return model;
	}

	@RequestMapping(value = "/kanban", method = RequestMethod.GET)
	public ModelAndView showKanbanBoard(@ModelAttribute("loggedUser") User user, @ModelAttribute("selectedProject") Project project) {
		ModelAndView check = checkSessionAttributes(user, project);
		if(check != null) {
			return check;
		}

		Connection dbConnection = DatabaseSettings.getDatabaseConnection();
		List<Task> taskList = taskDao.fetchTasks(dbConnection);

		ModelAndView model = injector.getIndexForSiteName("kanban", project, user);
		model.addObject("list", taskList);
		return model;
	}

	@RequestMapping(value = "/issues", method = RequestMethod.GET)
	public ModelAndView showIssues(@ModelAttribute("loggedUser") User user, @ModelAttribute("selectedProject") Project project) {
		ModelAndView check = checkSessionAttributes(user, project);
		if(check != null) {
			return check;
		}

		ModelAndView model = injector.getIndexForSiteName("issues", project, user);
		return model;
	}

	@RequestMapping(value = "/settings", method = RequestMethod.GET)
	public ModelAndView showProjectSettings(@ModelAttribute("loggedUser") User user, @ModelAttribute("selectedProject") Project project) {
		ModelAndView check = checkSessionAttributes(user, project);
		if(check != null) {
			return check;
		}

		ModelAndView model = injector.getIndexForSiteName("settings", project, user);
		return model;
	}

	@RequestMapping(value = "/userAdmin", method = RequestMethod.GET)
	public ModelAndView showUserPrivilagesSettings(@ModelAttribute("loggedUser") User user, @ModelAttribute("selectedProject") Project project,
												   @RequestParam(value = "upp", required = false) String usersPerPageString) {
		if (isUserNull(user)) {
			return getLoginModel();
		}

		int usersPerPage = 5;
		if(usersPerPageString != null) {
			usersPerPage = Integer.parseInt(usersPerPageString);
		}

		Connection dbConnection = DatabaseSettings.getDatabaseConnection();

		boolean isAdmin = UserRole.ADMIN.equals(user.getRole());
		boolean canEdit = userDao.checkIfHasProjectEditPreferences(dbConnection, user, project);

		if(!isAdmin && !canEdit) {
			return new ModelAndView("redirect:/");
		}

		ModelAndView model = injector.getIndexForSiteName("userAdmin", project, user);
		model.addObject("canEdit", canEdit);
		List<User> allUsers = userDao.listAllUsers(dbConnection);
		model.addObject("globalUsers", allUsers.subList(0, Math.min(allUsers.size(), usersPerPage)));
		return model;
	}

	@RequestMapping(value = "/userSettings", method = RequestMethod.GET)
	public ModelAndView showUserSettings(@ModelAttribute("loggedUser") User user, @ModelAttribute("selectedProject") Project project) {
		if (isUserNull(user)) {
			return getLoginModel();
		}

		ModelAndView model = injector.getIndexForSiteName("userSettings", project, user);
		return model;
	}

	private ModelAndView checkSessionAttributes(User user, Project project) {
		if (isUserNull(user)) {
			return getLoginModel();
		}

		if (isProjectNull(project)) {
			return showUserSettings(user, project);
		}

		return null;
	}

	private ModelAndView getLoginModel() {
		return getLoginModel(null);
	}

	private ModelAndView getLoginModel(String error) {
		ModelAndView model = new ModelAndView(LOGIN_JSP_LOCATION);
		model.addObject("loginError", error);
		return model;
	}

	private boolean isUserNull(User user) {
		return user.getId() < 0;
	}

	private boolean isProjectNull(Project project) {
		return project.getId() < 0;
	}
}
