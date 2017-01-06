package pl.pg.eti.kio.skroom.controller;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.ModelAndView;

import pl.pg.eti.kio.skroom.model.*;
import pl.pg.eti.kio.skroom.model.dao.*;
import pl.pg.eti.kio.skroom.model.enumeration.UserRole;
import pl.pg.eti.kio.skroom.settings.DatabaseSettings;

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
	@Autowired private TaskStatusDao taskStatusDao;
	@Autowired private UserDao userDao;
	@Autowired private SprintDao sprintDao;
	@Autowired private UserStoryDao userStoryDao;
	@Autowired private UserStoryStatusDao userStoryStatusDao;
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

		List<UserStory> userStories = userStoryDao.fetchUserStoriesForProject(dbConnection, userSettings.getRecentProject());
		List<TaskStatus> taskStatuses = taskStatusDao.fetchByProject(dbConnection,userSettings.getRecentProject());
		List<Sprint> sprints = sprintDao.fetchAvailableSprintsForProject(dbConnection, userSettings.getRecentProject());
		List<Task> taskList = taskDao.fetchTasks(dbConnection, userSettings.getRecentProject(), taskStatuses, userStories, sprints);

		ModelAndView model = injector.getIndexForSiteName(Views.DASHBOARD_JSP_LOCATION, "Dashboard", userSettings.getRecentProject(), user, request);
		model.addObject("list", taskList);

		return model;
	}

	@RequestMapping(value = "/productbacklog", method = RequestMethod.GET)
	public ModelAndView showProductBacklog(@ModelAttribute("loggedUser") User user, @ModelAttribute("userSettings") UserSettings userSettings,
										   @RequestParam(value = "p", required = false) Integer page) {
		ModelAndView check = checkSessionAttributes(user, userSettings);
		if(check != null) {
			return check;
		}

		int userStoriesPerPage = userSettings.getUserStoriesPerPage();
		if(page == null) {
			page = 0;
		}
		if(userStoriesPerPage < 1) {
			userStoriesPerPage = 10;
		}

		Connection dbConnection = DatabaseSettings.getDatabaseConnection();

		ModelAndView model = injector.getIndexForSiteName(Views.PRODUCT_BACKLOG_FORM_JSP_LOCATION, "Product Backlog", userSettings.getRecentProject(), user, request);

		List<UserStory> userStories = userStoryDao.fetchUserStoriesForProject(dbConnection, userSettings.getRecentProject());
		int pages = userStories.size() / userStoriesPerPage;
		boolean userStoriesFit = userStoriesPerPage > userStories.size();
		if(!userStoriesFit) {
			userStories = userStories.subList(Math.min(userStories.size() - 1, page * userStoriesPerPage), Math.min(userStories.size(), (page + 1) * userStoriesPerPage));
		}

		List<Sprint> sprints = sprintDao.fetchSprintsForProject(dbConnection, userSettings.getRecentProject());

		model.addObject("userStories", userStories);
		model.addObject("userStoriesFit", userStoriesFit);
		model.addObject("pages", pages);
		model.addObject("sprints", sprints);
		model.addObject("sprintCount", sprintDao.getSprintCountForProject(dbConnection, userSettings.getRecentProject()));

		return model;
	}

	@RequestMapping(value = "/sprintbacklog", method = RequestMethod.GET)
	public ModelAndView showSprintBacklog(@ModelAttribute("loggedUser") User user, @ModelAttribute("userSettings") UserSettings userSettings, @RequestParam(value = "spr", required = false) Integer sprintId) {
		ModelAndView check = checkSessionAttributes(user, userSettings);
		if(check != null) {
			return check;
		}
		
		Connection dbConnection = DatabaseSettings.getDatabaseConnection();
		ModelAndView model = injector.getIndexForSiteName(Views.SPRINT_BACKLOG_FORM_JSP_LOCATION, "sprintBacklog", userSettings.getRecentProject(), user, request);

		List<Sprint> sprints = sprintDao.fetchAvailableSprintsForProject(dbConnection, userSettings.getRecentProject());
		Sprint lastSprint = sprints.stream().sorted((s1, s2) -> s2.getStartDate().compareTo(s1.getStartDate())).findFirst().get();
		
		taskDao.moveTasksToCurrentSprint(dbConnection, lastSprint);
		
		List<UserStory> userStories = userStoryDao.fetchUserStoriesForProject(dbConnection, userSettings.getRecentProject());
		List<TaskStatus> taskStatuses = taskStatusDao.fetchByProject(dbConnection,userSettings.getRecentProject());
		List<Task> taskList = taskDao.fetchTasks(dbConnection, userSettings.getRecentProject(), taskStatuses, userStories, sprints);
		
		sprints.remove(lastSprint);

		if(sprintId == null) {
			sprintId = lastSprint.getId();
		}

		if(!userStories.isEmpty() && (sprintId == null || sprintId == lastSprint.getId())) {
			model.addObject("showNewButton", true);
		}
		model.addObject("sprintsWithoutLast", sprints);
		model.addObject("lastSprint", lastSprint);
		final int sprFin = sprintId;
		List<Task> tasksFiltered = taskList.stream().filter(t -> t.getSprint() != null && t.getSprint().getId() == sprFin).collect(Collectors.toList());
		model.addObject("tasks", tasksFiltered);
		return model;
	}

	@RequestMapping(value = "/kanban", method = RequestMethod.GET)
	public ModelAndView showKanbanBoard(@ModelAttribute("loggedUser") User user, @ModelAttribute("userSettings") UserSettings userSettings, @RequestParam(value = "spr", required = false) Integer sprintId) {
		ModelAndView check = checkSessionAttributes(user, userSettings);
		if(check != null) {
			return check;
		}

		Connection dbConnection = DatabaseSettings.getDatabaseConnection();
		List<Sprint> sprints = sprintDao.fetchAvailableSprintsForProject(dbConnection, userSettings.getRecentProject());
		Sprint lastSprint = sprints.stream().sorted((s1, s2) -> s2.getStartDate().compareTo(s1.getStartDate())).findFirst().get();
		
		taskDao.moveTasksToCurrentSprint(dbConnection, lastSprint);
		
		List<UserStory> userStories = userStoryDao.fetchUserStoriesForProject(dbConnection, userSettings.getRecentProject());
		List<TaskStatus> taskStatuses = taskStatusDao.fetchByProject(dbConnection,userSettings.getRecentProject());
		List<Task> taskList = taskDao.fetchTasks(dbConnection, userSettings.getRecentProject(), taskStatuses, userStories, sprints);
		
		sprints.remove(lastSprint);

		if(sprintId == null) {
			sprintId = lastSprint.getId();
		}


		ModelAndView model = injector.getIndexForSiteName(Views.KANBAN_BOARD_FORM_JSP_LOCATION, "Kanban Board", userSettings.getRecentProject(), user, request);
		model.addObject("sprintsWithoutLast", sprints);
		model.addObject("lastSprint", lastSprint);
		model.addObject("sprintId", sprintId);
		final int sprFin = sprintId;
		List<Task> tasksFiltered = taskList.stream().filter(t -> t.getSprint().getId() == sprFin).collect(Collectors.toList());
		model.addObject("userStories", userStories);
		model.addObject("tasks", tasksFiltered);
		model.addObject("taskStatuses", taskStatuses);
		return model;
	}

	@RequestMapping(value = "/admin", method = RequestMethod.GET)
	public ModelAndView showUserPrivilagesSettings(@ModelAttribute("loggedUser") User user, @ModelAttribute("userSettings") UserSettings userSettings,
												   @RequestParam(value = "upp", required = false) Integer usersPerPage, @RequestParam(value = "un", required = false) String userNameFilter) {
		if(usersPerPage == null || usersPerPage < 1) {
			usersPerPage = 5;
		}

		Connection dbConnection = DatabaseSettings.getDatabaseConnection();

		boolean isAdmin = UserRole.ADMIN.equals(user.getRole());
		boolean canEdit = userDao.checkIfHasProjectEditPermissions(dbConnection, user, userSettings.getRecentProject());

		if(!isAdmin && !canEdit) {
			return new ModelAndView("redirect:/");
		}

		ArrayList<UserStoryStatus> userStoryStatuses = userStoryStatusDao.listAllStatuses(dbConnection);

		ModelAndView model = injector.getIndexForSiteName(Views.USER_ADMIN_FORM_JSP_LOCATION,"userAdmin", userSettings.getRecentProject(), user, request);
		model.addObject("canEdit", canEdit);
		List<UserDao.UserContainer> allUsers = userDao.listAllUsers(dbConnection, userNameFilter);
		model.addObject("globalUsers", allUsers.subList(0, Math.min(allUsers.size(), usersPerPage)));
		model.addObject("usStatuses", userStoryStatuses);

		List<UserRolesInProject> allUserRolesInProjects = userRolesInProjectDao.listAllUserRolesInProject(dbConnection);
		model.addObject("globalUserRolesInProjects", allUserRolesInProjects);
		return model;
	}

	@RequestMapping(value = "/userSettings", method = RequestMethod.GET)
	public ModelAndView showUserSettings(@ModelAttribute("loggedUser") User user, @ModelAttribute("userSettings") UserSettings userSettings) {
		ModelAndView model = injector.getIndexForSiteName(Views.USER_SETTINGS_FORM_JSP_LOCATION, "User Settings", userSettings.getRecentProject(), user, request);
		return model;
	}

	@RequestMapping(value = "/selectProject", method = RequestMethod.GET)
	public ModelAndView noProjectForm(@ModelAttribute("loggedUser") User user, @ModelAttribute("userSettings") UserSettings userSettings) {
		ModelAndView model = injector.getIndexForSiteName(Views.NO_PROJECT_JSP_LOCATION, "Select project", userSettings.getRecentProject(), user, request);

		return model;
	}

	private ModelAndView checkSessionAttributes(User user, UserSettings userSettings) {
		if (userSettings.getRecentProject() == null) {
			return noProjectForm(user, userSettings);
		}
		return null;
	}
}
