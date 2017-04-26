package Edit.locker;

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
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import Edit.InvEditPan;
import db.DBManager;

public class Postbox extends JPanel implements MouseListener{
	Image img;
	URL url;
	
	JLabel lb_name;
	JPanel pan;
	String abpath;
	String name;
	Connection con;
	Vector vec=new Vector();
	PostInfo pi;
	Vector<PostInfo> ps=new Vector<>();
	InvEditPan iep;
	//벡터의 값
	 
	/*
	 * 
	 0 Invoice_id				//아이디
	 1	Invoice_barcode  //바코드
	 2	Invoice_arrtime   
	 3	Invoice_taker
	 4	Invoice_takeflag
	 5 time
	 6	Aptuser_id
	 7	Company_id
	 8	Box_num				//사물함번호
	 9 Box_use				//사용중인지
				
	 * 
	 * */
	
	
	
	//없는것 				있는것
	PostBoxMain pm;
	public Postbox(Vector vec,Container c,String path,Connection con,PostBoxMain pm) {
		this.vec=vec;
		this.con=con;
		this.pm=pm;
		setLayout(null);
		abpath=path;
		
		lb_name=new JLabel(vec.get(8).toString()+"");
		
		
		url=this.getClass().getResource(path);
		
		try {
			img=ImageIO.read(url);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
		add(lb_name);
		lb_name.setBounds(40, 20, 30, 20);
		this.setBorder(BorderFactory.createLineBorder(Color.WHITE,4));
		
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
		
		/*
		for (int i = 0; i < ps.size(); i++) {
			if(this.pi==ps.get(i)){
				
			}
			
		}
		new PostInfo(vec);
		*/
		//String name=null;
		//name=vec.get(6).toString();
		/*
		if(name.equals(null)){
			JOptionPane.showMessageDialog(this,"비어있습니다");
		}
		else{
			JOptionPane.showMessageDialog(this, name+"회원이 사용중");
		}
	*/
		boxCk();
		
		
	
		
	}
	
	public void boxCk(){
		if(vec.get(9).equals("N")){
			System.out.println("Y");
			int result=JOptionPane.showConfirmDialog(this, "선택한 사물함 번호가\n"+vec.get(8)+" 가 맞습니까?",
					"수거예정일",JOptionPane.YES_NO_OPTION);
			if(result==0){
				pm.iep.tf_box.setText(vec.get(8).toString());
			
			}
			else{
				return;
			}
		}
		else{
			JOptionPane.showMessageDialog(this, "해당 사물함은 현재 사용중입니다");
			return;
		}
		
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
			pm.repaint();
			
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
			pm.repaint();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
	}
}
