package pl.pg.eti.kio.skroom.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.ModelAndView;
import pl.pg.eti.kio.skroom.model.*;
import pl.pg.eti.kio.skroom.model.dao.TaskDao;
import pl.pg.eti.kio.skroom.model.dao.UserDao;
import pl.pg.eti.kio.skroom.model.dao.UserRolesInProjectDao;
import pl.pg.eti.kio.skroom.model.dao.UserStoryDao;
import pl.pg.eti.kio.skroom.model.dba.tables.UserStories;
import pl.pg.eti.kio.skroom.model.dba.tables.UsersSettings;
import pl.pg.eti.kio.skroom.model.enumeration.UserRole;
import pl.pg.eti.kio.skroom.settings.DatabaseSettings;

import javax.annotation.PostConstruct;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import static pl.pg.eti.kio.skroom.controller.Views.LOGIN_JSP_LOCATION;

/**
 * Main model and view controller.
 *
 * @author Wojciech Stanisławski
 * @since 22.05.16
 */
@Controller
@SessionAttributes({"loggedUser", "userSettings"})
public class MainController {

	@Autowired private TaskDao taskDao;
	@Autowired private UserDao userDao;
	@Autowired private UserStoryDao userStoryDao;
	@Autowired private DefaultTemplateDataInjector injector;
	@Autowired private WebRequest request;
	@Autowired private UserRolesInProjectDao userRolesInProjectDao;

	@PostConstruct
	public void initDatabase() {
		DatabaseSettings.initConnection("jdbc:sqlite:data.db");
	}

	@ModelAttribute("loggedUser")
	public User defaultNullUser() {
		return new User();
	}

	@ModelAttribute("userSettings")
	public UserSettings defaultNullUserSettings() {
		return new UserSettings();
	}

	@RequestMapping(value = { "/", "/dashboard" }, method = RequestMethod.GET)
	public ModelAndView showDashboard(@ModelAttribute("loggedUser") User user, @ModelAttribute("userSettings") UserSettings userSettings) {
		ModelAndView check = checkSessionAttributes(user, userSettings);
		if(check != null) {
			return check;
		}

		Connection dbConnection = DatabaseSettings.getDatabaseConnection();

		List<Task> taskList = taskDao.fetchTasks(dbConnection);

		ModelAndView model = injector.getIndexForSiteName(Views.DASHBOARD_JSP_LOCATION, "Dashboard", userSettings.getRecentProject(), user, request);
		model.addObject("list", taskList);

		return model;
	}

	@RequestMapping(value = "/productbacklog", method = RequestMethod.GET)
	public ModelAndView showProductBacklog(@ModelAttribute("loggedUser") User user, @ModelAttribute("userSettings") UserSettings userSettings,
										   @RequestParam(value = "upp", required = false) String userStoriesPerPageString, @RequestParam(value = "p", required = false) String pageString) {
		ModelAndView check = checkSessionAttributes(user, userSettings);
		if(check != null) {
			return check;
		}

		int userStoriesPerPage = 20, page=0;
		try {
			if(userStoriesPerPageString != null && !userStoriesPerPageString.isEmpty()) {
					userStoriesPerPage = Integer.parseInt(userStoriesPerPageString);
			}
			if(pageString != null && !pageString.isEmpty()) {
				page = Integer.parseInt(pageString);
			}
		}
		catch (Exception e) {
		}

		Connection dbConnection = DatabaseSettings.getDatabaseConnection();

		ModelAndView model = injector.getIndexForSiteName(Views.PRODUCT_BACKLOG_FORM_JSP_LOCATION, "Product Backlog", userSettings.getRecentProject(), user, request);

		List<UserStory> userStories = userStoryDao.fetchUserStoriesForProject(dbConnection, userSettings.getRecentProject());
		boolean userStoriesFit = userStoriesPerPage > userStories.size();
		if(!userStoriesFit) {
			userStories = userStories.subList(Math.min(userStories.size() - 1, page * userStoriesPerPage), Math.min(userStories.size(), (page + 1) * userStoriesPerPage));
		}
		model.addObject("userStories", userStories);
		model.addObject("userStoriesFit", userStoriesFit);
		model.addObject("pages", userStories.size() / userStoriesPerPage);

		return model;
	}

