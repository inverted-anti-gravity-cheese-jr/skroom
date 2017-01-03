package pl.pg.eti.kio.skroom.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.ModelAndView;
import pl.pg.eti.kio.skroom.SprintGeneratorService;
import pl.pg.eti.kio.skroom.exception.NoSuchTaskStatusException;
import pl.pg.eti.kio.skroom.model.*;
import pl.pg.eti.kio.skroom.model.dao.*;
import pl.pg.eti.kio.skroom.settings.DatabaseSettings;

import java.sql.Connection;
import java.util.List;

import static pl.pg.eti.kio.skroom.PlainTextUtil.*;
import static pl.pg.eti.kio.skroom.controller.Views.TASK_FORM_JSP_LOCATION;

/**
 * Main controller for managing tasks in project.
 *
 * @author Wojciech Stanisławski
 * @since 10.12.16
 */
@Controller
@SessionAttributes({"loggedUser", "userSettings"})
public class TaskManagementController {

	@Autowired private UserDao userDao;
	@Autowired private TaskDao taskDao;
	@Autowired private SprintDao sprintDao;
	@Autowired private UserStoryDao userStoryDao;
	@Autowired private TaskStatusDao taskStatusDao;
	@Autowired private SprintGeneratorService generatorService;
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

	@RequestMapping(value = "addTask", method = RequestMethod.GET)
	public ModelAndView addTaskForm(@ModelAttribute("loggedUser") User user, @ModelAttribute("userSettings") UserSettings userSettings) {
		ModelAndView modelAndView = injector.getIndexForSiteName(TASK_FORM_JSP_LOCATION, "addTask", userSettings.getRecentProject(), user, request);

		Connection dbConnection = DatabaseSettings.getDatabaseConnection();
		List<TaskStatus> taskStatuses = taskStatusDao.fetchByProject(dbConnection, userSettings.getRecentProject());
		List<User> users = userDao.listAllUsersForProject(dbConnection, userSettings.getRecentProject());
		List<UserStory> userStories = userStoryDao.fetchUserStoriesForProject(dbConnection, userSettings.getRecentProject());

		modelAndView.addObject("taskStatuses", taskStatuses);
		modelAndView.addObject("userStories", userStories);
		modelAndView.addObject("users", users);
		modelAndView.addObject("taskColor", "#008CFF");
		return modelAndView;
	}
	
	
	@RequestMapping(value = "assignTaskToMe/{taskId}")
	public ModelAndView addTask(@ModelAttribute("loggedUser") User user, @PathVariable Integer taskId) {
		Connection dbConnection = DatabaseSettings.getDatabaseConnection();
		
		if(taskId != null) {
			taskDao.changeTaskAssignee(dbConnection, taskId, user);
		}
		
		return new ModelAndView("redirect:/sprintbacklog");
	}

	@RequestMapping("deleteTaskStatus/{taskStatusId}")
	public ModelAndView deleteTaskStatus(@PathVariable Integer taskStatusId) {
		Connection dbConnection = DatabaseSettings.getDatabaseConnection();

		taskStatusDao.removeById(dbConnection, taskStatusId);

		return new ModelAndView("redirect:/settings");
	}
	
	@RequestMapping("viewTask/{taskId}")
	public ModelAndView viewTask(@ModelAttribute("loggedUser") User user, @ModelAttribute("userSettings") UserSettings userSettings, @PathVariable Integer taskId) {
		ModelAndView modelAndView = injector.getIndexForSiteName(TASK_FORM_JSP_LOCATION, "viewTask", userSettings.getRecentProject(), user, request);
		Connection dbConnection = DatabaseSettings.getDatabaseConnection();
		
		List<TaskStatus> taskStatuses = taskStatusDao.fetchByProject(dbConnection, userSettings.getRecentProject());
		List<UserStory> userStories = userStoryDao.fetchUserStoriesForProject(dbConnection, userSettings.getRecentProject());
		List<User> users = userDao.listAllUsersForProject(dbConnection, userSettings.getRecentProject());
		List<Sprint> sprints = sprintDao.fetchSprintsForProject(dbConnection, userSettings.getRecentProject());
		
		try {
			Task task = taskDao.fetchTaskById(dbConnection, taskId, users, userSettings.getRecentProject(), taskStatuses, userStories, sprints);
			modelAndView.addObject("task", task);
			modelAndView.addObject("taskStatuses", taskStatuses);
			modelAndView.addObject("userStories", userStories);
			modelAndView.addObject("users", users);
			modelAndView.addObject("taskColor", task.getColor());
		} catch (NoSuchTaskStatusException e) {
			e.printStackTrace();
		}
		
		return modelAndView;
	}
	
