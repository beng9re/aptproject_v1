package aptuser;

import java.awt.BorderLayout;
import java.awt.Choice;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

import db.DBManager;
import dto.Aptuser;

public abstract class UserInfo extends JPanel {
/*
 *	�ʵ��� �������ɿ��θ� ����, ��ӹ��� Ŭ�������� ����
	String[][] isEdit = { 
		{"ȸ��ID", "Y"}, {"���ڵ�", "Y"}, {"��й�ȣ", "Y"}, {"��й�ȣ Ȯ��", "Y"},
		{"�̸�", "Y"}, {"����ó", "Y"}, {"�����", "Y"}, {"�ּ�", "Y"}
	};
*/
//	�뵵������ ���� ����
	String[][] isEdit;
	boolean forUpdate;
	
	JPanel pnl_center, pnl_south;
	JLabel title;
	String titleStr;
	JLabel[] lbArr;
	HashMap<String, Component> fieldData;
	JButton btn_confirm;
	String btnTxt;

	AptuserByIDModel model;
	ArrayList<Aptuser> aptuser;

	// ���α׷� ��ü���� id�� conn�� �׻������̾���� (main���� ���� �����´�)
	String id = "admin";
	DBManager dbMgr = DBManager.getInstance();
	Connection conn = dbMgr.getConnection();

	public UserInfo() {
		//�ʵ����
		fieldData = new HashMap<>();
		fieldData.put("ȸ��ID", new JTextField());
		fieldData.put("���ڵ�", new JTextField());
		fieldData.put("��й�ȣ", new JPasswordField());
		fieldData.put("��й�ȣ Ȯ��", new JPasswordField());
		fieldData.put("�̸�", new JTextField());
		fieldData.put("����ó", new JTextField());
		fieldData.put("�����", new JTextField());
		
		//���ȭ�鿡�� ��¾���
		fieldData.put("�ּ�", new JTextField());
		//���ȭ�鿡���� ���(�ּ��� �������ο� ��ġ)
		fieldData.put("����", new JRadioButton());
		fieldData.put("��", new Choice());
		fieldData.put("��", new Choice());
		fieldData.put("ȣ", new Choice());
	}
	
