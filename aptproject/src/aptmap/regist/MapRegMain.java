package aptmap.regist;

import javax.swing.JFrame;
import javax.swing.JPanel;

import aptmap.Mainpan;

public class MapRegMain extends JFrame{

	MapRegPan mr;
	public MapRegMain() {
	mr=new MapRegPan();
		
	add(mr);
		
		pack();
		setVisible(true);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		
	}
	
	public static void main(String[] args) {
		new MapRegMain();
	}
	
}
