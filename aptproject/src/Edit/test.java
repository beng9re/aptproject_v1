package Edit;

import java.sql.Connection;

import javax.swing.JFrame;

import db.DBManager;

public class test extends JFrame{
	
	Connection con;
	public test() {
		con=DBManager.getInstance().getConnection();
		RetunPan rp=new RetunPan(con);
		add(rp);
		setVisible(true);
		pack();
		setDefaultCloseOperation(EXIT_ON_CLOSE);
	}
	
	public static void main(String[] args) {
		new test();
		
	}
}
