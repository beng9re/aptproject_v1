package apt.test;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JLabel;
import javax.swing.JPanel;

public class DrawApt extends JPanel{
	JLabel la_name;
	boolean ff=false;
	NewAptPanel aptPanel;
	
	public DrawApt(){
		la_name=new JLabel("");
		
		addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				
				if(ff=true){
					System.out.println(ff);
					System.out.println(aptPanel.unitN.toString());
				}
			}
			@Override
			public void mouseExited(MouseEvent e) {
				ff=false;
			}
		});

		
		
		add(la_name);
		setBackground(Color.white);
		setPreferredSize(new Dimension(25, 25));
		setVisible(true);
	
	}
	
	
}
