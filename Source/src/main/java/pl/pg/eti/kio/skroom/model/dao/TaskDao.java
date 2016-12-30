package pl.pg.eti.kio.skroom.model.dao;

import static pl.pg.eti.kio.skroom.model.dba.Tables.TASKS;
import static pl.pg.eti.kio.skroom.model.dba.Tables.TASK_STATUSES;
import static pl.pg.eti.kio.skroom.model.dba.Tables.USERS;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import org.jooq.DSLContext;
import org.jooq.Record1;
import org.jooq.Result;
import org.jooq.SelectConditionStep;
import org.jooq.impl.DSL;
import org.springframework.stereotype.Service;

import pl.pg.eti.kio.skroom.exception.NoSuchTaskStatusException;
import pl.pg.eti.kio.skroom.exception.NoSuchUserRoleException;
import pl.pg.eti.kio.skroom.model.Project;
import pl.pg.eti.kio.skroom.model.Sprint;
import pl.pg.eti.kio.skroom.model.Task;
import pl.pg.eti.kio.skroom.model.TaskStatus;
import pl.pg.eti.kio.skroom.model.User;
import pl.pg.eti.kio.skroom.model.UserStory;
import pl.pg.eti.kio.skroom.model.dba.tables.records.TasksRecord;
import pl.pg.eti.kio.skroom.model.dba.tables.records.UsersRecord;
import pl.pg.eti.kio.skroom.settings.DatabaseSettings;

/**
 * Class to access model's Task from database.
 *
 * @author Wojciech Stanisławski
 * @since 17.08.16
 */
@Service
public class TaskDao {

	private static final int NOT_STAYS_IN_SPRINT = 0;

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
	
	public boolean updateTask(Connection connection, Task task) {
		DSLContext query = DSL.using(connection, DatabaseSettings.getCurrentSqlDialect());

		int updatedRows = query.update(TASKS)
				.set(TASKS.NAME, task.getName())
				.set(TASKS.DESCRIPTION, task.getDescription())
				.set(TASKS.ASSIGNEE, task.getAssignee() == null ? null : task.getAssignee().getId())
				.set(TASKS.COLOR, task.getColor())
				.set(TASKS.ESTIMATED_TIME, task.getEstimatedTime())
				.set(TASKS.SPRINT_ID, task.getSprint().getId())
				.set(TASKS.USER_STORY_ID, task.getUserStory() == null ? null : task.getUserStory().getId())
				.set(TASKS.TASK_STATUS_ID, task.getStatus().getId())
				.where(TASKS.ID.eq(task.getId())).execute();
		
		return updatedRows == 1;
	}

	/**
	 * Method used to fetch list of tasks
	 *
	 * @param connection Connection to a database
	 * @return All tasks from the database
	 */
	public List<Task> fetchTasks(Connection connection, Project project, List<TaskStatus> taskStatuses, List<UserStory> userStories, List<Sprint> sprints) {
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
				User user = null;
				if(taskRecord.getAssignee() != null) {
					user = records.usersRecordsList.stream().findAny().filter(x -> x.getId() == taskRecord.getAssignee()).get();
				}
				Task task = Task.fromDba(taskRecord, user, project, taskStatuses, userStories, sprints);
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
	
	public boolean moveTasksToCurrentSprint(Connection connection, Sprint sprint) {
		DSLContext query = DSL.using(connection, DatabaseSettings.getCurrentSqlDialect());
		
		SelectConditionStep<Record1<Integer>> selectStep = query.select(TASKS.ID).from(TASKS, TASK_STATUSES)
			.where(TASKS.TASK_STATUS_ID.eq(TASK_STATUSES.ID))
			.and(TASKS.PROJECT_ID.eq(sprint.getProject().getId()))
			.and(TASK_STATUSES.STAYS_IN_SPRINT.eq(NOT_STAYS_IN_SPRINT));
		
		int movedTasks = query.update(TASKS)
			.set(TASKS.SPRINT_ID, sprint.getId())
			.where(TASKS.ID.in(selectStep)).execute();
		
		return movedTasks >= 0;
	}
	
	public boolean changeTaskAssignee(Connection connection, int id, User user) {
		DSLContext query = DSL.using(connection, DatabaseSettings.getCurrentSqlDialect());
		
		int changedRows = query.update(TASKS).set(TASKS.ASSIGNEE, user.getId()).where(TASKS.ID.eq(id)).execute();
		
		return changedRows == 1;
	}
	
	public Task fetchTaskById(Connection connection, int id, List<User> users, Project project, List<TaskStatus> taskStatusesList, List<UserStory> userStories, List<Sprint> sprints) throws NoSuchTaskStatusException {
		DSLContext query = DSL.using(connection, DatabaseSettings.getCurrentSqlDialect());
		
		TasksRecord tasksRecord = query.selectFrom(TASKS).where(TASKS.ID.eq(id)).fetchOne();
		
		User user = null;
		if(tasksRecord.getAssignee() != null) {
			user = users.stream().filter(u -> u.getId() == tasksRecord.getAssignee()).findAny().get();
		}
		
		return Task.fromDba(tasksRecord, user, project, taskStatusesList, userStories, sprints);
	}
	
	public Task fetchTaskByIdForCurrentUser(Connection connection, int id, User user, Project project, List<TaskStatus> taskStatusesList, List<UserStory> userStories, List<Sprint> sprints) throws NoSuchTaskStatusException {
		DSLContext query = DSL.using(connection, DatabaseSettings.getCurrentSqlDialect());
		
		TasksRecord tasksRecord = query.selectFrom(TASKS).where(TASKS.ID.eq(id)).fetchOne();
		
		return Task.fromDba(tasksRecord, user, project, taskStatusesList, userStories, sprints);
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
				task.getProject().getId(), task.getUserStory().getId(), task.getSprint().getId()).execute();		
		
		return insertedRows == 1;
	}
	
	public boolean removeTaskById(Connection connection, int taskId) {
		DSLContext query = DSL.using(connection, DatabaseSettings.getCurrentSqlDialect());
		
		int deletedRows = query.deleteFrom(TASKS).where(TASKS.ID.eq(taskId)).execute();
		
		return deletedRows == 1;
	}
	
	/**
	 * Tuple containing tasks and users
	 */
	private class TasksAndUsers {
		public List<Task> tasksRecordList = new ArrayList<>();
		public List<User> usersRecordsList = new ArrayList<>();
	}
}
