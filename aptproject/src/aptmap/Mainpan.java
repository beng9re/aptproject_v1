package aptmap;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.io.IOException;
import java.net.URL;
import java.util.Vector;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class Mainpan  extends JPanel {
	

	Image img;
	URL url;
	Canvas can;
	Mapmain main;
	int pointx;
	int pointy;
	Vector<JButton> vcbu=new Vector<>();
	public Mainpan() {
		// TODO Auto-generated constructor stub
	}
	
	public Mainpan(Mapmain main) {
		
		this.main=main;
		url=getClass().getResource("/map.png");
	 	System.out.println(url);
		
		try {
			img=ImageIO.read(url);
		
	 	} catch (IOException e) {
	
			e.printStackTrace();
		}
		
		setLayout(null);
		
	
	
		
		
		
		//addMouseListener(mp);
		
		
		setPreferredSize(new Dimension(700, 700));
		 getMapIcon();
		
	}
	public void getMapIcon(){
		
		for(int i=0;i<main.mapIcon.size();i++){
			
			MapLabel lb1 = new MapLabel(this,main.mapIcon.get(i),Mapmain.ICONWITH,Mapmain.ICONHEIGHT);
			System.out.println(lb1);
			this.add(lb1);
			this.updateUI();
		}
	}
	@Override
	public void paintComponent(Graphics g) {
		g.drawImage(img, 0, 0, 700,700,null);
		
	}
	MouseAdapter mp=new MouseAdapter() {
		public void mouseClicked(java.awt.event.MouseEvent e) {
			
			Mainpan.this.removeAll();
			pointx=e.getX();
			pointy=e.getY();
			//MapLabel lb1 = new MapLabel("³×¾È³ç", pointx-10, pointy-5, 200, 200);
			
		
			Mainpan.this.updateUI();
		
		};
	
	};
	
	
}
