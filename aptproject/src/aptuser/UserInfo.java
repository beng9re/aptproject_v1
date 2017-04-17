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
	// �迭 isEdit�� �������ɿ���, column�� �׸��̸�
	char[] isEdit;
	String[][] column = {
			{ "ȸ��ID", "aptuser_id" },
			{ "���ڵ�", "aptuser_barcode" },
			{ "��й�ȣ", "aptuser_pw" },			
			{ "��й�ȣ Ȯ��", "aptuser_pw" },
			{ "�̸�", "aptuser_name" },
			{ "����ó", "aptuser_phone" },			
			{ "�����", "aptuser_regdate" },
			{ "�ּ�", "aptuser_address" }
		};
	JLabel title;
	JLabel[] lbArr;
	JTextField[] txfArr;
	JPasswordField[] pwArr;
	JButton btn_confirm;
	String titleStr;
	String btnTxt;

	//��ӽ� �뵵������ ���� ������ ���
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
		
		//�г� ��ü ���̾ƿ� ����
		setLayout(new BorderLayout());

		// �ݺ������� ������Ʈ ����
		pnl_center.add(title);
		pnl_center.add(Box.createVerticalStrut(140));
		JPanel pan = null;
		Font font = new Font("���� ���", Font.BOLD, 16);
		int j = 0; //��й�ȣ �ʵ� ������ ���� ����
		for (int i = 0; i < column.length; i++) {
			pan = new JPanel();
			pan.setLayout(new BoxLayout(pan, BoxLayout.Y_AXIS));
			pan.setPreferredSize(new Dimension(250, 70));

			// �󺧰� textfield �����ư��鼭 add
			lbArr[i] = new JLabel(column[i][0]);
			lbArr[i].setFont(font);
			pan.add(lbArr[i]);
			pan.add(Box.createVerticalStrut(10));

			// �ؽ�Ʈ�ʵ�� ��й�ȣ �ʵ� ����
			if (!column[i][1].equals("aptuser_pw")) {
				txfArr[i-j] = new JTextField();
				pan.add(txfArr[i-j]);
				// ������ ������ �����Ұ��� �ϰ�
				if (isEdit[i] == 'N') {
					txfArr[i-j].setEnabled(false);
				}

			} else {
				pwArr[j] = new JPasswordField();
				pan.add(pwArr[j]);
				// ������ ������ �����Ұ��� �ϰ�
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
		
		//��Ÿ�� ���� ����
		title.setFont(new Font("���� ���", Font.BOLD, 24));
		title.setPreferredSize(new Dimension(360, 55));
		title.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 2));
		btn_confirm.setBackground(Color.LIGHT_GRAY);
		btn_confirm.setPreferredSize(new Dimension(120, 40));
		pnl_center.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));
		pnl_center.setPreferredSize(new Dimension(600, 500));
		pnl_south.setBorder(BorderFactory.createEmptyBorder(20, 20, 40, 20));
		
		//�����ʿ���
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

	//ȸ�� ���� �ҷ�����
	public void loadInfo() {
		//���α׷� ��ü���� id�� conn�� �׻������̾����
		String id = "admin";
		DBManager dbMgr = DBManager.getInstance();
		Connection conn = dbMgr.getConnection();

		//ID�� ���������� ã�Ƽ� �����´�
		AptuserByIDModel model = new AptuserByIDModel(conn, id);
		ArrayList<Aptuser> aptuser = model.getData();
		
		//�ʵ忡 ȸ�������� �Է��Ѵ�
		txfArr[0].setText(aptuser.get(0).getAptuser_id());
		txfArr[1].setText(aptuser.get(0).getAptuser_code());
		txfArr[2].setText(aptuser.get(0).getAptuser_name());
		txfArr[3].setText(aptuser.get(0).getAptuser_phone());
		txfArr[4].setText(aptuser.get(0).getAptuser_regdate());
		txfArr[5].setText(Integer.toString(aptuser.get(0).getUnit_id()));
	}
	
	//��ư�� ������ �� ���۳���
	abstract public void confirm();
	
}
