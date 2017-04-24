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

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import db.DBManager;
import dto.Aptuser;
import dto.InvoiceCategory;
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
	Vector<InvoiceCategory> invoice = new Vector<InvoiceCategory>();
	Vector<Returninv> returninv = new Vector<Returninv>();

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
						invoiceModel.setValueAt(name, row, 4);
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

//	public void getUser() {
//		PreparedStatement pstmt = null;
//		ResultSet rs = null;
//		String sql = "select * from aptuser";
//
//		try {
//			pstmt = con.prepareStatement(sql);
//			rs = pstmt.executeQuery();
//
//			choice.removeAll();
//
//			ResultSetMetaData meta = rs.getMetaData();
//			for (int i = 1; i <= meta.getColumnCount(); i++) {
//				choice.add(meta.getColumnName(i));
//			}
//
//			while (rs.next()) {
//				Aptuser dto = new Aptuser();
//				dto.setAptuser_code(rs.getString("aptuser_code"));
//				dto.setAptuser_id(rs.getString("aptuser_id"));
//				dto.setAptuser_ip(rs.getString("aptuser_ip"));
//				dto.setAptuser_live(rs.getString("aptuser_live"));
//				dto.setAptuser_name(rs.getString("aptuser_name"));
//				dto.setAptuser_perm(rs.getString("aptuser_perm"));
//				dto.setAptuser_phone(rs.getString("aptuser_phone"));
//				dto.setAptuser_pw(rs.getString("aptuser_pw"));
//				dto.setAptuser_regdate(rs.getString("aptuser_regdate"));
//				user.add(dto);
//			}
//		} catch (SQLException e) {
//			e.printStackTrace();
//		}
//	}

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
				dto.setReturninv_arr(rs.getString("retruninv_arr"));
				dto.setReturninv_barcode(rs.getString("retruninv_barcode"));
				dto.setReturninv_commennt(rs.getString("retruninv_comment"));
				dto.setReturninv_date(rs.getString("retruninv_date"));
				dto.setReturninv_dep(rs.getString("retruninv_dep"));
				dto.setReturninv_id(rs.getString("retruninv_id"));
				dto.setReturninv_time(rs.getString("retruninv_time"));
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
				InvoiceCategory dto = new InvoiceCategory();
				dto.setInvoice_arrtime(rs.getString("invoice_arrtime"));
				dto.setInvoice_code(rs.getString("invoice_code"));
				dto.setInvoice_id(rs.getString("invoice_id"));
				dto.setInvoice_takeflag(rs.getString("invoice_takeflag"));
				dto.setInvoice_taker(rs.getString("invoice_taker"));
				dto.setInvoice_taketime(rs.getString("invoice_taketime"));
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

	}

	public void selectTable() {
		String msg = t_input.getText();
		String data = choice.getSelectedItem();
		String sql = "select * from " + tableName + " where " + data + "= '" + msg + "' ";
		if (rb_allInvoice.isSelected()) {
			invoiceModel.getList(sql);
		} else if (rb_invoice.isSelected()) {
			invoiceModel.getList(sql);
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
			tableName = "invoice";
			String sql = "select * from invoice";
			getInvoice(sql);
			getList(sql);
		} else if (obj == rb_invoice) {
			tableName = "invoice";
			String sql = "select * from invoice where invoice_takeflag is null or invoice_takeflag='N'";
			getList(sql);
		} else if (obj == rb_breturn) {
			tableName = "returninv";
			String sql = "select * from returninv";
			getReturninv(sql);
			getList(sql);
		}
		else if (obj == rb_areturn) {
			tableName = "returninv";
			String sql = "select * from returninv";
			getReturninv(sql);
			getList(sql);
		}
	}

	public void tableChanged(int row) {
		PreparedStatement pstmt = null;
		int col = 4;
		String value = (String) table.getValueAt(row, col);
		String column = invoiceModel.columnName.elementAt(col);
		String date_col = invoiceModel.columnName.elementAt(5);

		String sql = "update " + tableName + " set " + column + "=" + "'" + value + "'," + date_col
				+ "= sysdate, invoice_takeflag='Y'";
		sql += " where invoice_id=" + table.getValueAt(row, 0);

		System.out.println(sql);
		try {
			pstmt = con.prepareStatement(sql);
			int result = pstmt.executeUpdate();

			String re = "select * from invoice where invoice_takeflag is null or invoice_takeflag='N'";
			invoiceModel.getList(re);
			String a = (String) invoiceModel.getValueAt(row, 5);
			table.setValueAt(a, row, 5);

			if (result != 0) {
				JOptionPane.showConfirmDialog(this, "업데이트 완료");

				table.updateUI();
			}
			System.out.println(sql);
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
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
