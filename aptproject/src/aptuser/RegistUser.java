package aptuser;

public class RegistUser extends UserInfo {
	//회원ID, 바코드, 비밀번호, 비밀번호확인, 이름, 연락처, 등록일, 주소
	char[] isEdit = { 'Y', 'Y', 'Y', 'Y', 'Y', 'Y', 'Y', 'Y' };
	String titleStr = "회원등록";
	String btnTxt = "등록";
	
	public RegistUser() {
		init(isEdit, titleStr, btnTxt);
	}
	
	@Override
	public void confirm() {
		
	}
	
	public static void main(String[] args) {
		new RegistUser();
	}

}
