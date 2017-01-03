package pl.pg.eti.kio.skroom.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.ModelAndView;
import pl.pg.eti.kio.skroom.model.*;
import pl.pg.eti.kio.skroom.model.dao.*;
import pl.pg.eti.kio.skroom.settings.DatabaseSettings;

import java.sql.Connection;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static pl.pg.eti.kio.skroom.PlainTextUtil.*;

/**
 * Main controller for managing projects.
 *
 * @author Wojciech Stanisławski, Krzysztof Świeczkowski
 * @since 09.11.16
 */
@Controller
@SessionAttributes({"loggedUser", "userSettings"})
public class ProjectManagementController {

	private static final String ADDING_USER_PROJECT_NO_SUCH_USER = "User does not exist";
	private static final String ADDING_USER_PROJECT_USER_ALREADY_IN_PROJECT = "User already in project";

	@Autowired private ProjectDao projectDao;
	@Autowired private SprintDao sprintDao;
	@Autowired private UserDao userDao;
	@Autowired private TaskStatusDao taskStatusDao;
	@Autowired private UserStoryDao userStoryDao;
	@Autowired private DefaultTemplateDataInjector injector;
	@Autowired private WebRequest request;
	@Autowired private UserProjectDao userProjectDao;
	@Autowired private UserRolesInProjectDao userRolesInProjectDao;

	private static final List<TaskStatus> defaultTaskStatusesMock = Arrays.asList(
			new TaskStatus() {{
				setName("To do");
				setStaysInSprint(false);
			}},
			new TaskStatus() {{
				setName("In progress");
				setStaysInSprint(false);
			}},
			new TaskStatus() {{
				setName("Done");
				setStaysInSprint(true);
			}}
	);

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
		ModelAndView modelAndView = injector.getIndexForSiteName(Views.PROJECT_SETTINGS_FORM_JSP_LOCATION, "Add Project", userSettings.getRecentProject(), user, request);

		modelAndView.addObject("submitButtonText", "Add Project");
		modelAndView.addObject("createProject", "true");
		modelAndView.addObject("taskStatuses", defaultTaskStatusesMock);

