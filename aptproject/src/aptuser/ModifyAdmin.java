package aptuser;

public class ModifyAdmin extends UserInfo {
	//ȸ��ID, ���ڵ�, ��й�ȣ, ��й�ȣȮ��, �̸�, ����ó, �����, �ּ�
	char[] isEdit = { 'N', 'Y', 'Y', 'Y', 'N', 'N', 'N', 'N' };
	String titleStr = "������ ����";
	String btnTxt = "����";
	
	public ModifyAdmin() {
		init(isEdit, titleStr, btnTxt);
	}
	
	@Override
	public void confirm() {
		
	}
	
	public static void main(String[] args) {
		new ModifyAdmin();
	}

}
