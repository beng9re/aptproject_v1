package aptuser;

public class ModifyAdmin extends ModifyUser {
	//ȸ��ID, ���ڵ�, ��й�ȣ, ��й�ȣȮ��, �̸�, ����ó, �����, �ּ�
	char[] isEdit = { 'N', 'Y', 'Y', 'Y', 'Y', 'Y', 'N', 'N' };
	String titleStr = "������ ����";
	String btnTxt = "����";
	
	public ModifyAdmin() {
		//�����ڰ� �ƴϸ� �������� �ʴ´�
		init(isEdit, titleStr, btnTxt);
		loadInfo();
	}
	
}
