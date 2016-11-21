package pl.pg.eti.kio.skroom.model.dao;

import org.jooq.DSLContext;
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
}
