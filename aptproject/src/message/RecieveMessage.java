package message;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import db.DBManager;
import tree.TreeMain;

public class RecieveMessage extends JFrame implements ActionListener, Runnable {
	
	Connection con;	
	TreeMain treeMain;
	
	JPanel  p_south, p_center, p_north;
	JTextArea   area;
	JTable   table;
	JScrollPane  scroll, areaScroll;
	JTextField  t_input, t_title;
	JLabel  la_title;
	JButton  bt_search;
	
	RecieveMsgModel  model;
	
	public RecieveMessage(TreeMain treeMain) {
		this.treeMain = treeMain;
		this.con = treeMain.con;
		
		p_north = new JPanel();
		p_center = new JPanel();
		p_south = new JPanel();
		
		t_input = new JTextField(30);
		bt_search = new JButton("검색");
		
		table = new JTable();
		scroll = new JScrollPane(table);
		
		la_title = new JLabel("제목");
		t_title = new JTextField(40);
		area = new JTextArea();
		areaScroll = new JScrollPane(area);
		
		p_north.add(t_input);
		p_north.add(bt_search);
		
		p_center.setLayout(new BorderLayout());
		p_center.add(p_north, BorderLayout.NORTH);
		p_center.add(scroll);
		
		p_south.add(la_title);
		p_south.add(t_title);
		p_south.add(areaScroll);
		
		// size
		p_south.setPreferredSize(new Dimension(700, 100));
		p_north.setPreferredSize(new Dimension(700, 50));
		areaScroll.setPreferredSize(new Dimension(550, 50));

		add(p_center);
		add(p_south, BorderLayout.SOUTH);
		
		// 리스너 연결
		// 검색 버튼
		bt_search.addActionListener(this);
		t_input.addKeyListener(new KeyAdapter() {
			public void keyReleased(KeyEvent e) {
				int key = e.getKeyCode();
				if (key==KeyEvent.VK_ENTER){
					search();
				}
			}
		});
		// 마우스 이벤트
		table.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				showMessage();
			}
		});
		// Key 이벤트
		table.addKeyListener(new KeyAdapter() {
			public void keyReleased(KeyEvent e) {
				int key = e.getKeyCode();
				if (key==KeyEvent.VK_UP || key==KeyEvent.VK_DOWN){
					showMessage();
				}
			}
		});
		// 
		this.addWindowListener(new WindowAdapter() {			
			public void windowClosing(WindowEvent e) {
				close();
			}
		});
		
		init();
		
		setTitle("쪽지 수신함");
		setVisible(true);
		int X=(this.treeMain.getX()+200+10);
		int Y=(this.treeMain.getY()+30);
		setBounds(X, Y, 700, 700);
		//setLocationRelativeTo(null);
		
	}
	
	public void init(){
		
		model = new RecieveMsgModel(con);
		table.setModel(model);
		
		// content 컬럼 숨기기
		table.getColumn("msg_send_content").setWidth(0);
		table.getColumn("msg_send_content").setMinWidth(0);
		table.getColumn("msg_send_content").setMaxWidth(0);
		
		table.updateUI();
		
	}
	
	public void close(){
		this.treeMain.menuObjList.remove(this);		
	}
	
	public void search(){
		String srch = t_input.getText();
		model.getList(srch);
		table.updateUI();
	}

	public void actionPerformed(ActionEvent e) {
		Object obj = e.getSource();
		if (obj==bt_search){
			search();
		}
		
	}
	
	public void showMessage(){
		PreparedStatement  pstmt = null;
		ResultSet  rs=null;
		
		int row = table.getSelectedRow();
		
		int col;
		t_title.setText("");
		area.setText("");
		
		// title
		col =  table.getColumn("제목").getModelIndex();
		//System.out.println("제목="+col);
		String title = (String)table.getValueAt(row, col);
		t_title.setText(title);
		
		// content
		col =  table.getColumn("msg_send_content").getModelIndex();
		//System.out.println("제목="+col);
		String content = (String)table.getValueAt(row, col);
		area.setText(content);
		
		// 확인여부
		//col = table.getColumn("확인여부").getModelIndex();
		//System.out.println("확인여부="+col);
		//String flag  = "Y";
		//table.editCellAt(row, col);
		//table.setValueAt(flag, row, col);
		//model.setValueAt((Object)flag, row, col);
		//model.setValueAt('Y', row, col);
		//table.editingStopped(null);
		
		col =  table.getColumn("msg_recieve_id").getModelIndex();
		System.out.println("msg_recieve_id="+col + "value="+table.getValueAt(row, col));
		int msg_recieve_id=Integer.parseInt((String)table.getValueAt(row, col));
		
		StringBuffer sql = new StringBuffer();
		sql.append("update recieve_message ");
		sql.append("set       msg_confirm_flag = 'Y' ");
		sql.append("where  msg_recieve_id = ? ");
		//String sql="update recieve_message ";
		try {
			pstmt = con.prepareStatement(sql.toString());
			pstmt.setInt(1, msg_recieve_id);
			int result = pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (pstmt!=null)
				try {
					pstmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
		}
		model.getList(t_input.getText());
		table.updateUI();

	}

	public void run() {
		while (true){
			
		}
		
	}
	
//	public static void main(String[] args) {
//		new RecieveMessage();
//	}

}
