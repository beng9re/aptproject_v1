package aptmap;

import java.awt.Color;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JLabel;

public class MapLabel extends JLabel implements MouseListener{
	
	int width;
	int height;
	
	public MapLabel(String txt,int x,int y,int width,int height) {
		this.width=width;
		this.height=height;
		
		setText(txt);
		setBounds(x, y, width, height);
		setBackground(Color.RED);
		setOpaque(true);
		addMouseListener(this);
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
		setSize(width+200,height+200);
		this.repaint();
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		System.out.println("mouseExited");
		setSize(width,height);
		this.repaint();
		
	}
	

}
