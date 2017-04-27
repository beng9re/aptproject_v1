package aptuser;

import java.awt.Choice;
import java.awt.Component;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.TreeMap;
import java.util.TreeSet;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

import db.AptuserModel;
import db.ComplexModel;
import db.UnitModel;
import dto.Aptuser;
import dto.Unit;

public class RegistUser extends UserInfo implements ItemListener {
	// 회원ID, 바코드, 비밀번호, 비밀번호확인, 이름, 연락처, 등록일, 주소
	String[][] isEdit = { { "회원ID", "Y" }, { "바코드", "N" }, { "비밀번호", "Y" }, { "비밀번호 확인", "Y" }, { "이름", "Y" },
			{ "연락처", "Y" }, { "등록일", "Y" }, { "주소", "Y" } };
	String titleStr = "회원등록";
	String btnTxt = "등 록";
	// 중복되는 ID가 있는지 확인하는데 사용하고, 중복되는 ID가 없으면 null값이 되므로 입력에 재활용
	AptuserModel duChkModel;
	TreeMap<Integer, Integer> complex;
	TreeMap<Integer, TreeSet<Integer>> unitTotal;
	
	Choice ch_complex, ch_floor, ch_unit;

	public RegistUser(Connection conn, String id) {
		this.conn = conn;
		this.id = id;
		ch_complex = ((Choice) fieldData.get("동"));
		ch_floor = ((Choice) fieldData.get("층"));
		ch_unit = ((Choice) fieldData.get("호"));
		
		init(isEdit, titleStr, btnTxt, false);
		
		ComplexModel cpModel = new ComplexModel(conn);
		complex = cpModel.getMap();
		setComplex();
		
		ch_complex.addItemListener(this);
		ch_floor.addItemListener(this);
		ch_unit.addItemListener(this);
	}

	protected void mkBottomFields(JPanel pan, int i) {
		String lbTxt = null;
		JPanel pnl_sub = new JPanel();
		pnl_sub.setLayout(new BoxLayout(pnl_sub, BoxLayout.LINE_AXIS));
		pnl_sub.setAlignmentX(Component.LEFT_ALIGNMENT);
		
		if (isEdit[i][0].equals("등록일")) {
			lbTxt = "권한";
			JLabel desc = new JLabel("관리자면 체크해주세요");
			desc.setFont(font);
			pnl_sub.add(fieldData.get("권한"));
			pnl_sub.add(desc);
		} else {
			lbTxt = "주소입력";
			pnl_sub.add(ch_complex);
			pnl_sub.add(Box.createHorizontalStrut(5));
			pnl_sub.add(ch_floor);
			pnl_sub.add(Box.createHorizontalStrut(5));
			pnl_sub.add(ch_unit);
		}

		lbArr[i] = new JLabel(lbTxt);
		lbArr[i].setFont(font);
		pan.add(lbArr[i]);
		pan.add(Box.createVerticalGlue());
		pan.add(pnl_sub);
	}

	// 주소입력을 위해서 초이스컴포넌트에 등록된 주소들 목록을 띄운다
	private void setComplex() {
		ch_complex.removeAll();
		ch_complex.add("동");
		for (Integer item : complex.keySet()) {
			ch_complex.add(item.toString() + "동");
		}
	}

	private void setFloor(String selectedItem) {
		UnitModel utModel = new UnitModel(conn, complex.get(conversion(selectedItem)));
		ArrayList unit = utModel.getData();
		ch_floor.removeAll();
		ch_floor.add("층");
		ch_unit.removeAll();
		ch_unit.add("호");

		// 맵에 층과 호를 담는다
		unitTotal = new TreeMap<Integer, TreeSet<Integer>>();
		int res = 0;
		TreeSet<Integer> unitList = null;
		for (int i = 0; i < unit.size(); i++) {
			res = ((Unit) unit.get(i)).getUnit_name();
			int floor = res / 100;
			if (unitTotal.get(floor) == null) {
				unitTotal.put(floor, new TreeSet<Integer>());
			}
			unitList = unitTotal.get(Integer.valueOf(floor));
			unitList.add(Integer.valueOf(res % 100));
		}

		// 초이스 컴포넌트에 추가한다
		for (Integer item : unitTotal.keySet()) {
			ch_floor.add(item.toString() + "층");
		}
	}

