package Edit.locker;

import java.util.Vector;

import javax.swing.JFrame;
import javax.swing.JTextArea;

import dto.View_inbox;


public class PostInfo extends JFrame{
	JTextArea ta;
	Vector<View_inbox> vec=new Vector<View_inbox>();
	public PostInfo(Vector<View_inbox> vec) {
		this.vec=vec;
		ta=new JTextArea();
		add(ta);
		
		ta.append(vec.get(6)+"\n");
		
		setSize(500,400);
		setVisible(true);
	
	}
	
}
