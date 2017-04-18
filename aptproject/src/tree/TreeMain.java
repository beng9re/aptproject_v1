package tree;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;

import db.DBManager;
import dto.MenuDto;
import message.RecieveMessage;
import message.SendMessage;

public class TreeMain extends JFrame implements TreeSelectionListener{
	
	DBManager  instance = DBManager.getInstance();
	Connection  con;
	
	JTree  tree;
	JScrollPane  scroll;
	
	JPanel  p_west, p_center;
	DefaultMutableTreeNode  root;
	public Vector<Object>  menuObjList = new Vector<Object>();
	Vector<MenuDto> menuDtoList = new Vector<MenuDto>();
	
	public TreeMain() {
		
		p_west = new JPanel();
		p_center = new JPanel();
		
		root = new DefaultMutableTreeNode("Menu");	
		tree = new JTree(root);
		scroll = new JScrollPane(tree);

		// root menu menuDtoList 에 추가
		MenuDto menuDto = new MenuDto();
		menuDto.setMenu_id(0);
		menuDto.setMenu_name(root.getUserObject().toString());
		menuDtoList.add(menuDto);

		scroll.setBackground(Color.YELLOW);
		scroll.setPreferredSize(new Dimension(200, 700));
		
		add(scroll, BorderLayout.WEST);
		
		init();
		
		makeTree();
		
		tree.addTreeSelectionListener(this);
		
		setVisible(true);
		setSize(1000, 800);
		//setLocationRelativeTo(null);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		
	}
	
	public void init(){
		con = instance.getConnection();
		
	}
	
	public void addMenuDto(){
		
	}
	
	public void makeTree(){
		
		PreparedStatement  pstmt=null;
		ResultSet  rs=null;
		PreparedStatement  pstmtSub=null;
		ResultSet  rsSub=null;
		
		StringBuffer  sql=new StringBuffer();
		sql.append(" select m.MENU_NAME, m.MENU_ID, m.menu_class_name ");
		sql.append(" ,(select count(*) from menulist s ");
		sql.append("  where s.MENU_UP_ID = m.menu_id) subcnt ");
		sql.append(" from menulist m ");
		sql.append(" where m.MENU_LEVEL = 1 ");
		sql.append(" group by m.MENU_NAME, m.MENU_ID, m.menu_class_name ");
		sql.append(" order by m.MENU_ID ");
		
		StringBuffer  sqlSub=new StringBuffer();
		sqlSub.append("select s.menu_name, s.menu_id, s.menu_class_name, s.menu_up_id from menulist s ");
		sqlSub.append(" where s.MENU_UP_ID = ?" );
		sqlSub.append(" order by s.MENU_ID ");
		
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
				menuDto.setMenu_id(rs.getInt("MENU_ID"));
				menuDto.setMenu_name(rs.getString("menu_name"));
				menuDto.setMenu_class_name(rs.getString("menu_class_name"));
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
						menuDtoS.setMenu_id(rsSub.getInt("MENU_ID"));
						menuDtoS.setMenu_name(rsSub.getString("menu_name"));
						menuDtoS.setMenu_class_name(rsSub.getString("menu_class_name"));
						menuDtoS.setMenu_up_id(rsSub.getInt("menu_up_id"));
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
		if (className.equals("SendMessage")){  
			// 쪽지 보내기
			SendMessage  send;
			index=findOpenClassIndex("SendMessage");
			if (index == -1){
				send = new SendMessage(this);
				menuObjList.add(send);
			} else {
				((JFrame)menuObjList.get(index)).toFront();
			}
		} else if (className.equals("RecieveMessage")){
			// 수신 메세지
			index=findOpenClassIndex("RecieveMessage");
			if (index == -1){
				RecieveMessage  recMsg = new RecieveMessage(this);
				menuObjList.add(recMsg);
			} else {
				((JFrame)menuObjList.get(index)).toFront();
			}
		}
		
/*
		if (menuName.equals("쪽지 보내기")){
			index=findOpenClassIndex("SendMessage");
			if (index == -1){
				SendMessage  send = new SendMessage(this);
				menuObjList.add(send);
			} else {
				//JFrame  jf=(JFrame)menuObjList.get(index);
				//jf.show();
			}
		} else if (menuName.equals("받은 쪽지함")){
			index=findOpenClassIndex("RecieveMessage");
			if (index == -1){
				RecieveMessage recMsg = new RecieveMessage(this);
				menuObjList.add(recMsg);
			}	
		}
*/
	}
	
	// className 으로 열려있는 화면 체크
	public int findOpenClassIndex(String className){
		int index=-1;
		for (int i=0; i<menuObjList.size();i++){
			System.out.println(menuObjList.get(i).getClass().getSimpleName());
			if (menuObjList.get(i).getClass().getSimpleName().equals(className)){
				index=i;
				break;
			}
		}
		System.out.println(className + " index = "+index);
		return index;
	}

	public static void main(String[] args) {
		new TreeMain();

	}

}
