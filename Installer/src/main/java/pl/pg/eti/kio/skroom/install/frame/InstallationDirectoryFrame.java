package pl.pg.eti.kio.skroom.install.frame;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
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
import javax.swing.JButton;
import javax.swing.JTextPane;
import javax.swing.BoxLayout;
import javax.swing.Icon;
import javax.swing.JTextField;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class InstallationDirectoryFrame extends JFrame {

	private JPanel contentPane;
	private JTextField txtCprogramFilesskroom;
	private JFrame lastFrame;

	/**
	 * Create the frame.
	 */
	public InstallationDirectoryFrame() {
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

		JLabel titleLabel = new JLabel("Choose installation directory");
		titleLabel.setFont(new Font("Dialog", Font.BOLD, 18));
		contentPane.add(titleLabel, "4, 2, 5, 1");

		JPanel panel = new JPanel();
		contentPane.add(panel, "4, 4, 5, 1, fill, fill");
		panel.setLayout(new FormLayout(new ColumnSpec[] {
				ColumnSpec.decode("206px:grow"),
				FormSpecs.RELATED_GAP_COLSPEC,
				FormSpecs.DEFAULT_COLSPEC,},
				new RowSpec[] {
						RowSpec.decode("15px"),
						FormSpecs.RELATED_GAP_ROWSPEC,
						RowSpec.decode("max(24dlu;default)"),}));

		JLabel lblChooseInstallationDirectory = new JLabel("Choose installation directory");
		panel.add(lblChooseInstallationDirectory, "1, 1, left, center");

		txtCprogramFilesskroom = new JTextField();
		txtCprogramFilesskroom.setEditable(false);
		txtCprogramFilesskroom.setText("C:\\Program files\\Skroom");
		panel.add(txtCprogramFilesskroom, "1, 3, fill, default");
		txtCprogramFilesskroom.setColumns(10);

		final JFrame thisFrame = this;

		JButton btnBrowse = new JButton("Browse...");
		panel.add(btnBrowse, "3, 3");

		JButton btnCancel = new JButton("Cancel");
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

		JButton btnNext = new JButton("Next");
		contentPane.add(btnNext, "8, 6");

		btnNext.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				AdminPasswordFrame nextFrame = new AdminPasswordFrame();
				nextFrame.setLastFrame(thisFrame);
				thisFrame.setVisible(false);
				nextFrame.setVisible(true);
			}
		});
	}

	public void setLastFrame(JFrame frame) {
		this.lastFrame = frame;
	}
}
