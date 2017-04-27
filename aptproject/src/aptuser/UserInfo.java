package aptuser;

import java.awt.BorderLayout;
import java.awt.Choice;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.text.JTextComponent;

import db.AptuserModel;
import dto.Aptuser;

public abstract class UserInfo extends JPanel implements ActionListener {
	/*
	 * �ʵ��� �������ɿ��θ� ����, ��ӹ��� Ŭ�������� ���� String[][] isEdit = { {"ȸ��ID", "Y"},
	 * {"���ڵ�", "Y"}, {"��й�ȣ", "Y"}, {"��й�ȣ Ȯ��", "Y"}, {"�̸�", "Y"}, {"����ó", "Y"},
	 * {"�����", "Y"}, {"�ּ�", "Y"} };
	 */
	// �뵵������ ���� ����
	String[][] isEdit;
	boolean forUpdate;

	JPanel pnl_center, pnl_south;
	JLabel title;
	String titleStr;
	JLabel[] lbArr;
	HashMap<String, Component> fieldData;
	JButton btn_barcode, btn_confirm;
	String btnTxt;
	Font font;

	AptuserModel model;
	ArrayList<Aptuser> aptuser;

	// ���α׷� ��ü���� id�� conn�� �׻������̾���� (main���� ���� �����´�)
	String id;
	Connection conn;

	public UserInfo() {
		// �ʵ����
		fieldData = new HashMap<>();
		fieldData.put("ȸ��ID", new JTextField());
		fieldData.put("���ڵ�", new JTextField());
		fieldData.put("��й�ȣ", new JPasswordField());
		fieldData.put("��й�ȣ Ȯ��", new JPasswordField());
		fieldData.put("�̸�", new JTextField());
		fieldData.put("����ó", new JTextField());
		fieldData.put("�����", new JTextField());

		// ���ȭ�鿡�� ��¾���
		fieldData.put("�ּ�", new JTextField());
		// ���ȭ�鿡���� ���(�ּ��� �������ο� ��ġ)
		fieldData.put("����", new JRadioButton());
		fieldData.put("��", new Choice());
		fieldData.put("��", new Choice());
		fieldData.put("ȣ", new Choice());
	}