		return modelAndView;
	}

	@RequestMapping(value = "/addProject", method = RequestMethod.POST)
	public ModelAndView addProject(@ModelAttribute("loggedUser") User user, @ModelAttribute("userSettings") UserSettings userSettings,
                                   @RequestParam String projectName, @RequestParam String projectDescription,
                                   @RequestParam("sprint-length") String sprintLengthString, @RequestParam("first-sprint-name") String firstSprintName) {

		if(user.getId() < 0) {
			return new ModelAndView("redirect:/");
		}
		int sprintLength = 1;
		try {
			sprintLength = Integer.parseInt(sprintLengthString);
		}
		catch (Exception e) {
			e.printStackTrace();
		}

		Project project = new Project();

		project.setName(projectName);
		project.setDescription(projectDescription.replace(WINDOWS_ENDLINE_STRING, UNIX_ENDLINE_STRING).replace(PLAIN_TEXT_ENDLINE_STRING, HTML_ENDLINE_STRING));
		project.setDefaultSprintLength(sprintLength);

		Connection dbConnection = DatabaseSettings.getDatabaseConnection();

		projectDao.addProject(dbConnection, project, taskStatusDao);
		sprintDao.createFirstSprint(dbConnection, project, firstSprintName);

        userSettings.setRecentProject(project);
        userDao.saveUserSettings(dbConnection, userSettings);

		return new ModelAndView("redirect:/");
	}

	@RequestMapping(value = "/settings", method = RequestMethod.GET)
	public ModelAndView showProjectSettings(@ModelAttribute("loggedUser") User user, @ModelAttribute("userSettings") UserSettings userSettings) {
		Connection dbConnection = DatabaseSettings.getDatabaseConnection();
        if (userSettings.getRecentProject() == null) {
            return new ModelAndView("redirect:/userSettings");
        }

		List<TaskStatus> taskStatuses = taskStatusDao.fetchByProject(dbConnection, userSettings.getRecentProject());

		ModelAndView modelAndView = injector.getIndexForSiteName(Views.PROJECT_SETTINGS_FORM_JSP_LOCATION, "Project Settings", userSettings.getRecentProject(), user, request);
        modelAndView.addObject("projectIsEditable", projectDao.checkUserEditPermissionsForProject(dbConnection, userSettings.getRecentProject(), user));
        modelAndView.addObject("project", userSettings.getRecentProject());
        modelAndView.addObject("submitButtonText", "Update Project");
        modelAndView.addObject("createProject", "false");
		modelAndView.addObject("projectUsers", userProjectDao.listAllUsersForProject(dbConnection, userSettings.getRecentProject().getId()));
		modelAndView.addObject("taskStatuses", taskStatuses);

		return modelAndView;
	}

    @RequestMapping(value = "/settings", method = RequestMethod.POST)
    public ModelAndView saveProjectSettings(@ModelAttribute("loggedUser") User user, @ModelAttribute("userSettings") UserSettings userSettings,
											@RequestParam("projectName") String name, @RequestParam("projectDescription") String description,
											@RequestParam("sprint-length") String sprintLengthString, @RequestParam("tsNameKey[]") Integer[] taskStatusIds,
											@RequestParam("tsName[]") String[] taskNames, @RequestParam("tsStaysInSprint[]") Integer[] tasksStaysInSprint) {
        Connection dbConnection = DatabaseSettings.getDatabaseConnection();

		List<Integer> taskStatusesThatStaysInSprint = Arrays.asList(tasksStaysInSprint);
		List<TaskStatus> taskStatuses = new ArrayList<TaskStatus>();
		for(int i = 0; i < taskStatusIds.length; i++) {
			TaskStatus taskStatus = new TaskStatus();
			taskStatus.setId(taskStatusIds[i]);
			taskStatus.setName(taskNames[i]);
			taskStatus.setProject(userSettings.getRecentProject());
			taskStatus.setStaysInSprint(taskStatusesThatStaysInSprint.contains(taskStatusIds[i]));
			taskStatuses.add(taskStatus);
		}

        Project project = userSettings.getRecentProject();
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
        project.setDescription(description.replace(WINDOWS_ENDLINE_STRING, UNIX_ENDLINE_STRING).replace(PLAIN_TEXT_ENDLINE_STRING, HTML_ENDLINE_STRING));
        project.setDefaultSprintLength(sprintLength);

        projectDao.updateProject(dbConnection, project);
		taskStatusDao.updateTaskStatuses(dbConnection, taskStatuses);

        return new ModelAndView("redirect:/settings");
    }

	@RequestMapping(value = "/settings/{projectId}/", method = RequestMethod.GET)
	public ModelAndView addUserProject(@ModelAttribute("loggedUser") User user, @ModelAttribute("userSettings") UserSettings userSettings, @PathVariable int projectId) {
		Connection dbConnection = DatabaseSettings.getDatabaseConnection();
		if (!projectDao.checkUserEditPermissionsForProject(dbConnection, projectId, user)) {
			return new ModelAndView("redirect:/");
		}

		List<UserRolesInProject> allRoles = userRolesInProjectDao.listAllUserRolesInProject(dbConnection);

		ModelAndView modelAndView = injector.getIndexForSiteName(Views.USERS_PROJECTS_FORM_JSP_LOCATION, "Add Project User", userSettings.getRecentProject(), user, request);

		modelAndView.addObject("availableUserProjectRoles", allRoles);
		modelAndView.addObject("userHasAllRoles", false);
		modelAndView.addObject("availableProjectUsers", userProjectDao.availableUsersForProject(dbConnection, projectId));
		return modelAndView;
	}

	@RequestMapping(value = "/settings/{projectId}/", method = RequestMethod.POST)
	public ModelAndView addUserProject(@ModelAttribute("loggedUser") User user, @ModelAttribute("userSettings") UserSettings userSettings, @PathVariable int projectId, @RequestParam String projectUserName, @RequestParam String userRoleInProject) {
		Connection dbConnection = DatabaseSettings.getDatabaseConnection();
		if (!projectDao.checkUserEditPermissionsForProject(dbConnection, projectId, user)) {
			return new ModelAndView("redirect:/");
		}

		User projectUser = userDao.fetchByName(dbConnection, projectUserName);
		UserRolesInProject role = userRolesInProjectDao.fetchByName(dbConnection, userRoleInProject);

		List<String> errors = new ArrayList<>();
		if (projectUser == null) {
			errors.add(ADDING_USER_PROJECT_NO_SUCH_USER);
		} else if (userProjectDao.checkIfUserIsAlreadyInProject(dbConnection, projectUser, projectId)) {
			errors.add(ADDING_USER_PROJECT_USER_ALREADY_IN_PROJECT);
		}
		if (!errors.isEmpty()) {
			ModelAndView modelAndView = addUserProject(user, userSettings, projectId);
			modelAndView.addObject("lastSelectedRole", role.getRole());
			modelAndView.addObject("lastUserName", projectUserName);
			modelAndView.addObject("UserProjectErrors", errors);
			return modelAndView;
		}
		userProjectDao.add(dbConnection, projectUser.getId(), projectId, role.getId());
		return new ModelAndView(MessageFormat.format("redirect:/settings/{0}/{1}/", projectId, projectUser.getId()));
	}

	@RequestMapping(value = "/settings/{projectId}/{userId}", method = RequestMethod.GET)
	public ModelAndView showUserProject(@ModelAttribute("loggedUser") User user, @ModelAttribute("userSettings") UserSettings userSettings, @PathVariable int userId, @PathVariable int projectId) {
		Connection dbConnection = DatabaseSettings.getDatabaseConnection();
		if (!projectDao.checkUserEditPermissionsForProject(dbConnection, projectId, user)) {
			return new ModelAndView("redirect:/");
		}

		Optional<UserProjectContainer> userProjectContainer = this.userProjectDao.fetchContainerById(dbConnection, projectId, userId);
		if (!userProjectContainer.isPresent()) {
			return new ModelAndView(MessageFormat.format("redirect:/settings/{0}/", projectId));
		}
		List<UserRolesInProject> userRoles = userProjectContainer.get().getRoles();
		List<UserRolesInProject> allRoles = userRolesInProjectDao.listAllUserRolesInProject(dbConnection);

		ModelAndView modelAndView = injector.getIndexForSiteName(Views.USERS_PROJECTS_FORM_JSP_LOCATION, "Project User", userSettings.getRecentProject(), user, request);

		modelAndView.addObject("ProjectUser", userProjectContainer.get());
		modelAndView.addObject("availableUserProjectRoles", getUserAvailableRolesForProject(userRoles, allRoles));
		modelAndView.addObject("userHasAllRoles", allRoles.size() == userRoles.size());
		return modelAndView;
	}

	@RequestMapping(value = "/settings/{projectId}/{userId}", method = RequestMethod.POST)
	public ModelAndView showUserProject(@ModelAttribute("loggedUser") User user, @ModelAttribute("userSettings") UserSettings userSettings, @PathVariable int userId, @PathVariable int projectId, @RequestParam String userRoleInProject) {
		Connection dbConnection = DatabaseSettings.getDatabaseConnection();
		if (!projectDao.checkUserEditPermissionsForProject(dbConnection, projectId, user)) {
			return new ModelAndView("redirect:/");
		}


		UserRolesInProject newRole = userRolesInProjectDao.fetchByName(dbConnection, userRoleInProject);
		userProjectDao.add(dbConnection, userId, projectId, newRole.getId());
		return new ModelAndView(MessageFormat.format("redirect:/settings/{0}/{1}/", projectId, userId));
	}

	@RequestMapping(value = "/settings/{projectId}/{userId}/removeUserFromProject/")
	public ModelAndView removeUserFromProject(@ModelAttribute("loggedUser") User user, @ModelAttribute("userSettings") UserSettings userSettings, @PathVariable int userId, @PathVariable int projectId) {
		Connection dbConnection = DatabaseSettings.getDatabaseConnection();
		if (!projectDao.checkUserEditPermissionsForProject(dbConnection, projectId, user)) {
			return new ModelAndView("redirect:/");
		}
		userProjectDao.deleteUserFromProject(dbConnection, userId, projectId);
		return new ModelAndView("redirect:/settings/");
	}

	@RequestMapping(value = "/settings/{projectId}/{userId}/removeUserFromProject/{roleId}/")
	public ModelAndView removeUserFromProject(@ModelAttribute("loggedUser") User user, @ModelAttribute("userSettings") UserSettings userSettings, @PathVariable int userId, @PathVariable int projectId, @PathVariable int roleId) {
		Connection dbConnection = DatabaseSettings.getDatabaseConnection();
		if (!projectDao.checkUserEditPermissionsForProject(dbConnection, projectId, user)) {
			return new ModelAndView("redirect:/");
		}
		userProjectDao.deleteUserFromProject(dbConnection, userId, projectId, roleId);
		return new ModelAndView(MessageFormat.format("redirect:/settings/{0}/{1}/", projectId, userId));
	}

	private List<UserRolesInProject> getUserAvailableRolesForProject(List<UserRolesInProject> userProjectContainer, List<UserRolesInProject> allRoles) {
		List<Integer> userRolesIds = userProjectContainer.stream().map(UserRolesInProject::getId).collect(Collectors.toList());
		return allRoles.stream().filter(x -> !userRolesIds.contains(x.getId())).collect(Collectors.toList());
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
		taskStatusDao.removeForProject(dbConnection, project);
		projectDao.removeProject(dbConnection, projectId);

		userSettings.setRecentProject(null);
		userDao.saveUserSettings(dbConnection, userSettings);

		return new ModelAndView("redirect:/");
	}
}