package aptuser;

public class ModifyAdmin extends UserInfo {
	//ȸ��ID, ���ڵ�, ��й�ȣ, ��й�ȣȮ��, �̸�, ����ó, �����, �ּ�
	char[] isEdit = { 'N', 'Y', 'Y', 'Y', 'N', 'N', 'N', 'N' };
	String titleStr = "������ ����";
	String btnTxt = "����";
	
	public ModifyAdmin() {
		init(isEdit, titleStr, btnTxt);
		loadInfo();
	}
	
	@Override
	public void confirm() {
//		AptuserByIDModel model = new AptuserByIDModel(conn, id);
	}
	
	public static void main(String[] args) {
		new ModifyAdmin();
	}

}
