import java.awt.Color;
import java.awt.Component;

import javax.swing.JFrame;
import javax.swing.JPanel;


public class Projectmain extends JFrame {
Color[] color={Color.black,Color.blue,Color.yellow,Color.red,Color.gray};
	
	JPanel p_wast,p_center;
	JPanel p_up,p_down;
	JPanel p_list;
	JPanel pan;
	RetunPan pna;
	public Projectmain() {
		
		
		//pan=new ManuPanel();
		pna=new RetunPan();
		add(pna);
		//add(pan);
		
		
		
		
		setVisible(true);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setSize(700,700);
	
	}
	
	public static void main(String[] args){
		
		new Projectmain();
	}
}
