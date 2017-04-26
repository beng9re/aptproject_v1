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
import dto.AptStorageDto;
import dto.ComplexDto;
import dto.Unit;

public class NewAptPanel extends JPanel implements ActionListener {
	JPanel p_north, p_center;
	JButton bt_check;
	Choice choice;
	DrawApt drawApt;// ����Ʈ �г�
	SideApt sideApt;//���� ���׸� ����Ʈ �г�
	
	int index;// ���̽� ����Ʈ �ε����� ��� ����

	// ȣ���� ��� ����
	Vector<ComplexDto> complexData = new Vector<ComplexDto>();
	// ���� ��� ����
	Vector<Unit> unitData = new Vector<Unit>();
	//�� �Ѹ��� ��Ʈ�� �ϱ� ���� ���� ����
	Vector<DrawApt> list = new Vector<DrawApt>();
	//�ù� ������ ���θ� ���� ����
	Vector<AptStorageDto> boxData=new Vector<AptStorageDto>();
	///////////////////////////////////////////////
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

		getApt();// dto ���ؼ� ���Ϳ� ���
		Boxget();//box ���� ���ؼ� ���� ���

	}

	// dto ���ؼ� ���Ϳ� ���
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

		PreparedStatement pstmt = null;
		ResultSet rs = null;

		String sql = "select * FROM UNIT u WHERE u.COMPLEX_ID=? ORDER BY unit_name asc";
		try {

			pstmt = con.prepareStatement(sql);
			index = choice.getSelectedIndex();

			if (index - 1 >= 0) {
				ComplexDto dto = complexData.get(index - 1);

				pstmt.setInt(1, dto.getComplex_id());
				rs = pstmt.executeQuery();
			}
			unitData.removeAll(unitData);

			while (rs.next()) {
				Unit vo = new Unit();

				vo.setUnit_id(rs.getInt("unit_id"));
				vo.setUnit_name(rs.getString("unit_name"));
				vo.setComplex_id(rs.getInt("complex_id"));

				unitData.add(vo);

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

	// ȭ�鿡 ����Ʈ �Ѹ���
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
	//����Ʈ ���Ϳ� ��� �Ѹ���
	public void addApt() {
		for (int i = list.size()- 1; i >= 0; i--) {
			if (list.size() != 0) {
				System.out.println("asdasd");
				p_center.add(list.get(i));
				p_center.updateUI();
			}
		}
	}
	//�ù� �޾ƿ���
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
