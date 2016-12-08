package pl.pg.eti.kio.skroom.model.dao;

import org.jooq.DSLContext;
import org.jooq.Record1;
import org.jooq.Result;
import org.jooq.impl.DSL;
import org.jooq.types.DayToSecond;
import org.jooq.types.Interval;
import org.jooq.util.derby.sys.Sys;
import org.springframework.stereotype.Service;
import pl.pg.eti.kio.skroom.model.Project;
import pl.pg.eti.kio.skroom.model.Sprint;
import pl.pg.eti.kio.skroom.model.dba.Tables;
import pl.pg.eti.kio.skroom.model.dba.tables.records.SprintsRecord;
import pl.pg.eti.kio.skroom.settings.DatabaseSettings;

import java.sql.Connection;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;

import static pl.pg.eti.kio.skroom.model.dba.Tables.SPRINTS;

/**
 * Class to access sprint data.
 *
 * @author Wojciech Stanisławski
 * @since 05.12.16
 */
@Service
public class SprintDao {

	public List<Sprint> fetchSprintsForProject(Connection connection, Project project) {
		DSLContext query = DSL.using(connection, DatabaseSettings.getCurrentSqlDialect());

		List<Sprint> sprints = new ArrayList<Sprint>();

		Result<SprintsRecord> sprintsRecords = query.selectFrom(SPRINTS).where(SPRINTS.PROJECT_ID.eq(project.getId())).fetch();

		for(SprintsRecord record : sprintsRecords) {
			sprints.add(Sprint.fromDba(record, project));
		}

		return sprints;
	}

	public List<Sprint> fetchAvailableSprintsForProject(Connection connection, Project project) {
		DSLContext query = DSL.using(connection, DatabaseSettings.getCurrentSqlDialect());

		List<Sprint> sprints = new ArrayList<Sprint>();

		Result<SprintsRecord> sprintsRecords = query.selectFrom(SPRINTS)
				.where(SPRINTS.PROJECT_ID.eq(project.getId()))
				.and(SPRINTS.START_DAY.le(DSL.currentDate().add(new DayToSecond(1)))).fetch();

		for(SprintsRecord record : sprintsRecords) {
			if(record.getStartDay().toLocalDate().isBefore(LocalDate.now())) {
				sprints.add(Sprint.fromDba(record, project));
			}
		}

		return sprints;
	}

	public boolean createFirstSprint(Connection connection, Project project, String firstSprintName) {
		DSLContext query = DSL.using(connection, DatabaseSettings.getCurrentSqlDialect());

		if(firstSprintName == null || firstSprintName.isEmpty()) {
			firstSprintName = "Sprint 1";
		}
		LocalDate date = LocalDate.now();

		/*
		int insertedRows = query.insertInto(SPRINTS).values(null, firstSprintName,
				Date.from(date.atStartOfDay(ZoneId.systemDefault()).toInstant()),
				Date.from(date.plusWeeks(project.getDefaultSprintLength()).atStartOfDay(ZoneId.systemDefault()).toInstant()),
				project.getId()).execute();
				*/
		int insertedRows = query.insertInto(SPRINTS).values(null, firstSprintName,
				DSL.currentDate(),
				DSL.currentDate().add(new DayToSecond(7 * project.getDefaultSprintLength())),
				project.getId()).execute();

		return insertedRows == 1;
	}

	/**
	 * Removes all sprints for project.
	 *
	 * @param connection	Connection to a database.
	 * @param project		Project from which all sprints will be deleted.
	 * @return				Returns true if deleted any rows.
	 */
	public boolean removeSprintsForProject(Connection connection, Project project) {
		DSLContext query = DSL.using(connection, DatabaseSettings.getCurrentSqlDialect());

		int deletedRows = query.deleteFrom(SPRINTS).where(SPRINTS.PROJECT_ID.eq(project.getId())).execute();

		return deletedRows > 0;
	}

	/**
	 * Finds and returns ongoing sprint.
	 *
	 * @param connection	Connection to a database.
	 * @param project		Project from which all sprints will be deleted.
	 * @return				Ongoing sprint.
	 */
	public Sprint findCurrentSprint(Connection connection, Project project) {
		DSLContext query = DSL.using(connection, DatabaseSettings.getCurrentSqlDialect());

		SprintsRecord record = query.selectFrom(SPRINTS)
				.where(SPRINTS.START_DAY.le(DSL.currentDate())
					.and(SPRINTS.END_DAY.gt(DSL.currentDate()))
					.and(SPRINTS.PROJECT_ID.eq(project.getId()))).fetchOne();

		return Sprint.fromDba(record, project);
	}

	public boolean updateSprint(Connection connection, Sprint sprint) {
		DSLContext query = DSL.using(connection, DatabaseSettings.getCurrentSqlDialect());

		int updatedRows = query.update(SPRINTS)
				.set(SPRINTS.NAME, sprint.getName())
				.set(SPRINTS.START_DAY, DSL.date(sprint.getStartDate()))
				.set(SPRINTS.END_DAY, DSL.date(sprint.getEndDate()))
				.where(SPRINTS.ID.eq(sprint.getId()))
				.execute();

		return updatedRows == 1;
	}

	public int getSprintCountForProject(Connection connection, Project project) {
		DSLContext query = DSL.using(connection, DatabaseSettings.getCurrentSqlDialect());

		Record1<Integer> countRecord = query.selectCount().from(SPRINTS).where(SPRINTS.PROJECT_ID.eq(project.getId())).fetchOne();

		if(countRecord == null) {
			return 0;
		}
		else {
			return countRecord.value1().intValue();
		}
	}

	public Sprint getLastSprint(Connection connection, Project project) {
		DSLContext query = DSL.using(connection, DatabaseSettings.getCurrentSqlDialect());

		SprintsRecord sprintRecord = query.selectFrom(SPRINTS)
				.where(SPRINTS.PROJECT_ID.eq(project.getId()))
				.groupBy(SPRINTS.END_DAY)
				.orderBy(SPRINTS.END_DAY.desc()).fetchAny();

		return Sprint.fromDba(sprintRecord, project);
	}

	public boolean createSprint(Connection connection, Sprint sprint) {
		DSLContext query = DSL.using(connection, DatabaseSettings.getCurrentSqlDialect());

		int insertedRows = query.insertInto(SPRINTS)
				.values(null, sprint.getName(), sprint.getStartDate(),
						sprint.getEndDate(), sprint.getProject().getId()).execute();

		return insertedRows == 1;
	}

	public Sprint getNextSprint(Connection connection, Sprint sprint) {
		DSLContext query = DSL.using(connection, DatabaseSettings.getCurrentSqlDialect());

		SprintsRecord nextRecord = query.selectFrom(SPRINTS)
				.where(SPRINTS.PROJECT_ID.eq(sprint.getProject().getId()))
				.and(SPRINTS.START_DAY.eq(DSL.date(sprint.getEndDate())))
				.fetchOne();

		return Sprint.fromDba(nextRecord, sprint.getProject());
	}

}
