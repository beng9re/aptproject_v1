package test;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.border.LineBorder;
import javax.swing.event.AncestorEvent;
import javax.swing.event.AncestorListener;

import hjm.DBManager;

public class AptPanel extends JPanel implements ActionListener{
	JPanel p_north, p_center, p_south, p_menu;
	JLabel la_complex, la_unit;
	JButton bt_cancle, bt_regist;
	JTextField t_complex, t_unit;
	JRadioButton ra_regist, ra_add;
	ButtonGroup group;
	MiddlePanel mp;//��,ȣ���� �Է��� ����ִ� �г�

	public AptPanel() {
		p_north = new JPanel();
		p_center = new JPanel();
		p_south = new JPanel();
		p_menu = new JPanel();
		mp = new MiddlePanel();
		bt_cancle = new JButton("���");
		bt_regist = new JButton("���");
		ra_add = new JRadioButton("�߰����");
		ra_regist = new JRadioButton("�ǹ����");
		group = new ButtonGroup();

		// ���� �׷�����
		group.add(ra_add);
		group.add(ra_regist);

		setLayout(new BorderLayout());

		// ���� ������

		ra_regist.setFont(new Font("�������", Font.BOLD, 20));
		ra_regist.setBackground(Color.ORANGE);
		ra_regist.setFocusPainted(false);

		ra_add.setFont(new Font("�������", Font.BOLD, 20));
		ra_add.setBackground(Color.ORANGE);
		ra_add.setFocusPainted(false);

		// ��ư ������

		bt_cancle.setPreferredSize(new Dimension(100, 50));
		bt_cancle.setBorder(new LineBorder(Color.black, 3));
		bt_cancle.setFont(new Font("����", Font.PLAIN, 25));
		bt_cancle.setBackground(Color.LIGHT_GRAY);
		bt_cancle.setForeground(Color.WHITE);
		bt_cancle.setFocusPainted(false);

		bt_regist.setPreferredSize(new Dimension(100, 50));
		bt_regist.setBorder(new LineBorder(Color.black, 3));
		bt_regist.setFont(new Font("����", Font.PLAIN, 25));
		bt_regist.setBackground(Color.orange);
		bt_regist.setForeground(Color.white);
		bt_regist.setFocusPainted(false);

		// �г� ������

		p_north.setLayout(new BorderLayout());
		p_north.setPreferredSize(new Dimension(700, 200));

		p_menu.setPreferredSize(new Dimension(700, 50));
		p_menu.setLayout(new FlowLayout(FlowLayout.CENTER, 150, 10));
		p_menu.setBackground(Color.orange);

		p_center.setPreferredSize(new Dimension(700, 300));
		p_center.setBackground(Color.white);

		p_south.setPreferredSize(new Dimension(700, 200));
		p_south.setLayout(new FlowLayout(FlowLayout.CENTER, 30, 40));

		
		//��ư,������ �̺�Ʈ �ο�
		
		bt_regist.addActionListener(this);
		bt_cancle.addActionListener(this);
		
		ra_add.addActionListener(this);
		ra_regist.addActionListener(this);
		
		//�ؽ�Ʈ�� �̺�Ʈ �ο�
		
		mp.t_complex.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				int key=e.getKeyCode();
				if(key==KeyEvent.VK_ENTER){
					System.out.println(mp.t_complex.getText());
					mp.t_complex.setText("");
				}
			}
		});
		mp.t_unit.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				int key=e.getKeyCode();
				if(key==KeyEvent.VK_ENTER){
					System.out.println(mp.t_unit.getText());
					mp.t_unit.setText("");
				}
			}
		});
		
		// add �ϱ�

		p_menu.add(ra_regist);
		p_menu.add(ra_add);
		p_north.add(p_menu, BorderLayout.SOUTH);
		p_south.add(bt_cancle);
		p_south.add(bt_regist);
		p_center.add(mp);

		add(p_north, BorderLayout.NORTH);
		add(p_south, BorderLayout.SOUTH);
		add(p_center, BorderLayout.CENTER);
	

		setVisible(true);
		setSize(700, 700);

	}
	
	
	//��� ��ư �̺�Ʈ �޼���
	public void regist(){
		System.out.println("���");
		
	}
	//��� ��ư �̺�Ʈ �޼���
	public void cancle(){
		System.out.println("���");
		
	}

	//������ ��ư�� �̺�Ʈ �ο�
	@Override
	public void actionPerformed(ActionEvent e) {
		Object obj=e.getSource();
			if(obj==bt_regist){
				regist();
			}else if(obj==bt_cancle){
				cancle();
			}else if(obj==ra_regist){
				System.out.println("�ǹ����");
			}else if(obj==ra_add){
				System.out.println("�߰� ���");
			}
	}


	

}
