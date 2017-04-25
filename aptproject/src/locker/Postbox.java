package locker;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Vector;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;

import db.DBManager;

public class Postbox extends JPanel implements MouseListener{
	Image img;
	URL url;
	
	JLabel lb_name;
	JPanel pan;
	String abpath;
	String name;
	Connection con;
	
	//없는것 				있는것
	public Postbox(String name,Container c,String path,Connection con) {
		this.con=con;
		this.name=name;
		setLayout(null);
		abpath=path;
		
		lb_name=new JLabel(name);
		
		
		url=this.getClass().getResource(path);
		
		try {
			img=ImageIO.read(url);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
		add(lb_name);
		lb_name.setBounds(40, 20, 30, 20);
		this.setBorder(BorderFactory.createLineBorder(Color.WHITE,3));
		
		setPreferredSize(new Dimension(100, 60));
		c.add(this);
		this.addMouseListener(this);
	}
	
	
	
	public void info(){
		
		
		PreparedStatement pstmt=null;
		
		try {
			String sql="select * from";
			pstmt=con.prepareStatement(sql);
			pstmt.executeQuery();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	
	@Override
	protected void paintComponent(Graphics g) {
		g.drawImage(img, 0,0,100,60, this);
	}
	@Override
	public void mouseClicked(MouseEvent e) {
		System.out.println(name);
		
	}
	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		url=this.getClass().getResource("/box3.png");
		try {
			img=ImageIO.read(url);
			this.repaint();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
	}
	@Override
	public void mouseExited(MouseEvent e) {
		url=this.getClass().getResource(abpath);
		try {
			img=ImageIO.read(url);
			this.repaint();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
	}
}
