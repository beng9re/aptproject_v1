package apt.test;

import java.awt.BorderLayout;
import java.awt.Color;
import java.sql.Connection;

import javax.swing.JFrame;
import javax.swing.JPanel;

import junit.framework.Test;

public class AptnewMain extends JFrame{
	
	NewAptPanel aptPanel;
	
	
	
	public AptnewMain(){
	
	aptPanel=new NewAptPanel();
	
	
	add(aptPanel);
	
	pack();
	setVisible(true);
	setDefaultCloseOperation(EXIT_ON_CLOSE);
		
	}

	public static void main(String[] args) {
		new AptnewMain();
	}
}
