package aptuser;

import java.awt.BorderLayout;
import java.awt.Choice;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.text.JTextComponent;

import db.AptuserModel;
import dto.Aptuser;

public abstract class UserInfo extends JPanel implements ActionListener {
	/*
	 * 필드의 수정가능여부를 결정, 상속받은 클래스에서 정의 String[][] isEdit = { {"회원ID", "Y"},
	 * {"바코드", "Y"}, {"비밀번호", "Y"}, {"비밀번호 확인", "Y"}, {"이름", "Y"}, {"연락처", "Y"},
	 * {"등록일", "Y"}, {"주소", "Y"} };
	 */
	// 용도구분을 위한 변수
	String[][] isEdit;
	boolean forUpdate;

	JPanel pnl_center, pnl_south;
	JLabel title;
	String titleStr;
	JLabel[] lbArr;
	HashMap<String, Component> fieldData;
	JButton btn_barcode, btn_confirm;
	String btnTxt;
	Font font;

	AptuserModel model;
	ArrayList<Aptuser> aptuser;

	// 프로그램 전체에서 id와 conn은 항상보유중이어야함 (main으로 부터 가져온다)
	String id;
	Connection conn;

	public UserInfo() {
		// 필드생성
		fieldData = new HashMap<>();
		fieldData.put("회원ID", new JTextField());
		fieldData.put("바코드", new JTextField());
		fieldData.put("비밀번호", new JPasswordField());
		fieldData.put("비밀번호 확인", new JPasswordField());
		fieldData.put("이름", new JTextField());
		fieldData.put("연락처", new JTextField());
		fieldData.put("등록일", new JTextField());

		// 등록화면에서 출력안함
		fieldData.put("주소", new JTextField());
		// 등록화면에서만 출력(주소의 수정여부와 일치)
		fieldData.put("권한", new JRadioButton());
		fieldData.put("동", new Choice());
		fieldData.put("층", new Choice());
		fieldData.put("호", new Choice());
	}

