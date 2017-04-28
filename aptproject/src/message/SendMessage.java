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
		bt_search = new JButton("�˻�");
		
		la_title = new JLabel("����");
		t_title = new JTextField();
		area = new JTextArea();
		areaScroll = new JScrollPane(area);
		bt_send = new JButton("������");
		
		table = new JTable();
		scroll = new JScrollPane(table);
		
		la_explain = new JLabel("�� �Ǵ� ȣ�� �Ǵ� ����� ���� �Է��Ͻð� �˻��ϼ���");
		
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
		
		// ������ ����
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
		
		setTitle("���� ������");
		setVisible(true);
		setSize(frameWidth, frameHeight);
		
	}
	
	// �ʱ� �۾�
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
	
	// �޼��� ������
	public void send(){
		
		PreparedStatement pstmt=null;
		ResultSet  rs = null;
		StringBuffer  sql = new StringBuffer();

		//���õ� row ��
		int[] rows = table.getSelectedRows();
		
		// ���� ȣ���� �����ߴ��� check
		if (rows.length ==0){
			JOptionPane.showMessageDialog(this, "�޼��� ���� ȣ���� ������ �ּ���?");
			table.requestFocus();
			return;
		}
		
		// ���� �Է� ���� check
		if (t_title.getText().trim().length()==0){
			JOptionPane.showMessageDialog(this, "������ �Է��� �ּ���");
			t_title.requestFocus();
			return;
		}

		// �޼��� �Է� ���� üũ
		if (area.getText().trim().length()==0){
			JOptionPane.showMessageDialog(this, "������ �޼����� �Է��� �ּ���");
			area.requestFocus();
			return;
		}		
		
		try {
			// Connection Auto Commit ��� false
			con.setAutoCommit(false);
			
			//������ ����
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
			
			// �޴� ����			
			int count=0;
			// send �޼��� ��� �����ϸ�, �޴� ���� (recieve msg) ���
			if (result !=0){
				
				// recieve_message Insert �� ����
				sql.delete(0, sql.length());
				sql.append("insert into recieve_message (msg_recieve_id, msg_send_id, msg_recv_user_id, msg_recieve_time) ");
				sql.append(" values (seq_recieve_message.nextval, seq_send_message.currval, ?, sysdate)");
				
				// �����ID column index ���
				int user_id_col = table.getColumn("�����ID").getModelIndex();
				
				// ���õ� row ��ŭ recieve_message insert
				for (int i=0; i<rows.length;i++){
					
					// �޴� ��� user_id ���
					String msg_recv_user_id = (String)table.getValueAt(rows[i], user_id_col);
					
					pstmt = con.prepareStatement(sql.toString());					
					pstmt.setString(1, msg_recv_user_id);
					int result1 = pstmt.executeUpdate();
					count+=result1;
				}
				System.out.println(count);
				
				if (count !=0){
					JOptionPane.showMessageDialog(this, count+" ���� �޼����� �۽ŵǾ����ϴ�.");
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
			JOptionPane.showMessageDialog(this, "�޼��� �۽� �� ������ �߻��߽��ϴ�.");
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
				// Connection AutoCommit true �� �ٽ� ����
				con.setAutoCommit(true);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
	}
	
	// �˻�
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
