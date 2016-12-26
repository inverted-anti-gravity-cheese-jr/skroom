package pl.pg.eti.kio.skroom.model.dao;

import org.jooq.DSLContext;
import org.jooq.Result;
import org.jooq.impl.DSL;
import org.springframework.stereotype.Service;
import pl.pg.eti.kio.skroom.model.Project;
import pl.pg.eti.kio.skroom.model.TaskStatus;
import pl.pg.eti.kio.skroom.model.dba.tables.records.TaskStatusesRecord;
import pl.pg.eti.kio.skroom.settings.DatabaseSettings;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static pl.pg.eti.kio.skroom.model.dba.Tables.TASK_STATUSES;

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
		DSLContext query = DSL.using(connection, DatabaseSettings.getCurrentSqlDialect());

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

	public boolean createTaskStatus(Connection connection, TaskStatus taskStatus) {
		DSLContext query = DSL.using(connection, DatabaseSettings.getCurrentSqlDialect());

		int insertedRows = query.insertInto(TASK_STATUSES)
				.values(null,
						taskStatus.getName(),
						taskStatus.isStaysInSprint() ? 1 : 0,
						taskStatus.getProject().getId()).execute();

		return insertedRows == 1;
	}
}
