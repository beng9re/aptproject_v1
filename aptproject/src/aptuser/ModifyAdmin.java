package aptuser;

import java.sql.Connection;

import main.TreeMain;

public class ModifyAdmin extends ModifyUser {
	String[][] isEdit = { { "회원ID", "N" }, { "바코드", "N" }, { "비밀번호", "Y" }, { "비밀번호 확인", "Y" }, { "이름", "Y" },
			{ "연락처", "Y" }, { "등록일", "N" }, { "주소", "N" } };
	String titleStr = "관리자 정보";
	String btnTxt = "수 정";

	public ModifyAdmin(Connection conn, TreeMain main) {
		super.conn = conn;
		super.main = main;
		super.id = main.getUserID();
		
		init(isEdit, titleStr, btnTxt, true);
		loadInfo();
	}

}