	@RequestMapping(value = "removeTask/{taskId}")
	public ModelAndView removeTask(@PathVariable Integer taskId) {
		Connection dbConnection = DatabaseSettings.getDatabaseConnection();
		
		taskDao.removeTaskById(dbConnection, taskId);
		
		return new ModelAndView("redirect:/sprintbacklog");
	}
	
	@RequestMapping(value = "editTask/{taskId}", method = RequestMethod.POST)
	public ModelAndView editTask(@ModelAttribute("userSettings") UserSettings userSettings, @PathVariable Integer taskId,
			@RequestParam String taskName, @RequestParam String taskDescription, @RequestParam String taskStatus, @RequestParam Integer userStoryId,
			@RequestParam String taskAssignee, @RequestParam String taskColor, @RequestParam String taskEstimated) {
		
		Connection dbConnection = DatabaseSettings.getDatabaseConnection();
		return addOrEditTask(userSettings, taskId, taskName, taskDescription, taskStatus, userStoryId, taskAssignee, taskColor,
				taskEstimated, dbConnection);
	}
	
	@RequestMapping(value = "addTask", method = RequestMethod.POST)
	public ModelAndView addTask(@ModelAttribute("userSettings") UserSettings userSettings,
			@RequestParam String taskName, @RequestParam String taskDescription, @RequestParam String taskStatus, @RequestParam Integer userStoryId,
			@RequestParam String taskAssignee, @RequestParam String taskColor, @RequestParam String taskEstimated) {
		
		Connection dbConnection = DatabaseSettings.getDatabaseConnection();
		
		return addOrEditTask(userSettings, null, taskName, taskDescription, taskStatus, userStoryId, taskAssignee, taskColor,
				taskEstimated, dbConnection);
	}

	private ModelAndView addOrEditTask(UserSettings userSettings, Integer taskId, String taskName, String taskDescription, String taskStatus,
			Integer userStoryId, String taskAssignee, String taskColor, String taskEstimated, Connection dbConnection) {
		int estimatedTime = 0;
		try {
			estimatedTime = Integer.parseInt(taskEstimated);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		
		Task task = new Task();
		task.setName(taskName);
		task.setColor(taskColor);
		task.setDescription(taskDescription.replace(WINDOWS_ENDLINE_STRING, UNIX_ENDLINE_STRING).replace(PLAIN_TEXT_ENDLINE_STRING, HTML_ENDLINE_STRING));
		task.setEstimatedTime(estimatedTime);
		task.setProject(userSettings.getRecentProject());
		if(taskAssignee != null && !taskAssignee.trim().isEmpty()) {
			try {
				String userEmail = taskAssignee.substring(taskAssignee.indexOf('[') + 1);
				userEmail = userEmail.substring(0, userEmail.length() - 1);
				task.setAssignee(userDao.fetchByEmail(dbConnection, userEmail));
			}
			catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		if(userStoryId != null) {
			task.setUserStory(userStoryDao.fetchUserStoryById(dbConnection, userStoryId));
		}
		task.setStatus(taskStatusDao.fetchByName(dbConnection, taskStatus, userSettings.getRecentProject()));
		Sprint sprint = sprintDao.findCurrentSprint(dbConnection, userSettings.getRecentProject());
		if(sprint == null) {
			generatorService.generateAndUploadMissingSprint(dbConnection, userSettings.getRecentProject());
			sprint = sprintDao.findCurrentSprint(dbConnection, userSettings.getRecentProject());
		}
		task.setSprint(sprint);
		
		if(taskId == null) {
			taskDao.createTask(dbConnection, task);
			return new ModelAndView("redirect:/sprintbacklog");
		}
		else {
			task.setId(taskId);
			taskDao.updateTask(dbConnection, task);
			return new ModelAndView("redirect:/viewTask/" + taskId.toString());
		}
	}
	
}
