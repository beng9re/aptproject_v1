package message;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
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
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableRowSorter;

import org.apache.poi.ss.usermodel.HorizontalAlignment;

import db.DBManager;
import main.TreeMain;

public class RecieveMessage extends JFrame implements ActionListener, Runnable {
	
	Connection con;	
	TreeMain treeMain;
	
	JPanel  p_south, p_center, p_north;
	JTextArea   area;
	JTable   table;
	JScrollPane  scroll, areaScroll;
	JTextField  t_input, t_title;
	JLabel  la_title, la_new_msg_chk;
	JButton  bt_search;
	
	RecieveMsgModel  model;
	Thread  thread;
	boolean threadFlag=false;
	
	int frameWidth=600;
	int frameHeight=500;
	String userID;
	
	public RecieveMessage(TreeMain treeMain) {
		this.treeMain = treeMain;
		this.con = treeMain.getConnection();
		this.userID = treeMain.getUserID();
		
		p_north = new JPanel();
		p_center = new JPanel();
		p_south = new JPanel();
		
		t_input = new JTextField();
		bt_search = new JButton("�˻�");
		
		table = new JTable();
		scroll = new JScrollPane(table);
		
		la_title = new JLabel("����");
		t_title = new JTextField();
		area = new JTextArea();
		areaScroll = new JScrollPane(area);
		
		la_new_msg_chk = new JLabel("");

		p_north.add(t_input);
		p_north.add(bt_search);
		p_north.add(la_new_msg_chk);
		
		p_center.setLayout(new BorderLayout());
		p_center.add(p_north, BorderLayout.NORTH);
		p_center.add(scroll);
		
		p_south.add(la_title);
		p_south.add(t_title);
		p_south.add(areaScroll);
		
		add(p_center);
		add(p_south, BorderLayout.SOUTH);
		
		// ������ ����
		// �˻� ��ư
		bt_search.addActionListener(this);
		t_input.addKeyListener(new KeyAdapter() {
			public void keyReleased(KeyEvent e) {
				int key = e.getKeyCode();
				if (key==KeyEvent.VK_ENTER){
					search();
				}
			}
		});
		// ���콺 �̺�Ʈ
		table.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				showMessage();
			}
		});
		// Key �̺�Ʈ
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
		
		// size
		t_input.setPreferredSize(new Dimension(frameWidth-200, 20));
		p_north.setPreferredSize(new Dimension(frameWidth, 60));
		p_south.setPreferredSize(new Dimension(frameWidth, 90));
		t_title.setPreferredSize(new Dimension(frameWidth-160, 20));
		areaScroll.setPreferredSize(new Dimension(frameWidth-110, 50));
		
		// font
		la_new_msg_chk.setFont(new Font("����", Font.ITALIC, 13));
		
		// Editable
		t_title.setEditable(false);
		area.setEditable(false);
		
		// color
		la_new_msg_chk.setForeground(Color.BLUE);
		t_title.setBackground(Color.WHITE);
		area.setBackground(Color.WHITE);
		p_north.setBackground(Color.PINK);
		p_south.setBackground(Color.PINK);
		
		init();
		
		setTitle("���� ������");
		setVisible(true);
		setSize(frameWidth, frameHeight);
		
	}
	
	public void init(){
		
		model = new RecieveMsgModel(con, userID);
		table.setModel(model);
		table.setRowSorter(new TableRowSorter(model));
		
		// msg_send_content �÷� �����
		table.getColumn("msg_send_content").setWidth(0);
		table.getColumn("msg_send_content").setMinWidth(0);
		table.getColumn("msg_send_content").setMaxWidth(0);
/*		
		// msg_recieve_id �÷� �����
		table.getColumn("msg_recieve_id").setWidth(0);
		table.getColumn("msg_recieve_id").setMinWidth(0);
		table.getColumn("msg_recieve_id").setMaxWidth(0);
*/		
		// msg_send_user_id �÷� �����
		table.getColumn("msg_send_user_id").setWidth(0);
		table.getColumn("msg_send_user_id").setMinWidth(0);
		table.getColumn("msg_send_user_id").setMaxWidth(0);
		
		// Ȯ�ο��� size ����
		table.getColumn("Ȯ�ο���").setPreferredWidth(20);
		// Ȯ�ο��� text ���� center
		DefaultTableCellRenderer  cellRender = new DefaultTableCellRenderer();
		cellRender.setHorizontalAlignment(JLabel.CENTER);
		table.getColumn("Ȯ�ο���").setCellRenderer(cellRender);
		
		table.updateUI();
		
		threadFlag=true;
		thread = new Thread(this);
		thread.start();
		
	}
	
	public void close(){
		this.treeMain.removeMenuOpenList(this);
		threadFlag=false;
	}
	
	public void search(){
		
		table.clearSelection();
		System.out.println("search");
		String srch = t_input.getText();
		model.getList(srch);
		table.updateUI();
		
		showMessage();
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
		
		if (row!=-1){
			// title
			col =  table.getColumn("����").getModelIndex();
			System.out.println("����="+col);
			String title = (String)table.getValueAt(row, col);
			t_title.setText(title);
			
			// content
			col =  table.getColumn("msg_send_content").getModelIndex();
			//System.out.println("����="+col);
			String content = (String)table.getValueAt(row, col);
			area.setText(content);
			
			// Ȯ�ο���
			col = table.getColumn("Ȯ�ο���").getModelIndex();
			String confirmFlag = (String)table.getValueAt(table.getSelectedRow(), col);
			
			// ��Ȯ�� �� ��츸 Update
			if (confirmFlag.equalsIgnoreCase("N")){		
				
				// msg_recieve_id
				col =  table.getColumn("msg_recieve_id").getModelIndex();
				System.out.println("msg_recieve_id="+col + "value="+table.getValueAt(row, col));
				int msg_recieve_id=(Integer)table.getValueAt(row, col);
				
				StringBuffer sql = new StringBuffer();
				sql.append("update recieve_message ");
				sql.append("set       msg_confirm_flag = 'Y' ");
				sql.append("          ,msg_confirm_time=sysdate ");
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
				// table ������ �ٽ� ��ȸ
				model.getList(t_input.getText());
				table.updateUI();
			}
		}

	}
	
	public void checkNewMsg(){
		System.out.println("checkNewMsg");
		PreparedStatement  pstmt=null;
		ResultSet  rs=null;
		StringBuffer sql = new StringBuffer();
		
		int msg_recieve_id;
		int chk_msg_recieve_id;
		int findRow;
		int rowCount;
		
		
		int msg_recieve_id_col=table.getColumn("msg_recieve_id").getModelIndex();
		System.out.println("msg_recieve_id_col="+msg_recieve_id_col);
		int currSelectedRow=table.getSelectedRow();
		// ���� ���õ� row �� msg_recieve_id �� ��� ����
		int sel_msg_recieve_id=-1;
		if (currSelectedRow!=-1 && table.getRowCount()!=0){
			System.out.println("currSelectedRow = "+currSelectedRow + ", msg_recieve_id_col = "+msg_recieve_id_col);
			sel_msg_recieve_id=(Integer)table.getValueAt(currSelectedRow, msg_recieve_id_col);
			System.out.println( ", sel_msg_recieve_id = "+sel_msg_recieve_id);
		}
		
		// ��Ȯ�� �޼��� �Ǽ�, ������ id
		sql.append(" select count(*) cnt, max(r.msg_recieve_id) max_msg_recieve_id \n");
		sql.append(" from recieve_message r \n");
		sql.append(" where r.msg_recv_user_id = ?  \n");
		sql.append(" and NVL(r.msg_confirm_flag,'N')='N'  \n");
		System.out.println("checkNewMsg - sql : "+sql.toString());
		try {
			pstmt = con.prepareStatement(sql.toString());
			pstmt.setString(1, userID);
			rs = pstmt.executeQuery();
			if (rs.next()){
				
				if (rs.getInt("cnt")!=0){
					la_new_msg_chk.setText("(�ű� �޼����� �����մϴ�.)");
					
					// ���� �̼��� id
					int max_msg_recieve_id = rs.getInt("max_msg_recieve_id");
					
					// ���� ��ȸ�� �� �߿� ���� �̼��� �޼����� �ִ��� üũ
					findRow=-1;
					rowCount = table.getRowCount();
					for (int i=0; i<rowCount; i++){
						System.out.println("row="+i+", col="+msg_recieve_id_col);
						msg_recieve_id =(Integer)table.getValueAt(i, msg_recieve_id_col);
						if (max_msg_recieve_id==msg_recieve_id){
							findRow=i;
							break;
						}
					}
					
					// ���� ��ȸ�� �� �߿� ���� �̼��� �޼����� ���� ���. ����ȸ
					if (findRow==-1){
						// ����ȸ
						search();
						
						// ���� ���õ� row �� �ִ� ���. ��ȸ �� �ش� row ã�Ƽ� ������ �ش�.
						if (currSelectedRow !=-1){
							findRow=-1;
							for (int i=0; i<table.getRowCount(); i++){
								chk_msg_recieve_id=(Integer)table.getValueAt(i, msg_recieve_id_col);
								if (chk_msg_recieve_id==sel_msg_recieve_id) {
									findRow = i;
									break;
								}
							}
							
							// ���õǾ��� id ���� ���� row �� ã�� ���. �ش� row �� ����
							if (findRow!=-1){
								System.out.println("findRow = "+findRow);
								table.setRowSelectionInterval(findRow, findRow);
								// showMessage();
							}
							
						}
					}
				} else {
					la_new_msg_chk.setText("");
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

	public void run() {
		while (threadFlag){			
			try {
				thread.sleep(1000);
				checkNewMsg();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		
	}
}
