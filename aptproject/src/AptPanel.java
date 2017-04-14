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
	MiddlePanel mp;//동,호수를 입력이 들어있는 패널

	public AptPanel() {
		p_north = new JPanel();
		p_center = new JPanel();
		p_south = new JPanel();
		p_menu = new JPanel();
		mp = new MiddlePanel();
		bt_cancle = new JButton("취소");
		bt_regist = new JButton("등록");
		ra_add = new JRadioButton("추가등록");
		ra_regist = new JRadioButton("건물등록");
		group = new ButtonGroup();

		// 라디오 그룹지정
		group.add(ra_add);
		group.add(ra_regist);

		setLayout(new BorderLayout());

		// 라디오 디자인

		ra_regist.setFont(new Font("맑은고딕", Font.BOLD, 20));
		ra_regist.setBackground(Color.ORANGE);
		ra_regist.setFocusPainted(false);

		ra_add.setFont(new Font("맑은고딕", Font.BOLD, 20));
		ra_add.setBackground(Color.ORANGE);
		ra_add.setFocusPainted(false);

		// 버튼 디자인

		bt_cancle.setPreferredSize(new Dimension(100, 50));
		bt_cancle.setBorder(new LineBorder(Color.black, 3));
		bt_cancle.setFont(new Font("굴림", Font.PLAIN, 25));
		bt_cancle.setBackground(Color.LIGHT_GRAY);
		bt_cancle.setForeground(Color.WHITE);
		bt_cancle.setFocusPainted(false);

		bt_regist.setPreferredSize(new Dimension(100, 50));
		bt_regist.setBorder(new LineBorder(Color.black, 3));
		bt_regist.setFont(new Font("굴림", Font.PLAIN, 25));
		bt_regist.setBackground(Color.orange);
		bt_regist.setForeground(Color.white);
		bt_regist.setFocusPainted(false);

		// 패널 디자인

		p_north.setLayout(new BorderLayout());
		p_north.setPreferredSize(new Dimension(700, 200));

		p_menu.setPreferredSize(new Dimension(700, 50));
		p_menu.setLayout(new FlowLayout(FlowLayout.CENTER, 150, 10));
		p_menu.setBackground(Color.orange);

		p_center.setPreferredSize(new Dimension(700, 300));
		p_center.setBackground(Color.white);

		p_south.setPreferredSize(new Dimension(700, 200));
		p_south.setLayout(new FlowLayout(FlowLayout.CENTER, 30, 40));

		
		//버튼,라디오에 이벤트 부여
		
		bt_regist.addActionListener(this);
		bt_cancle.addActionListener(this);
		
		ra_add.addActionListener(this);
		ra_regist.addActionListener(this);
		
		//텍스트에 이벤트 부여
		
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
		
		// add 하기

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
	
	
	//등록 버튼 이벤트 메서드
	public void regist(){
		System.out.println("등록");
		
	}
	//취소 버튼 이벤트 메서드
	public void cancle(){
		System.out.println("취소");
		
	}

	//라디오와 버튼에 이벤트 부여
	@Override
	public void actionPerformed(ActionEvent e) {
		Object obj=e.getSource();
			if(obj==bt_regist){
				regist();
			}else if(obj==bt_cancle){
				cancle();
			}else if(obj==ra_regist){
				System.out.println("건물등록");
			}else if(obj==ra_add){
				System.out.println("추가 등록");
			}
	}


	

}
