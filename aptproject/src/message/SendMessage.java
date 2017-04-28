package message;

import java.awt.BorderLayout;
import java.awt.Checkbox;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.border.Border;
import javax.swing.table.TableRowSorter;

import db.DBManager;
import main.TreeMain;

public class SendMessage extends JFrame implements ActionListener{
	
	Connection con;
	
	TreeMain treeMain;
	
	JPanel  p_south, p_center, p_north;
	JTextArea   area;
	JButton  bt_send, bt_search;
	JTable   table;
	JScrollPane  scroll, areaScroll;
	CompUnitModel  model;
	JTextField  t_input, t_title;
	JLabel  la_title, la_explain;
	String userId;
	
	int frameWidth=600;
	int frameHeight=500;
	
	public SendMessage(TreeMain treeMain) {
		
		this.treeMain = treeMain;
		this.con = treeMain.getConnection();
		this.userId = treeMain.getUserID();
		
		p_north = new JPanel();
		p_center = new JPanel();
		p_south = new JPanel();
		
		t_input = new JTextField();
		bt_search = new JButton("검색");
		
		la_title = new JLabel("제목");
		t_title = new JTextField();
		area = new JTextArea();
		areaScroll = new JScrollPane(area);
		bt_send = new JButton("보내기");
		
		table = new JTable();
		scroll = new JScrollPane(table);
		
		la_explain = new JLabel("동 또는 호수 또는 사용자 명을 입력하시고 검색하세요");
		
		//la_title.setHorizontalAlignment(JLabel.LEFT);
		
		p_north.add(t_input);
		p_north.add(bt_search);
		
		p_center.setLayout(new BorderLayout());
		p_center.add(p_north, BorderLayout.NORTH);
		p_center.add(scroll);
		
		p_south.add(la_title);
		p_south.add(t_title);
		p_south.add(areaScroll);
		p_south.add(bt_send);		
		
		add(p_center);
		add(p_south, BorderLayout.SOUTH);
		
		// 리스너 연결
		bt_send.addActionListener(this);
		bt_search.addActionListener(this);
		t_input.addKeyListener(new KeyAdapter() {
			public void keyReleased(KeyEvent e) {
				int key = e.getKeyCode();
				if (key==KeyEvent.VK_ENTER){
					search();
				}
			}
		});
		t_input.addMouseListener(new MouseAdapter() {
			public void mouseMoved(MouseEvent e) {
				la_explain.setVisible(true);
				la_explain.setLocation(e.getX(), e.getY());
			}
			public void mouseExited(MouseEvent e) {
				//la_explain.setVisible(false);
			}
		
		});
		
		this.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				close();
			}
		});
		
		// size
		t_input.setPreferredSize(new Dimension(frameWidth-200, 20));
		p_north.setPreferredSize(new Dimension(frameWidth, 50));
		p_south.setPreferredSize(new Dimension(frameWidth, 90));
		t_title.setPreferredSize(new Dimension(frameWidth-216, 20));
		areaScroll.setPreferredSize(new Dimension(frameWidth-110, 50));
		table.setRowHeight(20);
		
		// color
		p_north.setBackground(new Color(247, 146, 30));
		p_south.setBackground(new Color(247, 146, 30));
		
		init();
		
		setTitle("쪽지 보내기");
		setVisible(true);
		setSize(frameWidth, frameHeight);
		
	}
	
	// 초기 작업
	public void init(){
		
		model = new CompUnitModel(con);
		table.setModel(model);
		table.setRowSorter(new TableRowSorter(model));
		
		table.updateUI();
		
	}
	
	public void close(){
		this.treeMain.removeMenuOpenList(this);
		dispose();
	}
	
	// 메세지 보내기
	public void send(){
		
		PreparedStatement pstmt=null;
		ResultSet  rs = null;
		StringBuffer  sql = new StringBuffer();

		//선택된 row 들
		int[] rows = table.getSelectedRows();
		
		// 보낼 호수를 선태했는지 check
		if (rows.length ==0){
			JOptionPane.showMessageDialog(this, "메세지 보낼 호수를 선택해 주세요?");
			table.requestFocus();
			return;
		}
		
		// 제목 입력 여부 check
		if (t_title.getText().trim().length()==0){
			JOptionPane.showMessageDialog(this, "제목을 입력해 주세요");
			t_title.requestFocus();
			return;
		}

		// 메세지 입력 여부 체크
		if (area.getText().trim().length()==0){
			JOptionPane.showMessageDialog(this, "보내실 메세지를 입력해 주세요");
			area.requestFocus();
			return;
		}		
		
		try {
			// Connection Auto Commit 잠시 false
			con.setAutoCommit(false);
			
			//보내는 쪽지
			String send_title = t_title.getText();
			String msg_content = area.getText();
			sql.append(" insert into send_message (msg_send_id, msg_send_user_id, msg_send_title, msg_send_content, msg_sendtime) ");
			sql.append(" values (seq_send_message.nextval, ?, ?, ?, sysdate)");
			pstmt = con.prepareStatement(sql.toString());
			pstmt.setString(1, userId);
			pstmt.setString(2, send_title);
			pstmt.setString(3, msg_content);
			int result = pstmt.executeUpdate();
			System.out.println("result = "+result);
			
			// 받는 쪽지			
			int count=0;
			// send 메세지 등록 성공하면, 받는 쪽지 (recieve msg) 등록
			if (result !=0){
				
				// recieve_message Insert 문 구성
				sql.delete(0, sql.length());
				sql.append("insert into recieve_message (msg_recieve_id, msg_send_id, msg_recv_user_id, msg_recieve_time) ");
				sql.append(" values (seq_recieve_message.nextval, seq_send_message.currval, ?, sysdate)");
				
				// 사용자ID column index 얻기
				int user_id_col = table.getColumn("사용자ID").getModelIndex();
				
				// 선택된 row 만큼 recieve_message insert
				for (int i=0; i<rows.length;i++){
					
					// 받는 사람 user_id 얻기
					String msg_recv_user_id = (String)table.getValueAt(rows[i], user_id_col);
					
					pstmt = con.prepareStatement(sql.toString());					
					pstmt.setString(1, msg_recv_user_id);
					int result1 = pstmt.executeUpdate();
					count+=result1;
				}
				System.out.println(count);
				
				if (count !=0){
					JOptionPane.showMessageDialog(this, count+" 명에게 메세지가 송신되었습니다.");
					t_title.setText("");
					area.setText("");
					table.clearSelection();
				}
			}
			
		} catch (SQLException e) {
			// Rollback;
			try {
				con.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			JOptionPane.showMessageDialog(this, "메세지 송신 중 에러가 발생했습니다.");
			e.printStackTrace();
		} finally {
			// Commit;
			try {
				con.commit();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			if (rs!=null)
				try {
					rs.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			if (pstmt!=null)
				try {
					pstmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			
			try {
				// Connection AutoCommit true 로 다시 지정
				con.setAutoCommit(true);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
	}
	
	// 검색
	public void search(){
		String srch = t_input.getText();
		model.getList(srch);
		table.updateUI();
		
	}

	public void actionPerformed(ActionEvent e) {
		Object obj = e.getSource();
		if (obj==bt_send){
			send();
		} else if (obj==bt_search){
			search();
		}
		
	}

}
