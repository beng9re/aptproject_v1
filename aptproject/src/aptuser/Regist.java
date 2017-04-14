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
	// �迭 ù ��°�� �������ɿ���, �� ��°�� �׸��̸�
	char[] permission = { 'N', 'N', 'Y', 'Y', 'Y', 'Y', 'N', 'N' };
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
	JLabel[] lbArr = new JLabel[column.length];
	JTextField[] txfArr = new JTextField[column.length - 2];
	JPasswordField[] pwArr = new JPasswordField[2];
	JButton btn_confirm;
	String titleStr = "ȸ����������";
	String btnTxt = "����";

/*	
	//��ӽ� �뵵������ ���� ������
	public Regist(char[] permission, String titleStr, String btnTxt) {
		this.permission = permission;
		this.titleStr = titleStr;
		this.btnTxt = btnTxt;
		
	}
*/
	//�г��� �����ϴ� �޼���
	public Regist() {
		pnl_center = new JPanel();
		pnl_south = new JPanel();
		title = new JLabel(titleStr, JLabel.CENTER);
		btn_confirm = new JButton(btnTxt);

		setLayout(new BorderLayout());

		// �ݺ������� ������Ʈ ����
		pnl_center.add(title);
		pnl_center.add(Box.createVerticalStrut(140));
		JPanel pan = null;
		Font font = new Font("���� ���", Font.BOLD, 16);
		int j = 0;
		int k = 0;
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
				txfArr[j] = new JTextField();
				pan.add(txfArr[j]);
				// ������ ������ �����Ұ��� �ϰ�
				if (permission[i] == 'N') {
					txfArr[j].setEnabled(false);
				}
				j++;

			} else {
				pwArr[k] = new JPasswordField();
				pan.add(pwArr[k]);
				// ������ ������ �����Ұ��� �ϰ�
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

		title.setFont(new Font("���� ���", Font.BOLD, 24));
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

	//��ư�� ������ �� ���۳���
	public abstract void confirm();

}
