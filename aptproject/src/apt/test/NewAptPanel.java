package apt.test;

import java.awt.Choice;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;

import complex.regist.table.TableController;
import db.DBManager;
import dto.AptDto;
import dto.ComplexDto;
import dto.Unit;

public class NewAptPanel extends JPanel implements ActionListener {
	JPanel p_north, p_center,p_info,p_info1;
	JButton bt_check;
	Choice choice;
	DrawApt drawApt;//아파트 패널
	JLabel la_info;
	int index;//초이스 셀렉트 인덱스를 담는 변수
	

	//호수를 담는 벡터
	Vector<ComplexDto> complexData = new Vector<ComplexDto>();
	//동을 담는 벡터
	Vector<Unit> unitdata = new Vector<Unit>();
	
	Vector<DrawApt> list=new Vector<DrawApt>();
	
	
	Connection con;
	DBManager manager = DBManager.getInstance();

	public NewAptPanel() {
		con = manager.getConnection();
		setLayout(null);
		bt_check = new JButton("조회");
		p_north = new JPanel();
		p_center = new JPanel();
		p_info=new JPanel();
		p_info1=new JPanel();
		la_info=new JLabel("");
		
		
		choice = new Choice();
		choice.add("▼동을 선택해주세요");
		choice.setPreferredSize(new Dimension(200	,10));
		
		
		//버튼 디자인
		
		bt_check.setPreferredSize(new Dimension(80, 30));
		bt_check.setBorder(new LineBorder(Color.black, 3));
		bt_check.setFont(new Font("굴림", Font.PLAIN, 15));
		bt_check.setBackground(Color.LIGHT_GRAY);
		bt_check.setFocusPainted(false);
		
		la_info.setFont(new Font("돋움", Font.BOLD, 30));
		
		
		//패널 디자인

		p_center.setBackground(Color.cyan);
		p_center.setBounds(190, 160, 300, 500);
		p_center.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 10));

		p_north.setBounds(0, 50, 700, 100);
		p_north.setBackground(Color.pink);
		p_north.add(choice);
		p_north.add(bt_check);
		p_north.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 30));
		
		p_info.setBounds(500, 457, 150, 200);
		p_info.setBackground(Color.cyan);
		p_info.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 40));
		p_info.add(p_info1);
		
		
		p_info1.setPreferredSize(new Dimension(140, 50));
		p_info1.setBounds(0, 0, 100, 50);
		p_info1.setBackground(Color.white);
		p_info1.add(la_info);
		
		
		add(p_north);
		add(p_center);
		add(p_info);
		
		choice.addItemListener(new ItemListener() {

			@Override
			public void itemStateChanged(ItemEvent e) {
				getUnit();
			}
		});
		bt_check.addActionListener(this);
		

		setPreferredSize(new Dimension(700, 700));
		setBackground(Color.WHITE);
		setVisible(true);

		getApt();

	}

	// dto 구해서 벡터에 담기
	public void getApt() {
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		String sql = "select * from complex ORDER BY complex.COMPLEX_ID ASC";
		try {
			pstmt = con.prepareStatement(sql);
			rs = pstmt.executeQuery();

			while (rs.next()) {
				ComplexDto dto = new ComplexDto();

				dto.setComplex_id(rs.getInt("complex_id"));
				dto.setComplex_name(rs.getString("complex_name"));

				complexData.add(dto);
				choice.add(dto.getComplex_name());

			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	// 화면에 뿌릴 호수 구하기
	public void getUnit() {

		PreparedStatement pstmt = null;
		ResultSet rs = null;

		String sql = "select * FROM UNIT u WHERE u.COMPLEX_ID=? ORDER BY unit_name asc";
		try {
			
			pstmt = con.prepareStatement(sql);
			index = choice.getSelectedIndex();

			if (index-1 >= 0) {
				ComplexDto dto = complexData.get(index-1);
				
				pstmt.setInt(1, dto.getComplex_id());
				rs = pstmt.executeQuery();
			}
			unitdata.removeAll(unitdata);

			while (rs.next()) {
				Unit vo = new Unit();

				vo.setUnit_id(rs.getInt("unit_id"));
				vo.setUnit_name(rs.getString("unit_name"));
				vo.setComplex_id(rs.getInt("complex_id"));

				unitdata.add(vo);

			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	//화면에 아파트 뿌리기
	public void createApt() {
		p_center.removeAll();
		list.removeAll(list);
		for (int i = 0; i <unitdata.size() ; i++) {

			drawApt = new DrawApt();
			drawApt.la_name.setText(unitdata.get(i).getUnit_name());	
			list.add(drawApt);

		}
		
		la_info.setText(complexData.get(index-1).getComplex_name());
		addApt();
		System.out.println(unitdata.size());
		System.out.println(list.size());
		
		
		

	}
	public void addApt(){
		for (int i =list.size()-1; i >=0; i--) {
			if(list.size()!=0){
				System.out.println("asdasd");
				p_center.add(list.get(i));
				p_center.updateUI();
			}
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		Object obj = (JButton) e.getSource();
		if (obj == bt_check) {		
			createApt();
		}

	}


}
