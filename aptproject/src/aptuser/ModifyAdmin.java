package aptuser;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class ModifyAdmin extends ModifyUser {
	String[][] isEdit = {
		{"ȸ��ID", "N"}, {"���ڵ�", "Y"}, {"��й�ȣ", "Y"}, {"��й�ȣ Ȯ��", "Y"},
		{"�̸�", "Y"}, {"����ó", "Y"}, {"�����", "N"}, {"�ּ�", "N"}
	};
	String titleStr = "������ ����";
	String btnTxt = "�� ��";
	
	public ModifyAdmin() {
		init(isEdit, titleStr, btnTxt, true);
		loadInfo();
	}

}
