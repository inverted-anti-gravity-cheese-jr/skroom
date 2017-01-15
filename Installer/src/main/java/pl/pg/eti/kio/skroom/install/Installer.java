package pl.pg.eti.kio.skroom.install;

import java.io.IOException;
import java.sql.SQLException;

public class Installer {
	public static void main(String[] args) {
		System.out.println("ehlo");
		
		try {
			new DatabaseCreator().createDatabase("adminName", "adminMail@foo.com", "admin", "Question?", "Password");
		} catch (SQLException | IOException e) {
			e.printStackTrace();
		}
	}
}
