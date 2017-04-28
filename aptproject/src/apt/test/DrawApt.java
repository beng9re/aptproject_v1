package apt.test;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Connection;

import javax.swing.JLabel;
import javax.swing.JPanel;

public class DrawApt extends JPanel{
	JLabel la_name;
	boolean ff=false;
	NewAptPanel aptPanel;
	Connection con;
	public DrawApt(){
		la_name=new JLabel("");
		

		
		add(la_name);
		setBackground(Color.white);
		setPreferredSize(new Dimension(25, 25));
		setVisible(true);
	
	}
	
	
}
