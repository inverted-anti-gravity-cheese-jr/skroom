package pl.pg.eti.kio.skroom.controller;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.jooq.util.derby.sys.Sys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.SessionAttributes;

import org.springframework.web.servlet.ModelAndView;
import pl.pg.eti.kio.skroom.model.*;
import pl.pg.eti.kio.skroom.model.dao.*;
import pl.pg.eti.kio.skroom.model.enumeration.UserRole;
import pl.pg.eti.kio.skroom.settings.DatabaseSettings;

import static pl.pg.eti.kio.skroom.controller.Views.TASK_FULL_TABLE_BODY_JSP_LOCATION;

/**
 * Main controller for rest API.
 *
 * @author Wojciech Stanisławski
 * @since 22.05.16
 */
@RestController
@RequestMapping("/rest")
@SessionAttributes({"userSettings", "loggedUser"})
public class MainRestController {

	private static final String TASKS_PENDING_ERROR_STATUS = "TASKS";
	private static final String OK_STATUS = "OK";
	private static final String ERROR_STATUS = "ERROR";


	@Autowired private TaskDao taskDao;
	@Autowired private UserDao userDao;
	@Autowired private ProjectDao projectDao;
	@Autowired private TaskStatusDao taskStatusDao;
	@Autowired private SprintDao sprintDao;
	@Autowired private UserStoryDao userStoryDao;

	@RequestMapping(value = "/task/update", method = RequestMethod.POST)
	public void reloadTask(@RequestParam("taskId") Integer taskId, @RequestParam("statusId") Integer statusId) {
		Connection dbConnection = DatabaseSettings.getDatabaseConnection();
		taskDao.updateTaskStatus(dbConnection, taskId, statusId);
	}

	@RequestMapping(value = "taskStatus/removeTaskStatus", method = RequestMethod.POST)
	public String removeTaskStatus(@RequestParam Integer taskStatusId) {
		Connection dbConnection = DatabaseSettings.getDatabaseConnection();

		if(taskDao.countTasksForStatus(dbConnection, taskStatusId) > 0) {
			return TASKS_PENDING_ERROR_STATUS;
		}

		if(taskStatusDao.removeById(dbConnection, taskStatusId)) {
			return OK_STATUS;
		}
		return ERROR_STATUS;
	}

	@RequestMapping(value="sprintBacklog/taskQuery", method = RequestMethod.POST)
	public ModelAndView taskQuery(@RequestParam String query, @RequestParam Integer sprintId, @ModelAttribute("userSettings") UserSettings userSettings) {
		Connection dbConnection = DatabaseSettings.getDatabaseConnection();

		List<Sprint> sprints = sprintDao.fetchAvailableSprintsForProject(dbConnection, userSettings.getRecentProject());
		List<UserStory> userStories = userStoryDao.fetchUserStoriesForProject(dbConnection, userSettings.getRecentProject());
		List<TaskStatus> taskStatuses = taskStatusDao.fetchByProject(dbConnection,userSettings.getRecentProject());
		List<Task> taskList = taskDao.fetchTasks(dbConnection, userSettings.getRecentProject(), taskStatuses, userStories, sprints);
		taskList = taskList.stream().filter(t -> t.getSprint() != null && t.getSprint().getId() == sprintId).collect(Collectors.toList());

		ModelAndView mav = new ModelAndView(TASK_FULL_TABLE_BODY_JSP_LOCATION);

		if(query != null && !query.isEmpty()) {
			mav.addObject("tasks",
					taskList.stream()
							.filter(task -> task.getName() != null && task.getName().toLowerCase().contains(query.toLowerCase()))
							.collect(Collectors.toList()));
		}
		else {
			mav.addObject("tasks", taskList);
		}

		return mav;
	}

	@RequestMapping(value="sprintBacklog/tasksPerPage", method = RequestMethod.POST)
	public void changeTasksPerPage(@RequestParam String perPage, @ModelAttribute("userSettings") UserSettings userSettings) {
		Connection connection = DatabaseSettings.getDatabaseConnection();
		try {
			int perPageInt = Integer.parseInt(perPage);
			userSettings.setTasksPerPage(perPageInt);

			userDao.saveUserSettings(connection, userSettings);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

	@RequestMapping(value="userSettings/userStoriesPerPage", method = RequestMethod.POST)
	public void changeUserStoriesPerPage(@RequestParam String perPage, @ModelAttribute("userSettings") UserSettings userSettings) {
		Connection connection = DatabaseSettings.getDatabaseConnection();
		try {
			int perPageInt = Integer.parseInt(perPage);
			userSettings.setUserStoriesPerPage(perPageInt);

			userDao.saveUserSettings(connection, userSettings);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

	@RequestMapping(value="userSettings/usersPerPage", method = RequestMethod.POST)
	public void changeUsersPerPage(@RequestParam String perPage, @ModelAttribute("userSettings") UserSettings userSettings) {
		Connection connection = DatabaseSettings.getDatabaseConnection();
		try {
			int perPageInt = Integer.parseInt(perPage);
			userSettings.setUsersPerPage(perPageInt);

			userDao.saveUserSettings(connection, userSettings);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

	@RequestMapping(value="userSettings/selectProject", method = RequestMethod.POST)
	public void pickProject(@RequestParam Integer projectId, @ModelAttribute("userSettings") UserSettings userSettings) {
		Connection connection = DatabaseSettings.getDatabaseConnection();
		try {
			userSettings.setRecentProject(projectDao.fetchProjectById(connection, projectId));

			userDao.saveUserSettings(connection, userSettings);
			System.out.println("selected project " + userSettings.getRecentProject());
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

	@RequestMapping(value = "admin/deleteUserStoryStatus", method = RequestMethod.POST)
	public String deleteUserStoryStatus(@ModelAttribute("loggedUser") User user, @RequestParam("ussId") Integer id) {
		Connection dbConnection = DatabaseSettings.getDatabaseConnection();

		if(!UserRole.ADMIN.equals(user.getRole())) {
			return "NOADMIN";
		}
		if(userStoryDao.countUserStoriesWithStatus(dbConnection, id) > 0) {
			return "USER_STORIES_EXIST";
		}
		if(userStoryDao.removeUserStoryStatusWithId(dbConnection, id)) {
			return "OK";
		}

		return "ERROR";
	}

	@RequestMapping(value = "admin/changeUserStoryStatuses", method = RequestMethod.POST)
	public String changeUserStoryStatuses(@RequestParam("usIds[]") Integer[] ids, @RequestParam("usNames[]") String[] names, @RequestParam("usColors[]") String[] colors, @RequestParam(value = "isArchive[]", required = false) Integer[] archives) {
		List<UserStoryStatus> statuses = new ArrayList<>();
		List<Integer> archivedIds = archives != null ? Arrays.asList(archives) : Collections.EMPTY_LIST;
		for(int i = 0; i < ids.length; i++) {
			UserStoryStatus uss = new UserStoryStatus();
			uss.setId(ids[i]);
			uss.setName(names[i]);
			uss.setColor(colors[i]);
			uss.setArchive(archivedIds.contains(ids[i]));
			statuses.add(uss);
		}
		Connection dbConnection = DatabaseSettings.getDatabaseConnection();
		if(userStoryDao.updateUserStoryStatuses(dbConnection, statuses)) {
			return "OK";
		}
		return "ERROR";
	}
}
