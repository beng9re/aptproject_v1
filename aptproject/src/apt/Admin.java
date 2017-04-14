package apt;

import java.awt.BorderLayout;
import java.awt.Choice;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.border.EtchedBorder;

import db.UserCategory;


public class Admin extends JFrame implements ActionListener {
	JPanel p_north, p_north_left, p_north_right, p_south;
	JTable table;
	JScrollPane scroll;
	Choice choice;
	JButton bt_find, bt_copy, bt_xls;
	JTextField t_input;
	//Checkbox ch_a, ch_b, ch_c;
	
	DBManager manager;
	Connection con;
	ListModel listModel;
	JFileChooser chooser;
	
	Vector <UserCategory>user = new Vector<UserCategory>();

	public Admin() {
		chooser = new JFileChooser();
		p_north = new JPanel();
		p_north_left = new JPanel();
		p_north_right = new JPanel();
		p_south = new JPanel();
		table = new JTable();
		scroll = new JScrollPane(table);
		choice = new Choice();
		bt_find = new JButton("¡∂»∏");
		bt_copy = new JButton("¿Œº‚");
		bt_xls = new JButton("xml∑Œ ≥ª∫∏≥ª±‚");
		t_input = new JTextField(15);
//		ch_a = new Checkbox("∏Ù∂Û");
//		ch_b = new Checkbox("∏Ù∂Û");
//		ch_c = new Checkbox("∏Ù∂Û");

		setLayout(new BorderLayout());

		p_north_left.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 20));
		p_north_right.setLayout(new FlowLayout(FlowLayout.RIGHT, 5, 20));

		p_north_left.add(choice);
		p_north_left.add(t_input);
//		p_north_left.add(ch_a);
//		p_north_left.add(ch_b);
//		p_north_left.add(ch_c);

		p_north_right.add(bt_copy);
		p_north_right.add(bt_xls);
		p_north_right.add(bt_find);

		p_north.add(p_north_left);
		p_north.add(p_north_right);

		p_north.setBorder(new EtchedBorder(EtchedBorder.RAISED));

		p_south.add(scroll);

		scroll.setPreferredSize(new Dimension(680, 500));
		choice.setPreferredSize(new Dimension(150, 30));

		// p_north_left.setPreferredSize(new Dimension(350, 100));
		// p_north_right.setPreferredSize(new Dimension(350, 100));
		p_north.setPreferredSize(new Dimension(700, 80));
		
		bt_copy.addActionListener(this); 
		bt_find.addActionListener(this); 
		bt_xls.addActionListener(this); 
		
		t_input.addKeyListener(new KeyAdapter() {
			public void keyReleased(KeyEvent e) {
				int key=e.getKeyCode();
				if(key==KeyEvent.VK_ENTER){
					String msg=t_input.getText();
					String data=choice.getSelectedItem();
					String sql= "select * from aptuser where "+data+"= '"+msg+"' ";
					
					listModel.getList(sql);
					table.updateUI();
//					int colNum=choice.getSelectedIndex();
//			
//					for(int i=0; i<listModel.data.size();i++){
//						if(listModel.data.elementAt(i).elementAt(colNum)==t_input.getText()){
//							
//						}
//					}
				}
			}
		});
		
		add(p_north, BorderLayout.NORTH);
		add(p_south, BorderLayout.CENTER);
		setVisible(true);
		setSize(700, 700);

		init();
		getUser();
	}

	public void init() {
		manager = DBManager.getInstance();
		this.con = manager.getConnection();
	}
	
	
	public void getUser(){
		PreparedStatement pstmt=null;
		ResultSet rs=null;
		String sql = "select * from aptuser";
		
		try {
			pstmt=con.prepareStatement(sql);
			rs=pstmt.executeQuery();
			
			ResultSetMetaData meta= rs.getMetaData();
			for(int i=1; i<=meta.getColumnCount(); i++){
				choice.add(meta.getColumnName(i));
			}
			
			while(rs.next()){
				UserCategory dto = new UserCategory();
				dto.setAptuser_code(rs.getString("aptuser_code"));
				dto.setAptuser_id(rs.getString("aptuser_id"));
				dto.setAptuser_ip(rs.getString("aptuser_ip"));
				dto.setAptuser_live(rs.getString("aptuser_live"));
				dto.setAptuser_name(rs.getString("aptuser_name"));
				dto.setAptuser_perm(rs.getString("aptuser_perm"));
				dto.setAptuser_phone(rs.getString("aptuser_phone"));
				dto.setAptuser_pw(rs.getString("aptuser_pw"));
				dto.setAptuser_regdate(rs.getString("aptuser_regdate"));
				user.add(dto);
			}
		} catch (SQLException e) {
			
			e.printStackTrace();
		}
	}

	public void getList(){
			table.setModel(listModel = new ListModel(con));
			table.updateUI();
	}

	public void actionPerformed(ActionEvent e) {
		Object obj = e.getSource();
		if (obj == bt_find) {
			getList();
		}
		else if(obj == bt_xls) {
			
		}
		else if(obj == bt_copy) {
			
		}
	}

	public static void main(String[] args) {
		new Admin();
	}

}
