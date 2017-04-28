package apt.test;

import java.awt.Choice;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
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
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;

import db.DBManager;
import dto.AptStorageDto;
import dto.ComplexDto;

public class NewAptPanel extends JPanel implements ActionListener {
	JPanel p_north, p_center;
	JButton bt_check;
	Choice choice;
	SideApt sideApt;//���� ���׸� ����Ʈ �г�
	
	int index;// ���̽� ����Ʈ �ε����� ��� ����

	// ȣ���� ��� ����
	Vector<ComplexDto> complexData = new Vector<ComplexDto>();
	// ���� ��� ����
	
	ArrayList<Integer> Apt=new ArrayList<Integer>();

	
	
	//////////////////////////////////////////////////////////
	TreeMap<Integer, TreeSet<Integer>> unitTotal;
	//////////////////////////////////////////////////////////
	
	//Vector<Unit> unitData = new Vector<Unit>();
	//�� �Ѹ��� ��Ʈ�� �ϱ� ���� ���� ����
	Vector<DrawApt> list = new Vector<DrawApt>();
	
	
	//�ù� ������ ���θ� ���� ����
	Vector<AptStorageDto> boxData=new Vector<AptStorageDto>();
	///////////////////////////////////////////////
	
	
	HashMap<String, Integer> unitInvoice;
	String selectedComplex;
	
	Connection con;
	DBManager manager = DBManager.getInstance();

	public NewAptPanel() {
		con = manager.getConnection();
		setLayout(null);
		bt_check = new JButton("��ȸ");
		p_north = new JPanel();
		p_center = new JPanel();
		sideApt=new SideApt();
		
		choice = new Choice();
		choice.add("�嵿�� �������ּ���");
		choice.setPreferredSize(new Dimension(200, 10));

		// ��ư ������

		bt_check.setPreferredSize(new Dimension(80, 30));
		bt_check.setBorder(new LineBorder(Color.black, 3));
		bt_check.setFont(new Font("", Font.PLAIN, 15));
		bt_check.setBackground(Color.LIGHT_GRAY);
		bt_check.setFocusPainted(false);



		// �г� ������

		p_center.setBackground(Color.cyan);
		p_center.setBounds(90, 125, 400, 530);
		p_center.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 10));

		p_north.setBounds(0, 30, 700, 80);
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

		getApt();// dto ���ؼ� ���Ϳ� ���
		Boxget();//box ���� ���ؼ� ���� ���
		
	}

	// dto ���ؼ� ���Ϳ� ���
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

	// ȭ�鿡 �Ѹ� ȣ�� ���ϱ�
	public void getUnit() {
		//kingMap.remove(kingMap);
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		String sql = "select unit_name FROM view_cput WHERE complex_name=? ORDER BY unit_name asc";
		try {

			pstmt = con.prepareStatement(sql);
			int index = choice.getSelectedIndex();
			selectedComplex = choice.getSelectedItem();
			
			if (index - 1 >= 0) {
				ComplexDto dto = complexData.get(index - 1);
				
				pstmt.setString(1, selectedComplex);
				rs = pstmt.executeQuery();
				
			}
			//unitData.removeAll(unitData); ������ ����
			
			
			while (rs.next()) {
				
				int unit=Integer.parseInt(rs.getString("unit_name").split("\\s")[0]);
				Apt.add(unit);
			
		
			}
			
			sortunit();
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
	public void sortunit(){
		
		unitTotal = new TreeMap<Integer, TreeSet<Integer>>();
		int res = 0;
		TreeSet<Integer> unitList = null;
		for (int i = 0; i < Apt.size(); i++) {
			res = Apt.get(i);
			int floor = res / 100;
			if (unitTotal.get(floor) == null) {
				unitTotal.put(floor, new TreeSet<Integer>());
			}
			unitList = unitTotal.get(Integer.valueOf(floor));
			unitList.add(Integer.valueOf(res % 100));
		}
		System.out.println(unitTotal);
	}
	
	
	// ȭ�鿡 ����Ʈ �Ѹ���
	public void createApt() {
		p_center.removeAll();
		list.removeAll(list);
		for (int i = 1; i <= unitTotal.size(); i++) {

			int fl = i*100;
			ArrayList<Integer> unitList = new ArrayList<Integer>(unitTotal.get(Integer.valueOf(i)));
			for (Integer unitNum : unitList) {
				DrawApt drawApt = new DrawApt();
				String yORn = null;
				String unitN = Integer.toString(fl+unitNum);
				
				//������ �Դµ� �Ȱ��� ����.
				 if (unitInvoice.get(selectedComplex+"-"+unitN+" ȣN")!=null){	
					drawApt.setBackground(Color.red);
					
				
					
				}
				drawApt.la_name.setText(unitN);
				list.add(drawApt);
				
			}
			System.out.println(list);

		}

//		SideApt.la_info.setText(complexData.get(index - 1).getComplex_name());
		addApt();
//		System.out.println(unitData.size());
//		System.out.println(list.size());

	}
	//����Ʈ ���Ϳ� ��� �Ѹ���
	public void addApt() {
		for (int i =list.size()-1; i >= 0; i--) {
			if (list.size() != 0) {
				p_center.add(list.get(i));
				p_center.updateUI();
			}
		}
	}
	//�ù� �޾ƿ���
	public void Boxget(){
		PreparedStatement pstmt=null;
		ResultSet rs=null;
		
		String sql="select complex_name,unit_name,invoice_id,INVOICE_TAKEFLAG";
				sql+=" FROM VIEW_ACIS";
				System.out.println(sql);
		try {
			pstmt=con.prepareStatement(sql);
			
			rs=pstmt.executeQuery();
			unitInvoice=new HashMap<String, Integer>();
			
			while(rs.next()){		
				unitInvoice.put(rs.getString("complex_name")+"-"+rs.getString("unit_name")+rs.getString("invoice_takeflag"),rs.getInt("invoice_id"));
				
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
	//apt �����ϱ�
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
