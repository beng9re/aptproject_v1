package main;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRootPane;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.border.Border;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;

import Edit.InvEditPan;
import db.DBManager;
import dto.MenuDto;
import message.RecieveMessage;
import message.SendMessage;
import message.SendMessageList;
import viewer.Admin_InvoiceView;
import viewer.Admin_UserView;
import viewer.User;
import Edit.RetunPan;
import aptuser.ModifyAdmin;
import aptuser.ModifyUser;
import aptuser.RegistUser;
import chat.ChatClient;
import chat.ChatServer;
import complex.regist.ComplexPanel;

public class TreeMain extends JFrame implements TreeSelectionListener, ActionListener{
	
	DBManager  instance;
	Connection  con=null;
	
	int winWidth=900;
	int winHeight = 700;
	int treeScrollHeight=520;
	int westWidth=200;
	int centerWidth=700;
	int centerHeight=700;
	String userId="";
	String userName="";
	
	JTree  tree;
	JScrollPane  scroll;	
	JPanel  p_west, p_center, p_west_center, p_west_south;
	JLabel  la_welcom;
	JButton  bt_exit;
	
	DefaultMutableTreeNode  root;
	Vector<Object>  menuOpenList = new Vector<Object>();
	Vector<MenuDto> menuDtoList = new Vector<MenuDto>();
	Vector<JPanel> panelList = new Vector<JPanel>();
	
	public TreeMain(DBManager  instance, String userId) {

		this.instance = instance;
		this.con = instance.getConnection();
		this.userId = userId;
		
		p_west = new JPanel();
		p_west_center = new JPanel();
		p_west_south = new JPanel();
		p_center = new JPanel();
		
		root = new DefaultMutableTreeNode("Menu");	
		tree = new JTree(root);
		scroll = new JScrollPane(tree);
		
		la_welcom = new JLabel(" �� ȯ���մϴ�");
		bt_exit = new JButton("����");

		// root menu menuDtoList �� �߰�
		MenuDto menuDto = new MenuDto();
		menuDto.setMenu_id(0);
		menuDto.setMenu_name(root.getUserObject().toString());
		menuDtoList.add(menuDto);

		scroll.setBackground(Color.YELLOW);		
		
		// Size		
		scroll.setPreferredSize(new Dimension(westWidth, treeScrollHeight));
		p_west_center.setPreferredSize(new Dimension(westWidth, treeScrollHeight));
		//System.out.println(p_west_center.getHeight());
		p_west_south.setPreferredSize(new Dimension(westWidth, winHeight-treeScrollHeight-50));
		la_welcom.setPreferredSize(new Dimension(westWidth-10, 50));
		
		p_west_center.setLayout(new BorderLayout());
		p_west_center.add(scroll);
		
		p_west_south.setBackground(Color.WHITE);
		p_west_south.add(la_welcom);
		p_west_south.add(bt_exit);
		
		p_west.setLayout(new BorderLayout());
		p_west.add(p_west_center);
		p_west.add(p_west_south, BorderLayout.SOUTH);
		
		p_center.setLayout(new BorderLayout());
		
		add(p_west, BorderLayout.WEST);
		add(p_center);
		
		// Tree ���� �۾�
		makeTree();
		
		// ������ ����.
		tree.addTreeSelectionListener(this);
		bt_exit.addActionListener(this);
		
		setTitle("***** ȯ���մϴ� *****");
		setVisible(true);
		setSize(winWidth, winHeight);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		
		// �ʱ� �۾�
		init();
		
	}
	
	public void init(){
		PreparedStatement pstmt=null;
		ResultSet  rs=null;
		
		// Chat Server
		ChatServer server=  new ChatServer();

		// UserName ��ȸ
		String sql = "select aptuser_name from aptuser where aptuser_id = ? ";
		try {
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, userId);
			rs = pstmt.executeQuery();
			if (rs.next()){
				userName = rs.getString("aptuser_name");
			}
		} catch (SQLException e) {
			JOptionPane.showMessageDialog(this, "����ڸ� ��ȸ�� Error �߻� ");
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
		la_welcom.setText(userName+" �� ȯ���մϴ�");
	}
	
	public Connection getConnection(){
		return con;
	}
	
	public String getUserId(){
		return userId;
	}
	
	public void close(){
		
		instance.disConnect(con);
		
		System.exit(0);
	}
	
	public JTree getTree(){
		return tree;
	}
	
