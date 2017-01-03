package pl.pg.eti.kio.skroom.controller;

import java.sql.Connection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.SessionAttributes;

import pl.pg.eti.kio.skroom.model.UserSettings;
import pl.pg.eti.kio.skroom.model.dao.ProjectDao;
import pl.pg.eti.kio.skroom.model.dao.TaskDao;
import pl.pg.eti.kio.skroom.model.dao.TaskStatusDao;
import pl.pg.eti.kio.skroom.model.dao.UserDao;
import pl.pg.eti.kio.skroom.settings.DatabaseSettings;

/**
 * Main controller for rest API.
 *
 * @author Wojciech Stanisławski
 * @since 22.05.16
 */
@RestController
@RequestMapping("/rest")
@SessionAttributes({"userSettings"})
public class MainRestController {

	private static final String TASKS_PENDING_ERROR_STATUS = "TASKS";
	private static final String OK_STATUS = "OK";
	private static final String ERROR_STATUS = "ERROR";


	@Autowired private TaskDao taskDao;
	@Autowired private UserDao userDao;
	@Autowired private ProjectDao projectDao;
	@Autowired private TaskStatusDao taskStatusDao;

	@RequestMapping(value = "/task/update", method = RequestMethod.POST)
	public void reloadTask(@RequestParam("taskId") String taskId, @RequestParam("status") String status) {
		/*
		TaskStatus newStatusEnum = TaskStatus.NOT_STARTED;
		switch (status) {
		case "inProgress":
			newStatusEnum = TaskStatus.IN_PROGRESS;
			break;
		case "notStarted":
			newStatusEnum = TaskStatus.NOT_STARTED;
			break;
		case "completed":
			newStatusEnum = TaskStatus.COMPLETED;
			break;
		case "accepted":
			newStatusEnum = TaskStatus.ACCEPTED;
			break;
		case "awaitingReview":
			newStatusEnum = TaskStatus.AWAITING_REVIEW;
			break;
		case "blocked":
			newStatusEnum = TaskStatus.BLOCKED;
			break;
		}


		final int taskIdInt = Integer.parseInt(taskId);

		Connection dbConnection = DatabaseSettings.getDatabaseConnection();
		List<Task> tasks = taskDao.fetchTasks(dbConnection);

		Optional<Task> task = tasks.stream().filter(x -> x.getId() == taskIdInt).findFirst();
		if (task.isPresent()) {
			task.get().setStatus(newStatusEnum);
			taskDao.updateTaskStatus(dbConnection, task.get());
		}
		*/
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

	@RequestMapping(value="userSettings/selectProject", method = RequestMethod.POST)
	public void pickProject(@RequestParam Integer projectId, @ModelAttribute("userSettings") UserSettings userSettings) {
		Connection connection = DatabaseSettings.getDatabaseConnection();
		System.out.println("------------------------------------------------------------------------------------------");
		System.out.println(projectId);
		try {
			userSettings.setRecentProject(projectDao.fetchProjectById(connection, projectId));

			userDao.saveUserSettings(connection, userSettings);
			System.out.println("selected project " + userSettings.getRecentProject());
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
}
