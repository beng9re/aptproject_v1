package aptuser;

import java.sql.Connection;

import main.TreeMain;

public class ModifyAdmin extends ModifyUser {
	String[][] isEdit = { { "ȸ��ID", "N" }, { "���ڵ�", "N" }, { "��й�ȣ", "Y" }, { "��й�ȣ Ȯ��", "Y" }, { "�̸�", "Y" },
			{ "����ó", "Y" }, { "�����", "N" }, { "�ּ�", "N" } };
	String titleStr = "������ ����";
	String btnTxt = "�� ��";

	public ModifyAdmin(Connection conn, TreeMain main) {
		super.conn = conn;
		super.main = main;
		super.id = main.getUserID();
		
		init(isEdit, titleStr, btnTxt, true);
		loadInfo();
	}

}
