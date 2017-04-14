package apt;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class AppMain extends JFrame implements ActionListener {
	JPanel p_west;
	Admin list = new Admin();

	// User user = new User();
	public AppMain() {
		p_west = new JPanel();
		setLayout(new BorderLayout());
		add(p_west, BorderLayout.WEST);
		add(list, BorderLayout.CENTER);
		// add(user,BorderLayout.CENTER);

		p_west.setPreferredSize(new Dimension(200, 700));

		setVisible(true);
		setSize(900, 700);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
	}

	public void actionPerformed(ActionEvent e) {
		Object obj = e.getSource();
		
	}

	public static void main(String[] args) {
		new AppMain();

	}

}
