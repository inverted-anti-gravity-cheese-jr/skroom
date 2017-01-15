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
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JTextPane;
import javax.swing.BoxLayout;
import javax.swing.Icon;
import javax.swing.JTextField;

public class AdminPasswordFrame extends JFrame {

	private JPanel contentPane;
	private JTextField txtAdmin;
	private JTextField txtAdminfoocom;
	private JTextField txtAdmin_1;
	private JTextField txtSecurityQuestion;
	private JTextField txtAnswer;
	private JFrame lastFrame;

	/**
	 * Create the frame.
	 */
	public AdminPasswordFrame() {
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
		
		JLabel titleLabel = new JLabel("Set up admin account");
		titleLabel.setFont(new Font("Dialog", Font.BOLD, 18));
		contentPane.add(titleLabel, "4, 2, 5, 1");
		
		JPanel panel = new JPanel();
		contentPane.add(panel, "4, 4, 5, 1, fill, fill");
		panel.setLayout(new FormLayout(new ColumnSpec[] {
				FormSpecs.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("default:grow"),},
			new RowSpec[] {
				FormSpecs.RELATED_GAP_ROWSPEC,
				FormSpecs.DEFAULT_ROWSPEC,
				FormSpecs.RELATED_GAP_ROWSPEC,
				FormSpecs.DEFAULT_ROWSPEC,
				FormSpecs.RELATED_GAP_ROWSPEC,
				FormSpecs.DEFAULT_ROWSPEC,
				FormSpecs.RELATED_GAP_ROWSPEC,
				FormSpecs.DEFAULT_ROWSPEC,
				FormSpecs.RELATED_GAP_ROWSPEC,
				FormSpecs.DEFAULT_ROWSPEC,
				FormSpecs.RELATED_GAP_ROWSPEC,
				FormSpecs.DEFAULT_ROWSPEC,
				FormSpecs.RELATED_GAP_ROWSPEC,
				FormSpecs.DEFAULT_ROWSPEC,
				FormSpecs.RELATED_GAP_ROWSPEC,
				FormSpecs.DEFAULT_ROWSPEC,
				FormSpecs.RELATED_GAP_ROWSPEC,
				FormSpecs.DEFAULT_ROWSPEC,
				FormSpecs.RELATED_GAP_ROWSPEC,
				FormSpecs.DEFAULT_ROWSPEC,}));
		
		JLabel lblAdminAccountName = new JLabel("Admin account name");
		panel.add(lblAdminAccountName, "2, 2");
		
		txtAdmin = new JTextField();
		txtAdmin.setText("admin");
		panel.add(txtAdmin, "2, 4, fill, default");
		txtAdmin.setColumns(10);
		
		JLabel lblAdminEmailAddress = new JLabel("Admin email address");
		panel.add(lblAdminEmailAddress, "2, 6");
		
		txtAdminfoocom = new JTextField();
		txtAdminfoocom.setText("admin@foo.com");
		panel.add(txtAdminfoocom, "2, 8, fill, default");
		txtAdminfoocom.setColumns(10);
		
		JLabel lblAdminPassword = new JLabel("Admin password");
		panel.add(lblAdminPassword, "2, 10");
		
		txtAdmin_1 = new JTextField();
		txtAdmin_1.setText("admin");
		panel.add(txtAdmin_1, "2, 12, fill, default");
		txtAdmin_1.setColumns(10);
		
		JLabel lblAdminSecurityQuestion = new JLabel("Admin security question");
		panel.add(lblAdminSecurityQuestion, "2, 14");
		
		txtSecurityQuestion = new JTextField();
		txtSecurityQuestion.setText("Security question");
		panel.add(txtSecurityQuestion, "2, 16, fill, default");
		txtSecurityQuestion.setColumns(10);
		
		JLabel lblAnswer = new JLabel("Answer");
		panel.add(lblAnswer, "2, 18");
		
		txtAnswer = new JTextField();
		txtAnswer.setText("Answer");
		panel.add(txtAnswer, "2, 20, fill, default");
		txtAnswer.setColumns(10);
		
		final JFrame thisFrame = this;
		
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
				InstallFrame nextFrame = new InstallFrame();
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
