package aptuser;

import java.awt.Component;
import java.util.ArrayList;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import dto.Aptuser;
import dto.ViewCPUT;

public class RegistUser extends UserInfo {
	// ȸ��ID, ���ڵ�, ��й�ȣ, ��й�ȣȮ��, �̸�, ����ó, �����, �ּ�
	String[][] isEdit = { { "ȸ��ID", "Y" }, { "���ڵ�", "Y" }, { "��й�ȣ", "Y" }, { "��й�ȣ Ȯ��", "Y" }, { "�̸�", "Y" },
			{ "����ó", "Y" }, { "�����", "Y" }, { "�ּ�", "Y" } };
	String titleStr = "ȸ�����";
	String btnTxt = "�� ��";
	// �ߺ��Ǵ� ID�� �ִ��� Ȯ���ϴµ� ����ϰ�, �ߺ��Ǵ� ID�� ������ null���� �ǹǷ� �Է¿� ��Ȱ��
	AptuserByIDModel duChkModel;

	public RegistUser() {
		init(isEdit, titleStr, btnTxt, false);
		setAddress();
	}

	protected void mkBottomFields(JPanel pan, int i) {
		Component field;
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
	}

	// �ּ��Է��� ���ؼ� ���̽�������Ʈ�� ��ϵ� �ּҵ� ����� ����
	private void setAddress() {
		CPUTModel cputModel = new CPUTModel(conn);
		ArrayList<ViewCPUT> cput = cputModel.getData();
		ArrayList<String> cpName = new ArrayList<String>();
		for (ViewCPUT item : cput) {
			cpName.add(item.getComplex_name());
		}
		cpName.retainAll(cpName);
	}

	// ȸ��ID �ߺ�Ȯ��, ����ִ� �ʵ�Ȯ�ο��� �������� ���� ��
	protected boolean idDuChk() {
		String new_id = ((JTextField) fieldData.get("ȸ��ID")).getText();
		duChkModel = new AptuserByIDModel(conn, new_id);
		ArrayList<Aptuser> res_id = duChkModel.getData();
		// �ߺ��Ǵ� ID�� ������ true�� ��ȯ�Ѵ�
		if (res_id.size() != 0) {
			JOptionPane.showMessageDialog(this, new_id + "�� �̹� �����մϴ�. �ٸ� ID�� �Է����ּ���");
			return true;
		}
		return false;
	}

	@Override
	// �ʵ峻���� ��� insert�Ѵ�
	protected void confirm() {
		ArrayList<Aptuser> insertData = duChkModel.getData();
		Aptuser insertUser = new Aptuser();
		insertUser.setAptuser_id(((JTextField) fieldData.get("ȸ��ID")).getText());
		insertUser.setAptuser_code(((JTextField) fieldData.get("���ڵ�")).getText());
		insertUser.setAptuser_pw(String.valueOf(((JPasswordField) fieldData.get("��й�ȣ")).getPassword()));
		insertUser.setAptuser_name(((JTextField) fieldData.get("�̸�")).getText());
		insertUser.setAptuser_phone(((JTextField) fieldData.get("����ó")).getText());
		// ���� �ּҸ� ��ȯ�ؼ� �Է��Ѵ�
		// insertUser.setUnit_id(unit_id);
		insertData.add(insertUser);
//		duChkModel.insertData();
	}

}
