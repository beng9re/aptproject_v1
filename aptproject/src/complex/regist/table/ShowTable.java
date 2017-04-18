package complex.regist.table;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.Connection;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import db.DBManager;

public class ShowTable extends JFrame{
	Connection con;
	DBManager manager=DBManager.getInstance();
	JTable table;
	JScrollPane scroll;
	db_Table db_table;
	
	public ShowTable(){
		con=manager.getConnection();
		table=new JTable(db_table=new db_Table(con));
		scroll=new JScrollPane(table);
		
		
		
		add(scroll);
		
		setBounds(600, 50, 500, 700);
		setVisible(true);
		
		
	}

	public static void main(String[] args) {
		new ShowTable();

	}

}
