package pl.pg.eti.kio.skroom.model.dao;

import org.jooq.DSLContext;
import org.jooq.Result;
import org.jooq.impl.DSL;
import org.springframework.stereotype.Service;
import pl.pg.eti.kio.skroom.model.Project;
import pl.pg.eti.kio.skroom.model.Sprint;
import pl.pg.eti.kio.skroom.model.dba.Tables;
import pl.pg.eti.kio.skroom.model.dba.tables.records.SprintsRecord;
import pl.pg.eti.kio.skroom.settings.DatabaseSettings;

import java.sql.Connection;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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

	public boolean createFirstSprint(Connection connection, Project project, String firstSprintName) {
		DSLContext query = DSL.using(connection, DatabaseSettings.getCurrentSqlDialect());

		if(firstSprintName == null || firstSprintName.isEmpty()) {
			firstSprintName = "Sprint 1";
		}
		LocalDate date = LocalDate.now();

		int insertedRows = query.insertInto(SPRINTS).values(null, firstSprintName,
				Date.from(date.atStartOfDay(ZoneId.systemDefault()).toInstant()),
				Date.from(date.plusWeeks(project.getDefaultSprintLength()).atStartOfDay(ZoneId.systemDefault()).toInstant()),
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

}
