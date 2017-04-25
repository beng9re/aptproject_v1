package locker;

import java.awt.BorderLayout;
import java.awt.Choice;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.Border;

import db.DBManager;
import dto.Storagebox;
import dto.ViewCPUT;
import dto.View_inbox;


public class PostBoxMain  extends JFrame{
	
	JPanel p_south,p_north,p_boxgrid,p_emp;
	JLabel lb_title;
	
	Postbox ps;
	Vector<Vector> box=new Vector<Vector>();
	
	
	
	
	String[] path={"/box.png","/box2.png"};
	Connection con;

	public PostBoxMain() {
		con=DBManager.getInstance().getConnection();
		p_north=new JPanel();
		p_north.setPreferredSize(new Dimension(600,100));
		p_north.setBackground(Color.PINK);
		p_north.setLayout(null);
		
		p_north.add(lb_title=new JLabel("STORAGE BOX"));
		lb_title.setFont(new Font("∞ÌµÒ√º", Font.BOLD, 40));
		lb_title.setBounds(230, 30, 300, 40);
		
		p_south=new JPanel();
		p_south.setBackground(Color.WHITE);
		p_boxgrid=new JPanel();
		
		
		setBox();
		add(p_north,BorderLayout.NORTH);
		add(p_south);
	    
		 addList();
		
		
		
		setBounds(100, 100, 750, 650);
		setVisible(true);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		
	}
	
	public void setInbox(){
		PreparedStatement pstmt=null;
		ResultSet rs=null;
		
		String sql="select * from view_inbox order by box_num asc";
		try {
			pstmt=con.prepareStatement(sql);
			while (rs.next()){
				
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public void setBox(){
	
		PreparedStatement pstmt=null;
		ResultSet rs=null;
		
		String sql="select * from view_inbox order by box_num asc";
		try {
			pstmt=con.prepareStatement(sql);
			rs=pstmt.executeQuery();
			while (rs.next()){

				View_inbox dto=new View_inbox();
				Vector<String> ss= new Vector<String>();
				dto.setInvoice_id(rs.getInt("invoice_id"));
				dto.setBox_use(rs.getString("box_use"));
				dto.setInvoice_barcode(rs.getString("invoice_barcode"));
				dto.setInvoice_arrtime(rs.getString("invoice_arrtime"));
				dto.setInvoice_taker(rs.getString("invoice_taker"));
				dto.setInvoice_takeflag(rs.getString("invoice_takeflag"));
				dto.setAptuser_id(rs.getString("aptuser_id"));
				dto.setCompany_id(rs.getInt("company_id"));
				dto.setBox_num(rs.getInt("box_num"));
				
				ss.add(Integer.toString(dto.getInvoice_id()));
				ss.add(dto.getBox_use());
				ss.add(dto.getInvoice_barcode());
				ss.add(dto.getInvoice_arrtime());
				ss.add(dto.getInvoice_taker());
				ss.add(dto.getInvoice_takeflag());
				ss.add(dto.getAptuser_id());
				ss.add(Integer.toString(dto.getCompany_id()));
				ss.add(Integer.toString(dto.getBox_num()));
		
				box.add(ss);
			
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			try {
				if(rs!=null){rs.close();};
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				if(pstmt!=null)pstmt.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public void addList(){
		p_south.setLayout(new GridLayout(8,6));
		
		Postbox ps=null;
		int flag=0;
		for(int i=0;i<box.size();i++){
	
			System.out.println(i);
			if(box.get(i).get(1).toString().equals("N")){
				flag=1;
			}else{
				flag=0;
			}
			ps=new Postbox(box.get(i),p_south,path[flag],con);
			
		};
	}
	public static void main(String[] args) {
		new PostBoxMain();
	}
	
	


}
