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
import javax.swing.JTextField;
import javax.swing.border.EtchedBorder;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableRowSorter;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor.PINK;

import db.DBManager;
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

	DBManager manager;
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

	public Admin_InvoiceView() {
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
		p_south.setLayout(new FlowLayout(FlowLayout.RIGHT, 20, 10));

		p_north_left.add(choice);
		p_north_left.add(t_input);
		p_north_right.add(bt_find);

		p_south.add(bt_xls);

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
				int row = table.getSelectedRow();
			
				if (rb_invoice.isSelected()) {
					String name = JOptionPane.showInputDialog("수령인의 성함을 기입해 주세요");
					if (name != null) {
						invoiceModel.setValueAt(name, row, 3);
						tableChanged(row);
					}
				}		
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

	public void init() {
		manager = DBManager.getInstance();
		this.con = manager.getConnection();
	}

	public void getReturninv(String sql) {
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
				Returninv dto = new Returninv();
				dto.setReturninv_arr(rs.getString("반송ID"));
				dto.setReturninv_barcode(rs.getString("등록시간"));
				dto.setReturninv_commennt(rs.getString("반송날짜"));
				dto.setReturninv_date(rs.getString("메모"));
				dto.setReturninv_dep(rs.getString("입고시간"));
				dto.setReturninv_id(rs.getString("출고시간"));
				dto.setReturninv_time(rs.getString("반송바코드"));
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
		String msg = t_input.getText();
		String data = choice.getSelectedItem();
		String option = null;

		if (boxflag == true) {
			if (data.equals("송장ID")) {
				option = "invoice_id";
			} else if (data.equals("송장바코드")) {
				option = "invoice_barcode";
			} else if (data.equals("등록시간")) {
				option = "invoice_arrtime";
			} else if (data.equals("수령인")) {
				option = "invoice_taker";
			} else if (data.equals("수령시간")) {
				option = "invoice_taketime";
			} else if (data.equals("수령여부")) {
				option = "invoice_takeflag";
			} else if (data.equals("회원ID")) {
				option = "aptuser_id";
			}
			String sql = "select invoice_id as 송장ID, invoice_barcode as 송장바코드, invoice_arrtime as 등록시간, invoice_taker as 수령인, invoice_taketime as 수령시간, invoice_takeflag as 수령여부, aptuser_id as 회원ID from "
					+ tableName + " where " + option + "= '" + msg + "' order by invoice_arrtime desc";
			if (rb_allInvoice.isSelected()) {
				invoiceModel.getList(sql);
			} else if (rb_invoice.isSelected()) {
				invoiceModel.getList(sql);
			}
		}
		else if (boxflag == false) {
			if (data.equals("반송ID")) {
				option = "returninv_id";
			} else if (data.equals("등록시간")) {
				option = "returninv_time";
			} else if (data.equals("반송날짜")) {
				option = "returninv_date";
			} else if (data.equals("메모")) {
				option = "returninv_comment";
			} else if (data.equals("입고시간")) {
				option = "returninv_arr";
			} else if (data.equals("출고시간")) {
				option = "returninv_dep";
			} else if (data.equals("반송바코드")) {
				option = "returninv_barcode";
			}
			String sql = "select returninv_id as 반송ID,returninv_time 등록시간, returninv_date 반송날짜,returninv_comment 메모, returninv_arr 입고시간, returninv_dep 출고시간, returninv_barcode 반송바코드    from "
					+ tableName + " where " + option + "= '" + msg + "' order by returninv_arr desc";
			if (rb_allInvoice.isSelected()) {
				invoiceModel.getList(sql);
			} else if (rb_invoice.isSelected()) {
				invoiceModel.getList(sql);
			}
		}
		table.updateUI();
	}

	public void actionPerformed(ActionEvent e) {
		Object obj = e.getSource();
		if (obj == bt_find) {
			selectTable();
		} else if (obj == bt_xls) {
			createExcel();
		} else if (obj == rb_allInvoice) {
			boxflag = true;
			tableName = "invoice";
			String sql = "select invoice_id as 송장ID, invoice_barcode as 송장바코드, invoice_arrtime as 등록시간, invoice_taker as 수령인, invoice_taketime as 수령시간, invoice_takeflag as 수령여부, aptuser_id as 회원ID from invoice order by invoice_arrtime desc";
			getInvoice(sql);
			getList(sql);
		} else if (obj == rb_invoice) {
			boxflag = true;
			tableName = "invoice";
			String sql = "select invoice_id as 송장ID, invoice_barcode as 송장바코드, invoice_arrtime as 등록시간, invoice_taker as 수령인, invoice_taketime as 수령시간, invoice_takeflag as 수령여부, aptuser_id as 회원ID from invoice where invoice_takeflag is null or invoice_takeflag='N' order by invoice_arrtime desc";
			getInvoice(sql);
			getList(sql);
		} else if (obj == rb_breturn) {
			boxflag = false;
			tableName = "returninv";
			String sql = "select returninv_id as 반송ID,returninv_time 등록시간, returninv_date 반송날짜,returninv_comment 메모, returninv_arr 입고시간, returninv_dep 출고시간, returninv_barcode 반송바코드    from returninv order by returninv_arr desc";
			getReturninv(sql);
			getList(sql);
		} else if (obj == rb_areturn) {
			boxflag = false;
			tableName = "returninv";
			String sql = "select returninv_id as 반송ID,returninv_time 등록시간, returninv_date 반송날짜,returninv_comment 메모, returninv_arr 입고시간 , returninv_dep 출고시간, returninv_barcode 반송바코드    from returninv where returninv_dep is not null order by returninv_arr desc";
			getReturninv(sql);
			getList(sql);
		}
		
	}

	public void tableChanged(int row) {
		PreparedStatement pstmt = null;
		int col = 3;
		String value = (String) table.getValueAt(row, col);
		
		
		String sql = "update " + tableName + " set invoice_taker =" + "'" + value + "', invoice_taketime= sysdate, invoice_takeflag='Y'";
		sql += " where invoice_id=" + table.getValueAt(row, 0);

		System.out.println(sql);
		try {
			pstmt = con.prepareStatement(sql);
			int result = pstmt.executeUpdate();

			String re = "select * from "+tableName;
			invoiceModel.getList(re);
			String a = (String) invoiceModel.getValueAt(row, 4);
			table.setValueAt(a, row, 4);

			if (result != 0) {
				JOptionPane.showConfirmDialog(this, "업데이트 완료");

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

	public static void main(String[] args) {
		new Admin_InvoiceView();
	}
}
