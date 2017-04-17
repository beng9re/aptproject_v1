import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.LineBorder;

public class MiddlePanel extends JPanel{
	JLabel la_complex, la_unit;
	JTextField t_complex, t_unit;

	public MiddlePanel() {
		la_complex = new JLabel("µ¿");
		la_unit = new JLabel("È£¼ö");
		t_complex = new JTextField(20);
		t_unit = new JTextField(20);

		setLayout(null);

		// À§Ä¡ ÁöÁ¤
		la_complex.setBounds(20, 80, 50, 50);
		la_unit.setBounds(10, 120, 50, 50);
		t_complex.setBounds(70, 90, 200, 30);
		t_unit.setBounds(70, 130, 200, 30);

		// µðÀÚÀÎ
		la_complex.setFont(new Font("¸¼Àº°íµñ", Font.BOLD, 20));
		la_unit.setFont(new Font("¸¼Àº°íµñ", Font.BOLD, 20));

		t_complex.setBorder(new LineBorder(Color.orange, 5));
		t_unit.setBorder(new LineBorder(Color.orange, 5));

		add(la_complex);
		add(t_complex);
		add(la_unit);
		add(t_unit);

		setPreferredSize(new Dimension(300, 300));
		setBackground(Color.white);

	}
}
