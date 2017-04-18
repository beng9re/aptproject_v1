package coplex_regist;

import javax.swing.JFrame;

public class Main extends JFrame{
	AptPanel aptPanel;
	
	public Main(){
		aptPanel=new AptPanel();
		
		add(aptPanel);
		setSize(900, 700);
		setVisible(true);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		
		
	}
	
	public static void main(String[] args) {
			new Main();
		}
}
