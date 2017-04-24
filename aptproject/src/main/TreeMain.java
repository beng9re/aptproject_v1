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
		
		la_welcom = new JLabel(" 님 환영합니다");
		bt_exit = new JButton("종료");

		// root menu menuDtoList 에 추가
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
		
		// Tree 구성 작업
		makeTree();
		
		// 리스너 연결.
		tree.addTreeSelectionListener(this);
		bt_exit.addActionListener(this);
		
		setTitle("***** 환영합니다 *****");
		setVisible(true);
		setSize(winWidth, winHeight);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		
		// 초기 작업
		init();
		
	}
	
	public void init(){
		PreparedStatement pstmt=null;
		ResultSet  rs=null;
		
		// Chat Server
		ChatServer server=  new ChatServer();

		// UserName 조회
		String sql = "select aptuser_name from aptuser where aptuser_id = ? ";
		try {
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, userId);
			rs = pstmt.executeQuery();
			if (rs.next()){
				userName = rs.getString("aptuser_name");
			}
		} catch (SQLException e) {
			JOptionPane.showMessageDialog(this, "사용자명 조회시 Error 발생 ");
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
		la_welcom.setText(userName+" 님 환영합니다");
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
	
	// Tree 구성 작업
	public void makeTree(){
		System.out.println("makeTree");
		
		PreparedStatement  pstmt=null;
		ResultSet  rs=null;
		PreparedStatement  pstmtSub=null;
		ResultSet  rsSub=null;
		
		// 상위 메뉴
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
		
		// 하위 메뉴
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
			
			// Menu 생성
			while (rs.next()){
				String menu = rs.getString("menu_name");
				
				DefaultMutableTreeNode  node=null;
				node = new DefaultMutableTreeNode(menu);
				
				root.add(node);
				
				// node 를 menuDtoList 에 추가
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
				
				
				// SubMenu 생성
				if (rs.getInt("subcnt")!=0){
					pstmtSub = con.prepareStatement(sqlSub.toString());
					//System.out.println("menu_id="+rs.getInt("menu_id"));
					pstmtSub.setInt(1, rs.getInt("menu_id"));
					rsSub = pstmtSub.executeQuery();
					//System.out.println("rsSub = "+rsSub);
					
					while (rsSub.next()){
						// Menu node 추가
						DefaultMutableTreeNode  nodeSub=null;
						nodeSub = new DefaultMutableTreeNode(rsSub.getString("menu_name"));
						node.add(nodeSub);
						
						// node 를 menuDtoList 에 추가
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
		
		// tree 모두 펼치기
		int r=0;
		while (r < tree.getRowCount()){
			tree.expandRow(r);
			r++;
		}
		
	}

	// Tree 선택시 수행
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

	// Tree 에서 메뉴 선택시 해당 화면 Open
	public void openWindowOnTree(){
		//System.out.println("openWindowOnTree");
		// Title 초기화
		this.setTitle("");
		
		// 등록된 Panel 모두  Visible=false
		for (int i=0; i<panelList.size(); i++){
			panelList.get(i).setVisible(false);
			System.out.println(i+" : visible false");
		}
		
		// 선택된 메뉴 node check
		DefaultMutableTreeNode  node = (DefaultMutableTreeNode)tree.getLastSelectedPathComponent();
		
		// 메뉴명
		String menuName=node.getUserObject().toString();
		//System.out.println("node : "+menuName);		
		
		// menuDtoList 에서 해당 메뉴의 index 체크
		int dtoIndex=-1;
		for (int i=0; i<menuDtoList.size();i++){
			if (menuDtoList.get(i).getMenu_name().equals(menuName)){
				dtoIndex=i;
				break;
			}
		}
		// dto 에 없는 경우 skip
		System.out.println("dtoIndex="+dtoIndex + ", menuName = "+menuName);
		if (dtoIndex==-1) return;
		
		// menuDto 에서 해당 메뉴의 className 가져오기
		String className = menuDtoList.get(dtoIndex).getMenu_class_name();
		//System.out.println("className = "+className);
		
		// className 이 없는 경우, 최종 메뉴가 아니므로 (상위 메뉴 이므로) skip
		if (className==null){ 
			JOptionPane.showMessageDialog(this, "화면의 Class 가 등록되지 않았습니다.");
			return;
		}

		// menuType 이 Frame 인지 Panel 인지 체크하여 open 
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
	
	// className 으로 열려있는 화면 index 체크
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
		// 이미 열려 있는 menu 인치 체크
		int index=findOpenClassIndex(className);
		
		if (index !=-1){
			// 이미 open 되어 있으면 맨 앞으로 보여준다.
			((JFrame)menuOpenList.get(index)).toFront();
		} else {
			// open 되기 전이면, new 하여 open 하고, menuOpenList 에 추가한다.
			if (className.equalsIgnoreCase("ChatClient")){
				// 채팅
				System.out.println("ChatClient -------------------");
				ChatClient  chatClient = new ChatClient();
				menuOpenList.add(chatClient);
				
			} else if (className.equalsIgnoreCase("SendMessage")){  
				// 쪽지 보내기
				SendMessage  send = new SendMessage(this);
				menuOpenList.add(send);
				
			} else if (className.equalsIgnoreCase("SendMessageList")){  
				// 쪽지 송신함
				SendMessageList sendMsgList = new SendMessageList(this);
				menuOpenList.add(sendMsgList);
				
			} else if (className.equalsIgnoreCase("RecieveMessage")){  
				// 쪽지 수신함
				RecieveMessage  recm = new RecieveMessage(this);
				menuOpenList.add(recm);				
			}			
		}
		
	}
	
	// Panel menu open
	public void panelOpen(String className, String menuName){		
		
		// p_center 재정비
		setTitle("");
		p_center.updateUI();
		
		//System.out.println("panelOpen");
		JPanel  curPanel = new JPanel();
		
		// 이미 열려 있는 menu 인치 체크
		int index=findOpenClassIndex(className);
		System.out.println(menuName + ", "+className + ", open index = "+index);
				
	    // 열려 있지 않은 경우, 해당 Panel 을 new 한다.
	    if (index==-1){
	    	
	    	if (className.equalsIgnoreCase("InvEditPan")){
	    		// 송장등록
	    		InvEditPan invEditPan = new InvEditPan();
	    		curPanel = invEditPan;				
	    	} else if (className.equalsIgnoreCase("Admin_InvoiceView")){
	    		// 관리자물품목록
	    		Admin_InvoiceView adminInvoice = new Admin_InvoiceView();
	    		System.out.println("adminInvoice = "+adminInvoice);
	    		curPanel = adminInvoice;	
	    	} else if (className.equalsIgnoreCase("RetunPan")){
	    		// 반송등록
	    		RetunPan returnPan = new RetunPan();
	    		curPanel = returnPan;	
	    	} else if (className.equalsIgnoreCase("User")){
	    		// 사용자물품목록
	    		User userPan = new User();
	    		curPanel = userPan;	
	    	} else if (className.equalsIgnoreCase("RegistUser")){
	    		// 회원등록
	    		RegistUser registUser = new RegistUser();
	    		curPanel = registUser;	
	    	} else if (className.equalsIgnoreCase("Admin_UserView")){
	    		// 회원목록
	    		Admin_UserView adminUserView = new Admin_UserView();
	    		curPanel = adminUserView;	
	    	} else if (className.equalsIgnoreCase("ModifyAdmin")){
	    		// 관리자정보수정
	    		ModifyAdmin modifyAdmin = new ModifyAdmin();
	    		curPanel = modifyAdmin;	
	    	} else if (className.equalsIgnoreCase("ModifyUser")){
	    		// 회원정보수정
	    		ModifyUser midifyUser = new ModifyUser();
	    		curPanel = midifyUser;	
	    	} else if (className.equalsIgnoreCase("ComplexPanel")){
	    		// 동호수 등록
	    		ComplexPanel complexPanel = new ComplexPanel();
	    		curPanel = complexPanel;	
	    	} else {
	    		System.out.println("menu 없음.");
	    		curPanel=null;
	    	}
	    	
	    	if (curPanel!=null){
		    	p_center.add(curPanel);
		    	
		    	// 생성된 Panel 을 menuOpenList 에 추가
		    	menuOpenList.add(curPanel);
		    	
		    	// 생성된 Panel 을 panelList 에 추가
				panelList.add(curPanel);
	    	}
				
			// menuOpenList 에 추가 되었으므로, 최종 index 로 체크
	    	index = findOpenClassIndex(className);
	    }
	    
	    System.out.println("get index = "+index);
	    //System.out.println("panelList.size() = "+panelList.size());
	    
	    // index 가 -1 이 아닌 경우, 즉 Panel 이 존재하는 경우 해당 Panel visible=true
	    if (index!=-1){
	    	for (int i=0; i<panelList.size(); i++){
	    		if (panelList.get(i)==menuOpenList.get(index)){
	    			// panel 사이즈 p_center 의 사이즈로 만들기
					panelList.get(i).setPreferredSize(new Dimension(centerWidth, centerHeight));
					// panel 보이기
					panelList.get(i).setVisible(true);
					// Title 변경
					setTitle(menuName);
	    		}
	    	}
	    } else {
	    	JOptionPane.showMessageDialog(this, "화면이 존재하지 않습니다.");
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
