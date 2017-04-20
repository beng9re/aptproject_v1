package aptuser;

import javax.swing.JOptionPane;

public class ModifyUser extends UserInfo {
	String[][] isEdit = {
		{"회원ID", "Y"}, {"바코드", "Y"}, {"비밀번호", "Y"}, {"비밀번호 확인", "Y"},
		{"이름", "Y"}, {"연락처", "Y"}, {"등록일", "Y"}, {"주소", "Y"}
	};
	String titleStr = "회원정보 수정";
	String btnTxt = "수정";
	
	public ModifyUser() {
		init(isEdit, titleStr, btnTxt);
		loadInfo();
	}
	
	@Override
	public void confirm() {
		JOptionPane.showMessageDialog(this, "정보가 수정되었습니다");
		model.updateData();
	}
	
}