	/*
	 * ��ӽ� �뵵������ ���� ������ ��� ����� �ʵ�� ������ �� �������� �ʾƵ� �ǰ�, ��� �ּ��Է��� ���̽��� ���� �����ϰ�
	 * (�����ʹ� Complex, Unit ���κ��� �����´�)
	 */
	public void init(String[][] isEdit, String titleStr, String btnTxt, boolean forUpdate) {
		this.isEdit = isEdit;
		this.titleStr = titleStr;
		this.btnTxt = btnTxt;
		this.forUpdate = forUpdate;
		
		pnl_center = new JPanel();
		pnl_south = new JPanel();
		title = new JLabel(titleStr, JLabel.CENTER);
		lbArr = new JLabel[isEdit.length];
		btn_barcode = new JButton("���ڵ� ����");
		btn_confirm = new JButton(btnTxt);

		// �г� ��ü ���̾ƿ� ����
		setLayout(new BorderLayout());
		pnl_center.add(title);
		pnl_center.add(Box.createVerticalStrut(140));
		JPanel pan = null;
		font = new Font("���� ���", Font.BOLD, 16);
		
		// ȭ�鿡 ǥ�õ� �Է��ʵ带 �����Ѵ�
		mkFields();
		
		pnl_south.add(btn_barcode);
		pnl_south.add(Box.createHorizontalStrut(40));
		pnl_south.add(btn_confirm);
		add(pnl_center, BorderLayout.CENTER);
		add(pnl_south, BorderLayout.SOUTH);

		// ��Ÿ�� ���� ����
		title.setFont(new Font("���� ���", Font.BOLD, 24));
		title.setPreferredSize(new Dimension(360, 55));
		title.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 2));
		btn_barcode.setBackground(Color.LIGHT_GRAY);
		btn_barcode.setPreferredSize(new Dimension(140, 40));
		btn_confirm.setBackground(Color.LIGHT_GRAY);
		btn_confirm.setPreferredSize(new Dimension(140, 40));
		pnl_center.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));
		pnl_center.setPreferredSize(new Dimension(600, 500));
		pnl_south.setBorder(BorderFactory.createEmptyBorder(20, 20, 40, 20));

		// �����ʿ���
		btn_barcode.addActionListener(this);
		btn_confirm.addActionListener(this);

		setPreferredSize(new Dimension(700, 700));
	}
	
	// �󺧰� �ؽ�Ʈ�ʵ� �����ư��鼭 add, �����, �ּ��� ��� ��Ȳ�� ���� �ٸ���
	private void mkFields() {
		JPanel pan;
		for (int i = 0; i < isEdit.length; i++) {
			pan = new JPanel();
			pan.setLayout(new BoxLayout(pan, BoxLayout.PAGE_AXIS));
			pan.setPreferredSize(new Dimension(250, 70));

			// ȸ���Է¿��� �ּ� == ���̽�������Ʈ��
			Component field = null;
			if (!forUpdate && (isEdit[i][0].equals("�����") || isEdit[i][0].equals("�ּ�"))) {
				// ȸ����Ͽ����� ���Ǵ� �ʵ带 �����Ѵ�
				mkBottomFields(pan, i);

			// ������������������ ����� ������Ʈ �Է� (���ܰ� �ƴϸ� �̰͸� �־ �ȴ�)
			} else {
				lbArr[i] = new JLabel(isEdit[i][0]);
				lbArr[i].setFont(font);
				pan.add(lbArr[i]);
				pan.add(Box.createVerticalStrut(10));
				field = fieldData.get(isEdit[i][0]);
				pan.add(field);
				// �������ɿ��� ����
				if (isEdit[i][1] == "N") {
					field.setEnabled(false);
					((JTextComponent)field).setDisabledTextColor(Color.GRAY);
				}
			}
			pan.add(Box.createVerticalStrut(10));
			pnl_center.add(pan);
		}
	}
	
	// ȸ�� ���� �ҷ�����(������ ���� �ҷ����� �ʾƵ� �ȴ�)
	public void loadInfo() {
		// ID�� ���������� ã�Ƽ� �����´�
		model = new AptuserModel(conn, id);
		aptuser = model.getData();

		// �ʵ忡 ȸ�������� �Է��Ѵ�
		((JTextField) fieldData.get("ȸ��ID")).setText(aptuser.get(0).getAptuser_id());
		((JTextField) fieldData.get("���ڵ�")).setText(aptuser.get(0).getAptuser_code());
		((JTextField) fieldData.get("�̸�")).setText(aptuser.get(0).getAptuser_name());
		((JTextField) fieldData.get("����ó")).setText(aptuser.get(0).getAptuser_phone());
		((JTextField) fieldData.get("�����")).setText(aptuser.get(0).getAptuser_regdate());
		String address = aptuser.get(0).getComplex_name() + " " + aptuser.get(0).getUnit_name();
		((JTextField) fieldData.get("�ּ�")).setText(address);
	}

	// �ʵ� �Է¿��� Ȯ��
	private void fieldCheck() {
		if (autoCheck()) {
			//�ʵ尡 ��������� �ƹ��͵� ���� ����
		} else {
			// ���������� ��й�ȣ ��ȿ�� �˻�
			String pw1 = String.valueOf(((JPasswordField) fieldData.get("��й�ȣ")).getPassword());
			String pw2 = String.valueOf(((JPasswordField) fieldData.get("��й�ȣ Ȯ��")).getPassword());
			if (pw1.equals(pw2)) {
				// ������ ���� ��й�ȣ �˻�(ȸ���߰� �Ҷ��� �Է°���)
				if (pw1.equals("") && pw2.equals("") && !forUpdate) {
					JOptionPane.showMessageDialog(this, "��й�ȣ�� �Է����ּ���");
				} else {
					confirm();
				}
			} else {
				JOptionPane.showMessageDialog(this, "��й�ȣ�� ��ġ���� �ʽ��ϴ�");
			}
		}
	}

	// ��й�ȣ �ʵ带 ������ �׸��� �Է¿��� Ȯ��
	private boolean autoCheck() {
		// ����ִ��� Ȯ���ؾ��ϴ� �ʵ�
		String[] nullChkField = { "ȸ��ID", "�̸�", "����ó" };
		for (String chkField : nullChkField) {
			if (((JTextField) fieldData.get(chkField)).getText().equals("")) {
				JOptionPane.showMessageDialog(this, chkField + "��(��) �Է����ּ���");
				return true;
			}
		}
		// ����ó ����üũ
		String phoneNum = ((JTextField) fieldData.get("����ó")).getText();
		if(!phoneNum.matches("^\\d{2,3}-\\d{3,4}-\\d{4}$") || 
				!phoneNum.matches("^01(?:0|1[6-9])-(?:\\d{3}|\\d{4})-\\d{4}$")) {
			JOptionPane.showMessageDialog(this, "��ȭ��ȣ�� 032-225-1212�� \n 010-2255-1212 ���� �������� �Է����ּ���");
			return true;
		}
		  
		// ȸ�� ���� ���� �ʵ� Ȯ��
		if (!((JTextField) fieldData.get("ȸ��ID")).getText().equals("") && !forUpdate) {
			return regFieldChk();
		}
		return false;
	}
	
	/*
	 * ���ڵ� ���� ��Ģ : ���� 4�ڸ� + �ð���� ���� 8�ڸ�
	 * ���ڵ��ʵ�� unique�����̾ �ߺ��˻縦 �ؾ�������
	 * �������� Ȯ���ƴϸ� �ߺ��ȵǹǷ� �����Ƽ� ����
	 */
	private String mkBarcode() {
		Random rand = new Random();
		String randNum1 = String.format("%04d", rand.nextInt(10000));
		String randNum2Temp = Long.toString(System.currentTimeMillis());
		String randNum2 = randNum2Temp.substring(randNum2Temp.length()-10, randNum2Temp.length());
		// ������ ���ڵ带 �ʵ忡 ���
		((JTextField) fieldData.get("���ڵ�")).setText(randNum1+randNum2);
		return randNum1+randNum2;
	}
	
	// ȸ�� �ĺ� ���ڵ� ���� �޼���
	private void barcodeOption() {
		String barcodeData = null;
		if (((JTextField) fieldData.get("���ڵ�")).getText().equals("")) {
			// ���ڵ� ����
			barcodeData = mkBarcode();
			JOptionPane.showMessageDialog(this, "���ڵ尡 �����Ǿ����ϴ�");
		} else {
			// ���ڵ� �����
			int res = JOptionPane.showConfirmDialog(this, "���ڵ带 ����� �Ͻðڽ��ϱ�?", "���ڵ� ����", JOptionPane.OK_CANCEL_OPTION);
			if (res == JOptionPane.OK_OPTION) {
				barcodeData = mkBarcode();
			}
		}
		// ���Ϸ� ���
		int isFile = JOptionPane.showConfirmDialog(this, "���ڵ带 ���Ϸ� ����Ͻðڽ��ϱ�?", "���ڵ� ���Ϻ�ȯ", JOptionPane.OK_CANCEL_OPTION);
		if (isFile == JOptionPane.OK_OPTION) {
			JFileChooser chooser = new JFileChooser();
			chooser.setFileFilter(new FileNameExtensionFilter("PNG File", "png"));
			int saveRes = chooser.showSaveDialog(this);
			if (saveRes == JFileChooser.APPROVE_OPTION) {
				File file = chooser.getSelectedFile();
				String path = file.getAbsolutePath();
				new MakeBarcode(this, barcodeData, path);
			}
		}
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource()==btn_barcode) {
			barcodeOption();
		} else if (e.getSource()==btn_confirm) {
			fieldCheck();
		}
	}

	// ȸ�� ��Ͽ����� @Override
	abstract protected void mkBottomFields(JPanel pan, int i);
	
	// �߰� �ʵ� üũ(ȸ����Ͽ��� ���), �ʿ信 ���� @Override
	abstract protected boolean regFieldChk();

	// ��ư�� ������ �� ���۳���
	abstract protected void confirm();

}