	@RequestMapping(value = "/sprintbacklog", method = RequestMethod.GET)
	public ModelAndView showSprintBacklog(@ModelAttribute("loggedUser") User user, @ModelAttribute("userSettings") UserSettings userSettings) {
		ModelAndView check = checkSessionAttributes(user, userSettings);
		if(check != null) {
			return check;
		}

		ModelAndView model = injector.getIndexForSiteName(Views.SPRINT_BACKLOG_FORM_JSP_LOCATION, "sprintBacklog", userSettings.getRecentProject(), user, request);
		return model;
	}

	@RequestMapping(value = "/kanban", method = RequestMethod.GET)
	public ModelAndView showKanbanBoard(@ModelAttribute("loggedUser") User user, @ModelAttribute("userSettings") UserSettings userSettings) {
		ModelAndView check = checkSessionAttributes(user, userSettings);
		if(check != null) {
			return check;
		}

		Connection dbConnection = DatabaseSettings.getDatabaseConnection();
		List<Task> taskList = taskDao.fetchTasks(dbConnection);

		ModelAndView model = injector.getIndexForSiteName(Views.KANBAN_BOARD_FORM_JSP_LOCATION, "Kanban Board", userSettings.getRecentProject(), user, request);
		model.addObject("list", taskList);
		return model;
	}

	@RequestMapping(value = "/issues", method = RequestMethod.GET)
	public ModelAndView showIssues(@ModelAttribute("loggedUser") User user, @ModelAttribute("userSettings") UserSettings userSettings) {
		ModelAndView check = checkSessionAttributes(user, userSettings);
		if(check != null) {
			return check;
		}

		ModelAndView model = injector.getIndexForSiteName(Views.PROJECT_ISSUES_FORM_JSP_LOCATION, "Issues", userSettings.getRecentProject(), user, request);
		return model;
	}

	@RequestMapping(value = "/settings", method = RequestMethod.GET)
	public ModelAndView showProjectSettings(@ModelAttribute("loggedUser") User user, @ModelAttribute("userSettings") UserSettings userSettings) {
		ModelAndView check = checkSessionAttributes(user, userSettings);
		if(check != null) {
			return check;
		}

		ModelAndView model = injector.getIndexForSiteName(Views.PROJECT_SETTINGS_FORM_JSP_LOCATION, "Settings", userSettings.getRecentProject(), user, request);
		return model;
	}

	@RequestMapping(value = "/userAdmin", method = RequestMethod.GET)
	public ModelAndView showUserPrivilagesSettings(@ModelAttribute("loggedUser") User user, @ModelAttribute("userSettings") UserSettings userSettings,
												   @RequestParam(value = "upp", required = false) String usersPerPageString) {
		int usersPerPage = 5;
		if(usersPerPageString != null) {
			try {
				usersPerPage = Integer.parseInt(usersPerPageString);
			}
			catch (Exception e) {
			}
		}

		Connection dbConnection = DatabaseSettings.getDatabaseConnection();

		boolean isAdmin = UserRole.ADMIN.equals(user.getRole());
		boolean canEdit = userDao.checkIfHasProjectEditPermissions(dbConnection, user, userSettings.getRecentProject());

		if(!isAdmin && !canEdit) {
			return new ModelAndView("redirect:/");
		}

		ModelAndView model = injector.getIndexForSiteName(Views.USER_ADMIN_FORM_JSP_LOCATION,"userAdmin", userSettings.getRecentProject(), user, request);
		model.addObject("canEdit", canEdit);
		List<User> allUsers = userDao.listAllUsers(dbConnection);
		model.addObject("globalUsers", allUsers.subList(0, Math.min(allUsers.size(), usersPerPage)));

		List<UserRolesInProject> allUserRolesInProjects = userRolesInProjectDao.listAllUserRolesInProject(dbConnection);
		model.addObject("globalUserRolesInProjects", allUserRolesInProjects);
		return model;
	}

	@RequestMapping(value = "/userSettings", method = RequestMethod.GET)
	public ModelAndView showUserSettings(@ModelAttribute("loggedUser") User user, @ModelAttribute("userSettings") UserSettings userSettings) {
		ModelAndView model = injector.getIndexForSiteName(Views.USER_SETTINGS_FORM_JSP_LOCATION, "User Settings", userSettings.getRecentProject(), user, request);
		return model;
	}

	private ModelAndView checkSessionAttributes(User user, UserSettings usersSettings) {
		if (usersSettings.getRecentProject() == null) {
			return showUserSettings(user, usersSettings);
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
}
