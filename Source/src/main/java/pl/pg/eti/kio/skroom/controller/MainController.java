package pl.pg.eti.kio.skroom.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;
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
	@Autowired private ProjectDao projectDao;

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

		ModelAndView model = new ModelAndView(INDEX_JSP_LOCATION);
		model.addObject("siteName", "dashboard");
		model.addObject("list", taskList);
		model.addObject("selectedProject", project);
		model.addObject("availableProjects", projectDao.getProjectsForUser(dbConnection, user));

		return model;
	}

	@RequestMapping(value = "/productbacklog", method = RequestMethod.GET)
	public ModelAndView showProductBacklog(@ModelAttribute("loggedUser") User user, @ModelAttribute("selectedProject") Project project) {
		ModelAndView check = checkSessionAttributes(user, project);
		if(check != null) {
			return check;
		}

		Connection dbConnection = DatabaseSettings.getDatabaseConnection();

		ModelAndView model = new ModelAndView(INDEX_JSP_LOCATION);
		model.addObject("siteName", "productBacklog");
		model.addObject("selectedProject", project);
		model.addObject("availableProjects", projectDao.getProjectsForUser(dbConnection, user));

		return model;
	}

	@RequestMapping(value = "/sprintbacklog", method = RequestMethod.GET)
	public ModelAndView showSprintBacklog(@ModelAttribute("loggedUser") User user, @ModelAttribute("selectedProject") Project project) {
		ModelAndView check = checkSessionAttributes(user, project);
		if(check != null) {
			return check;
		}

		Connection dbConnection = DatabaseSettings.getDatabaseConnection();

		ModelAndView model = new ModelAndView(INDEX_JSP_LOCATION);
		model.addObject("siteName", "sprintBacklog");
		model.addObject("selectedProject", project);
		model.addObject("availableProjects", projectDao.getProjectsForUser(dbConnection, user));

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

		ModelAndView model = new ModelAndView(INDEX_JSP_LOCATION);
		model.addObject("siteName", "kanban");
		model.addObject("list", taskList);
		model.addObject("selectedProject", project);
		model.addObject("availableProjects", projectDao.getProjectsForUser(dbConnection, user));

		return model;
	}

	@RequestMapping(value = "/issues", method = RequestMethod.GET)
	public ModelAndView showIssues(@ModelAttribute("loggedUser") User user, @ModelAttribute("selectedProject") Project project) {
		ModelAndView check = checkSessionAttributes(user, project);
		if(check != null) {
			return check;
		}

		Connection dbConnection = DatabaseSettings.getDatabaseConnection();

		ModelAndView model = new ModelAndView(INDEX_JSP_LOCATION);
		model.addObject("siteName", "issues");
		model.addObject("selectedProject", project);
		model.addObject("availableProjects", projectDao.getProjectsForUser(dbConnection, user));

		return model;
	}

	@RequestMapping(value = "/settings", method = RequestMethod.GET)
	public ModelAndView showProjectSettings(@ModelAttribute("loggedUser") User user, @ModelAttribute("selectedProject") Project project) {
		ModelAndView check = checkSessionAttributes(user, project);
		if(check != null) {
			return check;
		}

		Connection dbConnection = DatabaseSettings.getDatabaseConnection();

		ModelAndView model = new ModelAndView(INDEX_JSP_LOCATION);
		model.addObject("siteName", "settings");
		model.addObject("selectedProject", project);
		model.addObject("availableProjects", projectDao.getProjectsForUser(dbConnection, user));

		return model;
	}

	@RequestMapping(value = "/userAdmin", method = RequestMethod.GET)
	public ModelAndView showUserPrivilagesSettings(@ModelAttribute("loggedUser") User user, @ModelAttribute("selectedProject") Project project) {
		if (isUserNull(user)) {
			return getLoginModel();
		}

		if(!user.getRole().equals(UserRole.ADMIN)) {
			return showDashboard(user, project);
		}

		Connection dbConnection = DatabaseSettings.getDatabaseConnection();

		ModelAndView model = new ModelAndView(INDEX_JSP_LOCATION);
		model.addObject("siteName", "userAdmin");
		model.addObject("selectedProject", project);
		model.addObject("availableProjects", projectDao.getProjectsForUser(dbConnection, user));

		return model;
	}

	@RequestMapping(value = "/userSettings", method = RequestMethod.GET)
	public ModelAndView showUserSettings(@ModelAttribute("loggedUser") User user, @ModelAttribute("selectedProject") Project project) {
		if (isUserNull(user)) {
			return getLoginModel();
		}

		Connection dbConnection = DatabaseSettings.getDatabaseConnection();

		ModelAndView model = new ModelAndView(INDEX_JSP_LOCATION);
		model.addObject("siteName", "userSettings");
		model.addObject("selectedProject", project);
		model.addObject("availableProjects", projectDao.getProjectsForUser(dbConnection, user));

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
