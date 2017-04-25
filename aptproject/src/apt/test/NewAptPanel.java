package apt.test;

import java.awt.Choice;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

import javax.swing.JPanel;

import complex.regist.table.TableController;
import db.DBManager;
import dto.AptDto;
import dto.ComplexDto;
import dto.Unit;

public class NewAptPanel extends JPanel{
	JPanel p_north,p_center,p_test;
	Choice choice;
	DrawApt drawApt;
	
	Vector<ComplexDto> complexData=new Vector<ComplexDto>();
	Vector<Unit> unitdata=new Vector<Unit>();
	Connection con;
	
	TableController controller;
	
	
	public NewAptPanel(Conection con){
		this.con=con;
		setLayout(null);
		p_north=new JPanel();
		p_center=new JPanel();
		choice=new Choice();
		
		
		p_center.setBackground(Color.cyan);
		p_center.setPreferredSize(new Dimension(300, 700));
		p_center.setBounds(190, 200, 300, 500);
		
		
		p_north.setBackground(Color.BLACK);
		p_north.add(choice);
		p_north.setBounds(0, 0, 700, 200);
		
		
		add(p_north);
		add(p_center);
		choice.addItemListener(new ItemListener() {	
			
			@Override
			public void itemStateChanged(ItemEvent e) {
				getUnit();
				
				
			}
		});
		
		
		
		setPreferredSize(new Dimension(700, 700));
		setBackground(Color.red);
		setVisible(true);
		
		getApt();
		
	
	}
	
	//dto 구해서 벡터에 담기 
	public void getApt(){
		PreparedStatement pstmt=null;
		ResultSet rs=null;

		String sql="select * from complex ORDER BY complex.COMPLEX_ID ASC";
		try {
			pstmt=con.prepareStatement(sql);
			rs=pstmt.executeQuery();
			
			while(rs.next()){
				ComplexDto dto=new ComplexDto();
				
				
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

	//화면에 뿌릴 호수 구하기
	public void getUnit(){
		
		PreparedStatement pstmt=null;
		ResultSet rs=null;
		
		String sql="select * FROM UNIT u WHERE u.COMPLEX_ID=?";
		try {
			pstmt=con.prepareStatement(sql);
			
			int index=choice.getSelectedIndex();
			
			
			if(index>=0){
				ComplexDto dto=complexData.get(index);
				pstmt.setInt(1, dto.getComplex_id());
				rs=pstmt.executeQuery();
			}
				unitdata.removeAll(unitdata);
			
			while(rs.next()){
				Unit vo=new Unit();
				
				vo.setUnit_id(rs.getInt("unit_id"));
				vo.setUnit_name(rs.getString("unit_name"));
				vo.setComplex_id(rs.getInt("complex_id"));
	
				unitdata.add(vo);
											
			}
			createApt();
		
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	public void createApt(){
		for (int i = 0; i < unitdata.size(); i++) {
			drawApt=new DrawApt();
			p_center.add(drawApt);
			
		}	
			
			System.out.println(unitdata.size());	
			
		}
		

	
}
