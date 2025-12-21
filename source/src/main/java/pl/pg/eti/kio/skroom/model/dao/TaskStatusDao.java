package pl.pg.eti.kio.skroom.model.dao;

import static pl.pg.eti.kio.skroom.model.dba.Tables.TASK_STATUSES;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import org.jooq.DSLContext;
import org.jooq.Result;
import org.jooq.impl.DSL;
import org.springframework.stereotype.Service;

import pl.pg.eti.kio.skroom.model.Project;
import pl.pg.eti.kio.skroom.model.TaskStatus;
import pl.pg.eti.kio.skroom.model.dba.tables.records.TaskStatusesRecord;
import pl.pg.eti.kio.skroom.settings.DatabaseSettings;

/**
 * Class to access model's Task Status from database.
 *
 * @author Wojciech Stanisławski
 * @since 08.12.16
 */
@Service
public class TaskStatusDao {

	public TaskStatus fetchByName(Connection connection, String name, Project project) {
		DSLContext query = DSL.using(connection, DatabaseSettings.getCurrentSqlDialect());

		return TaskStatus.fromDba(query.selectFrom(TASK_STATUSES)
				.where(TASK_STATUSES.NAME.eq(name))
				.and(TASK_STATUSES.PROJECT_ID.eq(project.getId())).fetchAny(), project);
	}
	
	public List<TaskStatus> fetchByProject(Connection connection, Project project) {
		DSLContext query = DSL.using(connection, DatabaseSettings.getCurrentSqlDialect());

		ArrayList<TaskStatus> statuses = new ArrayList<TaskStatus>();
		if(project == null) {
			return statuses;
		}

		Result<TaskStatusesRecord> taskStatusesRecords = query.selectFrom(TASK_STATUSES).where(TASK_STATUSES.PROJECT_ID.eq(project.getId())).fetch();

		for(TaskStatusesRecord record : taskStatusesRecords) {
			statuses.add(TaskStatus.fromDba(record, project));
		}

		return statuses;
	}

	public boolean generateDefaultStatusesForProject(Connection connection, Project project) {
		boolean success = true;

		TaskStatus todo = new TaskStatus();
		todo.setName("To do");
		todo.setStaysInSprint(false);
		todo.setProject(project);
		success = createTaskStatus(connection, todo) && success;

		TaskStatus inProgress = new TaskStatus();
		inProgress.setName("In progress");
		inProgress.setStaysInSprint(false);
		inProgress.setProject(project);
		success = createTaskStatus(connection, inProgress) && success;

		TaskStatus done = new TaskStatus();
		done.setName("Done");
		done.setStaysInSprint(true);
		done.setProject(project);
		success = createTaskStatus(connection, done) && success;

		return success;
	}

	public boolean removeForProject(Connection connection, Project project) {
		DSLContext query = DSL.using(connection, DatabaseSettings.getCurrentSqlDialect());

		int deletedRows = query.deleteFrom(TASK_STATUSES).where(TASK_STATUSES.PROJECT_ID.eq(project.getId())).execute();

		return deletedRows > 0;
	}

	public boolean removeById(Connection connection, int tsId) {
		DSLContext query = DSL.using(connection, DatabaseSettings.getCurrentSqlDialect());

		int deletedRows = query.deleteFrom(TASK_STATUSES).where(TASK_STATUSES.ID.eq(tsId)).execute();

		return deletedRows > 0;
	}

	public boolean createTaskStatus(Connection connection, TaskStatus taskStatus) {
		DSLContext query = DSL.using(connection, DatabaseSettings.getCurrentSqlDialect());

		int insertedRows = query.insertInto(TASK_STATUSES)
				.values(null,
						taskStatus.getName(),
						taskStatus.isStaysInSprint() ? 1 : 0,
						taskStatus.getProject().getId()).execute();

		return insertedRows == 1;
	}

	public boolean updateTaskStatuses(Connection connection, List<TaskStatus> taskStatuses) {
		DSLContext query = DSL.using(connection, DatabaseSettings.getCurrentSqlDialect());

		try {
			query.transaction( t -> {
				for (TaskStatus taskStatus : taskStatuses) {
					DSLContext nquery = DSL.using(t);
					int results;
					if(taskStatus.getId() < 0) {
						results = createTaskStatus(connection, taskStatus) ? 1 : 0;
					}
					else {
						results = nquery.update(TASK_STATUSES)
								.set(TASK_STATUSES.NAME, taskStatus.getName())
								.set(TASK_STATUSES.STAYS_IN_SPRINT, taskStatus.isStaysInSprint() ? 1 : 0)
								.set(TASK_STATUSES.PROJECT_ID, taskStatus.getProject().getId())
								.where(TASK_STATUSES.ID.eq(taskStatus.getId()))
								.execute();
					}
					if(results < 1) {
						throw new Exception("Rollback on value " + taskStatus.toString());
					}
				}
			});
		}
		catch (Exception e) {
			// rollback
			e.printStackTrace();
			return false;
		}
		return true;
	}
}
