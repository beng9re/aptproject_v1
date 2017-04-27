package viewer;

import java.awt.BorderLayout;
import java.awt.Choice;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Vector;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.EtchedBorder;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableRowSorter;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import dto.Aptuser;
import dto.Invoice;
import dto.Returninv;

public class Admin_InvoiceView extends JPanel implements ActionListener {
	JPanel p_north_radio, p_north, p_north_left, p_north_right, p_center, p_south;
	JTable table;
	JScrollPane scroll;
	Choice choice;
	JButton bt_find, bt_copy, bt_xls;
	JTextField t_input;
	JTextArea area;

	Connection con;
	AdminModel adminModel;
	InvoiceModel invoiceModel;

	JFileChooser chooser;

	JRadioButton rb_allInvoice;
	JRadioButton rb_invoice;
	JRadioButton rb_breturn;
	JRadioButton rb_areturn;
	String tableName;

	FileOutputStream fos;

	Vector<Aptuser> user = new Vector<Aptuser>();
	Vector<Invoice> invoice = new Vector<Invoice>();
	Vector<Returninv> returninv = new Vector<Returninv>();

	Boolean boxflag;

	public Admin_InvoiceView(Connection con) {
	
		this.con = con;
		chooser = new JFileChooser();
		p_north = new JPanel();
		p_north_radio = new JPanel();
		p_north_left = new JPanel();
		p_north_right = new JPanel();
		p_center = new JPanel();
		p_south = new JPanel();
		table = new JTable();
		scroll = new JScrollPane(table);
		choice = new Choice();
		bt_find = new JButton("조회");
		bt_xls = new JButton("엑셀파일로 내보내기");
		t_input = new JTextField(15);
		rb_allInvoice = new JRadioButton("모든 택배 목록");
		rb_invoice = new JRadioButton("수령 전 택배 목록");
		rb_breturn = new JRadioButton("반송 전 물품");
		rb_areturn = new JRadioButton("반송 후 물품");
		area = new JTextArea(4, 45);

		rb_allInvoice.setSelected(true);
		ButtonGroup group = new ButtonGroup();

		setLayout(new BorderLayout());
		group.add(rb_allInvoice);
		group.add(rb_invoice);
		group.add(rb_breturn);
		group.add(rb_areturn);

		p_north_radio.setLayout(new GridLayout(2, 2));
		p_north_radio.add(rb_allInvoice);
		p_north_radio.add(rb_breturn);
		p_north_radio.add(rb_invoice);
		p_north_radio.add(rb_areturn);

		setLayout(new BorderLayout());

		p_north_left.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 10));
		p_north_right.setLayout(new FlowLayout(FlowLayout.RIGHT, 5, 10));
		// p_south.setLayout(new FlowLayout(FlowLayout.RIGHT, 700, 10));

		p_north_left.add(choice);
		p_north_left.add(t_input);
		p_north_right.add(bt_find);

		p_south.add(area);
		p_south.add(bt_xls);
		
		area.setVisible(false);

		p_north.add(p_north_radio);
		p_north.add(p_north_left);
		p_north.add(p_north_right);

		p_north.setBorder(new EtchedBorder(EtchedBorder.RAISED));

		p_center.add(scroll);

		scroll.setPreferredSize(new Dimension(680, 500));
		choice.setPreferredSize(new Dimension(150, 30));

		p_north.setPreferredSize(new Dimension(700, 80));
		p_south.setPreferredSize(new Dimension(700, 80));

		bt_find.addActionListener(this);
		bt_xls.addActionListener(this);
		rb_allInvoice.addActionListener(this);
		rb_invoice.addActionListener(this);
		rb_breturn.addActionListener(this);
		rb_areturn.addActionListener(this);
		table.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				choiceValue();
			}
		});

		t_input.addKeyListener(new KeyAdapter() {
			public void keyReleased(KeyEvent e) {
				int key = e.getKeyCode();
				if (key == KeyEvent.VK_ENTER) {
					selectTable();
				}
			}
		});
		
		table.addKeyListener(new KeyAdapter() {
			public void keyReleased(KeyEvent e) {
				int key = e.getKeyCode();
				if (key==KeyEvent.VK_UP || key==KeyEvent.VK_DOWN){
					choiceValue();
				}
			}
		});

		p_north.setBackground(Color.PINK);
		rb_allInvoice.setBackground(Color.PINK);
		rb_invoice.setBackground(Color.PINK);
		rb_breturn.setBackground(Color.PINK);
		rb_areturn.setBackground(Color.PINK);
		p_north_left.setBackground(Color.PINK);
		p_north_right.setBackground(Color.PINK);
		p_south.setBackground(Color.PINK);

		bt_find.setBackground(Color.WHITE);
		bt_xls.setBackground(Color.WHITE);
		add(p_center, BorderLayout.CENTER);
		add(p_north, BorderLayout.NORTH);
		add(p_south, BorderLayout.SOUTH);
		setVisible(true);
		setSize(700, 700);
		
		init();
	}
	public void init(){

		boxflag = true;
		tableName = "invoice";
		StringBuffer sb = new StringBuffer();
		sb.append(
				"select i.invoice_id as 송장ID, a.aptuser_id 회원ID, a.aptuser_name 이름, a.COMPLEX_NAME 동, a.unit_name 호, i.box_num 무인함번호,");
		sb.append(
				" i.invoice_barcode as 송장바코드, i.invoice_arrtime as 등록시간, i.invoice_taker as 수령인, i.invoice_taketime as 수령시간, i.invoice_takeflag as 수령여부 ");
		sb.append(" from  view_is i inner join view_ac a on i.APTUSER_ID=a.APTUSER_ID");

		getInvoice(sb.toString());
		getList(sb.toString());
	}
	
	public void getReturninv() {
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		StringBuffer sql = new StringBuffer();
		sql.append(
				"select r.returninv_id as 반송ID, r.returninv_barcode 반송바코드 ,i.aptuser_id 회원ID, i.aptuser_name 이름, i.COMPLEX_NAME 동,i.UNIT_NAME 호,r.returninv_time 등록시간, r.returninv_arr 입고시간, r.returninv_dep 출고시간,r.returninv_comment 메모");
		sql.append(" from returninv r inner join view_acis i");
		sql.append(" on i.invoice_id = r.invoice_id ");

		try {
			pstmt = con.prepareStatement(sql.toString());
			rs = pstmt.executeQuery();

			choice.removeAll();

			ResultSetMetaData meta = rs.getMetaData();
			for (int i = 1; i <= meta.getColumnCount(); i++) {
				choice.add(meta.getColumnName(i));
			}
			while (rs.next()) {
				Returninv dto = new Returninv();
				dto.setReturninv_id(rs.getString("반송ID"));
				dto.setReturninv_barcode(rs.getString("반송바코드"));
				// dto.setReturninv_date(rs.getString("반송날짜"));
				dto.setReturninv_commennt(rs.getString("메모"));
				dto.setReturninv_arr(rs.getString("입고시간"));
				dto.setReturninv_dep(rs.getString("출고시간"));
				dto.setReturninv_time(rs.getString("등록시간"));
				returninv.add(dto);

			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void getInvoice(String sql) {
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			pstmt = con.prepareStatement(sql);
			rs = pstmt.executeQuery();

			choice.removeAll();
			ResultSetMetaData meta = rs.getMetaData();
			for (int i = 1; i <= meta.getColumnCount(); i++) {
				choice.add(meta.getColumnName(i));
			}

			while (rs.next()) {
				Invoice dto = new Invoice();
				dto.setInvoice_barcode(rs.getString("송장바코드"));
				dto.setInvoice_id(rs.getString("송장ID"));
				dto.setInvoice_arrtime(rs.getString("등록시간"));
				dto.setInvoice_takeflag(rs.getString("수령여부"));
				dto.setInvoice_taker(rs.getString("수령인"));
				dto.setInvoice_taketime(rs.getString("수령시간"));
				dto.setAptuser_id(rs.getString("회원ID"));
				invoice.add(dto);

			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void getList(String sql) {
		if (rb_allInvoice.isSelected()) {
			table.setModel(invoiceModel = new InvoiceModel(con, sql));
			table.updateUI();
		} else if (rb_invoice.isSelected()) {
			table.setModel(invoiceModel = new InvoiceModel(con, sql));
			table.updateUI();
		} else if (rb_breturn.isSelected()) {
			table.setModel(invoiceModel = new InvoiceModel(con, sql));
			table.updateUI();
		} else if (rb_areturn.isSelected()) {
			table.setModel(invoiceModel = new InvoiceModel(con, sql));
			table.updateUI();
		}
		table.setRowSorter(new TableRowSorter(invoiceModel));
		JTableHeader header = table.getTableHeader();
		header.setBackground(Color.PINK);
	}

	public void selectTable() {
		StringBuffer sb = new StringBuffer();
		String msg = t_input.getText();
		String data = choice.getSelectedItem();
		String option = null;

		if (boxflag == true) {
			if (data.equals("송장ID")) {
				option = "invoice_id";
			} else if (data.equals("회원ID")) {
				option = "a.aptuser_id";
			} else if (data.equals("이름")) {
				option = "aptuser_name";
			} else if (data.equals("동")) {
				option = "a.COMPLEX_NAME";
			} else if (data.equals("동")) {
				option = "a.COMPLEX_NAME";
			} else if (data.equals("호")) {
				option = "a.unit_name";
			} else if (data.equals("무인함번호")) {
				option = "i.box_num";
			} else if (data.equals("송장바코드")) {
				option = "i.invoice_barcode";
			} else if (data.equals("등록시간")) {
				option = "i.invoice_arrtime";
			} else if (data.equals("수령인")) {
				option = "i.invoice_taker";
			} else if (data.equals("수령시간")) {
				option = "i.invoice_taketime";
			} else if (data.equals("수령여부")) {
				option = "i.invoice_takeflag";
			}

			sb.append(
					"select i.invoice_id as 송장ID, a.aptuser_id 회원ID, a.aptuser_name 이름, a.COMPLEX_NAME 동, a.unit_name 호, i.box_num 무인함번호,");
			sb.append(
					" i.invoice_barcode as 송장바코드, i.invoice_arrtime as 등록시간, i.invoice_taker as 수령인, i.invoice_taketime as 수령시간, i.invoice_takeflag as 수령여부 ");
			sb.append(" from  view_is i inner join view_ac a on i.APTUSER_ID=a.APTUSER_ID and " + option + "= '" + msg
					+ "' order by invoice_arrtime desc");
			

			invoiceModel.getList(sb.toString());

		} else if (boxflag == false) {
			if (data.equals("반송ID")) {
				option = "r.returninv_id";
			} else if (data.equals("회원ID")) {
				option = "i.aptuser_id";
			} else if (data.equals("이름")) {
				option = "i.aptuser_name";
			} else if (data.equals("동")) {
				option = "i.COMPLEX_NAME";
			} else if (data.equals("호")) {
				option = "i.UNIT_NAME";
			} else if (data.equals("등록시간")) {
				option = "r.returninv_time";
			} else if (data.equals("메모")) {
				option = "r.returninv_comment";
			} else if (data.equals("입고시간")) {
				option = "r.returninv_arr";
			} else if (data.equals("출고시간")) {
				option = "r.returninv_dep";
			} else if (data.equals("반송바코드")) {
				option = "r.returninv_barcode";
			}
		
			sb.append(
					"select r.returninv_id as 반송ID,r.returninv_comment 메모 i.aptuser_id 회원ID, i.aptuser_name 이름, i.COMPLEX_NAME 동,i.UNIT_NAME 호,r.returninv_time 등록시간,");
			sb.append(" , r.returninv_arr 입고시간, r.returninv_dep 출고시간,r.returninv_comment 메모 ");
			sb.append(" from returninv r inner join view_acis i");
			sb.append(" on i.invoice_id = r.invoice_id and " + option + "= '" + msg + "' order by returninv_arr desc");

			invoiceModel.getList(sb.toString());

		}
		table.updateUI();
	}

	public void actionPerformed(ActionEvent e) {
		Object obj = e.getSource();
		StringBuffer sb = new StringBuffer();
		if (obj == bt_find) {
			selectTable();
		} else if (obj == bt_xls) {
			createExcel();
		} else if (obj == rb_allInvoice) {
			area.setVisible(false);
			boxflag = true;
			tableName = "invoice";
			sb.append(
					"select i.invoice_id as 송장ID, a.aptuser_id 회원ID, a.aptuser_name 이름, a.COMPLEX_NAME 동, a.unit_name 호, i.box_num 무인함번호,");
			sb.append(
					" i.invoice_barcode as 송장바코드, i.invoice_arrtime as 등록시간, i.invoice_taker as 수령인, i.invoice_taketime as 수령시간, i.invoice_takeflag as 수령여부 ");
			sb.append(" from  view_is i inner join view_ac a on i.APTUSER_ID=a.APTUSER_ID");

			getInvoice(sb.toString());
			getList(sb.toString());
		} else if (obj == rb_invoice) {
			area.setVisible(false);
			boxflag = true;
			tableName = "invoice";
			sb.append(
					"select i.invoice_id as 송장ID, a.aptuser_id 회원ID, a.aptuser_name 이름, a.COMPLEX_NAME 동, a.unit_name 호, i.box_num 무인함번호,");
			sb.append(
					" i.invoice_barcode as 송장바코드, i.invoice_arrtime as 등록시간, i.invoice_taker as 수령인, i.invoice_taketime as 수령시간, i.invoice_takeflag as 수령여부 ");
			sb.append(
					" from  view_is i inner join view_ac a on i.APTUSER_ID=a.APTUSER_ID and (i.invoice_takeflag is null or i.invoice_takeflag='N') order by invoice_arrtime desc");
			getInvoice(sb.toString());
			getList(sb.toString());
		} else if (obj == rb_breturn) {
			area.setVisible(true);
			boxflag = false;
			tableName = "returninv";
			sb.append(
					"select r.returninv_id 반송ID, r.returninv_barcode 반송바코드 ,i.aptuser_id 회원ID, i.aptuser_name 이름, i.COMPLEX_NAME 동,i.UNIT_NAME 호,r.returninv_time 등록시간, r.returninv_arr 입고시간, r.returninv_dep 출고시간,r.returninv_comment 메모 ");
			sb.append(" from returninv r inner join view_acis i");
			sb.append(" on i.invoice_id = r.invoice_id and returninv_dep is null");

			getReturninv();
			getList(sb.toString());
		} else if (obj == rb_areturn) {
			area.setVisible(true);
			boxflag = false;
			tableName = "returninv";
			sb.append(
					"select r.returninv_id as 반송ID, r.returninv_barcode 반송바코드 ,i.aptuser_id 회원ID, i.aptuser_name 이름, i.COMPLEX_NAME 동,i.UNIT_NAME 호,r.returninv_time 등록시간, r.returninv_arr 입고시간, r.returninv_dep 출고시간,r.returninv_comment 메모");
			sb.append(" from returninv r inner join view_acis i");
			sb.append(" on i.invoice_id = r.invoice_id and returninv_dep is not null");
			getReturninv();
			getList(sb.toString());
		}

	}

	public void tableChanged(int row, int col) {
		if (rb_invoice.isSelected()) {
			PreparedStatement pstmt = null;
			String value = (String) table.getValueAt(row, 8);

			String sql = "update " + tableName + " set invoice_taker =" + "'" + value
					+ "', invoice_taketime= sysdate, invoice_takeflag='Y'";
			sql += " where invoice_id=" + table.getValueAt(row, 0);

			try {
				pstmt = con.prepareStatement(sql);
				int result = pstmt.executeUpdate();
				StringBuffer sb = new StringBuffer();
				sb.append(
						"select i.invoice_id as 송장ID, a.aptuser_id 회원ID, a.aptuser_name 이름, a.COMPLEX_NAME 동, a.unit_name 호, i.box_num 무인함번호,");
				sb.append(
						" i.invoice_barcode as 송장바코드, i.invoice_arrtime as 등록시간, i.invoice_taker as 수령인, i.invoice_taketime as 수령시간, i.invoice_takeflag as 수령여부 ");
				sb.append(" from  view_is i inner join view_ac a on i.APTUSER_ID=a.APTUSER_ID and invoice_taker is null");
				invoiceModel.getList(sb.toString());
				String a = (String) invoiceModel.getValueAt(row, 9);
				table.setValueAt(a, row, 9);

				if (result != 0) {
					table.updateUI();
				}

			} catch (SQLException e1) {

				e1.printStackTrace();
			} finally {
				if (pstmt != null) {
					try {
						pstmt.close();
					} catch (SQLException e1) {
						e1.printStackTrace();
					}
				}
			}
		}

		else if (rb_breturn.isSelected()) {
			PreparedStatement pstmt = null;
			String value = (String) table.getValueAt(row, col);
			String sql = null;
			if (col == 7) {
				sql = "update " + tableName + " set returninv_arr= sysdate";
				sql += " where returninv_id=" + table.getValueAt(row, 0);
			} else if (col == 8) {
				sql = "update " + tableName + " set returninv_dep= sysdate";
				sql += " where returninv_id=" + table.getValueAt(row, 0);
			}

			try {
				pstmt = con.prepareStatement(sql);
				int result = pstmt.executeUpdate();
				StringBuffer sb = new StringBuffer();
				sb.append(
						"select r.returninv_id 반송ID, r.returninv_barcode 반송바코드 ,i.aptuser_id 회원ID, i.aptuser_name 이름, i.COMPLEX_NAME 동,i.UNIT_NAME 호,r.returninv_time 등록시간, r.returninv_arr 입고시간, r.returninv_dep 출고시간,r.returninv_comment 메모");
				sb.append(" from returninv r inner join view_acis i");
				sb.append(" on i.invoice_id = r.invoice_id and returninv_dep is null");
				invoiceModel.getList(sb.toString());
				String a = (String) invoiceModel.getValueAt(row, col);
				table.setValueAt(a, row, col);

				if (result != 0) {
					table.updateUI();
				}

			} catch (SQLException e1) {

				e1.printStackTrace();
			} finally {
				if (pstmt != null) {
					try {
						pstmt.close();
					} catch (SQLException e1) {
						e1.printStackTrace();
					}
				}
			}
		}
	}

	public void createExcel() {
		HSSFWorkbook workBook = new HSSFWorkbook();
		HSSFSheet sheet = workBook.createSheet();
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
				JOptionPane.showMessageDialog(this, "엑셀 생성 완료");
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
	public void choiceValue(){
		int row = table.getSelectedRow();
		int col = table.getSelectedColumn();

		if (rb_invoice.isSelected()) {
			String name = JOptionPane.showInputDialog("수령인의 성함을 기입해 주세요");
			if (name != null) {
				invoiceModel.setValueAt(name, row, 8);
				tableChanged(row, col);
			}
		} else if (rb_breturn.isSelected()) {
			String memo =(String)invoiceModel.getValueAt(row, 9);
			area.setText(memo);
			if (col == 7) {
				int cho = JOptionPane.showConfirmDialog(Admin_InvoiceView.this, "반송 입고를 등록하시겠습니까?");
				if (cho == 0) {
					tableChanged(row, col);
				}
			} else if (col == 8) {
				int cho = JOptionPane.showConfirmDialog(Admin_InvoiceView.this, "반송 출고를 등록하시겠습니까?");
				if (cho == 0) {
					tableChanged(row, col);
				}
			}
		} else if (rb_areturn.isSelected()) {

			String memo =(String)invoiceModel.getValueAt(row, 9);
			area.setText(memo);
		}
	}

}
