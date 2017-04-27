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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;

import com.sun.javafx.collections.MappingChange.Map;
import com.sun.javafx.scene.paint.GradientUtils.Parser;

import complex.regist.table.TableController;
import db.DBManager;
import dto.AptDto;
import dto.AptStorageDto;
import dto.ComplexDto;
import dto.Unit;

public class NewAptPanel extends JPanel implements ActionListener {
	JPanel p_north, p_center;
	JButton bt_check;
	Choice choice;
	DrawApt drawApt;// 아파트 패널
	SideApt sideApt;//옆의 조그만 아파트 패널
	
	int index;// 초이스 셀렉트 인덱스를 담는 변수

	// 호수를 담는 벡터
	Vector<ComplexDto> complexData = new Vector<ComplexDto>();
	// 동을 담는 벡터
	
	
	
	HashMap<Integer, Integer> map1=new HashMap<Integer,Integer>();
	
	HashMap<Integer,HashMap<Integer, Integer>> kingMap=new HashMap<Integer,HashMap<Integer, Integer>>();
	
	
	
	
	
	//Vector<Unit> unitData = new Vector<Unit>();
	//동 뿌릴떄 컨트롤 하기 위해 담은 벡터
	Vector<DrawApt> list = new Vector<DrawApt>();
	//택배 보관함 여부를 담은 벡터
	Vector<AptStorageDto> boxData=new Vector<AptStorageDto>();
	///////////////////////////////////////////////
	Connection con;
	DBManager manager = DBManager.getInstance();

	public NewAptPanel() {
		con = manager.getConnection();
		setLayout(null);
		bt_check = new JButton("조회");
		p_north = new JPanel();
		p_center = new JPanel();
		sideApt=new SideApt();
		
		choice = new Choice();
		choice.add("▼동을 선택해주세요");
		choice.setPreferredSize(new Dimension(200, 10));

		// 버튼 디자인

		bt_check.setPreferredSize(new Dimension(80, 30));
		bt_check.setBorder(new LineBorder(Color.black, 3));
		bt_check.setFont(new Font("", Font.PLAIN, 15));
		bt_check.setBackground(Color.LIGHT_GRAY);
		bt_check.setFocusPainted(false);



		// 패널 디자인

		p_center.setBackground(Color.CYAN);
		p_center.setBounds(190, 160, 300, 500);
		p_center.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 10));

		p_north.setBounds(0, 50, 700, 100);
		p_north.setBackground(Color.pink);
		p_north.add(choice);
		p_north.add(bt_check);
		p_north.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 30));

		
		add(p_north);
		add(p_center);
		add(sideApt);
		
		
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

		getApt();// dto 구해서 벡터에 담기
		Boxget();//box 정보 구해서 벡터 담기
		
	}

	// dto 구해서 벡터에 담기
	public void getApt() {
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		String sql = "select * from complex ORDER BY complex.complex_name ASC";
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
		finally{
			if(rs!=null){
				try {
					rs.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if(pstmt!=null){
				try {
					pstmt.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}

	// 화면에 뿌릴 호수 구하기
	public void getUnit() {
		kingMap.remove(kingMap);
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		String sql = "select unit_name FROM view_cput WHERE complex_name=? ORDER BY unit_name asc";
		try {

			pstmt = con.prepareStatement(sql);
			String index = choice.getSelectedItem();

			//if (index - 1 >= 0) {
				//ComplexDto dto = complexData.get(index - 1);

				pstmt.setString(1, index);
				rs = pstmt.executeQuery();
				
			//}
			//unitData.removeAll(unitData); 데이터 비우기
			
			int count=1;
			int x=100*count;
			int count1=0;
			
			
			while (rs.next()) {
				
				
				int unit=Integer.parseInt(rs.getString("unit_name").split("\\s")[0]);
				//System.out.println("이거는 유닛"+unit);
				int y=unit/(x);
				
				if(y==2){
					kingMap.put(count, map1);
					count=count+1;
					count1=0;
					map1.remove(map1);
				}
					
				
				map1.put(count1++, unit);
						
				/*
				Unit vo = new Unit();

				vo.setUnit_name(rs.getString("unit_name"));
				
				unitData.put(vo);
				 */
			}
			
			test();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally{
			if(rs!=null){
				try {
					rs.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if(pstmt!=null){
				try {
					pstmt.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}

	}

	public void test(){
		System.out.println(kingMap.size()+"이거는 킹맵 사이즈");
		System.out.println(kingMap.get(1).size()+"이거는 겟 1 사이즈");
		System.out.println(kingMap.get(2).get(1));
		System.out.println(kingMap.get(2).get(2));
		System.out.println(kingMap.get(2).get(3));
		System.out.println(kingMap.get(2).get(4)+"///////");
		
		
	}
	
	// 화면에 아파트 뿌리기
	public void createApt() {
		p_center.removeAll();
		list.removeAll(list);
		for (int i = 0; i < unitData.size(); i++) {

			drawApt = new DrawApt();
			drawApt.la_name.setText(unitData.get(i).getUnit_name());
			list.add(drawApt);

		}

		SideApt.la_info.setText(complexData.get(index - 1).getComplex_name());
		addApt();
		System.out.println(unitData.size());
		System.out.println(list.size());

	}
	//아파트 벡터에 담고 뿌리기
	public void addApt() {
		for (int i =list.size()-1; i >= 0; i--) {
			if (list.size() != 0) {
				System.out.println("asdasd");
				p_center.add(list.get(i));
				p_center.updateUI();
			}
		}
	}
	//택배 받아오기
	public void Boxget(){
		PreparedStatement pstmt=null;
		ResultSet rs=null;
		
		String sql="select v.complex_name,v.unit_name,b.box_num,b.box_use";
				sql+=" FROM VIEW_ACIS v,storagebox b WHERE b.INVOICE_ID=v.INVOICE_ID";
				System.out.println(sql);
		try {
			pstmt=con.prepareStatement(sql);
			
			rs=pstmt.executeQuery();
			
			while(rs.next()){
				AptStorageDto dto=new AptStorageDto();
				dto.setComplex_name(rs.getString("complex_name"));
				dto.setUnit_name(rs.getString("unit_name"));
				dto.setBox_num(rs.getInt("box_num"));
				dto.setBox_use(rs.getString("box_use"));
				
				boxData.add(dto);			
			}
			Boxset();
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally{
			if(rs!=null){
				try {
					rs.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if(pstmt!=null){
				try {
					pstmt.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}                                                                                                                                                                                                                                                                                                                                                                                        		
	}
	//apt 설정하기
	public void Boxset(){
		System.out.println(boxData.size());
		
	}
 
	
	@Override
	public void actionPerformed(ActionEvent e) {
		Object obj = (JButton) e.getSource();
		if (obj == bt_check) {
			createApt();
		}

	}

}
