package pl.pg.eti.kio.skroom.model.dao;

import org.jooq.DSLContext;
import org.jooq.Result;
import org.jooq.impl.DSL;
import org.springframework.stereotype.Service;
import pl.pg.eti.kio.skroom.exception.NoSuchTaskStatusException;
import pl.pg.eti.kio.skroom.exception.NoSuchUserRoleException;
import pl.pg.eti.kio.skroom.model.Task;
import pl.pg.eti.kio.skroom.model.User;
import pl.pg.eti.kio.skroom.model.dba.Tables;
import pl.pg.eti.kio.skroom.model.dba.tables.records.TasksRecord;
import pl.pg.eti.kio.skroom.model.dba.tables.records.UsersRecord;
import pl.pg.eti.kio.skroom.settings.DatabaseSettings;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

/**
 * Class to access model's Task from database.
 *
 * @author Wojciech Stanisławski
 * @since 17.08.16
 */
@Service
public class TaskDao {

	/**
	 * Method for updating task status in the database
	 *
	 * @param connection Connection to a database
	 * @param task Task to update
	 */
	public void updateTaskStatus(Connection connection, Task task) {
		DSLContext query = DSL.using(connection, DatabaseSettings.getCurrentSqlDialect());

		query.update(Tables.TASKS).set(Tables.TASKS.TASK_STATUS, task.getStatus().getCode())
				.where(Tables.TASKS.ID.eq(task.getId())).execute();
	}

	/**
	 * Method used to fetch list of tasks
	 *
	 * @param connection Connection to a database
	 * @return All tasks from the database
	 */
	public List<Task> fetchTasks(Connection connection) {
		return fetchTasksAndUsers(connection).tasksRecordList;
	}

	/**
	 * Method used to fetch list of tasks and list of users who are assigned to at least one task.
	 *
	 * @param connection Connection to a database
	 * @return Tuple containing lists of tasks and users
	 */
	public TasksAndUsers fetchTasksAndUsers(Connection connection) {
		DSLContext query = DSL.using(connection, DatabaseSettings.getCurrentSqlDialect());

		// fetch all task records
		Result<TasksRecord> tasksRecords = query.selectFrom(Tables.TASKS).fetch();

		// fetch all those users which are assigned to any task
		Result<UsersRecord> usersRecords = query.selectFrom(Tables.USERS).where(
				Tables.USERS.ID.in(tasksRecords.getValues(Tables.TASKS.ASSIGNEE))).fetch();

		TasksAndUsers records = new TasksAndUsers();

		try {
			// convert all users to model used by the system
			for(UsersRecord userRecord : usersRecords) {
				User user = User.fromDba(userRecord);
				records.usersRecordsList.add(user);
			}

			// convert all tasks to model used by the system
			for (TasksRecord taskRecord : tasksRecords) {
				// find user in the list with supplied ID
				User user = records.usersRecordsList.stream().findAny().filter(x -> x.getId() == taskRecord.getAssignee()).get();
				Task task = Task.fromDba(taskRecord, user);
				records.tasksRecordList.add(task);
			}
		}
		catch(NoSuchTaskStatusException nstse) {
			nstse.printStackTrace();
		} catch (NoSuchUserRoleException nsure) {
			nsure.printStackTrace();
		}

		return records;
	}

	/**
	 * Tuple containing tasks and users
	 */
	public class TasksAndUsers {
		public List<Task> tasksRecordList = new ArrayList<>();
		public List<User> usersRecordsList = new ArrayList<>();
	}
}
