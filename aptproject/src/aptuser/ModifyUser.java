package aptuser;

import javax.swing.JOptionPane;

public class ModifyUser extends UserInfo {
	//ȸ��ID, ���ڵ�, ��й�ȣ, ��й�ȣȮ��, �̸�, ����ó, �����, �ּ�
	char[] isEdit = { 'N', 'N', 'Y', 'Y', 'Y', 'Y', 'N', 'N' };
	String titleStr = "ȸ������ ����";
	String btnTxt = "����";
	
	public ModifyUser() {
		init(isEdit, titleStr, btnTxt);
		loadInfo();
	}
	
	@Override
	public void confirm() {
		String pw1 = String.valueOf(pwArr[0].getPassword());
		String pw2 = String.valueOf(pwArr[1].getPassword());
		if (pw1.equals(pw2)) {
			if (pw1.equals("") && pw2.equals("")) {
				System.out.println("��й�ȣ ����");
			} else {
				aptuser.get(0).setAptuser_name(txfArr[2].getText());	
				aptuser.get(0).setAptuser_phone(txfArr[3].getText());
				JOptionPane.showMessageDialog(this, "������ �����Ǿ����ϴ�");
				model.updateData();
			}
		} else {
			JOptionPane.showMessageDialog(this, "��й�ȣ�� ��ġ���� �ʽ��ϴ�");
		}
	}
	
}