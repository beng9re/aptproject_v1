package aptuser;

public class ModifyAdmin extends ModifyUser {
	String[][] isEdit = { 
		{"회원ID", "Y"}, {"바코드", "Y"}, {"비밀번호", "Y"}, {"비밀번호 확인", "Y"},
		{"이름", "Y"}, {"연락처", "Y"}, {"등록일", "Y"}, {"주소", "Y"}
	};
	String titleStr = "관리자 정보";
	String btnTxt = "수정";
	
	public ModifyAdmin() {
		//관리자가 아니면 실행하지 않는다
		init(isEdit, titleStr, btnTxt);
		loadInfo();
	}
	
}
