package viewer;

import java.awt.BorderLayout;
import java.awt.Choice;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
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

import dto.Aptuser;
import dto.Invoice;

public class Admin_UserView extends JPanel implements ActionListener {
	JPanel p_north, p_north_left, p_north_right, p_center, p_south;
	JTable table;
	JScrollPane scroll;
	Choice choice;
	JButton bt_find, bt_copy, bt_xls;
	JTextField t_input;
	Connection con;
	AdminModel adminModel;
	InvoiceModel invoiceModel;
	UserModel userModel;
	JFileChooser chooser;

	JRadioButton rb_user;
	String tableName;

	FileOutputStream fos;

	Vector<Aptuser> user = new Vector<Aptuser>();
	Vector<Invoice> invoice = new Vector<Invoice>();

	public Admin_UserView(Connection con) {
		this.con = con;
		chooser = new JFileChooser();
		p_north = new JPanel();
		p_south = new JPanel();
		p_north_left = new JPanel();
		p_north_right = new JPanel();
		p_center = new JPanel();
		table = new JTable();
		scroll = new JScrollPane(table);
		choice = new Choice();
		bt_find = new JButton("��ȸ");
		bt_copy = new JButton("�μ�");
		bt_xls = new JButton("�������Ϸ� ��������");
		t_input = new JTextField(10);
		rb_user = new JRadioButton("�ֹ� ���");

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

		p_center.add(scroll);

		scroll.setPreferredSize(new Dimension(680, 500));
		choice.setPreferredSize(new Dimension(150, 30));

		p_north.setPreferredSize(new Dimension(700, 80));
		p_south.setPreferredSize(new Dimension(700, 80));

		bt_copy.addActionListener(this);
		bt_find.addActionListener(this);
		bt_xls.addActionListener(this);
		rb_user.addActionListener(this);
		
		table.setRowHeight(20);

		table.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				choiceValue();

			}
		});

		table.addKeyListener(new KeyAdapter() {
			public void keyReleased(KeyEvent e) {
				int key = e.getKeyCode();
				if (key == KeyEvent.VK_UP || key == KeyEvent.VK_DOWN) {
					choiceValue();
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
		rb_user.setBackground(Color.PINK);
		p_north_left.setBackground(Color.PINK);
		p_south.setBackground(Color.PINK);
		p_north_right.setBackground(Color.PINK);
		bt_copy.setBackground(Color.WHITE);
		bt_find.setBackground(Color.WHITE);
		bt_xls.setBackground(Color.WHITE);

		add(p_north, BorderLayout.NORTH);
		add(p_center, BorderLayout.CENTER);
		add(p_south, BorderLayout.SOUTH);

		setVisible(true);
		setSize(700, 700);
		init();
	}

	public void init() {
		rb_user.setSelected(true);
		tableName = "aptuser";
		getUser();
		getList();

	}

	public void getUser() {
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = "select aptuser_id �ֹ�ID,aptuser_name �̸� ,aptuser_phone ��ȭ��ȣ, aptuser_regdate ��ϳ�¥, aptuser_live ���ֿ���, complex_name �� ,unit_name ȣ  from view_ac order by aptuser_id asc";

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
				dto.setAptuser_id(rs.getString("�ֹ�ID"));
				dto.setAptuser_ip(rs.getString("�̸�"));
				dto.setAptuser_live(rs.getString("��ȭ��ȣ"));
				dto.setAptuser_name(rs.getString("��ϳ�¥"));
				// dto.setAptuser_perm(rs.getInt("�մ�"));
				dto.setAptuser_phone(rs.getString("���ֿ���"));
				// dto.setAptuser_regdate(rs.getString(""));
				user.add(dto);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void getList() {
		table.setModel(adminModel = new AdminModel(con));
		table.setRowSorter(new TableRowSorter(adminModel));
		JTableHeader header = table.getTableHeader();
		header.setBackground(Color.PINK);
		table.updateUI();
	}

	public void selectTable() {
		String msg = t_input.getText();
		String data = choice.getSelectedItem();
		String option = null;

		if (data.equals("�ֹ�ID")) {
			option = "aptuser_id";
		} else if (data.equals("�̸�")) {
			option = "aptuser_name";
		} else if (data.equals("��ȭ��ȣ")) {
			option = "aptuser_phone";
		} else if (data.equals("��ϳ�¥")) {
			option = "aptuser_regdate";
		} else if (data.equals("���ֿ���")) {
			option = "aptuser_live";
//		} else if (data.equals("��")) {
//			option = "complex_name";
//		} else if (data.equals("ȣ")) {
//			option = "unit_name";
		}

		String sql = "select aptuser_id �ֹ�ID,aptuser_name �̸� ,aptuser_phone ��ȭ��ȣ, aptuser_regdate ��ϳ�¥, aptuser_live ���ֿ���, complex_name �� ,unit_name ȣ  from view_ac where "
				+ option + "= '" + msg + "' order by aptuser_id asc";

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
		String data = choice.getItem(col);
		String option = null;

		if (data.equals("�ֹ�ID")) {
			option = "aptuser_id";
		} else if (data.equals("�̸�")) {
			option = "aptuser_name";
		} else if (data.equals("��ȭ��ȣ")) {
			option = "aptuser_phone";
		} else if (data.equals("��ϳ�¥")) {
			option = "aptuser_regdate";
		} else if (data.equals("���ֿ���")) {
			option = "aptuser_live";
//		} else if (data.equals("��")) {
//			option = "complex_name";
//		} else if (data.equals("ȣ")) {
//			option = "unit_name";
		}

		String sql = "update " + tableName + " set " + option + "=" + "'" + value + "' ";
		sql += "where aptuser_id=" + "'" + table.getValueAt(row, 0) + "'";

		try {
			pstmt = con.prepareStatement(sql);
			int result = pstmt.executeUpdate();
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
				JOptionPane.showMessageDialog(this, "���� ���� �Ϸ�");
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

	public void choiceValue() {
		int row = table.getSelectedRow();
		int col = table.getSelectedColumn();
		String colName = table.getColumnName(col);
		String value = (String) table.getValueAt(row, col);
		if (colName.equals("�̸�") || colName.equals("��ȭ��ȣ") || colName.equals("���ֿ���")) {
			String name = JOptionPane.showInputDialog(colName + "�� ������ �ּ���");
			if (name != null) {
				adminModel.setValueAt(name, row, col);
				tableChanged(row, col);
			}
		} else if (colName.equals("ȣ")) {
			PreparedStatement pstmt = null;
			String option = "unit_name";
			String name = JOptionPane.showInputDialog(colName + "�� ������ �ּ���");
			String sql = "update unit set " + option + "=" + "'" + name + "' ";
			sql += "where " + option + "= '" + value + "'";

			try {
				pstmt = con.prepareStatement(sql);
				pstmt.executeUpdate();

				if (name != null) {
					adminModel.setValueAt(name, row, col);
					for (int i = 0; i < table.getRowCount(); i++) {
						table.setValueAt(name, i, col);
					}
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
		} else if (colName.equals("��")) {
			PreparedStatement pstmt = null;
			String option = "complex_name";
			String name = JOptionPane.showInputDialog(colName + "�� ������ �ּ���");

			String sql = "update complex set " + option + "=" + "'" + name + "' ";
			sql += "where " + option + "= '" + value + "'";

			try {
				pstmt = con.prepareStatement(sql);
				pstmt.executeUpdate();

				if (name != null) {
					adminModel.setValueAt(name, row, col);
					for (int i = 0; i < table.getRowCount(); i++) {
						table.setValueAt(name, i, col);
					}
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
}
