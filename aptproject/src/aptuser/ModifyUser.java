package aptuser;

import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

public class ModifyUser extends UserInfo {
	String[][] isEdit = { { "회원ID", "N" }, { "바코드", "N" }, { "비밀번호", "Y" }, { "비밀번호 확인", "Y" }, { "이름", "Y" },
			{ "연락처", "Y" }, { "등록일", "N" }, { "주소", "N" } };
	String titleStr = "회원정보 수정";
	String btnTxt = "수 정";

	public ModifyUser() {
		init(isEdit, titleStr, btnTxt, true);
		loadInfo();
	}

	// 정보수정이므로 구현하지 않음
	@Override
	protected void mkBottomFields(JPanel pan, int i) {
	}

	// id중복확인 하지 않음(false)
	@Override
	protected boolean idDuChk() {
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

		JOptionPane.showMessageDialog(this, "정보가 수정되었습니다");
	}

}