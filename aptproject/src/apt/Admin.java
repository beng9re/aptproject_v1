package apt;

import java.awt.BorderLayout;
import java.awt.Checkbox;
import java.awt.Choice;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.EtchedBorder;


public class Admin extends JFrame implements ActionListener {
	JPanel p_north, p_north_left, p_north_right, p_south;
	JTable table;
	JScrollPane scroll;
	Choice choice;
	JButton bt_find, bt_copy, bt_xls;
	Checkbox ch_a, ch_b, ch_c;
	DBManager manager;
	Connection con;
	ListModel listModel;
	JFileChooser chooser;

	public Admin() {
		chooser = new JFileChooser();
		p_north = new JPanel();
		p_north_left = new JPanel();
		p_north_right = new JPanel();
		p_south = new JPanel();
		table = new JTable();
		scroll = new JScrollPane(table);
		choice = new Choice();
		bt_find = new JButton("¡∂»∏");
		bt_copy = new JButton("¿Œº‚");
		bt_xls = new JButton("xml∑Œ ≥ª∫∏≥ª±‚");
		ch_a = new Checkbox("∏Ù∂Û");
		ch_b = new Checkbox("∏Ù∂Û");
		ch_c = new Checkbox("∏Ù∂Û");

		setLayout(new BorderLayout());

		p_north_left.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 20));
		p_north_right.setLayout(new FlowLayout(FlowLayout.RIGHT, 5, 20));

		p_north_left.add(choice);
		p_north_left.add(ch_a);
		p_north_left.add(ch_b);
		p_north_left.add(ch_c);

		p_north_right.add(bt_copy);
		p_north_right.add(bt_xls);
		p_north_right.add(bt_find);

		p_north.add(p_north_left);
		p_north.add(p_north_right);

		p_north.setBorder(new EtchedBorder(EtchedBorder.RAISED));

		p_south.add(scroll);

		scroll.setPreferredSize(new Dimension(680, 500));
		choice.setPreferredSize(new Dimension(150, 30));

		// p_north_left.setPreferredSize(new Dimension(350, 100));
		// p_north_right.setPreferredSize(new Dimension(350, 100));
		p_north.setPreferredSize(new Dimension(700, 80));
		
		bt_copy.addActionListener(this); 
		bt_find.addActionListener(this); 
		bt_xls.addActionListener(this); 
		
		add(p_north, BorderLayout.NORTH);
		add(p_south, BorderLayout.CENTER);
		setVisible(true);
		setSize(700, 700);

		init();
	}

	public void init() {
		manager = DBManager.getInstance();
		this.con = manager.getConnection();
	}

	public void getList(){
			table.setModel(listModel = new ListModel(con));
			table.updateUI();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		Object obj = e.getSource();
		if (obj == bt_find) {
			getList();
		}
		else if(obj == bt_xls) {
			
		}
		else if(obj == bt_copy) {
			
		}
	}

	public static void main(String[] args) {
		new Admin();
	}

}