	/*
	 * 상속시 용도변경을 위한 생성자 대용 등록일 필드는 가입할 때 보여주지 않아도 되고, 대신 주소입력을 초이스로 만들어서 선택하게
	 * (데이터는 Complex, Unit 으로부터 가져온다)
	 */
	public void init(String[][] isEdit, String titleStr, String btnTxt, boolean forUpdate) {
		this.isEdit = isEdit;
		this.titleStr = titleStr;
		this.btnTxt = btnTxt;
		this.forUpdate = forUpdate;
		
		pnl_center = new JPanel();
		pnl_south = new JPanel();
		title = new JLabel(titleStr, JLabel.CENTER);
		lbArr = new JLabel[isEdit.length];
		btn_barcode = new JButton("바코드 생성");
		btn_confirm = new JButton(btnTxt);

		// 패널 전체 레이아웃 설정
		setLayout(new BorderLayout());
		pnl_center.add(title);
		pnl_center.add(Box.createVerticalStrut(140));
		JPanel pan = null;
		font = new Font("맑은 고딕", Font.BOLD, 16);
		
		// 화면에 표시될 입력필드를 생성한다
		mkFields();
		
		pnl_south.add(btn_barcode);
		pnl_south.add(Box.createHorizontalStrut(40));
		pnl_south.add(btn_confirm);
		add(pnl_center, BorderLayout.CENTER);
		add(pnl_south, BorderLayout.SOUTH);

		// 스타일 관련 설정
		title.setFont(new Font("맑은 고딕", Font.BOLD, 24));
		title.setPreferredSize(new Dimension(360, 55));
		title.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 2));
		btn_barcode.setBackground(Color.LIGHT_GRAY);
		btn_barcode.setPreferredSize(new Dimension(140, 40));
		btn_confirm.setBackground(Color.LIGHT_GRAY);
		btn_confirm.setPreferredSize(new Dimension(140, 40));
		pnl_center.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));
		pnl_center.setPreferredSize(new Dimension(600, 500));
		pnl_south.setBorder(BorderFactory.createEmptyBorder(20, 20, 40, 20));

		// 리스너연결
		btn_barcode.addActionListener(this);
		btn_confirm.addActionListener(this);

		setPreferredSize(new Dimension(700, 700));
	}
	
	// 라벨과 텍스트필드 번갈아가면서 add, 등록일, 주소의 경우 상황에 따라 다르게
	private void mkFields() {
		JPanel pan;
		for (int i = 0; i < isEdit.length; i++) {
			pan = new JPanel();
			pan.setLayout(new BoxLayout(pan, BoxLayout.PAGE_AXIS));
			pan.setPreferredSize(new Dimension(250, 70));

			// 회원입력에서 주소 == 초이스컴포넌트로
			Component field = null;
			if (!forUpdate && (isEdit[i][0].equals("등록일") || isEdit[i][0].equals("주소"))) {
				// 회원등록에서만 사용되는 필드를 생성한다
				mkBottomFields(pan, i);

			// 정보수정페이지에서 사용할 컴포넌트 입력 (예외가 아니면 이것만 있어도 된다)
			} else {
				lbArr[i] = new JLabel(isEdit[i][0]);
				lbArr[i].setFont(font);
				pan.add(lbArr[i]);
				pan.add(Box.createVerticalStrut(10));
				field = fieldData.get(isEdit[i][0]);
				pan.add(field);
				// 수정가능여부 적용
				if (isEdit[i][1] == "N") {
					field.setEnabled(false);
					((JTextComponent)field).setDisabledTextColor(Color.GRAY);
				}
			}
			pan.add(Box.createVerticalStrut(10));
			pnl_center.add(pan);
		}
	}
	
	// 회원 정보 불러오기(가입할 때는 불러오지 않아도 된다)
	public void loadInfo() {
		// ID로 유저정보를 찾아서 가져온다
		model = new AptuserModel(conn, id);
		aptuser = model.getData();

		// 필드에 회원정보를 입력한다
		((JTextField) fieldData.get("회원ID")).setText(aptuser.get(0).getAptuser_id());
		((JTextField) fieldData.get("바코드")).setText(aptuser.get(0).getAptuser_code());
		((JTextField) fieldData.get("이름")).setText(aptuser.get(0).getAptuser_name());
		((JTextField) fieldData.get("연락처")).setText(aptuser.get(0).getAptuser_phone());
		((JTextField) fieldData.get("등록일")).setText(aptuser.get(0).getAptuser_regdate());
		String address = aptuser.get(0).getComplex_name() + " " + aptuser.get(0).getUnit_name();
		((JTextField) fieldData.get("주소")).setText(address);
	}

	// 필드 입력여부 확인
	private void fieldCheck() {
		if (autoCheck()) {
			//필드가 비어있으면 아무것도 하지 않음
		} else {
			// 마지막으로 비밀번호 유효성 검사
			String pw1 = String.valueOf(((JPasswordField) fieldData.get("비밀번호")).getPassword());
			String pw2 = String.valueOf(((JPasswordField) fieldData.get("비밀번호 확인")).getPassword());
			if (pw1.equals(pw2)) {
				// 목적에 따른 비밀번호 검사(회원추가 할때만 입력강제)
				if (pw1.equals("") && pw2.equals("") && !forUpdate) {
					JOptionPane.showMessageDialog(this, "비밀번호를 입력해주세요");
				} else {
					confirm();
				}
			} else {
				JOptionPane.showMessageDialog(this, "비밀번호가 일치하지 않습니다");
			}
		}
	}

	// 비밀번호 필드를 제외한 항목의 입력여부 확인
	private boolean autoCheck() {
		// 비어있는지 확인해야하는 필드
		String[] nullChkField = { "회원ID", "이름", "연락처" };
		for (String chkField : nullChkField) {
			if (((JTextField) fieldData.get(chkField)).getText().equals("")) {
				JOptionPane.showMessageDialog(this, chkField + "을(를) 입력해주세요");
				return true;
			}
		}
		// 연락처 형식체크
		String phoneNum = ((JTextField) fieldData.get("연락처")).getText();
		if(!phoneNum.matches("^\\d{2,3}-\\d{3,4}-\\d{4}$") || 
				!phoneNum.matches("^01(?:0|1[6-9])-(?:\\d{3}|\\d{4})-\\d{4}$")) {
			JOptionPane.showMessageDialog(this, "전화번호는 032-225-1212나 \n 010-2255-1212 같은 형식으로 입력해주세요");
			return true;
		}
		  
		// 회원 가입 관련 필드 확인
		if (!((JTextField) fieldData.get("회원ID")).getText().equals("") && !forUpdate) {
			return regFieldChk();
		}
		return false;
	}
	
	/*
	 * 바코드 생성 규칙 : 난수 4자리 + 시간기반 난수 8자리
	 * 바코드필드는 unique제약이어서 중복검사를 해야하지만
	 * 벼락맞을 확률아니면 중복안되므로 귀찮아서 생략
	 */
	private String mkBarcode() {
		Random rand = new Random();
		String randNum1 = String.format("%04d", rand.nextInt(10000));
		String randNum2Temp = Long.toString(System.currentTimeMillis());
		String randNum2 = randNum2Temp.substring(randNum2Temp.length()-10, randNum2Temp.length());
		// 생성된 바코드를 필드에 출력
		((JTextField) fieldData.get("바코드")).setText(randNum1+randNum2);
		return randNum1+randNum2;
	}
	
	// 회원 식별 바코드 생성 메서드
	private void barcodeOption() {
		String barcodeData = null;
		if (((JTextField) fieldData.get("바코드")).getText().equals("")) {
			// 바코드 생성
			barcodeData = mkBarcode();
			JOptionPane.showMessageDialog(this, "바코드가 생성되었습니다");
		} else {
			// 바코드 재생성
			int res = JOptionPane.showConfirmDialog(this, "바코드를 재생성 하시겠습니까?", "바코드 생성", JOptionPane.OK_CANCEL_OPTION);
			if (res == JOptionPane.OK_OPTION) {
				barcodeData = mkBarcode();
			}
		}
		// 파일로 출력
		int isFile = JOptionPane.showConfirmDialog(this, "바코드를 파일로 출력하시겠습니까?", "바코드 파일변환", JOptionPane.OK_CANCEL_OPTION);
		if (isFile == JOptionPane.OK_OPTION) {
			JFileChooser chooser = new JFileChooser();
			chooser.setFileFilter(new FileNameExtensionFilter("PNG File", "png"));
			int saveRes = chooser.showSaveDialog(this);
			if (saveRes == JFileChooser.APPROVE_OPTION) {
				File file = chooser.getSelectedFile();
				String path = file.getAbsolutePath();
				new MakeBarcode(this, barcodeData, path);
			}
		}
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource()==btn_barcode) {
			barcodeOption();
		} else if (e.getSource()==btn_confirm) {
			fieldCheck();
		}
	}

	// 회원 등록에서만 @Override
	abstract protected void mkBottomFields(JPanel pan, int i);
	
	// 추가 필드 체크(회원등록에서 사용), 필요에 따라 @Override
	abstract protected boolean regFieldChk();

	// 버튼을 눌렀을 때 동작내용
	abstract protected void confirm();

}
