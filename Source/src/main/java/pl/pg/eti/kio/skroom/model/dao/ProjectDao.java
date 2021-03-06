package pl.pg.eti.kio.skroom.model.dao;

import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.Record1;
import org.jooq.Record3;
import org.jooq.Result;
import org.jooq.impl.DSL;
import org.springframework.stereotype.Service;
import pl.pg.eti.kio.skroom.model.Project;
import pl.pg.eti.kio.skroom.model.User;
import pl.pg.eti.kio.skroom.model.dba.Tables;
import pl.pg.eti.kio.skroom.model.dba.tables.records.ProjectsRecord;
import pl.pg.eti.kio.skroom.model.enumeration.UserRole;
import pl.pg.eti.kio.skroom.settings.DatabaseSettings;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import static pl.pg.eti.kio.skroom.model.dba.Tables.PROJECTS;

/**
 * Class to access model's Project from database.
 *
 * @author Wojciech Stanisławski, Krzysztof Świeczkowski
 * @since 03.11.16
 */
@Service
public class ProjectDao {

	/**
	 * Gets project with supplied id
	 *
	 * @param connection Connection to a database
	 * @param id Id of the project
	 * @return Project class from the model
	 */
	public Project fetchProjectById(Connection connection, int id) {
		DSLContext query = DSL.using(connection, DatabaseSettings.getCurrentSqlDialect());

		ProjectsRecord projectRecord = query.selectFrom(PROJECTS).where(PROJECTS.ID.eq(id)).fetchOne();

		return Project.fromDba(projectRecord);
	}

	public boolean addProject(Connection connection, Project project, TaskStatusDao dao) {
		DSLContext query = DSL.using(connection, DatabaseSettings.getCurrentSqlDialect());

		int inserted = query.insertInto(PROJECTS).values(null, project.getName(), project.getDescription(), project.getDefaultSprintLength()).execute();

		if(inserted == 1) {
			Result<Record> projIdQuery = query.fetch("SELECT seq FROM sqlite_sequence WHERE name='" + PROJECTS.getName() +"'");
			Record projIdRes = projIdQuery.iterator().next();
			if(projIdRes != null) {
				Integer projectId = (Integer) projIdRes.get(0);
				
				project.setId(projectId);
				dao.generateDefaultStatusesForProject(connection, project);
			}
		}
		
		return inserted == 1;
	}

	public void updateProject(Connection connection, Project project) {
		DSLContext query = DSL.using(connection, DatabaseSettings.getCurrentSqlDialect());

		query.update(PROJECTS)
				.set(PROJECTS.NAME, project.getName())
				.set(PROJECTS.DESCRIPTION, project.getDescription())
				.set(PROJECTS.DEFAULT_SPRINT_LENGTH, project.getDefaultSprintLength())
				.where(PROJECTS.ID.equal(project.getId())).execute();
	}

	public void removeProject(Connection connection, Integer projectId) {
		DSLContext query = DSL.using(connection, DatabaseSettings.getCurrentSqlDialect());

		query.delete(PROJECTS).where(PROJECTS.ID.equal(projectId)).execute();
	}

	/**
	 * Method check if user is assigned to given project.
	 *
	 * @param connection Connection to a database
	 * @param project Project class from model
	 * @param user User class from model
	 * @return Returns true if user has any privileges that allow him to edit project, and false otherwise
	 */
	public boolean checkUserViewPermissionsForProject(Connection connection, Project project, User user) {
		if(UserRole.ADMIN.equals(user.getRole())) {
			return true;
		}

		DSLContext query = DSL.using(connection, DatabaseSettings.getCurrentSqlDialect());

		Record1<Integer> resultPrivilege;
		resultPrivilege = query.select(Tables.USER_ROLES_IN_PROJECT.PRIVILIGES)
				.from(Tables.USER_ROLES_IN_PROJECT, Tables.USERS_PROJECTS)
				.where(Tables.USERS_PROJECTS.USER_ID.eq(user.getId()))
				.and(Tables.USERS_PROJECTS.PROJECT_ID.eq(project.getId()))
				.fetchOne();
		if(resultPrivilege != null && resultPrivilege.value1() == 1) {
			return true;
		}
		return false;
	}

	/**
	 * Method check if user has any privileges that allow him to edit project.
	 *
	 * @param connection Connection to a database
	 * @param project Project class from model
	 * @param user User class from model
	 * @return Returns true if user has any privileges that allow him to edit project, and false otherwise
	 */
	public boolean checkUserEditPermissionsForProject(Connection connection, Project project, User user) {
		if(project == null) {
			return false;
		}
		if(UserRole.ADMIN.equals(user.getRole())) {
			return true;
		}
		UserDao userDao = new UserDao();
		return userDao.checkIfHasProjectEditPermissions(connection, user, project);
	}

	/**
	 * Method check if user has any privileges that allow him to edit project.
	 *
	 * @param connection Connection to a database
	 * @param projectId Project Id changed into the class from model
	 * @param user User class from model
	 * @return Returns true if user has any privileges that allow him to edit project, and false otherwise
	 */
	public boolean checkUserEditPermissionsForProject(Connection connection, int projectId, User user) {
		Project project = new Project();
		project.setId(projectId);
		return checkUserEditPermissionsForProject(connection, project, user);
	}

	/**
	 * Gets project with supplied project id
	 *
	 * @param connection Connection to a database
	 * @param projectId Id of the project
	 * @param user User class for model
	 * @return Project class from the model
	 */
	public Project getProjectForUser(Connection connection, int projectId, User user) {
		Project tempProject = new Project();
		tempProject.setId(projectId);
		if(!checkUserViewPermissionsForProject(connection, tempProject, user)) {
			return null;
		}

		DSLContext query = DSL.using(connection, DatabaseSettings.getCurrentSqlDialect());

		Result<Record3<Integer, String, String>> result;

		result = query.select(PROJECTS.ID, PROJECTS.NAME, PROJECTS.DESCRIPTION)
				.from(PROJECTS)
				.where(PROJECTS.ID.eq(projectId))
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

	/**
	 * Gets all available projects for user
	 *
	 * @param connection Connection to a database
	 * @param user User class for model
	 * @return ProjectContainer class containing project and editable flag
	 */
	public List<Project> getProjectsForUser(Connection connection, User user) {
		DSLContext query = DSL.using(connection, DatabaseSettings.getCurrentSqlDialect());

		List<Project> projects = new ArrayList<>();

		Result<Record3<Integer, String, String>> result;

		if(UserRole.ADMIN.equals(user.getRole())) {
			result = query.select(PROJECTS.ID, PROJECTS.NAME, PROJECTS.DESCRIPTION)
					.from(PROJECTS).fetch();
		}
		else {
			result = query.select(PROJECTS.ID, PROJECTS.NAME, PROJECTS.DESCRIPTION)
					.from(PROJECTS, Tables.USERS_PROJECTS)
					.where(Tables.USERS_PROJECTS.USER_ID.eq(user.getId())
							.and(PROJECTS.ID.eq(Tables.USERS_PROJECTS.PROJECT_ID))).fetch();
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