	// Tree ���� �۾�
	public void makeTree(){
		System.out.println("makeTree");
		
		PreparedStatement  pstmt=null;
		ResultSet  rs=null;
		PreparedStatement  pstmtSub=null;
		ResultSet  rsSub=null;
		
		// ���� �޴�
		StringBuffer  sql=new StringBuffer();
		sql.append(" select m.menu_id, m.menu_level, m.menu_up_level_id, m.menu_name \n");
		sql.append("         , m.menu_class_name, m.menu_type, m.order_seq, m.admin_role_flag \n");
		sql.append("         , m.user_role_flag, NVL(m.menu_use_flag,'Y') menu_use_flag \n");
		sql.append("         , (select count(*) from menulist s \n");
		sql.append("	           where  s.MENU_UP_LEVEL_ID = m.menu_id) subcnt \n");
		sql.append(" from   menulist m \n");
		sql.append(" where  m.menu_level = 1 \n");
		sql.append(" order by m.order_seq \n");
		//System.out.println("sql : "+sql.toString());
		
		// ���� �޴�
		StringBuffer  sqlSub=new StringBuffer();
		sqlSub.append(" select m.menu_id, m.menu_level, m.menu_up_level_id, m.menu_name \n");
		sqlSub.append("         , m.menu_class_name, m.menu_type, m.order_seq, m.admin_role_flag \n");
		sqlSub.append("         , m.user_role_flag, NVL(m.menu_use_flag,'Y') menu_use_flag \n");
		sqlSub.append(" from   menulist m \n");
		sqlSub.append(" where m.menu_up_level_id = ? \n" );
		sqlSub.append(" order by m.order_seq \n");
		//System.out.println("sqlSub : "+sqlSub.toString());
		
		try {
			pstmt = con.prepareStatement(sql.toString());
			rs = pstmt.executeQuery();
			//System.out.println("rs = "+rs);
			
			// Menu ����
			while (rs.next()){
				String menu = rs.getString("menu_name");
				
				DefaultMutableTreeNode  node=null;
				node = new DefaultMutableTreeNode(menu);
				
				root.add(node);
				
				// node �� menuDtoList �� �߰�
				MenuDto menuDto = new MenuDto();
				menuDto.setMenu_id(rs.getInt("menu_id"));
				menuDto.setMenu_level(rs.getInt("menu_level"));
				menuDto.setMenu_up_level_id(rs.getInt("menu_up_level_id"));
				menuDto.setMenu_name(rs.getString("menu_name"));
				menuDto.setMenu_class_name(rs.getString("menu_class_name"));
				menuDto.setMenu_type(rs.getString("menu_type"));
				menuDto.setOrder_seq(rs.getInt("order_seq"));
				menuDto.setAdmin_role_flag(rs.getString("admin_role_flag"));
				menuDto.setUser_role_flag(rs.getString("user_role_flag"));
				menuDto.setMenu_use_flag(rs.getString("menu_use_flag"));		
				menuDtoList.add(menuDto);
				
				
				// SubMenu ����
				if (rs.getInt("subcnt")!=0){
					pstmtSub = con.prepareStatement(sqlSub.toString());
					//System.out.println("menu_id="+rs.getInt("menu_id"));
					pstmtSub.setInt(1, rs.getInt("menu_id"));
					rsSub = pstmtSub.executeQuery();
					//System.out.println("rsSub = "+rsSub);
					
					while (rsSub.next()){
						// Menu node �߰�
						DefaultMutableTreeNode  nodeSub=null;
						nodeSub = new DefaultMutableTreeNode(rsSub.getString("menu_name"));
						node.add(nodeSub);
						
						// node �� menuDtoList �� �߰�
						MenuDto menuSubDto = new MenuDto();
						menuSubDto.setMenu_id(rsSub.getInt("menu_id"));
						menuSubDto.setMenu_level(rsSub.getInt("menu_level"));
						menuSubDto.setMenu_up_level_id(rsSub.getInt("menu_up_level_id"));
						menuSubDto.setMenu_name(rsSub.getString("menu_name"));
						menuSubDto.setMenu_class_name(rsSub.getString("menu_class_name"));
						menuSubDto.setMenu_type(rsSub.getString("menu_type"));
						menuSubDto.setOrder_seq(rsSub.getInt("order_seq"));
						menuSubDto.setAdmin_role_flag(rsSub.getString("admin_role_flag"));
						menuSubDto.setUser_role_flag(rsSub.getString("user_role_flag"));
						menuSubDto.setMenu_use_flag(rsSub.getString("menu_use_flag"));		
						menuDtoList.add(menuSubDto);
						//System.out.println("getMenu_name="+menuSubDto.getMenu_name());
					}
				}
				
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (rsSub!=null)
				try {
					rsSub.close();
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
			if (pstmtSub!=null)
				try {
					pstmtSub.close();
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
		}		
		
		// tree ��� ��ġ��
		int r=0;
		while (r < tree.getRowCount()){
			tree.expandRow(r);
			r++;
		}
		
	}

	// Tree ���ý� ����
	public void valueChanged(TreeSelectionEvent e) {
		//System.out.println("tree valueChanged");
		Object obj=e.getSource();
		if (obj==tree){
			openWindowOnTree();
		}
	}
	
	public void removeMenuOpenList(Object openObject){
		this.menuOpenList.remove(openObject);
	}

	// Tree ���� �޴� ���ý� �ش� ȭ�� Open
	public void openWindowOnTree(){
		//System.out.println("openWindowOnTree");
		// Title �ʱ�ȭ
		this.setTitle("");
		
		// ��ϵ� Panel ���  Visible=false
		for (int i=0; i<panelList.size(); i++){
			panelList.get(i).setVisible(false);
			System.out.println(i+" : visible false");
		}
		
		// ���õ� �޴� node check
		DefaultMutableTreeNode  node = (DefaultMutableTreeNode)tree.getLastSelectedPathComponent();
		
		// �޴���
		String menuName=node.getUserObject().toString();
		//System.out.println("node : "+menuName);		
		
		// menuDtoList ���� �ش� �޴��� index üũ
		int dtoIndex=-1;
		for (int i=0; i<menuDtoList.size();i++){
			if (menuDtoList.get(i).getMenu_name().equals(menuName)){
				dtoIndex=i;
				break;
			}
		}
		// dto �� ���� ��� skip
		System.out.println("dtoIndex="+dtoIndex + ", menuName = "+menuName);
		if (dtoIndex==-1) return;
		
		// menuDto ���� �ش� �޴��� className ��������
		String className = menuDtoList.get(dtoIndex).getMenu_class_name();
		//System.out.println("className = "+className);
		
		// className �� ���� ���, ���� �޴��� �ƴϹǷ� (���� �޴� �̹Ƿ�) skip
		if (className==null){ 
			JOptionPane.showMessageDialog(this, "ȭ���� Class �� ��ϵ��� �ʾҽ��ϴ�.");
			return;
		}

		// menuType �� Frame ���� Panel ���� üũ�Ͽ� open 
		String menuType = menuDtoList.get(dtoIndex).getMenu_type();
		
		//System.out.println("menuName="+menuName+", className="+className+", menuType="+menuType);

		if (menuType.equalsIgnoreCase("F")){
			// Frame Open
			frameOpen(className);
		} else {
			// Panel Open
			panelOpen(className, menuName);
		}
		
	}
	
	// className ���� �����ִ� ȭ�� index üũ
	public int findOpenClassIndex(String className){
		int index=-1;
		for (int i=0; i<menuOpenList.size();i++){
			//System.out.println(menuOpenList.get(i).getClass().getSimpleName());
			if (menuOpenList.get(i).getClass().getSimpleName().equals(className)){
				index=i;
				break;
			}
		}
		//System.out.println(className + " index = "+index);
		return index;
	}
	
	// Frame menu open
	public void frameOpen(String className){
		//System.out.println("frameOpen");
		// �̹� ���� �ִ� menu ��ġ üũ
		int index=findOpenClassIndex(className);
		
		if (index !=-1){
			// �̹� open �Ǿ� ������ �� ������ �����ش�.
			((JFrame)menuOpenList.get(index)).toFront();
		} else {
			// open �Ǳ� ���̸�, new �Ͽ� open �ϰ�, menuOpenList �� �߰��Ѵ�.
			if (className.equalsIgnoreCase("ChatClient")){
				// ä��
				System.out.println("ChatClient -------------------");
				ChatClient  chatClient = new ChatClient();
				menuOpenList.add(chatClient);
				
			} else if (className.equalsIgnoreCase("SendMessage")){  
				// ���� ������
				SendMessage  send = new SendMessage(this);
				menuOpenList.add(send);
				
			} else if (className.equalsIgnoreCase("SendMessageList")){  
				// ���� �۽���
				SendMessageList sendMsgList = new SendMessageList(this);
				menuOpenList.add(sendMsgList);
				
			} else if (className.equalsIgnoreCase("RecieveMessage")){  
				// ���� ������
				RecieveMessage  recm = new RecieveMessage(this);
				menuOpenList.add(recm);				
			}			
		}
		
	}
	
	// Panel menu open
	public void panelOpen(String className, String menuName){		
		
		// p_center ������
		setTitle("");
		p_center.updateUI();
		
		//System.out.println("panelOpen");
		JPanel  curPanel = new JPanel();
		
		// �̹� ���� �ִ� menu ��ġ üũ
		int index=findOpenClassIndex(className);
		System.out.println(menuName + ", "+className + ", open index = "+index);
				
	    // ���� ���� ���� ���, �ش� Panel �� new �Ѵ�.
	    if (index==-1){
	    	
	    	if (className.equalsIgnoreCase("InvEditPan")){
	    		// ������
	    		InvEditPan invEditPan = new InvEditPan();
	    		curPanel = invEditPan;				
	    	} else if (className.equalsIgnoreCase("Admin_InvoiceView")){
	    		// �����ڹ�ǰ���
	    		Admin_InvoiceView adminInvoice = new Admin_InvoiceView();
	    		System.out.println("adminInvoice = "+adminInvoice);
	    		curPanel = adminInvoice;	
	    	} else if (className.equalsIgnoreCase("RetunPan")){
	    		// �ݼ۵��
	    		RetunPan returnPan = new RetunPan();
	    		curPanel = returnPan;	
	    	} else if (className.equalsIgnoreCase("User")){
	    		// ����ڹ�ǰ���
	    		User userPan = new User();
	    		curPanel = userPan;	
	    	} else if (className.equalsIgnoreCase("RegistUser")){
	    		// ȸ�����
	    		RegistUser registUser = new RegistUser();
	    		curPanel = registUser;	
	    	} else if (className.equalsIgnoreCase("Admin_UserView")){
	    		// ȸ�����
	    		Admin_UserView adminUserView = new Admin_UserView();
	    		curPanel = adminUserView;	
	    	} else if (className.equalsIgnoreCase("ModifyAdmin")){
	    		// ��������������
	    		ModifyAdmin modifyAdmin = new ModifyAdmin();
	    		curPanel = modifyAdmin;	
	    	} else if (className.equalsIgnoreCase("ModifyUser")){
	    		// ȸ����������
	    		ModifyUser midifyUser = new ModifyUser();
	    		curPanel = midifyUser;	
	    	} else if (className.equalsIgnoreCase("ComplexPanel")){
	    		// ��ȣ�� ���
	    		ComplexPanel complexPanel = new ComplexPanel();
	    		curPanel = complexPanel;	
	    	} else {
	    		System.out.println("menu ����.");
	    		curPanel=null;
	    	}
	    	
	    	if (curPanel!=null){
		    	p_center.add(curPanel);
		    	
		    	// ������ Panel �� menuOpenList �� �߰�
		    	menuOpenList.add(curPanel);
		    	
		    	// ������ Panel �� panelList �� �߰�
				panelList.add(curPanel);
	    	}
				
			// menuOpenList �� �߰� �Ǿ����Ƿ�, ���� index �� üũ
	    	index = findOpenClassIndex(className);
	    }
	    
	    System.out.println("get index = "+index);
	    //System.out.println("panelList.size() = "+panelList.size());
	    
	    // index �� -1 �� �ƴ� ���, �� Panel �� �����ϴ� ��� �ش� Panel visible=true
	    if (index!=-1){
	    	for (int i=0; i<panelList.size(); i++){
	    		if (panelList.get(i)==menuOpenList.get(index)){
	    			// panel ������ p_center �� ������� �����
					panelList.get(i).setPreferredSize(new Dimension(centerWidth, centerHeight));
					// panel ���̱�
					panelList.get(i).setVisible(true);
					// Title ����
					setTitle(menuName);
	    		}
	    	}
	    } else {
	    	JOptionPane.showMessageDialog(this, "ȭ���� �������� �ʽ��ϴ�.");
	    }

	}

	public void actionPerformed(ActionEvent e) {
		Object obj = e.getSource();
		if (obj==bt_exit){
			close();
		}
	}
/*
	public static void main(String[] args) {
		new TreeMain(con);

	}
*/
}
