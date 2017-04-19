package aptuser;

public class ModifyAdmin extends ModifyUser {
	//회원ID, 바코드, 비밀번호, 비밀번호확인, 이름, 연락처, 등록일, 주소
	char[] isEdit = { 'N', 'Y', 'Y', 'Y', 'Y', 'Y', 'N', 'N' };
	String titleStr = "관리자 정보";
	String btnTxt = "수정";
	
	public ModifyAdmin() {
		//관리자가 아니면 실행하지 않는다
		init(isEdit, titleStr, btnTxt);
		loadInfo();
	}
	
}
