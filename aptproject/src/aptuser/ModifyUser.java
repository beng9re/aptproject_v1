package aptuser;

public class ModifyUser extends UserInfo {
	//ȸ��ID, ���ڵ�, ��й�ȣ, ��й�ȣȮ��, �̸�, ����ó, �����, �ּ�
	char[] isEdit = { 'N', 'N', 'Y', 'Y', 'Y', 'Y', 'N', 'N' };
	String titleStr = "ȸ������ ����";
	String btnTxt = "����";
	
	public ModifyUser() {
		init(isEdit, titleStr, btnTxt);
	}
	
	@Override
	public void confirm() {
		
	}
	
	public static void main(String[] args) {
		new ModifyUser();
	}

}