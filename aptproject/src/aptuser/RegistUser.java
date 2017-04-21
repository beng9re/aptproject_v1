package aptuser;

import java.awt.Component;
import java.util.ArrayList;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import dto.Aptuser;
import dto.ViewCPUT;

public class RegistUser extends UserInfo {
	// 회원ID, 바코드, 비밀번호, 비밀번호확인, 이름, 연락처, 등록일, 주소
	String[][] isEdit = { { "회원ID", "Y" }, { "바코드", "Y" }, { "비밀번호", "Y" }, { "비밀번호 확인", "Y" }, { "이름", "Y" },
			{ "연락처", "Y" }, { "등록일", "Y" }, { "주소", "Y" } };
	String titleStr = "회원등록";
	String btnTxt = "등 록";
	// 중복되는 ID가 있는지 확인하는데 사용하고, 중복되는 ID가 없으면 null값이 되므로 입력에 재활용
	AptuserByIDModel duChkModel;

	public RegistUser() {
		init(isEdit, titleStr, btnTxt, false);
		setAddress();
	}

	protected void mkBottomFields(JPanel pan, int i) {
		Component field;
		String lbTxt = null;
		JPanel pnl_sub = new JPanel();
		pnl_sub.setLayout(new BoxLayout(pnl_sub, BoxLayout.LINE_AXIS));
		pnl_sub.setAlignmentX(Component.LEFT_ALIGNMENT);
		if (isEdit[i][0].equals("등록일")) {
			lbTxt = "권한";
			field = fieldData.get("권한");
			JLabel desc = new JLabel("관리자면 체크해주세요");
			desc.setFont(font);
			pnl_sub.add(field);
			pnl_sub.add(desc);
		} else {
			lbTxt = "주소입력";
			pnl_sub.add(fieldData.get("동"));
			pnl_sub.add(Box.createHorizontalStrut(5));
			pnl_sub.add(fieldData.get("층"));
			pnl_sub.add(Box.createHorizontalStrut(5));
			pnl_sub.add(fieldData.get("호"));
		}

		lbArr[i] = new JLabel(lbTxt);
		lbArr[i].setFont(font);
		pan.add(lbArr[i]);
		pan.add(Box.createVerticalGlue());
		pan.add(pnl_sub);
	}

	// 주소입력을 위해서 초이스컴포넌트에 등록된 주소들 목록을 띄운다
	private void setAddress() {
		CPUTModel cputModel = new CPUTModel(conn);
		ArrayList<ViewCPUT> cput = cputModel.getData();
		ArrayList<String> cpName = new ArrayList<String>();
		for (ViewCPUT item : cput) {
			cpName.add(item.getComplex_name());
		}
		cpName.retainAll(cpName);
	}

	// 회원ID 중복확인, 비어있는 필드확인에서 연속으로 실행 됨
	protected boolean idDuChk() {
		String new_id = ((JTextField) fieldData.get("회원ID")).getText();
		duChkModel = new AptuserByIDModel(conn, new_id);
		ArrayList<Aptuser> res_id = duChkModel.getData();
		// 중복되는 ID가 있으면 true를 반환한다
		if (res_id.size() != 0) {
			JOptionPane.showMessageDialog(this, new_id + "는 이미 존재합니다. 다른 ID를 입력해주세요");
			return true;
		}
		return false;
	}

	@Override
	// 필드내용을 모두 insert한다
	protected void confirm() {
		ArrayList<Aptuser> insertData = duChkModel.getData();
		Aptuser insertUser = new Aptuser();
		insertUser.setAptuser_id(((JTextField) fieldData.get("회원ID")).getText());
		insertUser.setAptuser_code(((JTextField) fieldData.get("바코드")).getText());
		insertUser.setAptuser_pw(String.valueOf(((JPasswordField) fieldData.get("비밀번호")).getPassword()));
		insertUser.setAptuser_name(((JTextField) fieldData.get("이름")).getText());
		insertUser.setAptuser_phone(((JTextField) fieldData.get("연락처")).getText());
		// 받은 주소를 변환해서 입력한다
		// insertUser.setUnit_id(unit_id);
		insertData.add(insertUser);
//		duChkModel.insertData();
	}

}
