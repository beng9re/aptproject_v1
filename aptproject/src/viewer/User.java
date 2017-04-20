package viewer;

import java.awt.BorderLayout;
import java.awt.Choice;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
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

public class User extends JPanel implements ActionListener, ItemListener {
	JPanel p_radio, p_north, p_north_right, p_center, p_south;
	JTable table;
	JScrollPane scroll;
	Choice choice;
	JButton bt_find, bt_xls;
	JRadioButton rb_a, rb_b;
	DBManager manager;
	Connection con;
	JFileChooser chooser;
	FileOutputStream fos;

	public User() {

		chooser = new JFileChooser();
		p_radio = new JPanel();
		p_north = new JPanel();
		p_north_right = new JPanel();
		p_center = new JPanel();
		p_south = new JPanel();
		table = new JTable();
		scroll = new JScrollPane(table);
		bt_find = new JButton("조회");
		bt_xls = new JButton("엑셀파일로 내보내기");
		rb_a = new JRadioButton("주문 물품");
		rb_b = new JRadioButton("반송 물품");
		choice = new Choice();

		choice.addItem("▼목록을 선택하세요");
		
		ButtonGroup group = new ButtonGroup();
		group.add(rb_a);
		group.add(rb_b);

		setLayout(new BorderLayout());
		p_radio.setLayout(new GridLayout(2, 1));

		p_radio.add(rb_a);
		p_radio.add(rb_b);

		p_north_right.add(p_radio);
		p_north_right.add(choice);
		p_north_right.add(bt_xls);
		p_north_right.add(bt_find);

		p_north.add(p_north_right);

		p_north.setBorder(new EtchedBorder(EtchedBorder.RAISED));

		p_center.add(scroll);

		scroll.setPreferredSize(new Dimension(680, 500));

		p_north.setPreferredSize(new Dimension(700, 80));
		p_south.setPreferredSize(new Dimension(700, 80));

		bt_find.addActionListener(this);
		bt_xls.addActionListener(this);
		rb_a.addActionListener(this);
		rb_b.addActionListener(this);
		choice.addItemListener(this);

		rb_a.setBackground(Color.pink);
		rb_b.setBackground(Color.pink);
		p_north.setBackground(Color.PINK);
		p_south.setBackground(Color.PINK);
		p_north_right.setBackground(Color.PINK);
		bt_find.setBackground(Color.WHITE);
		bt_xls.setBackground(Color.WHITE);

		add(p_north, BorderLayout.NORTH);
		add(p_center, BorderLayout.CENTER);
		add(p_south, BorderLayout.SOUTH);
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

	public void createExcel() {
		HSSFWorkbook workBook = new HSSFWorkbook();
		HSSFSheet sheet = workBook.createSheet("목록");
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
				JOptionPane.showMessageDialog(this, "엑셀 파일 생성 완료");
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

	public void itemStateChanged(ItemEvent e) {

		if (rb_a.isSelected()) {
			if (choice.getSelectedIndex() == 1) {
				StringBuffer sb = new StringBuffer();
				sb.append(
						"select INVOICE_ID as 상품, aptuser_name as 택배주인 , INVOICE_TAKETIME as 수령시간, INVOICE_TAKER as 수령인");
				sb.append(" from invoice i inner join aptuser a on a.aptuser_id = i.aptuser_id and a.aptuser_perm=503");
				getList(sb);
			} else if (choice.getSelectedIndex() == 2) {
				StringBuffer sb = new StringBuffer();
				sb.append(
						"select INVOICE_ID as 상품, aptuser_name as 택배주인 , INVOICE_TAKETIME as 수령시간, INVOICE_TAKER as 수령인");
				sb.append(" from invoice i inner join aptuser a on a.aptuser_id = i.aptuser_id and a.aptuser_id=2011");
				getList(sb);
			} else if (choice.getSelectedIndex() == 3) {
				StringBuffer sb = new StringBuffer();
				sb.append(
						"select INVOICE_ID as 상품, aptuser_name as 택배주인 , INVOICE_TAKETIME as 수령시간, INVOICE_TAKER as 수령인");
				sb.append(
						" from invoice i inner join aptuser a on a.aptuser_id = i.aptuser_id and i.invoice_takeflag ='N' ");
				getList(sb);
			} else if (choice.getSelectedIndex() == 4) {
				StringBuffer sb = new StringBuffer();
				sb.append(
						"select INVOICE_ID as 상품, aptuser_name as 택배주인 , INVOICE_TAKETIME as 수령시간, INVOICE_TAKER as 수령인");
				sb.append(
						" from invoice i inner join aptuser a on a.aptuser_id = i.aptuser_id and i.invoice_takeflag ='Y' ");
				getList(sb);
			}
		} else if (rb_b.isSelected()) {
			if (choice.getSelectedIndex() == 1) {
				StringBuffer sb = new StringBuffer();
				sb.append(
						"select returninv_id as 상품, aptuser_name as 택배주인 , INVOICE_TAKETIME as 수령시간, INVOICE_TAKER as 수령인");
				sb.append(" from returninv r inner join aptuser a on a.aptuser_id = r.aptuser_id and a.aptuser_perm=503");
				getList(sb);
			} else if (choice.getSelectedIndex() == 2) {
				StringBuffer sb = new StringBuffer();
				sb.append(
						"select returninv_id as 상품, aptuser_name as 택배주인 , INVOICE_TAKETIME as 수령시간, INVOICE_TAKER as 수령인");
				sb.append(" from returninv r inner join aptuser a on a.aptuser_id = r.aptuser_id and a.aptuser_id=2011");
				getList(sb);
			} else if (choice.getSelectedIndex() == 3) {
				StringBuffer sb = new StringBuffer();
				sb.append(
						"select returninv_id as 상품, aptuser_name as 택배주인 , INVOICE_TAKETIME as 수령시간, INVOICE_TAKER as 수령인");
				sb.append(
						" from returninv r inner join aptuser a on a.aptuser_id = r.aptuser_id and r.invoice_takeflag ='N' ");
				getList(sb);
			} else if (choice.getSelectedIndex() == 4) {
				StringBuffer sb = new StringBuffer();
				sb.append(
						"select returninv_id as 상품, aptuser_name as 택배주인 , INVOICE_TAKETIME as 수령시간, INVOICE_TAKER as 수령인");
				sb.append(
						" from returninv r inner join aptuser a on a.aptuser_id = r.aptuser_id and r.invoice_takeflag ='Y' ");
				getList(sb);
			}
		}
	}

	public void actionPerformed(ActionEvent e) {
		Object obj = e.getSource();
		if (obj == bt_find) {

		} else if (obj == bt_xls) {
			createExcel();
		} else if (obj == rb_a) {
			choice.removeAll();
			choice.addItem("▼목록을 선택하세요");
			choice.addItem("전체 목록보기");
			choice.addItem("내 목록보기");
			choice.addItem("수령 전 목록보기");
			choice.addItem("수령 완료 목록보기");
		} else if (obj == rb_b) {
			choice.removeAll();
			choice.addItem("▼목록을 선택하세요");
			choice.addItem("전체 목록보기");
			choice.addItem("내 목록보기");
			choice.addItem("반송 전 목록보기");
			choice.addItem("반송 완료  목록보기");
		}
	}

	public static void main(String[] args) {
		new User();
	}

}
