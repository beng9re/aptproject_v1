package aptuser;

public class ModifyAdmin extends UserInfo {
	//회원ID, 바코드, 비밀번호, 비밀번호확인, 이름, 연락처, 등록일, 주소
	char[] isEdit = { 'N', 'Y', 'Y', 'Y', 'N', 'N', 'N', 'N' };
	String titleStr = "관리자 정보";
	String btnTxt = "수정";
	
	public ModifyAdmin() {
		init(isEdit, titleStr, btnTxt);
	}
	
	@Override
	public void confirm() {
		
	}
	
	public static void main(String[] args) {
		new ModifyAdmin();
	}

}
