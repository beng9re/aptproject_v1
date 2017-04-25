package locker;

import java.util.Vector;

import javax.swing.JFrame;
import javax.swing.JTextArea;


public class PostInfo extends JFrame{
	JTextArea ta;
	Vector vec=new Vector();
	public PostInfo(Vector vec) {
		ta=new JTextArea();
		add(ta);
		
		ta.append(vec.get(1)+"\n");
		
		setSize(500,400);
		setVisible(true);
	
	}
	
}
