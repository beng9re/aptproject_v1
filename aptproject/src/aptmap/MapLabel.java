package aptmap;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
import java.net.URL;
import java.util.Vector;

import javax.imageio.ImageIO;
import javax.swing.JLabel;

public class MapLabel extends Canvas implements MouseListener{
	
	int width;
	int height;
	URL url;
	Image img;
	int x;
	int y;
	JLabel name;
	Vector info;
	Mainpan mp;
	public MapLabel(Mainpan mp,Vector info,int width,int height) {
		this.mp=mp;
		this.info=info;
		this.x=(Integer)info.get(2)-10;
		this.y=(Integer)info.get(3)-5;
		this.width=width;
		this.height=height;
		url=this.getClass().getResource("/icon.png");
		try {
			img=ImageIO.read(url);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		setBounds(x, y, width, height);
		this.addMouseListener(this);
		

	}
	@Override
	public void paint(Graphics g) {
		g.drawImage(img, 0, 0,width,height, this);
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		
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
		System.out.println("mouseEntered");
		width+=20;
		height+=20;
		name=new JLabel((String)info.get(1));
		name.setBounds(x, y-20, 100, 20);
		
		mp.add(name);
		mp.updateUI();
		
		
		System.out.println(info.get(1));
		this.setSize(width,height);
		this.repaint();
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		System.out.println("mouseExited");
		width=Mapmain.ICONWITH;
		height=Mapmain.ICONHEIGHT;
		this.setSize(width,height);
		mp.remove(name);
		mp.updateUI();
		this.repaint();
		
	}
	

}
