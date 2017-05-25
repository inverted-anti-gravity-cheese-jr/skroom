package pl.pg.eti.kio.skroom.stateless;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.Statement;
import java.util.Scanner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import pl.pg.eti.kio.skroom.settings.DatabaseSettings;

@Service
public class InitDao {

    private static final Logger LOGGER = LoggerFactory.getLogger(InitDao.class);

    // TODO: implement statelessness
    public void createDatabase() {
        String dialect = DatabaseSettings.getCurrentSqlDialect().getName().toLowerCase();
        Connection dbConnection = DatabaseSettings.getDatabaseConnection();
        try {
            Statement stmt = dbConnection.createStatement();
            int numberOfUsers = stmt.executeQuery("SELECT COUNT(*) FROM USERS").getInt(0);
            if (numberOfUsers < 1) {
                generateInitialData(dbConnection, dialect);
            }
        }
        catch (Exception e) {
            LOGGER.info("Could not open database, generating new one {}", e.getMessage());
            try {
                generateTables(dbConnection, dialect);
                generateInitialData(dbConnection, dialect);
            }
            catch (Exception ex) {
                LOGGER.info("Could not init database, reason {}", ex.getMessage());
            }
        }
    }

    private void generateInitialData(Connection connection, String dialect) throws Exception {
        String ddlSql = readSqlFile("default-" + dialect + ".sql");
        executeMultipleStatements(connection, ddlSql);
    }

    private void generateTables(Connection connection, String dialect) throws Exception {
        String ddlSql = readSqlFile("ddl-" + dialect + ".sql");
        executeMultipleStatements(connection, ddlSql);
    }

    private void executeMultipleStatements(Connection connection, String statements) throws Exception {
        String[] statementsArray = statements.split(";");
        for(String statement: statementsArray) {
            Statement stmt = connection.createStatement();
            stmt.execute(statement + ";");
            LOGGER.info("Database init - execute: {}", statement);
        }
    }

    private String readSqlFile(String fileName) {
        StringBuilder result = new StringBuilder();
        File file = new File(getClass().getClassLoader().getResource(fileName).getFile());

        try (Scanner scanner = new Scanner(file)) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                result.append(line);
            }

            scanner.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return result.toString();
    }

}