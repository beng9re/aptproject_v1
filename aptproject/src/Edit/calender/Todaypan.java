package Edit.calender;

import java.awt.Color;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class Todaypan extends JPanel{
 
	JButton[] lb=new JButton[7];
	String[] lb_tx={"일","월","화","수","목","금","토"};
	
	public Todaypan() {
		setLayout(new GridLayout(1,7));
		
		for (int i = 0; i < lb.length; i++) {
			lb[i]=new JButton(lb_tx[i]);
			add(lb[i]);
			lb[i].setFocusable(false);
			lb[i].setFocusPainted(false);
			lb[i].setCursor(null);
			lb[i].setBorder(BorderFactory.createLineBorder(Color.pink,2));
			lb[i].setBackground(Color.white);
			lb[i].setContentAreaFilled(false);
			lb[i].setOpaque(false);
			}
	
	
	}
}
