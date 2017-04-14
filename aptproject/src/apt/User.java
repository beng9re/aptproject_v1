package apt;

import java.awt.BorderLayout;
import java.awt.Choice;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

public class User extends JFrame implements ActionListener {
	JPanel p_north, p_south;
	JTable table;
	JScrollPane scroll;
	Choice choice;
	JButton bt_find, bt_copy, bt_xls;
	Connection con;
	DBManager manager;
	

	public User() {
	
		p_north = new JPanel();
		p_south = new JPanel();
		table = new JTable(3, 4);
		scroll = new JScrollPane(table);
		bt_find = new JButton("조회");
		bt_copy = new JButton("인쇄");
		bt_xls = new JButton("xml로 내보내기");

		setLayout(new BorderLayout());

		p_north.add(bt_copy);
		p_north.add(bt_xls);
		p_north.add(bt_find);

		p_south.add(scroll);

		scroll.setPreferredSize(new Dimension(680, 500));
		p_north.setPreferredSize(new Dimension(700, 100));

		add(p_north, BorderLayout.NORTH);
		add(p_south, BorderLayout.CENTER);

		bt_find.addActionListener(this);
		bt_copy.addActionListener(this);
		bt_xls.addActionListener(this);

		setVisible(true);
		setSize(700, 700);
		init();
	}
	
	public void init() {
		manager = DBManager.getInstance();
		this.con = manager.getConnection();
	}
	
	public void getList(){
		table.setModel(new ListModel(con));
		table.updateUI();
	}
	public void copy(){
		
	}
	
	public void save(){
		
	}
	
	public void actionPerformed(ActionEvent e) {
		Object obj = e.getSource();
		if (obj == bt_find) {
			getList();
		}
		else if (obj == bt_copy) {
			copy();
		}
		else if (obj == bt_xls) {
			save();
		}
	}
	public static void main(String[] args) {
		new User();
	}

}
