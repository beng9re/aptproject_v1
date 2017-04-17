package aptuser;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import db.Aptuser;
import db.DBManager;

public abstract class UserInfo extends JFrame {
	JPanel pnl_center, pnl_south;
	// 배열 isEdit은 수정가능여부, column은 항목이름
	char[] isEdit;
	String[][] column = {
			{ "회원ID", "aptuser_id" },
			{ "바코드", "aptuser_barcode" },
			{ "비밀번호", "aptuser_pw" },			
			{ "비밀번호 확인", "aptuser_pw" },
			{ "이름", "aptuser_name" },
			{ "연락처", "aptuser_phone" },			
			{ "등록일", "aptuser_regdate" },
			{ "주소", "aptuser_address" }
		};
	JLabel title;
	JLabel[] lbArr;
	JTextField[] txfArr;
	JPasswordField[] pwArr;
	JButton btn_confirm;
	String titleStr;
	String btnTxt;

	//상속시 용도변경을 위한 생성자 대용
	public void init(char[] isEdit, String titleStr, String btnTxt) {
		this.isEdit = isEdit;
		this.column = column;
		this.titleStr = titleStr;
		this.btnTxt = btnTxt;

		lbArr = new JLabel[column.length];
		txfArr = new JTextField[column.length - 2];
		pwArr = new JPasswordField[2];
		pnl_center = new JPanel();
		pnl_south = new JPanel();
		title = new JLabel(titleStr, JLabel.CENTER);
		btn_confirm = new JButton(btnTxt);
		
		//패널 전체 레이아웃 설정
		setLayout(new BorderLayout());

		// 반복문으로 오브젝트 생성
		pnl_center.add(title);
		pnl_center.add(Box.createVerticalStrut(140));
		JPanel pan = null;
		Font font = new Font("맑은 고딕", Font.BOLD, 16);
		int j = 0; //비밀번호 필드 생성을 위한 변수
		for (int i = 0; i < column.length; i++) {
			pan = new JPanel();
			pan.setLayout(new BoxLayout(pan, BoxLayout.Y_AXIS));
			pan.setPreferredSize(new Dimension(250, 70));

			// 라벨과 textfield 번갈아가면서 add
			lbArr[i] = new JLabel(column[i][0]);
			lbArr[i].setFont(font);
			pan.add(lbArr[i]);
			pan.add(Box.createVerticalStrut(10));

			// 텍스트필드와 비밀번호 필드 생성
			if (!column[i][1].equals("aptuser_pw")) {
				txfArr[i-j] = new JTextField();
				pan.add(txfArr[i-j]);
				// 권한이 없으면 수정불가능 하게
				if (isEdit[i] == 'N') {
					txfArr[i-j].setEnabled(false);
				}

			} else {
				pwArr[j] = new JPasswordField();
				pan.add(pwArr[j]);
				// 권한이 없으면 수정불가능 하게
				if (isEdit[i] == 'N') {
					pwArr[j].setEnabled(false);
				}
				j++;
			}

			pan.add(Box.createVerticalStrut(10));
			pnl_center.add(pan);
		}

		pnl_south.add(btn_confirm);
		add(pnl_center, BorderLayout.CENTER);
		add(pnl_south, BorderLayout.SOUTH);
		
		//스타일 관련 설정
		title.setFont(new Font("맑은 고딕", Font.BOLD, 24));
		title.setPreferredSize(new Dimension(360, 55));
		title.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 2));
		btn_confirm.setBackground(Color.LIGHT_GRAY);
		btn_confirm.setPreferredSize(new Dimension(120, 40));
		pnl_center.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));
		pnl_center.setPreferredSize(new Dimension(600, 500));
		pnl_south.setBorder(BorderFactory.createEmptyBorder(20, 20, 40, 20));
		
		//리스너연결
		btn_confirm.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				confirm();
			}
		});
		
//		setPreferredSize(new Dimension(700, 700));
		setVisible(true);
		setSize(700,700);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
	}

	//회원 정보 불러오기
	public void loadInfo() {
		//프로그램 전체에서 id와 conn은 항상보유중이어야함
		String id = "admin";
		DBManager dbMgr = DBManager.getInstance();
		Connection conn = dbMgr.getConnection();

		//ID로 유저정보를 찾아서 가져온다
		AptuserByIDModel model = new AptuserByIDModel(conn, id);
		ArrayList<Aptuser> aptuser = model.getData();
		
		//필드에 회원정보를 입력한다
		txfArr[0].setText(aptuser.get(0).getAptuser_id());
		txfArr[1].setText(aptuser.get(0).getAptuser_code());
		txfArr[2].setText(aptuser.get(0).getAptuser_name());
		txfArr[3].setText(aptuser.get(0).getAptuser_phone());
		txfArr[4].setText(aptuser.get(0).getAptuser_regdate());
		txfArr[5].setText(Integer.toString(aptuser.get(0).getUnit_id()));
	}
	
	//버튼을 눌렀을 때 동작내용
	abstract public void confirm();
	
}
