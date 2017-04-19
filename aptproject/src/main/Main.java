package main;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.Connection;

import javax.swing.JFrame;
import javax.swing.JPanel;

import Edit.InvEditPan;
import aptuser.ModifyAdmin;
import aptuser.ModifyUser;
import aptuser.RegistUser;
import db.DBManager;
import viewer.Admin;

public class Main extends JFrame {
	DBManager dbMgr;
	Connection conn;
	String id;
	
	JPanel pnl_content;
	Menu menu;
	
	//실행할 패널들을 미리 생성한다
	Admin admin; //물품목록(관리자용)
	InvEditPan invEdit; //물품등록
	RegistUser regiUser; //회원등록
	ModifyUser modUser; //회원정보수정
	ModifyAdmin modAdmin; //관리자 정보 수정
		
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
//		modUser = new ModifyUser();
		modAdmin = new ModifyAdmin();
		
		menu = new Menu(this);
		pnl_content = new JPanel();
		pnl_content.setLayout(new CardLayout());
		
		//테스트 할 패널
		////////////////////////////////////////////////////
		pnl_content.add(modAdmin, "modU");
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
		if (admin==null) {
			admin = new Admin();
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
