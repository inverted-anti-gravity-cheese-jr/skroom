package pl.pg.eti.kio.skroom.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;
import pl.pg.eti.kio.skroom.model.Task;
import pl.pg.eti.kio.skroom.model.User;
import pl.pg.eti.kio.skroom.model.dao.TaskDao;
import pl.pg.eti.kio.skroom.model.dao.UserDao;
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
@SessionAttributes({"loggedUser"})
public class MainController {

	@Autowired private TaskDao taskDao;
	@Autowired private UserDao userDao;

	@PostConstruct
	public void initDatabase() {
		DatabaseSettings.initConnection("jdbc:sqlite:data.db");
	}

	@ModelAttribute("loggedUser")
	public User defaultNullUser() {
		return new User();
	}

	@RequestMapping(value = { "/", "/dashboard" }, method = RequestMethod.GET)
	public ModelAndView showDashboard(@ModelAttribute("loggedUser") User user) {
		if (isUserNull(user)) {
			return getLoginModel();
		}

		Connection dbConnection = DatabaseSettings.getDatabaseConnection();

		List<Task> taskList = taskDao.fetchTasks(dbConnection);

		ModelAndView model = new ModelAndView(INDEX_JSP_LOCATION);
		model.addObject("siteName", "dashboard");
		model.addObject("list", taskList);

		return model;
	}

	@RequestMapping(value = "/productbacklog", method = RequestMethod.GET)
	public ModelAndView showProductBacklog(@ModelAttribute("loggedUser") User user) {
		if (isUserNull(user)) {
			return getLoginModel();
		}

		ModelAndView model = new ModelAndView(INDEX_JSP_LOCATION);
		model.addObject("siteName", "productBacklog");

		return model;
	}

	@RequestMapping(value = "/sprintbacklog", method = RequestMethod.GET)
	public ModelAndView showSprintBacklog(@ModelAttribute("loggedUser") User user) {
		if (isUserNull(user)) {
			return getLoginModel();
		}

		ModelAndView model = new ModelAndView(INDEX_JSP_LOCATION);
		model.addObject("siteName", "sprintBacklog");

		return model;
	}

	@RequestMapping(value = "/kanban", method = RequestMethod.GET)
	public ModelAndView showKanbanBoard(@ModelAttribute("loggedUser") User user) {
		if (isUserNull(user)) {
			return getLoginModel();
		}
		Connection dbConnection = DatabaseSettings.getDatabaseConnection();
		List<Task> taskList = taskDao.fetchTasks(dbConnection);

		ModelAndView model = new ModelAndView(INDEX_JSP_LOCATION);
		model.addObject("siteName", "kanban");
		model.addObject("list", taskList);

		return model;
	}

	@RequestMapping(value = "/issues", method = RequestMethod.GET)
	public ModelAndView showIssues(@ModelAttribute("loggedUser") User user) {
		if (isUserNull(user)) {
			return getLoginModel();
		}

		ModelAndView model = new ModelAndView(INDEX_JSP_LOCATION);
		model.addObject("siteName", "issues");

		return model;
	}

	@RequestMapping(value = "/settings", method = RequestMethod.GET)
	public ModelAndView showProjectSettings(@ModelAttribute("loggedUser") User user) {
		if (isUserNull(user)) {
			return getLoginModel();
		}

		ModelAndView model = new ModelAndView(INDEX_JSP_LOCATION);
		model.addObject("siteName", "settings");

		return model;
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
}
