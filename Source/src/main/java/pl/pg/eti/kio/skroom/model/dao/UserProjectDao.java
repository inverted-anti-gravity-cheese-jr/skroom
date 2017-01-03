package pl.pg.eti.kio.skroom.model.dao;

import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.Record1;
import org.jooq.Result;
import org.jooq.impl.DSL;
import org.springframework.stereotype.Service;
import pl.pg.eti.kio.skroom.exception.NoSuchUserRoleException;
import pl.pg.eti.kio.skroom.model.User;
import pl.pg.eti.kio.skroom.model.UserProjectContainer;
import pl.pg.eti.kio.skroom.model.UserRolesInProject;
import pl.pg.eti.kio.skroom.model.dba.tables.records.UsersProjectsRecord;
import pl.pg.eti.kio.skroom.model.dba.tables.records.UsersRecord;
import pl.pg.eti.kio.skroom.settings.DatabaseSettings;

import java.sql.Connection;
import java.util.*;
import java.util.stream.Collectors;

import static pl.pg.eti.kio.skroom.model.dba.Tables.*;

/**
 * Created by Marek Czerniawski on 2017-01-03.
 */
@Service
public class UserProjectDao {
    public List<UserProjectContainer> listAllUsersForProject(Connection connection, int projectId) {
        DSLContext query = DSL.using(connection, DatabaseSettings.getCurrentSqlDialect());

        Result<Record> records = query.select().from(USERS_PROJECTS)
                .join(USERS).onKey()
                .join(PROJECTS).onKey()
                .join(USER_ROLES_IN_PROJECT).onKey()
                .where(PROJECTS.ID.eq(projectId))
                .orderBy(USERS_PROJECTS.USER_ID, USERS_PROJECTS.USER_ROLE_ID)
                .fetch();

        return getUserProjectContainers(records);
    }

    private List<UserProjectContainer> getUserProjectContainers(Result<Record> records) {
        Map<Integer, UserProjectContainer> results = new HashMap<>();

        try {
            for (Record record : records) {
                Integer userProjectId = record.into(USERS_PROJECTS).getProjectId();
                User user = User.fromDba(record.into(USERS));
                UserRolesInProject role = UserRolesInProject.fromDba(record.into(USER_ROLES_IN_PROJECT));
                results.computeIfAbsent(user.getId(), k -> new UserProjectContainer(userProjectId, user))
                        .getRoles().add(role);
            }
        } catch (NoSuchUserRoleException e) {
            e.printStackTrace();
        }

        return new ArrayList<>(results.values());
    }

    public Optional<UserProjectContainer> fetchContainerById(Connection connection, int projectId, int userId) {
        DSLContext query = DSL.using(connection, DatabaseSettings.getCurrentSqlDialect());

        Result<Record> records = query.select().from(USERS_PROJECTS)
                .join(USERS).onKey()
                .join(USER_ROLES_IN_PROJECT).onKey()
                .where(USERS_PROJECTS.PROJECT_ID.eq(projectId))
                .and((USERS_PROJECTS.USER_ID.eq(userId)))
                .orderBy(USERS_PROJECTS.USER_ROLE_ID)
                .fetch();

        return getUserProjectContainers(records).stream().findFirst();
    }

    public boolean add(Connection connection, int userId, int projectId, int roleId) {
        DSLContext query = DSL.using(connection, DatabaseSettings.getCurrentSqlDialect());

        int insertedRows = query.insertInto(USERS_PROJECTS)
                .values(null, userId, projectId, roleId)
                .execute();

        return insertedRows > 0;
    }

    public List<User> availableUsersForProject(Connection connection, int projectId) {
        DSLContext query = DSL.using(connection, DatabaseSettings.getCurrentSqlDialect());

        Result<UsersRecord> records = query.selectFrom(USERS)
                .where(USERS.ID.notIn(query.select(USERS_PROJECTS.USER_ID)
                        .from(USERS_PROJECTS)
                        .where(USERS_PROJECTS.PROJECT_ID.eq(projectId))))
                .fetch();

        List<User> result = new ArrayList<>();
        try {
            for (UsersRecord record : records) {
                result.add(User.fromDba(record));
            }
        } catch (NoSuchUserRoleException e) {
            result.clear();
        }
        return result;
    }

    public boolean checkIfUserIsAlreadyInProject(Connection connection, User user, int projectId) {
        DSLContext query = DSL.using(connection, DatabaseSettings.getCurrentSqlDialect());

        Result<UsersProjectsRecord> records = query.selectFrom(USERS_PROJECTS)
                .where(USERS_PROJECTS.USER_ID.eq(user.getId()))
                .and(USERS_PROJECTS.PROJECT_ID.eq(projectId))
                .fetch();

        return !records.isEmpty();
    }
}