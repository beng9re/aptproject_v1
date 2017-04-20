package aptmap;

import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Panel;
import java.io.IOException;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class Mapmain extends JFrame {
	

	JPanel map;
	JPanel south;
	Image img;
	JTextField tf_complexid;
	JTextField tf_classid;
	URL url;
	Mainpan mp;
	public Mapmain() {
		mp=new Mainpan(this);
		south =new JPanel();
		

		
		
		
		add(mp);
		pack();
		
		setVisible(true);
		
		setDefaultCloseOperation(EXIT_ON_CLOSE);
	}
	
	
		
	
		
	

	
	public static void main(String[] args) {
		
		new Mapmain();
		
	}

}
