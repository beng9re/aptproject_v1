package aptuser;

import java.sql.Connection;

import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import main.TreeMain;

public class ModifyUser extends UserInfo {
	String[][] isEdit = { { "회원ID", "N" }, { "바코드", "N" }, { "비밀번호", "Y" }, { "비밀번호 확인", "Y" }, { "이름", "Y" },
			{ "연락처", "Y" }, { "등록일", "N" }, { "주소", "N" } };
	String titleStr = "회원정보 수정";
	String btnTxt = "수 정";
	TreeMain main;
	
	// 상속을 위한 기본 생성자
	public ModifyUser() {
	}
	
	public ModifyUser(Connection conn, TreeMain main) {
		this.conn = conn;
		this.main = main;
		this.id = main.getUserID();
		
		init(isEdit, titleStr, btnTxt, true);
		loadInfo();
	}

	// 정보수정이므로 구현하지 않음
	@Override
	protected void mkBottomFields(JPanel pan, int i) {
	}

	// id중복확인 하지 않음(false)
	@Override
	protected boolean regFieldChk() {
		return false;
	}

	@Override
	public void confirm() {
		aptuser.get(0).setAptuser_name(((JTextField) fieldData.get("이름")).getText());
		aptuser.get(0).setAptuser_code(((JTextField) fieldData.get("바코드")).getText());
		String passwd = String.valueOf(((JPasswordField) fieldData.get("비밀번호")).getPassword());
		if (!passwd.equals("")) {
			aptuser.get(0).setAptuser_pw(String.valueOf(((JPasswordField) fieldData.get("비밀번호")).getPassword()));
		}
		aptuser.get(0).setAptuser_phone(((JTextField) fieldData.get("연락처")).getText());
		model.updateData();
		
		main.updateUser();
		JOptionPane.showMessageDialog(this, "정보가 수정되었습니다");
	}

}