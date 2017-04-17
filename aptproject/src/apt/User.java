package apt;

import java.awt.BorderLayout;
import java.awt.Checkbox;
import java.awt.Choice;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.EtchedBorder;
import javax.swing.plaf.basic.BasicBorders.RadioButtonBorder;

public class User extends JFrame implements ActionListener {
	JPanel p_north, p_north_right, p_south;
	JTable table;
	JScrollPane scroll;
	Choice choice;
	JButton bt_find, bt_copy, bt_xls;
	JRadioButton rb_a, rb_b, rb_c;
	DBManager manager;
	Connection con;
	JFileChooser chooser;
	

	public User() {
	
		chooser = new JFileChooser();
		p_north = new JPanel();
		p_north_right = new JPanel();
		p_south = new JPanel();
		table = new JTable();
		scroll = new JScrollPane(table);
		bt_find = new JButton("조회");
		bt_copy = new JButton("인쇄");
		bt_xls = new JButton("xml로 내보내기");
		rb_a = new JRadioButton("전체 목록 보기");
		rb_b = new JRadioButton("내 목록 보기");
		rb_c = new JRadioButton("수령 완료 목록 보기");
		
		ButtonGroup group = new ButtonGroup();
		
		group.add(rb_a);
		group.add(rb_b);
		group.add(rb_c);

		setLayout(new BorderLayout());

	
		p_north_right.setLayout(new FlowLayout(FlowLayout.RIGHT, 5, 20));

		p_north_right.add(rb_a);
		p_north_right.add(rb_b);
		p_north_right.add(rb_c);
		p_north_right.add(bt_copy);
		p_north_right.add(bt_xls);
		p_north_right.add(bt_find);

		p_north.add(p_north_right);

		p_north.setBorder(new EtchedBorder(EtchedBorder.RAISED));

		p_south.add(scroll);

		scroll.setPreferredSize(new Dimension(680, 500));
	

		// p_north_left.setPreferredSize(new Dimension(350, 100));
		// p_north_right.setPreferredSize(new Dimension(350, 100));
		p_north.setPreferredSize(new Dimension(700, 80));
		
		bt_copy.addActionListener(this); 
		bt_find.addActionListener(this); 
		bt_xls.addActionListener(this); 
		rb_a.addActionListener(this); 
		rb_b.addActionListener(this); 
		rb_c.addActionListener(this); 
		
		rb_a.setBackground(Color.pink);
		rb_b.setBackground(Color.pink);
		rb_c.setBackground(Color.pink);
		p_north.setBackground(Color.PINK);
		p_north_right.setBackground(Color.PINK);
		
		
		add(p_north, BorderLayout.NORTH);
		add(p_south, BorderLayout.CENTER);
		setVisible(true);
		setSize(700, 700);


		setVisible(true);
		setSize(700, 700);
		init();
	}
	
	public void init() {
		manager = DBManager.getInstance();
		this.con = manager.getConnection();
	}
	
	public void getList(StringBuffer sb){
		
		table.setModel(new UserModel(con,sb));
		table.updateUI();
	}
	
	public void copy(){
		
	}
	
	public void save(){
		
	}
	
	public void actionPerformed(ActionEvent e) {
		Object obj = e.getSource();
		if (obj == bt_find) {
			
		}
		else if (obj == bt_copy) {
			copy();
		}
		else if (obj == bt_xls) {
			save();
		}
		else if (obj == rb_a) {
			StringBuffer sb = new StringBuffer();
			sb.append("select INVOICE_ID as 상품, aptuser_name as 택배주인 , INVOICE_TAKETIME as 수령시간, INVOICE_TAKER as 수령인");
			sb.append(" from invoice i inner join aptuser a on a.aptuser_id = i.aptuser_id and a.aptuser_perm=503");
			getList(sb);
		}
		else if (obj == rb_b) {
			StringBuffer sb = new StringBuffer();
			sb.append("select INVOICE_ID as 상품, aptuser_name as 택배주인 , INVOICE_TAKETIME as 수령시간, INVOICE_TAKER as 수령인");
			sb.append(" from invoice i inner join aptuser a on a.aptuser_id = i.aptuser_id and a.aptuser_id=2011");
			getList(sb);
		}
		else if (obj == rb_c) {
			StringBuffer sb = new StringBuffer();
			sb.append("select INVOICE_ID as 상품, aptuser_name as 택배주인 , INVOICE_TAKETIME as 수령시간, INVOICE_TAKER as 수령인");
			sb.append(" from invoice i inner join aptuser a on a.aptuser_id = i.aptuser_id and i.invoice_taker is not null");
			getList(sb);
		}
	}
	public static void main(String[] args) {
		new User();
	}

}
