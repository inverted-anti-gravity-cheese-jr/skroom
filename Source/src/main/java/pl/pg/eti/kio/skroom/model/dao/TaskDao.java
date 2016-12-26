package pl.pg.eti.kio.skroom.model.dao;

import org.jooq.DSLContext;
import org.jooq.Result;
import org.jooq.impl.DSL;
import org.springframework.stereotype.Service;
import pl.pg.eti.kio.skroom.exception.NoSuchTaskStatusException;
import pl.pg.eti.kio.skroom.exception.NoSuchUserRoleException;
import pl.pg.eti.kio.skroom.model.Project;
import pl.pg.eti.kio.skroom.model.Task;
import pl.pg.eti.kio.skroom.model.TaskStatus;
import pl.pg.eti.kio.skroom.model.User;
import pl.pg.eti.kio.skroom.model.dba.tables.records.TasksRecord;
import pl.pg.eti.kio.skroom.model.dba.tables.records.UsersRecord;
import pl.pg.eti.kio.skroom.settings.DatabaseSettings;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import static pl.pg.eti.kio.skroom.model.dba.Tables.TASKS;
import static pl.pg.eti.kio.skroom.model.dba.Tables.USERS;

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

		query.update(TASKS).set(TASKS.TASK_STATUS_ID, task.getStatus().getId())
				.where(TASKS.ID.eq(task.getId())).execute();
	}

	/**
	 * Method used to fetch list of tasks
	 *
	 * @param connection Connection to a database
	 * @return All tasks from the database
	 */
	public List<Task> fetchTasks(Connection connection, Project project, List<TaskStatus> taskStatuses) {
		DSLContext query = DSL.using(connection, DatabaseSettings.getCurrentSqlDialect());

		// fetch all task records
		Result<TasksRecord> tasksRecords = query.selectFrom(TASKS).fetch();

		// fetch all those users which are assigned to any task
		Result<UsersRecord> usersRecords = query.selectFrom(USERS).where(
				USERS.ID.in(tasksRecords.getValues(TASKS.ASSIGNEE))).fetch();

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
				Task task = Task.fromDba(taskRecord, user, project, taskStatuses);
				records.tasksRecordList.add(task);
			}
		}
		catch(NoSuchTaskStatusException nstse) {
			nstse.printStackTrace();
		} catch (NoSuchUserRoleException nsure) {
			nsure.printStackTrace();
		}

		return records.tasksRecordList;
	}

	/**
	 * Inserts a task into database.
	 * 
	 * @param connection	Connection to database.
	 * @param task			Task to insert into database.
	 * @return				Returns true if successfully inserted task.
	 */
	public boolean createTask(Connection connection, Task task) {
		DSLContext query = DSL.using(connection, DatabaseSettings.getCurrentSqlDialect());
		
		Integer assigneeId = (task.getAssignee() != null ? task.getAssignee().getId() : null);
		
		int insertedRows = query.insertInto(TASKS).values(null, task.getName(), task.getDescription(),
				task.getColor(), assigneeId, task.getStatus().getId(), task.getEstimatedTime(),
				task.getProject().getId()).execute();
		
		return insertedRows == 1;
	}
	
	/**
	 * Tuple containing tasks and users
	 */
	private class TasksAndUsers {
		public List<Task> tasksRecordList = new ArrayList<>();
		public List<User> usersRecordsList = new ArrayList<>();
	}
}
