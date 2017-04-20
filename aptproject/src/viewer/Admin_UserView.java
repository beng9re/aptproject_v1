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

public class Admin_UserView extends JPanel implements ActionListener {
	JPanel p_north, p_north_left, p_north_right, p_south;
	JTable table;
	JScrollPane scroll;
	Choice choice;
	JButton bt_find, bt_copy, bt_xls;
	JTextField t_input;

	DBManager manager;
	Connection con;
	AdminModel adminModel;
	InvoiceModel invoiceModel;
	UserModel userModel;
	JFileChooser chooser;

	JRadioButton rb_user;
	String tableName;

	FileOutputStream fos;

	Vector<Aptuser> user = new Vector<Aptuser>();
	Vector<InvoiceCategory> invoice = new Vector<InvoiceCategory>();

	public Admin_UserView() {
		chooser = new JFileChooser();
		p_north = new JPanel();

		p_north_left = new JPanel();
		p_north_right = new JPanel();
		p_south = new JPanel();
		table = new JTable();
		scroll = new JScrollPane(table);
		choice = new Choice();
		bt_find = new JButton("조회");
		bt_copy = new JButton("인쇄");
		bt_xls = new JButton("엑셀파일로 내보내기");
		t_input = new JTextField(10);
		rb_user = new JRadioButton("주민 목록");

		ButtonGroup group = new ButtonGroup();

		setLayout(new BorderLayout());
		group.add(rb_user);

		setLayout(new BorderLayout());

		p_north_left.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 10));
		p_north_right.setLayout(new FlowLayout(FlowLayout.RIGHT, 5, 10));

		p_north_left.add(rb_user);
		p_north_left.add(choice);
		p_north_left.add(t_input);

		p_north_right.add(bt_find);
		p_north_right.add(bt_copy);
		p_north_right.add(bt_xls);

		p_north.add(p_north_left);
		p_north.add(p_north_right);

		p_north.setBorder(new EtchedBorder(EtchedBorder.RAISED));

		p_south.add(scroll);

		scroll.setPreferredSize(new Dimension(680, 500));
		choice.setPreferredSize(new Dimension(150, 30));

		p_north.setPreferredSize(new Dimension(700, 80));

		bt_copy.addActionListener(this);
		bt_find.addActionListener(this);
		bt_xls.addActionListener(this);
		rb_user.addActionListener(this);

		table.addMouseListener(new MouseAdapter() {

			public void mouseClicked(MouseEvent e) {
				int row = table.getSelectedRow();
				int col = table.getSelectedColumn();
				String name = JOptionPane.showInputDialog("수정해 주세요");
				if (name != null) {
					adminModel.setValueAt(name, row, col);
					tableChanged(row, col);
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

		add(p_north, BorderLayout.NORTH);
		p_north.setBackground(Color.PINK);
		rb_user.setBackground(Color.PINK);
		p_north_left.setBackground(Color.PINK);
		p_north_right.setBackground(Color.PINK);
		bt_copy.setBackground(Color.WHITE);
		bt_find.setBackground(Color.WHITE);
		bt_xls.setBackground(Color.WHITE);
		add(p_south, BorderLayout.CENTER);
		setVisible(true);
		setSize(700, 700);

		init();

	}

	public void init() {
		manager = DBManager.getInstance();
		this.con = manager.getConnection();
	}

	public void getUser() {
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = "select * from aptuser";

		try {
			pstmt = con.prepareStatement(sql);
			rs = pstmt.executeQuery();

			choice.removeAll();

			ResultSetMetaData meta = rs.getMetaData();
			for (int i = 1; i <= meta.getColumnCount(); i++) {
				choice.add(meta.getColumnName(i));
			}

			while (rs.next()) {
				Aptuser dto = new Aptuser();
				dto.setAptuser_code(rs.getString("aptuser_code"));
				dto.setAptuser_id(rs.getString("aptuser_id"));
				dto.setAptuser_ip(rs.getString("aptuser_ip"));
				dto.setAptuser_live(rs.getString("aptuser_live"));
				dto.setAptuser_name(rs.getString("aptuser_name"));
				dto.setAptuser_perm(rs.getString("aptuser_perm"));
				dto.setAptuser_phone(rs.getString("aptuser_phone"));
				dto.setAptuser_pw(rs.getString("aptuser_pw"));
				dto.setAptuser_regdate(rs.getString("aptuser_regdate"));
				user.add(dto);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void getList() {

		table.setModel(adminModel = new AdminModel(con));
		table.updateUI();
	}

	public void selectTable() {
		String msg = t_input.getText();
		String data = choice.getSelectedItem();
		String sql = "select * from " + tableName + " where " + data + "= '" + msg + "' ";
		adminModel.getList(sql);

		table.updateUI();
	}

	public void actionPerformed(ActionEvent e) {
		Object obj = e.getSource();
		if (obj == bt_find) {
			selectTable();
		} else if (obj == bt_xls) {
			createExcel();
		} else if (obj == bt_copy) {

		} else if (obj == rb_user) {
			tableName = "aptuser";
			getUser();
			getList();
		}
	}

	public void tableChanged(int row, int col) {
		PreparedStatement pstmt = null;

		String value = (String) table.getValueAt(row, col);
		String column = adminModel.columnName.elementAt(col);

		String sql = "update " + tableName + " set " + column + "=" + "'" + value + "' ";
		sql += "where aptuser_id=" + table.getValueAt(row, 0);

		try {
			pstmt = con.prepareStatement(sql);
			int result = pstmt.executeUpdate();
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
		new Admin_UserView();
	}
}
