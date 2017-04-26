package main;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;

import Edit.InvEditPan;
import Edit.RetunPan;
import aptuser.ModifyAdmin;
import aptuser.ModifyUser;
import aptuser.RegistUser;
import chat.ChatClient;
import chat.ChatServer;
import complex.regist.ComplexPanel;
import db.AptuserModelByID;
import db.DBManager;
import dto.Aptuser;
import dto.MenuDto;
import message.MessageAutoInsertThread;
import message.RecieveMessage;
import message.SendMessage;
import message.SendMessageList;
import viewer.Admin_InvoiceView;
import viewer.Admin_UserView;
import viewer.User;

public class TreeMain extends JFrame implements TreeSelectionListener, ActionListener{
	
	private DBManager  instance;
	private Connection  con;
	
	int winWidth=900;
	int winHeight = 700;
	int treeScrollHeight=520;
	int westWidth=200;
	int centerWidth=700;
	int centerHeight=700;
	
	private String userID;
	private String userName;
	private String adminUserID;
	private boolean  adminFlag=false;
	private boolean  adminMenuFlag=false;
	private String serverIP;
	private ChatServer chatSrv;
	private ChatClient chatClient;
	private RecieveMessage recieveMessage;
	private SendMessageList  sendMessageList;
	private MessageAutoInsertThread  msgAutoInsertThread;
	
	JTree  tree;
	JScrollPane  scroll;	
	JPanel  p_west, p_center, p_west_center, p_west_south;
	JLabel  la_welcom;
	JButton  bt_exit;
	
	Object currObject=new Object();
	
	DefaultMutableTreeNode  root;
	Vector<Object>  menuOpenList = new Vector<Object>();
	Vector<MenuDto> menuDtoList = new Vector<MenuDto>();
	Vector<JPanel> panelList = new Vector<JPanel>();
	ArrayList<Aptuser> userList;
	
