package apt.test;

import java.awt.Color;
import java.awt.Dimension;

import javax.swing.JLabel;
import javax.swing.JPanel;

public class DrawApt extends JPanel{
	JLabel la_name;
	
	public DrawApt(){
		la_name=new JLabel("");
		
		
		add(la_name);
		setBackground(Color.white);
		setPreferredSize(new Dimension(25, 25));
		setVisible(true);
	
	}
	
	
}