	private void setUnit(String selectedItem) {
		ch_unit.removeAll();
		ch_unit.add("호");
		
		// 초이스 컴포넌트에 추가한다
		for (Integer item : unitTotal.get(conversion(selectedItem))) {
			ch_unit.add(item + "호");
		}
	}
	
	private int conversion(String str) {
		return Integer.parseInt(str.replaceAll("\\D", ""));
	}

	@Override
	public void itemStateChanged(ItemEvent e) {
		if (e.getSource() == ch_complex && ((Choice) e.getSource()).getSelectedIndex() != 0) {
			setFloor(((Choice) e.getSource()).getSelectedItem());
		} else if (e.getSource() == ch_floor && ((Choice) e.getSource()).getSelectedIndex() != 0) {
			setUnit(((Choice) e.getSource()).getSelectedItem());
		}
	}

	// 회원ID 중복확인, 주소입력 여부 확인 : 비어있는 필드확인에서 연속으로 실행 됨
	protected boolean regFieldChk() {
		String new_id = ((JTextField) fieldData.get("회원ID")).getText();
		duChkModel = new AptuserModel(conn, new_id);
		ArrayList<Aptuser> res_id = duChkModel.getData();
		// 중복되는 ID가 있으면 true를 반환한다
		if (res_id.size() != 0) {
			JOptionPane.showMessageDialog(this, new_id + "는 이미 존재합니다. 다른 ID를 입력해주세요");
			return true;
		}
		
		// 주소필드 값이 있는지 확인한다
		if (((Choice)ch_unit).getSelectedItem()==null || ((Choice)ch_unit).getSelectedItem().equals("호")) {
			JOptionPane.showMessageDialog(this, "정확한 주소를 입력하세요");
			return true;
		}
		
		// 조건을 모두 만족하면 false 반환
		return false;
	}

	// 입력필드 초기화
	private void clearForms() {
		for (int i = 0; i < isEdit.length; i++) {
			((JTextField) fieldData.get(isEdit[i][0])).setText("");
		}
		((JRadioButton)fieldData.get("권한")).setSelected(false);
		setComplex();
		((Choice)ch_floor).removeAll();
		((Choice)ch_unit).removeAll();
	}
	
	// 입력필드의 주소를 가지고 와서 unit_id로 변환한다
	private int searchAddress() {
		int complex_id = complex.get(conversion(ch_complex.getSelectedItem()));
		int floor = conversion(ch_floor.getSelectedItem());
		int unit = conversion(ch_unit.getSelectedItem());
		int unit_name = floor*100+unit;
		// Unit 테이블 정보를 가지고 온다
		UnitModel unitModel = new UnitModel(conn);
		ArrayList<Unit> unitData = unitModel.getData();
		// 일치하는 unit_id를 검색한다
		for (int i=0; i<unitData.size(); i++) {
			if(unitData.get(i).getComplex_id()==complex_id && unitData.get(i).getUnit_name()==unit_name) {
				return unitData.get(i).getUnit_id();
			}
		}
		System.out.println("주소 변환 실패");
		return 0;
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
		
		// 권한에 관련된 라디오버튼 결과 값을 반영한다
		Object[] permission = ((JRadioButton) fieldData.get("권한")).getSelectedObjects();
		if (permission!=null) {
			insertUser.setAptuser_perm(9); //checked인 경우 관리자권한인 9를 부여
		}
		
		// 받은 주소를 변환해서 입력한다
		insertUser.setUnit_id(searchAddress());
		// DB에 데이터를 입력한다
		insertData.add(insertUser);
		duChkModel.insertData();
		
		// 입력필드를 초기화한다
		JOptionPane.showMessageDialog(this, "회원등록 완료");
		clearForms();
	}

}
