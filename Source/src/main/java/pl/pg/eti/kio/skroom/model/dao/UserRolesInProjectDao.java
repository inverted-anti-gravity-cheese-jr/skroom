package pl.pg.eti.kio.skroom.model.dao;

import javafx.scene.control.Tab;
import org.jooq.DSLContext;
import org.jooq.Record1;
import org.jooq.Result;
import org.jooq.impl.DSL;
import org.springframework.stereotype.Service;
import pl.pg.eti.kio.skroom.model.UserRolesInProject;
import pl.pg.eti.kio.skroom.model.dba.Tables;
import pl.pg.eti.kio.skroom.model.dba.tables.records.UserRolesInProjectRecord;
import pl.pg.eti.kio.skroom.settings.DatabaseSettings;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import static pl.pg.eti.kio.skroom.model.dba.tables.UserRolesInProject.USER_ROLES_IN_PROJECT;

/**
 * Created by Marek Czerniawski on 2016-11-21.
 */
@Service
public class UserRolesInProjectDao {

    public List<UserRolesInProject> listAllUserRolesInProject(Connection connection) {
        DSLContext query = DSL.using(connection, DatabaseSettings.getCurrentSqlDialect());

        List<UserRolesInProject> userRolesInProjects = new ArrayList<UserRolesInProject>();

        Result<UserRolesInProjectRecord> records = query.selectFrom(Tables.USER_ROLES_IN_PROJECT).fetch();

        for (UserRolesInProjectRecord record : records) {
            userRolesInProjects.add(UserRolesInProject.fromDba(record));
        }

        return userRolesInProjects;
    }

    public UserRolesInProject fetchById(Connection connection, int id) {
        DSLContext query = DSL.using(connection, DatabaseSettings.getCurrentSqlDialect());

        UserRolesInProjectRecord record = query.selectFrom(USER_ROLES_IN_PROJECT)
                .where(USER_ROLES_IN_PROJECT.ID.eq(id))
                .fetchOne();

        return UserRolesInProject.fromDba(record);
    }

    public UserRolesInProject fetchByName(Connection connection, String role) {
        DSLContext query = DSL.using(connection, DatabaseSettings.getCurrentSqlDialect());

        UserRolesInProjectRecord record = query.selectFrom(USER_ROLES_IN_PROJECT)
                .where(USER_ROLES_IN_PROJECT.ROLE.like(role))
                .fetchOne();

        return UserRolesInProject.fromDba(record);
    }

    public boolean update(Connection connection, UserRolesInProject userRolesInProject) {
        DSLContext query = DSL.using(connection, DatabaseSettings.getCurrentSqlDialect());

        int updatedRows = query.update(USER_ROLES_IN_PROJECT)
                .set(USER_ROLES_IN_PROJECT.ROLE, userRolesInProject.getRole())
                .set(USER_ROLES_IN_PROJECT.COLOR, userRolesInProject.getColor())
                .set(USER_ROLES_IN_PROJECT.PRIVILIGES, userRolesInProject.isPrivileges() ? 1 : 0)
                .where(USER_ROLES_IN_PROJECT.ID.eq(userRolesInProject.getId()))
                .execute();

        return updatedRows > 0;
    }

    public boolean insert(Connection connection, UserRolesInProject userRolesInProject) {
        DSLContext query = DSL.using(connection, DatabaseSettings.getCurrentSqlDialect());

        int updatedRows = query.insertInto(USER_ROLES_IN_PROJECT,
                USER_ROLES_IN_PROJECT.ROLE,
                USER_ROLES_IN_PROJECT.COLOR,
                USER_ROLES_IN_PROJECT.PRIVILIGES)
                .values(userRolesInProject.getRole(), userRolesInProject.getColor(), userRolesInProject.isPrivileges() ? 1 : 0)
                .execute();

        return updatedRows > 0;
    }

    public boolean remove(Connection connection, Integer userRoleInProjectId) {
        DSLContext query = DSL.using(connection, DatabaseSettings.getCurrentSqlDialect());

        int deletedUserRoles = query.delete(Tables.USERS_PROJECTS)
                .where(Tables.USERS_PROJECTS.USER_ROLE_ID.eq(userRoleInProjectId))
                .execute();

        int updatedRows = query.delete(USER_ROLES_IN_PROJECT)
                .where(USER_ROLES_IN_PROJECT.ID.eq(userRoleInProjectId))
                .execute();
        return updatedRows > 0;
    }
}
