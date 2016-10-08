package pl.pg.eti.kio.skroom.settings;

import org.jooq.SQLDialect;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Global settings for database access.
 *
 * @author Wojciech Stanisławski
 * @since 18.08.16
 */
public class DatabaseSettings {

	private static final AtomicInteger currentDatabaseMode = new AtomicInteger();
	private static Connection dbConnection = null;

	public static SQLDialect getCurrentSqlDialect() {
		if(currentDatabaseMode.get() == 0) {
			return SQLDialect.SQLITE;
		}
		return SQLDialect.MYSQL;
	}

	public static Connection getDatabaseConnection() {
		return dbConnection;
	}

	public static void initConnection(String connectionString) {
		try {
			dbConnection = DriverManager.getConnection(connectionString);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static void initConnection(String connectionString, String user, String password) {
		try {
			dbConnection = DriverManager.getConnection(connectionString, user, password);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static void setDatabaseMode(int mode) {
		currentDatabaseMode.getAndSet(mode);
	}

}
