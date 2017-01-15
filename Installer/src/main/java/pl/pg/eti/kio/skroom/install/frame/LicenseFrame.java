package pl.pg.eti.kio.skroom.install.frame;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.RowSpec;
import com.jgoodies.forms.layout.FormSpecs;
import javax.swing.JLabel;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JTextPane;
import javax.swing.JScrollPane;

public class LicenseFrame extends JFrame {

	private JPanel contentPane;
	private JFrame lastFrame;

	/**
	 * Create the frame.
	 */
	public LicenseFrame() {
		setResizable(false);
		setTitle("Skroom setup");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 540, 475);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new FormLayout(new ColumnSpec[] {
				FormSpecs.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("left:max(81dlu;default)"),
				FormSpecs.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("left:max(57dlu;default):grow"),
				FormSpecs.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("max(31dlu;default)"),
				FormSpecs.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("default:grow"),},
			new RowSpec[] {
				FormSpecs.RELATED_GAP_ROWSPEC,
				RowSpec.decode("max(41dlu;default)"),
				FormSpecs.RELATED_GAP_ROWSPEC,
				RowSpec.decode("max(86dlu;default):grow"),
				FormSpecs.RELATED_GAP_ROWSPEC,
				RowSpec.decode("bottom:max(20dlu;default)"),}));
		
		JLabel iconLabel = new JLabel("");
		contentPane.add(iconLabel, "2, 2, 1, 5");
		
		try {
			Dimension d = new Dimension(iconLabel.getWidth(), iconLabel.getHeight());
			BufferedImage bi = ImageIO.read(this.getClass().getClassLoader().getResourceAsStream("logo.png"));
			Icon icon = new ImageIcon(bi);
			iconLabel.setIcon(icon);
			iconLabel.setMaximumSize(d);
		}
		catch (Exception e) { e.printStackTrace();}
		
		JLabel titleLabel = new JLabel("Skroom license");
		titleLabel.setFont(new Font("Dialog", Font.BOLD, 18));
		contentPane.add(titleLabel, "4, 2, 5, 1");
		
		JScrollPane scrollPane = new JScrollPane();
		contentPane.add(scrollPane, "4, 4, 5, 1, fill, fill");
		
		JPanel panel = new JPanel();
		scrollPane.setViewportView(panel);
		
		JTextPane textPane = new JTextPane();
		textPane.setText("This software is developed using MIT License\n\nCopyright (c) 2016 Wojciech Stanisławski\n\nPermission is hereby granted, free of charge, to any person obtaining a copy\nof this software and associated documentation files (the \"Software\"), to deal\nin the Software without restriction, including without limitation the rights\nto use, copy, modify, merge, publish, distribute, sublicense, and/or sell\ncopies of the Software, and to permit persons to whom the Software is\nfurnished to do so, subject to the following conditions:\n\nThe above copyright notice and this permission notice shall be included in all\ncopies or substantial portions of the Software.\n\nTHE SOFTWARE IS PROVIDED \"AS IS\", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR\nIMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,\nFITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE\nAUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER\nLIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,\nOUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE\nSOFTWARE.");
		textPane.setEditable(false);
		panel.add(textPane);
		
		final JFrame thisFrame = this;
		
		JButton btnCancel = new JButton("Back");
		contentPane.add(btnCancel, "6, 6");
		btnCancel.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				lastFrame.setVisible(true);
				thisFrame.setVisible(false);
				thisFrame.removeAll();
				thisFrame.dispose();
			}
		});
		
		JButton btnAgree = new JButton("I agree");
		contentPane.add(btnAgree, "8, 6");
		btnAgree.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				InstallationDirectoryFrame nextFrame = new InstallationDirectoryFrame();
				nextFrame.setLastFrame(thisFrame);
				thisFrame.setVisible(false);
				nextFrame.setVisible(true);
			}
		});
	}
	
	public JFrame getLastFrame() {
		return lastFrame;
	}

	public void setLastFrame(JFrame lastFrame) {
		this.lastFrame = lastFrame;
	}
}
