package aptuser;

import java.sql.Connection;

import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import main.TreeMain;

public class ModifyUser extends UserInfo {
	String[][] isEdit = { { "ȸ��ID", "N" }, { "���ڵ�", "N" }, { "��й�ȣ", "Y" }, { "��й�ȣ Ȯ��", "Y" }, { "�̸�", "Y" },
			{ "����ó", "Y" }, { "�����", "N" }, { "�ּ�", "N" } };
	String titleStr = "ȸ������ ����";
	String btnTxt = "�� ��";
	TreeMain main;
	
	// ����� ���� �⺻ ������
	public ModifyUser() {
	}
	
	public ModifyUser(Connection conn, TreeMain main) {
		this.conn = conn;
		this.main = main;
		this.id = main.getUserID();
		
		init(isEdit, titleStr, btnTxt, true);
		loadInfo();
	}

	// ���������̹Ƿ� �������� ����
	@Override
	protected void mkBottomFields(JPanel pan, int i) {
	}

	// id�ߺ�Ȯ�� ���� ����(false)
	@Override
	protected boolean regFieldChk() {
		return false;
	}

	@Override
	public void confirm() {
		aptuser.get(0).setAptuser_name(((JTextField) fieldData.get("�̸�")).getText());
		aptuser.get(0).setAptuser_code(((JTextField) fieldData.get("���ڵ�")).getText());
		String passwd = String.valueOf(((JPasswordField) fieldData.get("��й�ȣ")).getPassword());
		if (!passwd.equals("")) {
			aptuser.get(0).setAptuser_pw(String.valueOf(((JPasswordField) fieldData.get("��й�ȣ")).getPassword()));
		}
		aptuser.get(0).setAptuser_phone(((JTextField) fieldData.get("����ó")).getText());
		model.updateData();
		
		main.updateUser();
		JOptionPane.showMessageDialog(this, "������ �����Ǿ����ϴ�");
	}

}