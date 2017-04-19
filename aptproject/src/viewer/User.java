package viewer;

import java.awt.BorderLayout;
import java.awt.Choice;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Connection;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.EtchedBorder;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import db.DBManager;

public class User extends JPanel implements ActionListener {
	JPanel p_north, p_north_right, p_south;
	JTable table;
	JScrollPane scroll;
	Choice choice;
	JButton bt_find, bt_copy, bt_xls;
	JRadioButton rb_a, rb_b, rb_c;
	DBManager manager;
	Connection con;
	JFileChooser chooser;
	FileOutputStream fos;

	public User() {

		chooser = new JFileChooser();
		p_north = new JPanel();
		p_north_right = new JPanel();
		p_south = new JPanel();
		table = new JTable();
		scroll = new JScrollPane(table);
		bt_find = new JButton("��ȸ");
		bt_copy = new JButton("�μ�");
		bt_xls = new JButton("�������Ϸ� ��������");
		rb_a = new JRadioButton("��ü ��� ����");
		rb_b = new JRadioButton("�� ��� ����");
		rb_c = new JRadioButton("���� �Ϸ� ��� ����");

		ButtonGroup group = new ButtonGroup();

		setLayout(new BorderLayout());

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
		bt_copy.setBackground(Color.WHITE);
		bt_find.setBackground(Color.WHITE);
		bt_xls.setBackground(Color.WHITE);

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

	public void getList(StringBuffer sb) {

		table.setModel(new UserModel(con, sb));
		table.updateUI();
	}

	public void copy() {

	}

	public void createExcel() {
		HSSFWorkbook workBook = new HSSFWorkbook();
		HSSFSheet sheet = workBook.createSheet("���");
		for (int i = 0; i < table.getRowCount(); i++) {
			HSSFRow row = sheet.createRow(i);
			for (int j = 0; j < table.getColumnCount(); j++) {
				row.createCell(j).setCellValue((String) table.getValueAt(i, j));
			}
		}

		int result = chooser.showSaveDialog(this);
		if (result == JFileChooser.APPROVE_OPTION) {
			File file = chooser.getSelectedFile();

			try {
				fos = new FileOutputStream(file);
				workBook.write(fos);
				JOptionPane.showMessageDialog(this, "���� ���� ���� �Ϸ�");
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				if (fos != null) {
					try {
						fos.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}

		}

	}

	public void actionPerformed(ActionEvent e) {
		Object obj = e.getSource();
		if (obj == bt_find) {

		} else if (obj == bt_copy) {
			copy();
		} else if (obj == bt_xls) {
			createExcel();
		} else if (obj == rb_a) {
			StringBuffer sb = new StringBuffer();
			sb.append("select INVOICE_ID as ��ǰ, aptuser_name as �ù����� , INVOICE_TAKETIME as ���ɽð�, INVOICE_TAKER as ������");
			sb.append(" from invoice i inner join aptuser a on a.aptuser_id = i.aptuser_id and a.aptuser_perm=503");
			getList(sb);
		} else if (obj == rb_b) {
			StringBuffer sb = new StringBuffer();
			sb.append("select INVOICE_ID as ��ǰ, aptuser_name as �ù����� , INVOICE_TAKETIME as ���ɽð�, INVOICE_TAKER as ������");
			sb.append(" from invoice i inner join aptuser a on a.aptuser_id = i.aptuser_id and a.aptuser_id=2011");
			getList(sb);
		} else if (obj == rb_c) {
			StringBuffer sb = new StringBuffer();
			sb.append("select INVOICE_ID as ��ǰ, aptuser_name as �ù����� , INVOICE_TAKETIME as ���ɽð�, INVOICE_TAKER as ������");
			sb.append(
					" from invoice i inner join aptuser a on a.aptuser_id = i.aptuser_id and i.invoice_takeflag ='Y' ");
			getList(sb);
		}
	}

	public static void main(String[] args) {
		new User();
	}

}