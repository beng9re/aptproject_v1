package tree;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
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
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;

import Edit.InvEditPan;
import db.DBManager;
import dto.MenuDto;
import message.RecieveMessage;
import message.SendMessage;
import viewer.Admin;
import viewer.Admin_InvoiceView;
import viewer.Admin_UserView;
import viewer.User;
import Edit.RetunPan;
import aptuser.ModifyAdmin;
import aptuser.ModifyUser;
import aptuser.RegistUser;
import chat.ChatClient;

public class TreeMain extends JFrame implements TreeSelectionListener, ActionListener{
	
	DBManager  instance=null;
	public Connection  con=null;
	
	int winWidth=950;
	int winHeight = 750;
	int treeHeight=600;
	int westWidth=200;
	int centerWidth=700;
	int centerHeight=700;
	String userName="사용자";
	String userId="";
	
	JTree  tree;
	JScrollPane  scroll;	
	JPanel  p_west, p_center, p_west_center, p_west_south;
	JLabel  la_welcom;
	JButton  bt_exit;
	
	DefaultMutableTreeNode  root;
	public Vector<Object>  menuObjList = new Vector<Object>();
	Vector<MenuDto> menuDtoList = new Vector<MenuDto>();
	Vector<JPanel> basicPanelList = new Vector<JPanel>();
	
	public TreeMain() {
		// Connection con, String userId
		//this.con = con;
		//this.userId = userId;
		
		p_west = new JPanel();
		p_west_center = new JPanel();
		p_west_south = new JPanel();
		p_center = new JPanel();
		
		root = new DefaultMutableTreeNode("Menu");	
		tree = new JTree(root);
		scroll = new JScrollPane(tree);
		
		la_welcom = new JLabel(userName+" 님 환영합니다");
		bt_exit = new JButton("종료");

		// root menu menuDtoList 에 추가
		MenuDto menuDto = new MenuDto();
		menuDto.setMenu_id(0);
		menuDto.setMenu_name(root.getUserObject().toString());
		menuDtoList.add(menuDto);

		scroll.setBackground(Color.YELLOW);		
		
		// Size
		scroll.setPreferredSize(new Dimension(westWidth, treeHeight));
		p_west_center.setPreferredSize(new Dimension(westWidth, treeHeight));
		System.out.println(p_west_center.getHeight());
		p_west_south.setPreferredSize(new Dimension(westWidth, winHeight-treeHeight-50));
		la_welcom.setPreferredSize(new Dimension(westWidth-10, 50));
		
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
		
		// 초기 작업
		init();
		
		// Tree 구성 작업
		makeTree();
		
		// 리스너 연결.
		tree.addTreeSelectionListener(this);
		bt_exit.addActionListener(this);
		
		setTitle("아파트 관리");
		setVisible(true);
		setSize(winWidth, winHeight);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		
	}
	
	public void init(){
		// instance 
		if (instance==null){
			
			instance = DBManager.getInstance();
		}
		
		// Connection
		if (con==null){
			con = instance.getConnection();
		}
		
	}
	
