package aptuser;

public class RegistUser extends UserInfo {
	// 회원ID, 바코드, 비밀번호, 비밀번호확인, 이름, 연락처, 등록일, 주소
	String[][] isEdit = { 
		{"회원ID", "Y"}, {"바코드", "Y"}, {"비밀번호", "Y"}, {"비밀번호 확인", "Y"},
		{"이름", "Y"}, {"연락처", "Y"}, {"등록일", "Y"}, {"주소", "Y"}
	};
	String titleStr = "회원등록";
	String btnTxt = "등록";

	public RegistUser() {
		init(isEdit, titleStr, btnTxt);
		setAddress();
	}
	
	private void setAddress() {
		CPUTModel cput = new CPUTModel(conn);
		
	}

	@Override
	public void confirm() {

	}

}
