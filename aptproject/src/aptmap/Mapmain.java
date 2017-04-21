package aptmap;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Panel;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

import javax.imageio.ImageIO;
import javax.naming.spi.DirStateFactory.Result;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;

import db.DBManager;
import dto.Mapdto;

public class Mapmain extends JFrame {
	
	Vector<Vector> mapIcon=new Vector<Vector>();
	
	final static public  int ICONWITH=30;
	final static public  int ICONHEIGHT=30;
	
	JPanel map;
	JPanel south;
	Image img;
	JTextField tf_complexid;
	JTextField tf_classid;
	URL url;
	Mainpan mp;
	Connection con;
	
	
	public Mapmain() {

		roadIconInfo();
	
		mp=new Mainpan(this);
		south =new JPanel();
		mp.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				
				System.out.println(e.getX()+","+e.getY());
				};
			
			
			
			
		
			}
		);
		
		
		
		
		add(mp);
		pack();
		
		setVisible(true);
		
		setDefaultCloseOperation(EXIT_ON_CLOSE);
	}
	
	
	//아이콘정보
	private void roadIconInfo(){
		PreparedStatement pstmt=null;
		ResultSet rs=null;
		con=DBManager.getInstance().getConnection();
		String sql="select * from view_map";
		try {
			pstmt=con.prepareStatement(sql);
			rs=pstmt.executeQuery();
			while(rs.next()){
			
				Vector vec=new Vector();
				Mapdto dto= new Mapdto();
				dto.setComplex_id(rs.getInt("complex_id"));
				dto.setComplex_name(rs.getString("complex_name"));
				dto.setXpoint(rs.getInt("xpoint"));
				dto.setYpoint(rs.getInt("ypoint"));
			
				vec.add(dto.getComplex_id());
				vec.add(dto.getComplex_name());
				vec.add(dto.getXpoint());
				vec.add(dto.getYpoint());
				mapIcon.add(vec);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			if(rs!=null){
				try {
					rs.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if(pstmt!=null){
				try {
					pstmt.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		
		
		
		
	}
		
	
		
	

	
	public static void main(String[] args) {
		
		new Mapmain();
		
	}

}
