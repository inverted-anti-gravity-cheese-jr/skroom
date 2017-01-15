package pl.pg.eti.kio.skroom.install;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseCreator {
	
	public void createDatabase(String adminName, String adminEmail, String adminPassword, String secureQuestion, String secureAnswer) throws SQLException, IOException {
		Connection dbConnection = DriverManager.getConnection("jdbc:sqlite:data.db");
		
		String ddl = readSQLFile("ddl.sql", "UTF-8");
		String[] statements = ddl.split(";");
		for(String statement: statements) {
			statement = statement.trim();
			if(!statement.isEmpty()) {
				Statement stmt = dbConnection.createStatement();
				stmt.execute(statement + ";");
			}
		}
		
		String data = readSQLFile("default-data.sql", "UTF-8");
		statements = data.split(";");
		for(String statement: statements) {
			statement = statement.trim();
			if(!statement.isEmpty()) {
				Statement stmt = dbConnection.createStatement();
				stmt.execute(statement + ";");
			}
		}
		
		PreparedStatement pstmt;
		pstmt = dbConnection.prepareStatement("INSERT INTO USERS VALUES (NULL, ?, ?, NULL, 0);");
		pstmt.setString(1, adminName);
		pstmt.setString(2, adminEmail);
		pstmt.execute();
		
		pstmt = dbConnection.prepareStatement("INSERT INTO USERS_SECURITY VALUES (NULL, 1, ?, \"SALT\", ?, ?, 1);");
		pstmt.setString(1, adminPassword);
		pstmt.setString(2, secureQuestion);
		pstmt.setString(3, secureAnswer);
		pstmt.execute();
		
		pstmt = dbConnection.prepareStatement("INSERT INTO USERS_SETTINGS VALUES (NULL, 1, -1, 10, 25, 5);");
		pstmt.execute();
		
	}
	
	private String readSQLFile(String file, String encoding) throws IOException {
		InputStream is = this.getClass().getClassLoader().getResourceAsStream(file);
		
		BufferedReader br = new BufferedReader(new InputStreamReader(is, encoding));
		StringBuilder result = new StringBuilder();
		String line;
		while((line = br.readLine()) != null) {
			result.append(line);
			result.append('\t');
		}
		return result.toString();
	}

}