	/*
	 * ��ӽ� �뵵������ ���� ������ ��� ����� �ʵ�� ������ �� �������� �ʾƵ� �ǰ�,
	 * ��� �ּ��Է��� ���̽��� ���� �����ϰ� (�����ʹ� CPUTModel�κ��� �����´�)
	 */
	public void init(String[][] isEdit, String titleStr, String btnTxt) {
		this.isEdit = isEdit;
		this.titleStr = titleStr;
		this.btnTxt = btnTxt;

		pnl_center = new JPanel();
		pnl_south = new JPanel();
		title = new JLabel(titleStr, JLabel.CENTER);
		lbArr = new JLabel[isEdit.length];
		btn_confirm = new JButton(btnTxt);

		// �г� ��ü ���̾ƿ� ����
		setLayout(new BorderLayout());
		pnl_center.add(title);
		pnl_center.add(Box.createVerticalStrut(140));
		JPanel pan = null;
		Font font = new Font("���� ���", Font.BOLD, 16);
		
		for (int i = 0; i < isEdit.length; i++) {
			pan = new JPanel();
			pan.setLayout(new BoxLayout(pan, BoxLayout.PAGE_AXIS));
			pan.setPreferredSize(new Dimension(250, 70));
			
			// �󺧰� �ؽ�Ʈ�ʵ� �����ư��鼭 add, �ּ��� ��� ��Ȳ�� ���� �ٸ���
			
			//ȸ���Է¿��� �ּ� == ���̽�������Ʈ��
			Component field = null;
			if ( !forUpdate && (isEdit[i][0].equals("�����") || isEdit[i][0].equals("�ּ�")) ) {
				String lbTxt = null;
				JPanel pnl_sub = new JPanel();
				pnl_sub.setLayout(new BoxLayout(pnl_sub, BoxLayout.LINE_AXIS));
				pnl_sub.setAlignmentX(Component.LEFT_ALIGNMENT);
				if (isEdit[i][0].equals("�����")) {
					lbTxt = "����";
					field = fieldData.get("����");
					JLabel desc = new JLabel("�����ڸ� üũ���ּ���");
					desc.setFont(font);
					pnl_sub.add(field);
					pnl_sub.add(desc);
				} else {
					lbTxt = "�ּ��Է�";
					pnl_sub.add(fieldData.get("��"));
					pnl_sub.add(Box.createHorizontalStrut(5));
					pnl_sub.add(fieldData.get("��"));
					pnl_sub.add(Box.createHorizontalStrut(5));
					pnl_sub.add(fieldData.get("ȣ"));
				}
				
				lbArr[i] = new JLabel(lbTxt);
				lbArr[i].setFont(font);
				pan.add(lbArr[i]);
				pan.add(Box.createVerticalGlue());
				pan.add(pnl_sub);
			
			//������������������ ����� ������Ʈ �Է� (���ܰ� �ƴϸ� �̰͸� �־ �ȴ�)
			} else {
				lbArr[i] = new JLabel(isEdit[i][0]);
				lbArr[i].setFont(font);
				pan.add(lbArr[i]);
				pan.add(Box.createVerticalStrut(10));
				field = fieldData.get(isEdit[i][0]);
				pan.add(field);
				//�������ɿ��� ����
				if (isEdit[i][1] == "N") {
					field.setEnabled(false);
				}
			}
			pan.add(Box.createVerticalStrut(10));
			pnl_center.add(pan);
		}

		pnl_south.add(btn_confirm);
		add(pnl_center, BorderLayout.CENTER);
		add(pnl_south, BorderLayout.SOUTH);

		// ��Ÿ�� ���� ����
		title.setFont(new Font("���� ���", Font.BOLD, 24));
		title.setPreferredSize(new Dimension(360, 55));
		title.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 2));
		btn_confirm.setBackground(Color.LIGHT_GRAY);
		btn_confirm.setPreferredSize(new Dimension(120, 40));
		pnl_center.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));
		pnl_center.setPreferredSize(new Dimension(600, 500));
		pnl_south.setBorder(BorderFactory.createEmptyBorder(20, 20, 40, 20));

		// �����ʿ���
		btn_confirm.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
//				fieldCheck();
			}
		});

		setPreferredSize(new Dimension(700, 700));
	}

	// ȸ�� ���� �ҷ�����(������ ���� �ҷ����� �ʾƵ� �ȴ�)
	public void loadInfo() {
		// ȭ�� �뵵�� �����Ѵ�
		forUpdate = true;
		// ID�� ���������� ã�Ƽ� �����´�
		model = new AptuserByIDModel(conn, id);
		aptuser = model.getData();

		// �ʵ忡 ȸ�������� �Է��Ѵ�
		((JTextField)fieldData.get("ȸ��ID")).setText(aptuser.get(0).getAptuser_id());
		((JTextField)fieldData.get("���ڵ�")).setText(aptuser.get(0).getAptuser_code());
		((JTextField)fieldData.get("�̸�")).setText(aptuser.get(0).getAptuser_name());
		((JTextField)fieldData.get("����ó")).setText(aptuser.get(0).getAptuser_phone());
		((JTextField)fieldData.get("�����")).setText(aptuser.get(0).getAptuser_regdate());
		String address = aptuser.get(0).getComplex_name() + " " + aptuser.get(0).getUnit_name();
		((JTextField)fieldData.get("�ּ�")).setText(address);
	}

	// �ʵ� �Է¿��� Ȯ��
	private void fieldCheck() {
		if (autoCheck()) {
		} else {
			// ���������� ��й�ȣ ��ȿ�� �˻�
			String pw1 = String.valueOf(((JPasswordField)fieldData.get("��й�ȣ")).getPassword());
			String pw2 = String.valueOf(((JPasswordField)fieldData.get("��й�ȣ Ȯ��")).getPassword());
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
//		for (int i = 0; i < txfArr.length; i++) {
//			if (&&isEdit[i]=='Y') {
//				JOptionPane.showMessageDialog(this, column[i][0] + "��(��) �Է����ּ���");
//				return true;
//			}
//		}
		return false;
	}

	// ��ư�� ������ �� ���۳���
	abstract public void confirm();

}
