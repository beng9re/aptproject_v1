package aptuser;

public class ModifyAdmin extends ModifyUser {
	String[][] isEdit = { 
		{"ȸ��ID", "Y"}, {"���ڵ�", "Y"}, {"��й�ȣ", "Y"}, {"��й�ȣ Ȯ��", "Y"},
		{"�̸�", "Y"}, {"����ó", "Y"}, {"�����", "Y"}, {"�ּ�", "Y"}
	};
	String titleStr = "������ ����";
	String btnTxt = "����";
	
	public ModifyAdmin() {
		//�����ڰ� �ƴϸ� �������� �ʴ´�
		init(isEdit, titleStr, btnTxt);
		loadInfo();
	}
	
}
