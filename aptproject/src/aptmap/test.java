package aptmap;

import java.awt.Canvas;
import java.awt.Graphics;
import java.awt.Image;
import java.io.IOException;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class test extends JPanel{

	Image img;
	Canvas can;
	URL url;
	public test() {
		
		
		url=getClass().getResource("/map.jpg");
		try {
			img=ImageIO.read(url);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		can=new Canvas(){
			public void paint(Graphics g) {
				g.drawImage(img,0,0,100, 100, null);
				
			}
		};
		add(can);
		
		
		
		setVisible(true);
		setSize(700, 700);
	}
	
	public static void main(String[] args) {
		new test();
	}
}
