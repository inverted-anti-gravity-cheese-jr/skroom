package pl.pg.eti.kio.skroom.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.ModelAndView;
import pl.pg.eti.kio.skroom.model.TaskStatus;
import pl.pg.eti.kio.skroom.model.User;
import pl.pg.eti.kio.skroom.model.UserSettings;
import pl.pg.eti.kio.skroom.model.dao.TaskStatusDao;
import pl.pg.eti.kio.skroom.model.dao.UserDao;
import pl.pg.eti.kio.skroom.settings.DatabaseSettings;

import java.sql.Connection;
import java.util.List;

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
		return modelAndView;
	}
}
