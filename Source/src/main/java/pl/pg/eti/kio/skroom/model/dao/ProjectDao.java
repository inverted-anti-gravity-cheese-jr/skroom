package pl.pg.eti.kio.skroom.model.dao;

import org.jooq.DSLContext;
import org.jooq.Record1;
import org.jooq.Record3;
import org.jooq.Result;
import org.jooq.impl.DSL;
import org.jooq.util.xml.jaxb.Table;
import org.springframework.stereotype.Service;
import pl.pg.eti.kio.skroom.model.Project;
import pl.pg.eti.kio.skroom.model.User;
import pl.pg.eti.kio.skroom.model.dba.Tables;
import pl.pg.eti.kio.skroom.model.dba.tables.records.ProjectsRecord;
import pl.pg.eti.kio.skroom.model.enumeration.UserRole;
import pl.pg.eti.kio.skroom.settings.DatabaseSettings;

import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Wojciech Stanisławski, Krzysztof Świeczkowski
 * @since 03.11.16
 */
@Service
public class ProjectDao {

	public void addProject(Connection connection, Project project) {
		DSLContext query = DSL.using(connection, DatabaseSettings.getCurrentSqlDialect());

		Integer projectId = (Integer) query.fetchOne("SELECT seq FROM sqlite_sequence WHERE name='" + Tables.PROJECTS.getName() +"'").get(0);

		query.insertInto(Tables.PROJECTS).values(null, project.getName(), project.getDescription()).execute();
	}

	public boolean checkPrivilegesForProject(Connection connection, int projectId, User user) {
		if(UserRole.ADMIN.equals(user.getRole())) {
			return true;
		}

		DSLContext query = DSL.using(connection, DatabaseSettings.getCurrentSqlDialect());

		Result<Record1<Integer>> resultPrivileges;
		resultPrivileges = query.select(Tables.USER_ROLES_IN_PROJECT.PRIVILIGES)
				.from(Tables.USER_ROLES_IN_PROJECT, Tables.USERS_PROJECTS)
				.where(Tables.USERS_PROJECTS.USER_ID.eq(user.getId()))
				.fetch();
		if(resultPrivileges.isNotEmpty() && resultPrivileges.get(0).value1() == 1) {
			return true;
		}
		return false;
	}

	public Project getProjectForUser(Connection connection, int projectId, User user) {
		if(!checkPrivilegesForProject(connection, projectId, user)) {
			return null;
		}

		DSLContext query = DSL.using(connection, DatabaseSettings.getCurrentSqlDialect());

		Result<Record3<Integer, String, String>> result;

		result = query.select(Tables.PROJECTS.ID, Tables.PROJECTS.NAME, Tables.PROJECTS.DESCRIPTION)
				.from(Tables.PROJECTS)
				.where(Tables.PROJECTS.ID.eq(projectId))
				.limit(1).fetch();
		if(result.isEmpty()) {
			return null;
		}
		Record3<Integer, String, String> record  = result.get(0);
		Project project = new Project();
		project.setId(record.value1());
		project.setName(record.value2());
		project.setDescription(record.value3());

		return project;
	}

	public List<Project> getProjectsForUser(Connection connection, User user) {
		DSLContext query = DSL.using(connection, DatabaseSettings.getCurrentSqlDialect());

		List<Project> projects = new ArrayList<Project>();

		Result<Record3<Integer, String, String>> result;

		if(UserRole.ADMIN.equals(user.getRole())) {
			result = query.select(Tables.PROJECTS.ID, Tables.PROJECTS.NAME, Tables.PROJECTS.DESCRIPTION)
					.from(Tables.PROJECTS).fetch();
		}
		else {
			result = query.select(Tables.PROJECTS.ID, Tables.PROJECTS.NAME, Tables.PROJECTS.DESCRIPTION)
					.from(Tables.PROJECTS, Tables.USERS_PROJECTS)
					.where(Tables.USERS_PROJECTS.USER_ID.eq(user.getId())
							.and(Tables.PROJECTS.ID.eq(Tables.USERS_PROJECTS.PROJECT_ID))).fetch();
		}

		for(Record3<Integer, String, String> record : result) {
			Project project = new Project();
			project.setId(record.value1());
			project.setName(record.value2());
			project.setDescription(record.value3());
			projects.add(project);
		}

		return projects;
	}
}
