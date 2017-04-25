package complex.regist;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.border.LineBorder;

import complex.regist.table.TableController;

public class ComplexPanel extends JPanel implements ActionListener {
	JPanel p_north, p_center, p_south, p_menu;
	JButton bt_cancle, bt_regist, bt_confirm, bt_excel;
	JTextField t_path;
	JRadioButton ra_regist, ra_add;
	ButtonGroup group;
	MiddlePanel mp;// 동,호수를 입력이 들어있는 패널
	Connection con;
	

	Vector<String> vec = new Vector<String>();// 호수를 담기 위한 벡터
	Vector complex_list = new Vector();//

	// 동호수를 담기위한 변수
	int a = 0;
	//안내선 색깔 변경 변수
	boolean flag=false;

	TableController controller;

	public ComplexPanel(Connection con) {
		
		this.con=con;
		p_north = new JPanel();
		p_center = new JPanel();
		p_south = new JPanel();
		p_menu = new JPanel();
		mp = new MiddlePanel();
		bt_cancle = new JButton("취소");
		bt_regist = new JButton("등록");
		bt_confirm = new JButton("확인하기");
		bt_excel = new JButton("엑셀가져오기");
		ra_add = new JRadioButton("추가등록");
		ra_regist = new JRadioButton("건물등록");
		t_path = new JTextField(20);
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
		bt_cancle.setFocusPainted(false);

		bt_regist.setPreferredSize(new Dimension(100, 50));
		bt_regist.setBorder(new LineBorder(Color.black, 3));
		bt_regist.setFont(new Font("굴림", Font.PLAIN, 25));
		bt_regist.setBackground(Color.orange);
		bt_regist.setFocusPainted(false);

		bt_confirm.setPreferredSize(new Dimension(100, 50));
		bt_confirm.setBorder(new LineBorder(Color.black, 3));
		bt_confirm.setFont(new Font("굴림", Font.PLAIN, 20));
		bt_confirm.setBackground(Color.LIGHT_GRAY);
		bt_confirm.setFocusPainted(false);

		/*
		 * bt_excel.setPreferredSize(new Dimension(100, 30));
		 * bt_excel.setBorder(new LineBorder(Color.black, 3));
		 * bt_excel.setFont(new Font("굴림", Font.PLAIN, 11));
		 * bt_excel.setBackground(Color.LIGHT_GRAY);
		 * bt_excel.setFocusPainted(false);
		 */

		// 패널 디자인

		p_north.setLayout(new BorderLayout());
		p_north.setPreferredSize(new Dimension(700, 200));

		p_menu.setPreferredSize(new Dimension(700, 50));
		p_menu.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 10));
		p_menu.setBackground(Color.orange);

		p_center.setPreferredSize(new Dimension(700, 300));
		p_center.setBackground(Color.white);

		p_south.setPreferredSize(new Dimension(700, 200));
		p_south.setLayout(new FlowLayout(FlowLayout.CENTER, 30, 40));

		// 버튼에 이벤트 부여

		bt_regist.addActionListener(this);
		bt_cancle.addActionListener(this);
		bt_confirm.addActionListener(this);

		// 텍스트에 이벤트 부여

		mp.t_complex.addKeyListener(new KeyAdapter() {
			
			@Override
			public void keyTyped(KeyEvent e) {
				char key = e.getKeyChar();
				
					if(!Character.isDigit(key)){
						flag=true;
						mp.la_info.setText("동은 숫자로 입력하세요");
						mp.la_info.setBounds(	80, 200, 300, 30);
						info();
						e.consume();	
					}
					else{
						flag=false;
						info();
					}
				}
	
		});
		
		mp.t_floor2.addKeyListener(new KeyAdapter() {
			@Override
			public void keyTyped(KeyEvent e) {
				char key = e.getKeyChar();
				if(!Character.isDigit(key)){
					flag=true;
					mp.la_info.setText("호수는 숫자로 입력하세요");
					mp.la_info.setBounds(	80, 200, 300, 30);
					info();
					e.consume();	
				}
				else{
					flag=false;
					info();
				}
			}
		
		});
		
		mp.t_unit2.addKeyListener(new KeyAdapter() {
			@Override
			public void keyTyped(KeyEvent e) {
				char key = e.getKeyChar();
				if(!Character.isDigit(key)){
					flag=true;
					mp.la_info.setText("호수는 숫자로 입력하세요");
					mp.la_info.setBounds(	80, 200, 300, 30);
					info();
					e.consume();	
				}
				else{
					flag=false;
					info();
				}
			}
		
		});
		
		

		// add 하기

		// p_menu.add(t_path);
		// p_menu.add(bt_excel);
		p_north.add(p_menu, BorderLayout.SOUTH);
		p_south.add(bt_cancle);
		p_south.add(bt_regist);
		p_south.add(bt_confirm);
		p_center.add(mp);

		add(p_north, BorderLayout.NORTH);
		add(p_south, BorderLayout.SOUTH);
		add(p_center, BorderLayout.CENTER);

		setVisible(true);
		setSize(700, 700);
		
		info();
	}

	// 등록 버튼 이벤트 메서드
	public void regist() {
		System.out.println("등록");

		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;
		String complex = mp.t_complex.getText();
		// 등록했을때 동 데이터에 입력하기

		sql = "INSERT INTO COMPLEX(complex_id,COMPLEX_NAME)VALUES (seq_complex.nextval,?)";

		try {
			
			if(complex.toString().length()!=0){
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, complex+"동");
			int sq = pstmt.executeUpdate();
			con.setAutoCommit(false);

			if (sq > 0) {
				System.out.println("성공");
			} else {
				System.out.println("실패");
			}
			}
			
			// 듀얼테이블 값 하나짜리 생성해서 값 집어넣고 값 구하기
			sql = "select seq_complex.currval from dual";

			pstmt = con.prepareStatement(sql);
			rs = pstmt.executeQuery();

			if (rs.next()) {
				complex_list.add(rs.getInt("currval"));
			}
			System.out.println(complex_list.size());

			// 호수 구하기

			String ss;
			String floor = mp.t_floor2.getText();
			String unit = mp.t_unit2.getText();

			int c_id = (int) complex_list.get(a);
			// System.out.println(c_id);

			sql = "INSERT INTO unit(unit_id,unit_NAME,complex_id)VALUES (seq_unit.nextval,?,?)";

			vec.removeAll(vec);

			for (int i = 1; i <= Integer.parseInt(floor); i++) {
				for (int j = 1; j <= Integer.parseInt(unit); j++) {
					if (j < 10) {
						vec.add(i + "0" + j);
					} else {
						if (j < 10) {
							vec.add(i + "0" + j);
						} else if (j >= 10) {
							vec.add(i + "" + j);
						}
					}
				}
			}

			for (int i = 0; i < vec.size(); i++) {
				ss = vec.get(i);

				pstmt = con.prepareStatement(sql);
				pstmt.setString(1, ss+"호");
				pstmt.setInt(2, c_id);
				int sq1 = pstmt.executeUpdate();
				complex_list.removeAll(complex_list);
				

				if (sq1 != 0) {
					System.out.println("동입력성공");				
					
					try {
						controller.upDate();
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					con.commit();
					System.out.println("커밋 ㄱㄱ");
					JOptionPane.showMessageDialog(this, "등록에 성공하였습니다");
				}

			}

		} catch (SQLException e) {
			e.printStackTrace();
			try {
				con.rollback();
				System.out.println("롤백 ㄱㄱ");
				flag=true;
				info();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

		}

		finally {
			if (pstmt != null) {
				try {
					pstmt.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}

	}
	public boolean checkInputOnlyNumberAndAlphabet(){
		return flag;
		
	}
	
	
	
	


	
	//안내선 변경 이벤트 
	public void info(){
		if(flag==true){
			mp.la_info.setForeground(Color.red);
		}else if(flag==false){
			mp.la_info.setForeground(Color.white);
		}
		
	}

	// 취소 버튼 이벤트 메서드
	public void cancle() {
		mp.t_complex.setText(null);
		mp.t_floor2.setText(null);
		mp.t_unit2.setText(null);

	}

	// 테이블 확인 메서드
	public void confirm() {
		controller = new TableController();

	}

	// 라디오와 버튼에 이벤트 부여
	@Override
	public void actionPerformed(ActionEvent e) {
		Object obj = e.getSource();
		if (obj == bt_regist) {
			regist();
		} else if (obj == bt_cancle) {
			cancle();
		} else if (obj == bt_confirm) {
			confirm();
		}

	}

}
