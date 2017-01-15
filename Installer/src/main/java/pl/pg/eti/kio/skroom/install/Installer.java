package pl.pg.eti.kio.skroom.install;

import java.io.IOException;
import java.sql.SQLException;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import pl.pg.eti.kio.skroom.install.frame.WelcomeFrame;

public class Installer {
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			
			@Override
			public void run() {
				JFrame frame = new WelcomeFrame();
				frame.setVisible(true);
			}
		});
	}
}
