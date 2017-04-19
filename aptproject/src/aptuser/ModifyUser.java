package aptuser;

import javax.swing.JOptionPane;

public class ModifyUser extends UserInfo {
	//회원ID, 바코드, 비밀번호, 비밀번호확인, 이름, 연락처, 등록일, 주소
	char[] isEdit = { 'N', 'N', 'Y', 'Y', 'Y', 'Y', 'N', 'N' };
	String titleStr = "회원정보 수정";
	String btnTxt = "수정";
	
	public ModifyUser() {
		init(isEdit, titleStr, btnTxt);
		loadInfo();
	}
	
	@Override
	public void confirm() {
		String pw1 = String.valueOf(pwArr[0].getPassword());
		String pw2 = String.valueOf(pwArr[1].getPassword());
		if (pw1.equals(pw2)) {
			if (pw1.equals("") && pw2.equals("")) {
				System.out.println("비밀번호 공란");
			} else {
				aptuser.get(0).setAptuser_name(txfArr[2].getText());	
				aptuser.get(0).setAptuser_phone(txfArr[3].getText());
				JOptionPane.showMessageDialog(this, "정보가 수정되었습니다");
				model.updateData();
			}
		} else {
			JOptionPane.showMessageDialog(this, "비밀번호가 일치하지 않습니다");
		}
	}
	
}