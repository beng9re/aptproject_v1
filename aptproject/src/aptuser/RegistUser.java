package aptuser;

public class RegistUser extends UserInfo {
	// ȸ��ID, ���ڵ�, ��й�ȣ, ��й�ȣȮ��, �̸�, ����ó, �����, �ּ�
	String[][] isEdit = { 
		{"ȸ��ID", "Y"}, {"���ڵ�", "Y"}, {"��й�ȣ", "Y"}, {"��й�ȣ Ȯ��", "Y"},
		{"�̸�", "Y"}, {"����ó", "Y"}, {"�����", "Y"}, {"�ּ�", "Y"}
	};
	String titleStr = "ȸ�����";
	String btnTxt = "���";

	public RegistUser() {
		init(isEdit, titleStr, btnTxt);
		setAddress();
	}
	
	private void setAddress() {
		CPUTModel cput = new CPUTModel(conn);
		
	}

	@Override
	public void confirm() {

	}

}
