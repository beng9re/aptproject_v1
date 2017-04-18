package coplex_regist;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.MouseListener;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.LineBorder;

public class MiddlePanel extends JPanel{
	JLabel la_complex, la_unit, la_floor,la_1,la_2;
	JTextField t_complex, t_unit1,t_unit2,t_floor1,t_floor2;
	

	public MiddlePanel() {
		la_complex = new JLabel("µ¿");
		la_floor=new JLabel("Ãþ");
		la_unit = new JLabel("¶óÀÎ");
		la_1=new JLabel("~");
		la_2=new JLabel("~");
		t_floor1=new JTextField("1",10);
		t_floor2=new JTextField(10);
		t_complex = new JTextField(20);
		t_unit1 = new JTextField("1",10);
		t_unit2=new JTextField(10);
		setLayout(null);

		// À§Ä¡ ÁöÁ¤
		la_complex.setBounds(20, 60, 50, 50);
		t_complex.setBounds(60, 70, 200, 30);
		la_floor.setBounds(20, 100, 50, 50);
		t_floor1.setBounds(60, 110, 90, 30);
		la_1.setBounds(155, 110, 50, 30);
		t_floor2.setBounds(170, 110, 90, 30);
		la_unit.setBounds(0, 140, 50, 50);
		t_unit1.setBounds(60, 150, 90, 30);
		la_2.setBounds(155, 135, 50, 50);
		t_unit2.setBounds(170, 150, 90, 30);
		
		
		
		
		
		

		// µðÀÚÀÎ
		la_complex.setFont(new Font("¸¼Àº°íµñ", Font.BOLD, 20));
		la_unit.setFont(new Font("¸¼Àº°íµñ", Font.BOLD, 20));
		la_floor.setFont(new Font("¸¼Àº°íµñ", Font.BOLD, 20));
		
		la_1.setFont(new Font("¸¼Àº °íµñ", Font.BOLD, 20));
		la_2.setFont(new Font("¸¼Àº °íµñ", Font.BOLD, 20));

		t_complex.setBorder(new LineBorder(Color.orange, 5));
		t_unit1.setBorder(new LineBorder(Color.orange, 5));
		t_unit2.setBorder(new LineBorder(Color.orange, 5));
		t_floor1.setBorder(new LineBorder(Color.orange, 5));
		t_floor2.setBorder(new LineBorder(Color.orange, 5));

		add(la_complex);
		add(t_complex);
		add(la_floor);
		add(t_floor1);
		add(la_1);
		add(t_floor2);
		add(la_unit);
		add(t_unit1);
		add(la_2);
		add(t_unit2);

		setPreferredSize(new Dimension(300, 300));
		setBackground(Color.white);

	}
}
