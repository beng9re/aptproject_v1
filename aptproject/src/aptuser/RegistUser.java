package aptuser;

public class RegistUser extends UserInfo {
	//ȸ��ID, ���ڵ�, ��й�ȣ, ��й�ȣȮ��, �̸�, ����ó, �����, �ּ�
	char[] isEdit = { 'Y', 'Y', 'Y', 'Y', 'Y', 'Y', 'Y', 'Y' };
	String titleStr = "ȸ�����";
	String btnTxt = "���";
	
	public RegistUser() {
		init(isEdit, titleStr, btnTxt);
	}
	
	@Override
	public void confirm() {
		
	}
	
	public static void main(String[] args) {
		new RegistUser();
	}

}
