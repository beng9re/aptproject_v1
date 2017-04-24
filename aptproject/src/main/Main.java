package main;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.Connection;

import javax.swing.JFrame;
import javax.swing.JPanel;

import Edit.InvEditPan;
import aptuser.ModifyUser;
import aptuser.RegistUser;
import chat.ChatClient;
import chat.ChatServer;
import complex.regist.ComplexPanel;
import db.DBManager;
import viewer.Admin_InvoiceView;
import viewer.Admin_UserView;
import viewer.User;

public class Main extends JFrame {
	DBManager dbMgr;
	Connection conn;
	String id;
	
	JPanel pnl_content;
	Menu menu;

	
	
	//������ �гε��� �̸� �����Ѵ�
	Admin_InvoiceView admin_invoice; //��ǰ���(�����ڿ�)
	Admin_UserView admin_user;//����ڸ��(�����ڿ�)
	User user; //��ǰ���(����ڿ�)
	InvEditPan invEdit; //��ǰ���
	RegistUser regiUser; //ȸ�����
	ComplexPanel ComplexPanel;//�ǹ����
	ModifyUser modUser; //ȸ����������
		
	//�׽�Ʈ�� ���ؼ� �ӽû������ ������
	public Main() {
		init();
	}
	
	//�α���â���� ���� DB�Ŵ��� ��ü�� id�� �޴´�
	public Main(DBManager dbMgr, String id) {
		this.dbMgr = dbMgr;
		this.id = id;
		conn = dbMgr.getConnection();
		
		init();
	}
	
	//�г� ��ȯ�� ī�� ���̾ƿ��� ����غ� ����
	public void init() {
		invEdit = new InvEditPan();
		admin_invoice=new Admin_InvoiceView();
		admin_user=new Admin_UserView();
		user= new User();
		
		menu = new Menu(this);
		pnl_content = new JPanel();
		pnl_content.setLayout(new CardLayout());
		
		//�׽�Ʈ �� �г�
		////////////////////////////////////////////////////

		pnl_content.add(admin_invoice, "invE");

		
		add(menu, BorderLayout.WEST);
		add(pnl_content);
		
		this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				exit();
			}
		});
		
		setTitle("����Ʈ �ù����");
		setSize(900,700);
		setResizable(false);
		setVisible(true);
		setLocationRelativeTo(null);
	}
	
	//�г� ��ȯ�� ���� �޼���(Menu.java:Jtree ���� ȣ���Ѵ�)
	public void switchPanel() {
		
	}
	
	//�۵��ϴ� ��ư�� ����� JFrame
	/////////////////////////////////////////////////////////////
	public void list() {
		new ChatClient();
	}
	
	//���Ḧ ���� �޼��� Menu�� �ִ� ���ῡ���� �Բ� ���
	public void exit() {
//		dbMgr.disConnect(conn);
		System.exit(0);
	}
	
	//���α׷� �۵��� �α����� ���� �̷�����Ƿ� �������� ���θ޼���� ����
	public static void main(String[] args) {
		new Main();
		new ChatServer();
		new ChatClient();
	}

}
