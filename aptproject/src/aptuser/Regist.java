package aptuser;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

public abstract class Regist extends JFrame {
	JPanel pnl_center, pnl_south;
	// 배열 첫 번째는 수정가능여부, 두 번째는 항목이름
	char[] permission = { 'N', 'N', 'Y', 'Y', 'Y', 'Y', 'N', 'N' };
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
	JLabel[] lbArr = new JLabel[column.length];
	JTextField[] txfArr = new JTextField[column.length - 2];
	JPasswordField[] pwArr = new JPasswordField[2];
	JButton btn_confirm;
	String titleStr = "회원정보수정";
	String btnTxt = "수정";

/*	
	//상속시 용도변경을 위한 생성자
	public Regist(char[] permission, String titleStr, String btnTxt) {
		this.permission = permission;
		this.titleStr = titleStr;
		this.btnTxt = btnTxt;
		
	}
*/
	//패널을 생성하는 메서드
	public Regist() {
		pnl_center = new JPanel();
		pnl_south = new JPanel();
		title = new JLabel(titleStr, JLabel.CENTER);
		btn_confirm = new JButton(btnTxt);

		setLayout(new BorderLayout());

		// 반복문으로 오브젝트 생성
		pnl_center.add(title);
		pnl_center.add(Box.createVerticalStrut(140));
		JPanel pan = null;
		Font font = new Font("맑은 고딕", Font.BOLD, 16);
		int j = 0;
		int k = 0;
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
				txfArr[j] = new JTextField();
				pan.add(txfArr[j]);
				// 권한이 없으면 수정불가능 하게
				if (permission[i] == 'N') {
					txfArr[j].setEnabled(false);
				}
				j++;

			} else {
				pwArr[k] = new JPasswordField();
				pan.add(pwArr[k]);
				// 권한이 없으면 수정불가능 하게
				if (permission[i] == 'N') {
					pwArr[k].setEnabled(false);
				}
				k++;
			}

			pan.add(Box.createVerticalStrut(10));
			pnl_center.add(pan);
		}

		pnl_south.add(btn_confirm);
		add(pnl_center, BorderLayout.CENTER);
		add(pnl_south, BorderLayout.SOUTH);

		title.setFont(new Font("맑은 고딕", Font.BOLD, 24));
		title.setPreferredSize(new Dimension(360, 55));
		title.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 2));
		btn_confirm.setBackground(Color.LIGHT_GRAY);
		btn_confirm.setPreferredSize(new Dimension(120, 40));
		pnl_center.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));
		pnl_center.setPreferredSize(new Dimension(600, 500));
		pnl_south.setBorder(BorderFactory.createEmptyBorder(20, 20, 40, 20));

		btn_confirm.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				confirm();
			}
		});
		
//		setPreferredSize(new Dimension(700, 700));
		setSize(700, 700);
		setVisible(true);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
	}

	//버튼을 눌렀을 때 동작내용
	public abstract void confirm();

}
