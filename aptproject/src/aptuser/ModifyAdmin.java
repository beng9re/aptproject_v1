package aptuser;

import java.sql.Connection;

public class ModifyAdmin extends ModifyUser {
	String[][] isEdit = { { "ȸ��ID", "N" }, { "���ڵ�", "N" }, { "��й�ȣ", "Y" }, { "��й�ȣ Ȯ��", "Y" }, { "�̸�", "Y" },
			{ "����ó", "Y" }, { "�����", "N" }, { "�ּ�", "N" } };
	String titleStr = "������ ����";
	String btnTxt = "�� ��";

	public ModifyAdmin(Connection conn, String id) {
		this.conn = conn;
		this.id = id;
		init(isEdit, titleStr, btnTxt, true);
		loadInfo();
	}

}
