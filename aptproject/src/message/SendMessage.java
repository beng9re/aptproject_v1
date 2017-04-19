package message;

import java.awt.BorderLayout;
import java.awt.Checkbox;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
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
import javax.swing.border.Border;

import db.DBManager;
import dto.ComplexDto;
import tree.TreeMain;

public class SendMessage extends JFrame implements ActionListener{
	
	Connection con;
	
	TreeMain treeMain;
	
	JPanel  p_south, p_center, p_north;
	JTextArea   area;
	Vector<ComplexDto>  complexList = new Vector<ComplexDto>();
	Vector<Checkbox>  chkList = new Vector<Checkbox>();
	JButton  bt_send, bt_search;
	JTable   table;
	JScrollPane  scroll, areaScroll;
	CompUnitModel  model;
	JTextField  t_input, t_title;
	JLabel  la_title;
	
	public SendMessage(TreeMain treeMain) {
		
		this.treeMain = treeMain;
		this.con = treeMain.con;
		
		p_north = new JPanel();
		p_center = new JPanel();
		p_south = new JPanel();
		
		t_input = new JTextField(40);
		bt_search = new JButton("검색");
		
		la_title = new JLabel("제목");
		t_title = new JTextField(40);
		area = new JTextArea();
		areaScroll = new JScrollPane(area);
		bt_send = new JButton("보내기");
		
		table = new JTable();
		scroll = new JScrollPane(table);
		
		//la_title.setHorizontalAlignment(JLabel.LEFT);
		
		// color
		p_north.setBackground(Color.PINK);
		p_center.setBackground(Color.YELLOW);
		
		// size
		p_south.setPreferredSize(new Dimension(700, 100));
		p_north.setPreferredSize(new Dimension(700, 50));
		areaScroll.setPreferredSize(new Dimension(550, 50));
		
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
		
		this.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				close();
			}
		});
		
		init();
		
		setTitle("쪽지 보내기");
		setVisible(true);
		int X=(this.treeMain.getX()+200+10);
		int Y=(this.treeMain.getY()+30);
		setBounds(X, Y, 700, 700);
		//setLocationRelativeTo(null);
		//setDefaultCloseOperation(EXIT_ON_CLOSE);
		
	}
	
	// 초기 작업
	public void init(){
		
		model = new CompUnitModel(con);
		table.setModel(model);
		
		table.updateUI();
		
	}
	
	public void close(){
		//this.treeMain.menuObjList.remove(this);
	}
	
	// 메세지 보내기
	public void send(){
		
		//System.out.println(.length);
		int[] rows = table.getSelectedRows();
		
		if (rows.length ==0){
			JOptionPane.showMessageDialog(this, "메세지 보낼 호수를 선택해 주세요?");
			return;
		}
		
		PreparedStatement pstmt=null;
		ResultSet  rs = null;
		int msg_send_id=0;
		String msg_content = area.getText();
		
		// msg_send_id 받아 놓기
		String sql = "select seq_send_message.nextval msg_send_id from dual ";
		try {
			pstmt = con.prepareStatement(sql);
			rs = pstmt.executeQuery();
			
			while (rs.next()){
				msg_send_id = rs.getInt("msg_send_id");
			}
			
			//보내는 쪽지
			String send_user_id = "test"; // 임시로 사용. 나중에 로그인 아이디 받아 와야 함.
			String send_title = t_title.getText();
			sql = " insert into send_message (msg_send_id, msg_sendtime, msg_send_content, send_user_id, msg_send_title) "
			     + " values (?, sysdate, ?, ?, ?)"; 
			pstmt = con.prepareStatement(sql);
			pstmt.setInt(1, msg_send_id);
			pstmt.setString(2, msg_content);
			pstmt.setString(3, send_user_id);
			pstmt.setString(4, send_title);
			int result = pstmt.executeUpdate();
			System.out.println("result = "+result);
			
			int user_id_col = model.findColumn("사용자ID");
			int count=0;
			if (result !=0){
				// send 메세지 등록 성공하면, 받는 쪽지 (recieve msg) 등
				for (int i=0; i<rows.length;i++){
					String sql_in = "insert into recieve_message (msg_recieve_id, msg_send_id, recieve_user_id, msg_recieve_time, msg_confirm_time) "
							           + " values (seq_recieve_message.nextval, ?, ?, sysdate, null)";
					pstmt = con.prepareStatement(sql_in);
					pstmt.setInt(1, msg_send_id);
					System.out.println("id = "+msg_send_id+", row = "+rows[i]+", col="+user_id_col);
					String user_id = (String)table.getValueAt(rows[i], user_id_col);
					System.out.println("user_id="+user_id);
					pstmt.setString(2, user_id);
					System.out.println(sql_in);
					int result1 = pstmt.executeUpdate();
					System.out.println("result1="+result1);
					count+=result1;
					System.out.println(count);
				}
				
				if (count !=0){
					JOptionPane.showMessageDialog(this, count+"건 insert");
				}
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
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
