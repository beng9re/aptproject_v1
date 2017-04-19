package main;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.Connection;

import javax.swing.JFrame;
import javax.swing.JPanel;

import Edit.InvEditPan;
import aptuser.RegistUser;
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
	
	//실행할 패널들을 미리 생성한다
	Admin_InvoiceView admin_invoice; //물품목록(관리자용)
	Admin_UserView admin_user;//사용자목록(관리자용)
	User user; //물품목록(사용자용)
	InvEditPan invEdit; //물품등록
	RegistUser regiUser; //회원등록
		
	//테스트를 위해서 임시사용중인 생성자
	public Main() {
		init();
	}
	
	//로그인창으로 부터 DB매니저 객체와 id를 받는다
	public Main(DBManager dbMgr, String id) {
		this.dbMgr = dbMgr;
		this.id = id;
		conn = dbMgr.getConnection();
		
		init();
	}
	
	//패널 전환은 카드 레이아웃을 사용해볼 예정
	public void init() {
		invEdit = new InvEditPan();
		admin_invoice=new Admin_InvoiceView();
		admin_user=new Admin_UserView();
		user= new User();
		
		menu = new Menu(this);
		pnl_content = new JPanel();
		pnl_content.setLayout(new CardLayout());
		
		//테스트 할 패널
		////////////////////////////////////////////////////
		pnl_content.add(admin_invoice , "invE");
		////////////////////////////////////////////////////
		
		add(menu, BorderLayout.WEST);
		add(pnl_content);
		
		this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				exit();
			}
		});
		
		setTitle("아파트 택배관리");
		setSize(900,700);
		setResizable(false);
		setVisible(true);
		setLocationRelativeTo(null);
	}
	
	//패널 전환을 위한 메서드(Menu.java:Jtree 에서 호출한다)
	public void switchPanel() {
		
	}
	
	//작동하는 버튼에 연결된 JFrame
	/////////////////////////////////////////////////////////////
	public void list() {
		if (admin_invoice==null) {
			admin_invoice = new Admin_InvoiceView();
		}
//		regiUser = new RegistUser();
	}
	
	//종료를 위한 메서드 Menu에 있는 종료에서도 함께 사용
	public void exit() {
//		dbMgr.disconnect();
		System.exit(0);
	}
	
	//프로그램 작동은 로그인을 통해 이루어지므로 마지막에 메인메서드는 제거
	public static void main(String[] args) {
		new Main();
	}

}
