package aptuser;

import javax.swing.JOptionPane;

public class ModifyUser extends UserInfo {
	String[][] isEdit = {
		{"ȸ��ID", "Y"}, {"���ڵ�", "Y"}, {"��й�ȣ", "Y"}, {"��й�ȣ Ȯ��", "Y"},
		{"�̸�", "Y"}, {"����ó", "Y"}, {"�����", "Y"}, {"�ּ�", "Y"}
	};
	String titleStr = "ȸ������ ����";
	String btnTxt = "����";
	
	public ModifyUser() {
		init(isEdit, titleStr, btnTxt);
		loadInfo();
	}
	
	@Override
	public void confirm() {
		JOptionPane.showMessageDialog(this, "������ �����Ǿ����ϴ�");
		model.updateData();
	}
	
}