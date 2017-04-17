package aptuser;

public class ModifyUser extends UserInfo {
	//회원ID, 바코드, 비밀번호, 비밀번호확인, 이름, 연락처, 등록일, 주소
	char[] isEdit = { 'N', 'N', 'Y', 'Y', 'Y', 'Y', 'N', 'N' };
	String titleStr = "회원정보 수정";
	String btnTxt = "수정";
	
	public ModifyUser() {
		init(isEdit, titleStr, btnTxt);
	}
	
	@Override
	public void confirm() {
		
	}
	
	public static void main(String[] args) {
		new ModifyUser();
	}

}