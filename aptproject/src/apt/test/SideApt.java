package apt.test;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;

import javax.swing.JLabel;
import javax.swing.JPanel;

public class SideApt extends JPanel{
	JPanel  p_info1,p_design1,p_design2;
	static JLabel la_info;
	
	
	
	public SideApt(){
		setLayout(null);
		p_info1=new JPanel();
		p_design1=new JPanel();
		p_design2=new JPanel();
		
		
		la_info=new JLabel("");
		
		la_info.setFont(new Font("", Font.BOLD, 30));
		
		
		p_design1.setBounds(0, 0, 140, 135);
		p_design1.setBackground(Color.red);
		
		p_info1.setBounds(10, 140, 120, 50);
		p_info1.setBackground(Color.white);
		p_info1.add(la_info);
		
		setBounds(500, 457, 140, 200);
		setBackground(Color.cyan);
		
		add(p_design1);
		add(p_info1);
		
		

		
		
	}
	
}
