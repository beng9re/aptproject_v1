package login;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.Connection;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

public class Login extends JFrame implements ActionListener {
	private JPanel pnl_top, pnl_field, pnl_bot;
	private JLabel lb_id, lb_pw;
	private JTextField txf_id;
	private JPasswordField txf_pw;
	private JButton btn_login, btn_barcode;
	
//	private DBManager dbMgr;
	private Connection conn;
	
	public Login() {
		pnl_top = new JPanel();
		pnl_field = new JPanel();
		pnl_bot = new JPanel();
		lb_id = new JLabel("I  D");
		lb_pw = new JLabel("P  W");
		txf_id = new JTextField(15);
		txf_pw = new JPasswordField(15);
		btn_barcode = new JButton("���ڵ� �α���");
		btn_login = new JButton("�α���");
		
		pnl_field.setLayout(new GridLayout(3, 2));
		pnl_bot.setLayout(new FlowLayout(FlowLayout.RIGHT));
		
		//�г�, �����ӿ� �߰�
		pnl_field.add(lb_id);
		pnl_field.add(txf_id);
		pnl_field.add(Box.createVerticalStrut(10));
		pnl_field.add(Box.createVerticalStrut(10));
		pnl_field.add(lb_pw);
		pnl_field.add(txf_pw);
		
		pnl_top.add(pnl_field);
		pnl_bot.add(btn_barcode);
		pnl_bot.add(btn_login);
		
		add(pnl_top);
		add(pnl_bot, BorderLayout.SOUTH);
		
		//style ���� ����
		btn_barcode.setBackground(Color.LIGHT_GRAY);
		btn_login.setBackground(Color.LIGHT_GRAY);
		pnl_top.setBorder(BorderFactory.createEmptyBorder(20, 10, 5, 70));
		pnl_field.setPreferredSize(new Dimension(250, 70));
		pnl_bot.setBorder(BorderFactory.createEmptyBorder(5, 10, 20, 40));
		lb_id.setHorizontalAlignment(JLabel.CENTER);
		lb_pw.setHorizontalAlignment(JLabel.CENTER);
		
		//������ ����
		btn_barcode.addActionListener(this);
		btn_login.addActionListener(this);
		this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				//db���� ����
				System.exit(0);
			}
		});
		
		//������(������) ����
		setTitle("�α���");
		setSize(300,200);
		setLocationRelativeTo(null);
		setResizable(false);
		setVisible(true);
	}
	
	private void login() {
		System.out.println("�α��ι�ư");
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == btn_login) {
			login();
			
		} else if (e.getSource() == btn_barcode) {
			JOptionPane.showMessageDialog(Login.this, "���ڵ带 �Է����ּ���");
		}
	}
	
	public static void main(String[] args) {
		new Login();
	}

}