	public TreeMain(DBManager  instance, String userID) {

		this.instance = instance;
		this.con = instance.getConnection();
		this.userID = userID;
		
		p_west = new JPanel();
		p_west_center = new JPanel();
		p_west_south = new JPanel();
		p_center = new JPanel();
		
		root = new DefaultMutableTreeNode("Menu");	
		tree = new JTree(root);
		scroll = new JScrollPane(tree);
		
		la_welcom = new JLabel("");
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
		bt_exit.setPreferredSize(new Dimension(100, 30));
		la_welcom.setPreferredSize(new Dimension(westWidth-10, 50));
		
		// la_welcom text Bold, 가운데 정렬
		la_welcom.setFont(new Font("Default", Font.BOLD, 15));
		la_welcom.setHorizontalAlignment(JLabel.CENTER);
		la_welcom.setForeground(Color.BLUE);
		
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
		
		// 리스너 연결.
		tree.addTreeSelectionListener(this);
		bt_exit.addActionListener(this);
		// 프로그램 종료를 위한 윈도우 리스너
		this.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				close();
			}
		});
		
		setTitle("***** 환영합니다 *****");
		setVisible(true);
		setSize(winWidth, winHeight);
		setResizable(false);
		setLocationRelativeTo(null);
		
		// 초기 작업
		init();
		
	}
	
	public void init(){
		/* --------------- Chat 관련 Start -------------------------------------- */
		
		// admin User ///////////////////////////
		// aptuser테이블에서 데이터를 갖고오는 모델을 생성한다
		AptuserModelByID aptuser = new AptuserModelByID(con, "admin");
		userList = aptuser.getData();
		
		// 관리자 IP를 가지고 온다 (채팅 클라이언트에서 서버에 접속할 때 사용)
		serverIP = ((Aptuser)aptuser.getData().get(0)).getAptuser_ip();
		//System.out.println("serverIP = "+serverIP);
		
		// adminUserID 받아 놓는다.
		adminUserID = ((Aptuser)aptuser.getData().get(0)).getAptuser_id();		
		
		// 현재 User ////////////////////////////////////////////////////////////
		// UserName 조회
		aptuser.selectData(userID);
		userName = userList.get(0).getAptuser_name();
		
		// userID 가 admin 인 경우 adminFlag 부여
		if (userID.equalsIgnoreCase("admin")){
			adminFlag=true;
		}
		
		// 접속한 회원의 권한을 조회하고 Main의 권한변수에 대입한다
		// 접속한 유저의 Aptuser_perm=9 인 경우, admin Menu 권한 지정.
		if (userList.get(0).getAptuser_perm()==9){
			adminMenuFlag = true;
		}
		System.out.println("serverIP = "+serverIP);

		/* --------------- Chat 관련 End -------------------------------------- */
		
		//System.out.println("adminFlag="+adminFlag);
		la_welcom.setText(userName+" 님 환영합니다");
		
		// Tree 구성 작업
		makeTree();
		
		// (송장, 반송 체크하여 Message Insert 하는 Thread 시동)
		//msgAutoInsertThread = new MessageAutoInsertThread(this);
		//msgAutoInsertThread.setThreadFlag(true);
	}
	
	// get Connection 
	public Connection getConnection(){
		return con;
	}
	
	// get UserId
	public String getUserID(){
		return userID;
	}
	
	// get ServrIP
	public String getSeverIP() {
		return serverIP;
	}
	
	public boolean getAdminFlag(){
		return adminFlag;
	}
	
	public String getAdminUserID(){
		return adminUserID;
	}
	
	// 프로그램 종료를 위한 메서드
	public void close(){
		// 채팅 Server 종료
		if (chatSrv!=null) {
			chatSrv.close(); 
		}
		
		// 채팅 Client 종료
		for (Object obj : menuOpenList) {
			if (obj == chatClient) {
				chatClient.getThread().disconnect();
			} else if (obj==recieveMessage){
				// 수신 메세지 Thread 종료
				recieveMessage.setThreadFlag(false);
			} else if (obj==sendMessageList){
				// 송신 메세지 List Thread 종료
				sendMessageList.setThreadFlag(false);
			} else if (obj==msgAutoInsertThread){
				// Message 
				msgAutoInsertThread.setThreadFlag(false);
			} else {
			}
		}
		
		// Connection 종료
		instance.disConnect(con);
		
		// Window Close
		System.exit(0);
	}

	// Tree 구성 작업
	public void makeTree(){
		//System.out.println("makeTree");
		
		PreparedStatement  pstmt=null;
		ResultSet  rs=null;
		PreparedStatement  pstmtSub=null;
		ResultSet  rsSub=null;
		
		// 상위 메뉴
		StringBuffer  sql=new StringBuffer();
		sql.append(" select m.menu_id, m.menu_level, m.menu_up_level_id, m.menu_name \n");
		sql.append("         , m.menu_class_name, m.menu_type, m.order_seq \n");
		sql.append("         , nvl(m.admin_role_flag,'N') admin_role_flag, nvl(m.user_role_flag,'N') user_role_flag \n");
		sql.append("         , nvl(m.menu_use_flag,'Y') menu_use_flag \n");
		sql.append("         , (select count(*) from menulist s \n");
		sql.append("	           where  s.MENU_UP_LEVEL_ID = m.menu_id \n");
		sql.append("             and     nvl(s.menu_use_flag,'Y')='Y') subcnt \n");
		sql.append(" from   menulist m \n");
		sql.append(" where  m.menu_level = 1 \n");
		sql.append(" and      nvl(m.menu_use_flag,'Y') = 'Y' ");
		sql.append(" order by m.order_seq \n");
		//System.out.println("sql : "+sql.toString());
		
		// 하위 메뉴
		StringBuffer  sqlSub=new StringBuffer();
		sqlSub.append(" select m.menu_id, m.menu_level, m.menu_up_level_id, m.menu_name \n");
		sqlSub.append("         , m.menu_class_name, m.menu_type, m.order_seq \n");
		sqlSub.append("         , nvl(m.admin_role_flag,'N') admin_role_flag, nvl(m.user_role_flag,'N') user_role_flag \n");
		sqlSub.append("         , nvl(m.menu_use_flag,'Y') menu_use_flag \n");
		sqlSub.append(" from   menulist m \n");
		sqlSub.append(" where m.menu_up_level_id = ? \n" );
		sqlSub.append(" order by m.order_seq \n");
		//System.out.println("sqlSub : "+sqlSub.toString());
		
		try {
			pstmt = con.prepareStatement(sql.toString());
			rs = pstmt.executeQuery();
			
			// Menu 생성
			while (rs.next()){
				String menu = rs.getString("menu_name");
				
				DefaultMutableTreeNode  node=null;
				node = new DefaultMutableTreeNode(menu);
				
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
				
				// system user 인 경우,
				// Admin 유저이고 admin 권한 화면인 경우, 
				// 또는 admin 유저가 아니고, 유저 권한이 있는 화면 인 경우. 메뉴 추가
				if ( (adminMenuFlag==true && rs.getString("admin_role_flag").equalsIgnoreCase("Y")) ||
					 (adminMenuFlag==false && rs.getString("user_role_flag").equalsIgnoreCase("Y"))	) {
					
					root.add(node);
					menuDtoList.add(menuDto);				
				}
				
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

						// Admin 유저이고 admin 권한 화면인 경우, 또는 admin 유저가 아니고, 유저 권한이 있는 화면 인 경우. 메뉴 추가
						if ( (adminMenuFlag==true && rsSub.getString("admin_role_flag").equalsIgnoreCase("Y")) ||
							 (adminMenuFlag==false && rsSub.getString("user_role_flag").equalsIgnoreCase("Y"))	) {
							
							node.add(nodeSub);
							menuDtoList.add(menuSubDto);
						}
						
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
		
		// 초기 선택 화면 지정
		String firstMenuClasssName="";
		if (adminMenuFlag){ // 관리자
			firstMenuClasssName = "Admin_InvoiceView";  // 관리자물품목록			
		} else {
			firstMenuClasssName = "User"; // 사용자물품목록
		}
		
		// 초기화면 TreeIndex 찾기
		int defaulTreeIndex=-1;
		int chatServerIndex=-1;
		String chkClassName="";
		for (int i=0; i<menuDtoList.size(); i++){
			chkClassName = menuDtoList.get(i).getMenu_class_name();
			if (chkClassName!=null){
				// 최초 open 화면의 index Check
				//System.out.println("chkClassName = "+chkClassName);
				if (chkClassName.equalsIgnoreCase(firstMenuClasssName)){
					defaulTreeIndex = i;
					//System.out.println("defaultSelectRow = "+defaulTreeIndex);
				}
				
				// admin 일때, ChatServer 의 index Check				
				if (adminFlag==true && chkClassName.equalsIgnoreCase("ChatServer")){
					chatServerIndex = i;
					//System.out.println("chatServerIndex = "+chatServerIndex);
				}
			}
		}
		
		// 서버관리자(admin)인 경우 Chat Server 생성
		if (chatServerIndex != -1){
			tree.setSelectionInterval(chatServerIndex, chatServerIndex);
		}
		
		// 초기 선택 화면이 있는 경우, 그 화면 선택
		if (defaulTreeIndex!=-1){
			//tree.clearSelection();
			tree.setSelectionInterval(defaulTreeIndex, defaulTreeIndex);
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
		//System.out.println("dtoIndex="+dtoIndex + ", menuName = "+menuName);
		if (dtoIndex==-1) return;
		
		// menuDto 에서 해당 메뉴의 className 가져오기
		String className = menuDtoList.get(dtoIndex).getMenu_class_name();
		//System.out.println("className = "+className);
		
		// className 이 없는 경우, 최종 메뉴가 아니므로 (상위 메뉴 이므로) skip
		if (className==null){ 
			JOptionPane.showMessageDialog(this, menuName+" 화면의 Class 가 등록되지 않았습니다.");
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
		
		// Frame 이 열릴 위치
		int openFrameX = this.getX()+westWidth+20;
		int openFramY = this.getY()+40;
		
		//System.out.println("frameOpen");
		// 이미 열려 있는 menu 인치 체크
		int index=findOpenClassIndex(className);
		
		if (index !=-1){
			// 이미 open 되어 있으면 맨 앞으로 보여준다.
			((JFrame)menuOpenList.get(index)).toFront();
		} else {
			// open 되기 전이면, new 하여 open 하고, menuOpenList 에 추가한다.
			currObject=null;
			if (className.equalsIgnoreCase("ChatClient")){
				// 채팅
				//System.out.println("ChatClient -------------------");
				chatClient = new ChatClient(this);
				menuOpenList.add(chatClient);
				currObject=chatClient;
				
			} else if (className.equalsIgnoreCase("SendMessage")){  
				// 쪽지 보내기
				SendMessage  send = new SendMessage(this);
				menuOpenList.add(send);
				currObject=send;
				
			} else if (className.equalsIgnoreCase("SendMessageList")){  
				// 쪽지 송신함
				SendMessageList sendMsgList = new SendMessageList(this);
				menuOpenList.add(sendMsgList);
				currObject=sendMsgList;
				
			} else if (className.equalsIgnoreCase("RecieveMessage")){  
				// 쪽지 수신함
				recieveMessage = new RecieveMessage(this);
				menuOpenList.add(recieveMessage);		
				currObject=recieveMessage;
			}
			
			// 추가된 Frame 의 위치를 현재 treeFrame 의 위치에 맞게 조정한다.
			if (currObject!=null){
				((JFrame)currObject).setLocation(openFrameX, openFramY);
			}
			
		}
		
	}
	
	// Panel menu open
	public void panelOpen(String className, String menuName){		
		
		// 등록된 Panel 모두  Visible=false
		for (int i=0; i<panelList.size(); i++){
			panelList.get(i).setVisible(false);
			//System.out.println(i+" : visible false");
		}
		
		// p_center 재정비
		setTitle("");
		p_center.updateUI();
		
		//System.out.println("panelOpen");
		JPanel  curPanel = new JPanel();
		
		// 이미 열려 있는 menu 인치 체크
		int index=findOpenClassIndex(className);
		//System.out.println(menuName + ", "+className + ", open index = "+index);
				
	    // 열려 있지 않은 경우, 해당 Panel 을 new 한다.
	    if (index==-1){
	    	
	    	if (className.equalsIgnoreCase("InvEditPan")){
	    		// 송장등록
	    		InvEditPan invEditPan = new InvEditPan(con);
	    		curPanel = invEditPan;				
	    	} else if (className.equalsIgnoreCase("Admin_InvoiceView")){
	    		// 관리자물품목록
	    		Admin_InvoiceView adminInvoice = new Admin_InvoiceView(con);
	    		//System.out.println("adminInvoice = "+adminInvoice);
	    		curPanel = adminInvoice;	
	    	} else if (className.equalsIgnoreCase("RetunPan")){
	    		// 반송등록
	    		RetunPan returnPan = new RetunPan(con,userList);
	    		curPanel = returnPan;	
	    	} else if (className.equalsIgnoreCase("User")){
	    		// 사용자물품목록
	    		User userPan = new User(userList, con);
	    		curPanel = userPan;	
	    	} else if (className.equalsIgnoreCase("RegistUser")){
	    		// 회원등록
	    		RegistUser registUser = new RegistUser(con, userID);
	    		curPanel = registUser;	
	    	} else if (className.equalsIgnoreCase("Admin_UserView")){
	    		// 회원목록
	    		Admin_UserView adminUserView = new Admin_UserView(con);
	    		curPanel = adminUserView;	
	    	} else if (className.equalsIgnoreCase("ModifyAdmin")){
	    		// 관리자정보수정
	    		ModifyAdmin modifyAdmin = new ModifyAdmin(con, userID);
	    		curPanel = modifyAdmin;	
	    	} else if (className.equalsIgnoreCase("ModifyUser")){
	    		// 회원정보수정
	    		ModifyUser midifyUser = new ModifyUser(con, userID);
	    		curPanel = midifyUser;	
	    	} else if (className.equalsIgnoreCase("ComplexPanel")){
	    		// 동호수 등록
	    		ComplexPanel complexPanel = new ComplexPanel(con);
	    		curPanel = complexPanel;	
	    	} else if (className.equalsIgnoreCase("ChatServer")){
	    		// 채팅(서버)
	    		chatSrv = new ChatServer(this);
	    		curPanel = chatSrv;	
	    	} else {
	    		//System.out.println("menu 없음.");
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
	    
	    //System.out.println("get index = "+index);
	    //System.out.println("panelList.size() = "+panelList.size());
	    
	    // index 가 -1 이 아닌 경우, 즉 Panel 이 존재하는 경우 해당 Panel visible=true
	    if (index!=-1){
	    	for (int i=0; i<panelList.size(); i++){
	    		if (panelList.get(i)==menuOpenList.get(index)){
	    			// panel 사이즈 p_center 의 사이즈로 만들기
					//panelList.get(i).setPreferredSize(new Dimension(centerWidth, centerHeight));
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

//	TreeMain은 단독 실행되지 않고 login에서 생성됨
/*
	public static void main(String[] args) {
		new TreeMain(con);

	}
*/
}
