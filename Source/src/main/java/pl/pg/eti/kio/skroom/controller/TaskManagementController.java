package pl.pg.eti.kio.skroom.controller;

import static pl.pg.eti.kio.skroom.controller.Views.TASK_FORM_JSP_LOCATION;

import java.sql.Connection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.ModelAndView;

import pl.pg.eti.kio.skroom.model.Task;
import pl.pg.eti.kio.skroom.model.TaskStatus;
import pl.pg.eti.kio.skroom.model.User;
import pl.pg.eti.kio.skroom.model.UserSettings;
import pl.pg.eti.kio.skroom.model.dao.TaskDao;
import pl.pg.eti.kio.skroom.model.dao.TaskStatusDao;
import pl.pg.eti.kio.skroom.model.dao.UserDao;
import pl.pg.eti.kio.skroom.settings.DatabaseSettings;

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
	@Autowired private TaskStatusDao taskStatusDao;
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


		modelAndView.addObject("taskStatuses", taskStatuses);
		modelAndView.addObject("users", users);
		modelAndView.addObject("taskColor", "#008CFF");
		return modelAndView;
	}
	
	@RequestMapping(value = "addTask", method = RequestMethod.POST)
	public ModelAndView addTask(@ModelAttribute("loggedUser") User user, @ModelAttribute("userSettings") UserSettings userSettings,
			@RequestParam String taskName, @RequestParam String taskDescription, @RequestParam String taskStatus,
			@RequestParam String taskAssignee, @RequestParam String taskColor, @RequestParam String taskEstimated) {
		
		Connection dbConnection = DatabaseSettings.getDatabaseConnection();
		
		int estimatedTime = 0;
		try {
			estimatedTime = Integer.parseInt(taskEstimated);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		
		String userEmail = taskAssignee.substring(taskAssignee.indexOf('[') + 1);
		userEmail = userEmail.substring(0, userEmail.length() - 1);
		System.out.println(userEmail);
				
		Task task = new Task();
		task.setName(taskName);
		task.setColor(taskColor);
		task.setDescription(taskDescription);
		task.setEstimatedTime(estimatedTime);
		task.setProject(userSettings.getRecentProject());
		task.setAssignee(userDao.fetchByEmail(dbConnection, userEmail));
		task.setStatus(taskStatusDao.fetchByName(dbConnection, taskStatus, userSettings.getRecentProject()));
		
		taskDao.createTask(dbConnection, task);
			
		return new ModelAndView("redirect:/sprintbacklog");
	}
}