	public void close(){
		// Connection 종료
		if (con!=null)
			try {
				con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		
		System.exit(0);
	}

	// Tree 구성 작업
	public void makeTree(){
		
		PreparedStatement  pstmt=null;
		ResultSet  rs=null;
		PreparedStatement  pstmtSub=null;
		ResultSet  rsSub=null;
		
		StringBuffer  sql=new StringBuffer();
		sql.append(" select m.menu_name, m.menu_id, m.menu_class_name, m.menu_type, m.order_seq \n");
		sql.append(" ,(select count(*) from menulist s \n");
		sql.append("  where s.menu_up_level_id = m.menu_id) subcnt \n");
		sql.append(" from menulist m \n");
		sql.append(" where m.menu_level = 1 \n");
		sql.append(" group by m.menu_name, m.menu_id, m.menu_class_name, m.menu_type, m.order_seq \n");
		sql.append(" order by m.order_seq \n");
		System.out.println("sql : "+sql.toString());
		
		StringBuffer  sqlSub=new StringBuffer();
		sqlSub.append("select s.menu_name, s.menu_id, s.menu_class_name, s.menu_up_level_id, menu_type \n");
		sqlSub.append(" from menulist s \n");
		sqlSub.append(" where s.menu_up_level_id = ? \n" );
		sqlSub.append(" order by s.order_seq \n");
		System.out.println("sqlSub : "+sqlSub.toString());
		
		try {
			pstmt = con.prepareStatement(sql.toString());
			rs = pstmt.executeQuery();
			
			// Menu 생성
			while (rs.next()){
				String menu = rs.getString("menu_name");
				
				DefaultMutableTreeNode  node=null;
				node = new DefaultMutableTreeNode(menu);
				
				root.add(node);
				// node 를 menuDtoList 에 추가
				MenuDto menuDto = new MenuDto();
				menuDto.setMenu_id(rs.getInt("menu_id"));
				menuDto.setMenu_name(rs.getString("menu_name"));
				menuDto.setMenu_class_name(rs.getString("menu_class_name"));
				menuDto.setMenu_type(rs.getString("menu_type"));
				menuDtoList.add(menuDto);
				
				// SubMenu 생성
				if (rs.getInt("subcnt")!=0){
					pstmtSub = con.prepareStatement(sqlSub.toString());
					pstmtSub.setInt(1, rs.getInt("menu_id"));
					rsSub = pstmtSub.executeQuery();
					
					while (rsSub.next()){
						DefaultMutableTreeNode  nodeSub=null;
						nodeSub = new DefaultMutableTreeNode(rsSub.getString("menu_name"));
						node.add(nodeSub);
						
						// node 를 menuDtoList 에 추가
						MenuDto menuDtoS = new MenuDto();
						menuDtoS.setMenu_id(rsSub.getInt("menu_id"));
						menuDtoS.setMenu_name(rsSub.getString("menu_name"));
						menuDtoS.setMenu_class_name(rsSub.getString("menu_class_name"));
						menuDtoS.setMenu_up_level_id(rsSub.getInt("menu_up_level_id"));
						menuDtoList.add(menuDtoS);
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
		
	}

	public void valueChanged(TreeSelectionEvent e) {
		Object obj=e.getSource();
		JTree  tree = (JTree)obj;
		DefaultMutableTreeNode  node = (DefaultMutableTreeNode)tree.getLastSelectedPathComponent();
		
		// Title 초기화
		this.setTitle("");
		
		// 메뉴명
		String menuName=node.getUserObject().toString();
		System.out.println("node : "+menuName);		
		
		// munuDotList 에서 해당 메뉴의 className 찾기
		int dtoIndex=-1;
		for (int i=0; i<menuDtoList.size();i++){
			if (menuDtoList.get(i).getMenu_name().equals(menuName)){
				dtoIndex=i;
				break;
			}
		}
		System.out.println("dtoIndex="+dtoIndex + ", menuName = "+menuName);
		if (dtoIndex==-1) return;
		
		String className = menuDtoList.get(dtoIndex).getMenu_class_name();
		System.out.println("className = "+className);
		
		// className 이 없는 경우, 최종 메뉴가 아니므로 skip
		if (className==null) return;
		
		int index=-1;
	    if (className.equals("InvEditPan")){
			// 송장등록
			index=findOpenClassIndex("InvEditPan");
			if (index==-1){
				InvEditPan invEditPan = new InvEditPan();
				menuObjList.add(invEditPan);
				addPanelList(invEditPan);
			}			
			setVisiblePanel(className, menuName);
		} else if (className.equals("Admin_InvoiceView")){
			// 관리자물품목록
			index=findOpenClassIndex("Admin_InvoiceView");
			if (index==-1){
				Admin_InvoiceView adminInvoice = new Admin_InvoiceView();
				menuObjList.add(adminInvoice);
				addPanelList(adminInvoice);
			}
			setVisiblePanel(className, menuName);
		} else if (className.equals("RetunPan")){
			System.out.println("반송등록");
			// 반송등록
			index=findOpenClassIndex("RetunPan");
			if (index==-1){
				RetunPan returnPan = new RetunPan();
				menuObjList.add(returnPan);
				addPanelList(returnPan);
			}
			setVisiblePanel(className, menuName);
		} else if (className.equals("User")){
			// 사용자물품목록
			index=findOpenClassIndex("User");
			if (index==-1){
				User user = new User();
				menuObjList.add(user);
				addPanelList(user);
			}
			setVisiblePanel(className, menuName);
		} else if (className.equals("RegistUser")){
			// 회원등록
			index=findOpenClassIndex("RegistUser");
			if (index==-1){
				RegistUser registUser = new RegistUser();
				menuObjList.add(registUser);
				//addPanelList(registUser);
			}
			//setVisiblePanel(className, menuName);
		} else if (className.equals("Admin_UserView")){
			// 회원목록
			index=findOpenClassIndex("Admin_UserView");
			if (index==-1){
				Admin_UserView adminUserView = new Admin_UserView();
				menuObjList.add(adminUserView);
				addPanelList(adminUserView);
			}
			setVisiblePanel(className, menuName);
		} else if (className.equals("ModifyAdmin")){
			// 관리자정보수정
			index=findOpenClassIndex("ModifyAdmin");
			if (index==-1){
				ModifyAdmin modifyAdmin = new ModifyAdmin();
				menuObjList.add(modifyAdmin);
				//addPanelList(modifyAdmin);
			}
			//setVisiblePanel(className, menuName);
		} else if (className.equals("ModifyUser")){
			// 회원정보수정
			index=findOpenClassIndex("ModifyUser");
			if (index==-1){
				ModifyUser modifyUser = new ModifyUser();
				menuObjList.add(modifyUser);
				//addPanelList(modifyUser);
			}
			//setVisiblePanel(className, menuName);
	    } else if (className.equals("ChatClient")){  
			// 채팅
	    	ChatClient  chatClient;
			index=findOpenClassIndex("ChatClient");
			if (index == -1){
				chatClient = new ChatClient();
				menuObjList.add(chatClient);
			} else {
				if (menuObjList.get(index)==null){
					chatClient = new ChatClient();
					menuObjList.setElementAt(chatClient, index);
				} else {
					((JFrame)menuObjList.get(index)).toFront();
				}
			}
		} else if (className.equals("ComplexPanel")){
			// 동 호수 등록
			//index=findOpenClassIndex("ComplexPanel");
			if (index==-1){
				//ComplexPanel complexPanel = new ComplexPanel();
				//menuObjList.add(complexPanel);
				//addPanelList(complexPanel);
			}
			//setVisiblePanel(className, menuName);
	    } else if (className.equals("SendMessage")){  
			// 쪽지 보내기
			SendMessage  send;
			index=findOpenClassIndex("SendMessage");
			if (index == -1){
				send = new SendMessage(this);
				menuObjList.add(send);
			} else {
				if (menuObjList.get(index)==null){
					send = new SendMessage(this);
					menuObjList.setElementAt(send, index);
				} else {
					((JFrame)menuObjList.get(index)).toFront();
				}
			}
		} else if (className.equals("RecieveMessage")){
			// 쪽지 수신함
			index=findOpenClassIndex("RecieveMessage");
			if (index == -1){
				RecieveMessage  recMsg = new RecieveMessage(this);
				menuObjList.add(recMsg);
			} else {
				((JFrame)menuObjList.get(index)).toFront();
			}
		}
		
	}
	
	public void addPanelList(JPanel panel){
		
		panel.setPreferredSize(new Dimension(centerWidth, centerHeight));
		panel.setVisible(true);
		basicPanelList.add(panel);
		p_center.add(panel);
		
	}
	
	// className 으로 열려있는 화면 체크
	public int findOpenClassIndex(String className){
		int index=-1;
		for (int i=0; i<menuObjList.size();i++){
			//System.out.println(menuObjList.get(i).getClass().getSimpleName());
			if (menuObjList.get(i).getClass().getSimpleName().equals(className)){
				index=i;
				break;
			}
		}
		System.out.println(className + " index = "+index);
		return index;
	}
	
	public void setVisiblePanel(String className, String menuName){
		this.setTitle(menuName);
		System.out.println("basicPanelList.size() = "+basicPanelList.size());
		int index = findOpenClassIndex(className);
		System.out.println("setVisiblePanel : index = "+index);
		for (int i=0; i<basicPanelList.size(); i++){
			if (basicPanelList.get(i)==menuObjList.get(index)){
				basicPanelList.get(i).setVisible(true);
			} else {
				basicPanelList.get(i).setVisible(false);
			}
		}
		p_center.updateUI();
	}

	public void actionPerformed(ActionEvent e) {
		Object obj = e.getSource();
		if (obj==bt_exit){
			close();
		}
	}

	public static void main(String[] args) {
		new TreeMain();

	}

}
