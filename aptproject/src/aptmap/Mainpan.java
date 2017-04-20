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
	public Mainpan(Mapmain main) {
		this.main=main;
		url=getClass().getResource("/map.jpg");
	 	System.out.println(url);
		
		try {
			img=ImageIO.read(url);
		
	 	} catch (IOException e) {
	
			e.printStackTrace();
		}
		
		setLayout(null);
		
		MapLabel lb1 = new MapLabel("gasdsada",200,100,50,20);
		add(lb1);
		
		
		
		//addMouseListener(mp);
		
		
		setPreferredSize(new Dimension(700, 700));
		
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
			MapLabel lb1 = new MapLabel("³×¾È³ç", pointx-10, pointy-5, 50, 20);
			
			Mainpan.this.add((JLabel)lb1);
			Mainpan.this.updateUI();
		
		};
	
	};
	
	
}
