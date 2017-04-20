package complex.regist;

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
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Vector;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.border.LineBorder;
import javax.swing.event.AncestorEvent;
import javax.swing.event.AncestorListener;

import complex.regist.table.ShowTable;
import db.DBManager;
import dto.ComplexDto;


public class ComplexPanel extends JPanel implements ActionListener{
	JPanel p_north, p_center, p_south, p_menu;
	JButton bt_cancle, bt_regist,bt_confirm,bt_excel;
	JTextField t_path;
	JRadioButton ra_regist, ra_add;
	ButtonGroup group;
	MiddlePanel mp;//��,ȣ���� �Է��� ����ִ� �г�
	Connection con;
	DBManager manager=DBManager.getInstance();
	
	Vector<String> vec=new Vector<String>();//ȣ���� ��� ���� ����
	Vector complex_list=new Vector();//
	
	//��ȣ���� ������� ����
	int a=0;

	public ComplexPanel() {
		con=manager.getConnection();
		p_north = new JPanel();
		p_center = new JPanel();
		p_south = new JPanel();
		p_menu = new JPanel();
		mp = new MiddlePanel();
		bt_cancle = new JButton("���");
		bt_regist = new JButton("���");
		bt_confirm=new JButton("Ȯ���ϱ�");
		bt_excel=new JButton("������������");
		ra_add = new JRadioButton("�߰����");
		ra_regist = new JRadioButton("�ǹ����");
		t_path=new JTextField(20);
		group = new ButtonGroup();

		// ���� �׷�����
		group.add(ra_add);
		group.add(ra_regist);

		setLayout(new BorderLayout());

		// ���� ������

		ra_regist.setFont(new Font("��������", Font.BOLD, 20));
		ra_regist.setBackground(Color.ORANGE);
		ra_regist.setFocusPainted(false);

		ra_add.setFont(new Font("��������", Font.BOLD, 20));
		ra_add.setBackground(Color.ORANGE);
		ra_add.setFocusPainted(false);

		// ��ư ������

		bt_cancle.setPreferredSize(new Dimension(100, 50));
		bt_cancle.setBorder(new LineBorder(Color.black, 3));
		bt_cancle.setFont(new Font("����", Font.PLAIN, 25));
		bt_cancle.setBackground(Color.LIGHT_GRAY);
		bt_cancle.setFocusPainted(false);

		bt_regist.setPreferredSize(new Dimension(100, 50));
		bt_regist.setBorder(new LineBorder(Color.black, 3));
		bt_regist.setFont(new Font("����", Font.PLAIN, 25));
		bt_regist.setBackground(Color.orange);
		bt_regist.setFocusPainted(false);
		
		bt_confirm.setPreferredSize(new Dimension(100, 50));
		bt_confirm.setBorder(new LineBorder(Color.black, 3));
		bt_confirm.setFont(new Font("����", Font.PLAIN, 20));
		bt_confirm.setBackground(Color.LIGHT_GRAY);
		bt_confirm.setFocusPainted(false);
		
		/*
		bt_excel.setPreferredSize(new Dimension(100, 30));
		bt_excel.setBorder(new LineBorder(Color.black, 3));
		bt_excel.setFont(new Font("����", Font.PLAIN, 11));
		bt_excel.setBackground(Color.LIGHT_GRAY);
		bt_excel.setFocusPainted(false);
		*/
		

		// �г� ������

		p_north.setLayout(new BorderLayout());
		p_north.setPreferredSize(new Dimension(700, 200));

		p_menu.setPreferredSize(new Dimension(700, 50));
		p_menu.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 10));
		p_menu.setBackground(Color.orange);

		p_center.setPreferredSize(new Dimension(700, 300));
		p_center.setBackground(Color.white);

		p_south.setPreferredSize(new Dimension(700, 200));
		p_south.setLayout(new FlowLayout(FlowLayout.CENTER, 30, 40));

		
		//��ư�� �̺�Ʈ �ο�
		
		bt_regist.addActionListener(this);
		bt_cancle.addActionListener(this);
		bt_confirm.addActionListener(this);
		
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
		
		// add �ϱ�

		//p_menu.add(t_path);
		//p_menu.add(bt_excel);
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

	}
	
	
	//��� ��ư �̺�Ʈ �޼���
	public void regist(){
		System.out.println("���");
		
		registComplex();
		//registUnit();
		
	
	}
	
	public void registComplex(){
		PreparedStatement pstmt=null;
		//��������� �� �����Ϳ� �Է��ϱ�
		String complex=mp.t_complex.getText();
	
		
		String sql="INSERT INTO COMPLEX(complex_id,COMPLEX_NAME)VALUES (seq_complex.nextval,?)";
		
		try {
			pstmt=con.prepareStatement(sql);
			pstmt.setString(1, complex);
			int sq=pstmt.executeUpdate();
			
			
			if(sq>0){
			System.out.println("����");
			}else{
				System.out.println("����");
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			if(pstmt!=null){
				try {
					pstmt.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		getComplex();
	}
	//complex�� �� ���ؿ��� �޼���
	public void getComplex(){
		PreparedStatement pstmt=null;
		ResultSet rs=null;
		String sql="select seq_complex.currval from dual";
		//������̺� �� �ϳ�¥�� �����ؼ� �� ����ְ�  �� ���ϱ�
		
		try {
			pstmt=con.prepareStatement(sql);
			rs=pstmt.executeQuery();
			
			if(rs.next()){
	
				complex_list.add(rs.getInt("currval"));
	
			}
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			if(pstmt!=null){
				try {
					pstmt.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				if(rs!=null){
					try {
						rs.close();
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				
			}
		}
		registUnit();
	}
	
	
	//ȣ�� �Է� �޼��� 
	public void registUnit(){
		PreparedStatement pstmt=null;
		String ss;
		String floor=mp.t_floor2.getText();
		String unit=mp.t_unit2.getText();
	
		int c_id=(int) complex_list.get(a);
		System.out.println(c_id);
		
		String sql="INSERT INTO unit(unit_id,unit_NAME,complex_id)VALUES (seq_unit.nextval,?,?)";
		
		vec.removeAll(vec);
		
		for (int i = 1; i <=Integer.parseInt(floor); i++) {
			for (int j = 1; j <= Integer.parseInt(unit); j++) {
				if(j<10){
					vec.add(i+"0"+j);
				}else {
					if(j<10){
						vec.add(i+"0"+j);
					}else if(j>=10){
						vec.add(i+""+j);
					}
				}
			}
		}
		
		for (int i = 0; i < vec.size(); i++) {
			ss=vec.get(i);
			
		
			try {
				pstmt=con.prepareStatement(sql);
				pstmt.setString(1, ss);
				pstmt.setInt(2, c_id);
				int sq=pstmt.executeUpdate();
				
				complex_list.removeAll(complex_list);
				
				
				if(sq>0){
					System.out.println("���Է¼���");
				}else{
					System.out.println("���Է½���");
				}
				
				
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}finally{
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
		
		
		
	}
	
	//��� ��ư �̺�Ʈ �޼���
	public void cancle(){
		mp.t_complex.setText(null);
		mp.t_floor2.setText(null);
		mp.t_unit2.setText(null);
		
		
	}
	
	//���̺� Ȯ�� �޼���
	public void confirm(){
		ShowTable sh_table=new ShowTable();
		
	}
	

	//������ ��ư�� �̺�Ʈ �ο�
	@Override
	public void actionPerformed(ActionEvent e) {
		Object obj=e.getSource();
			if(obj==bt_regist){
				regist();
			}else if(obj==bt_cancle){
				cancle();
			}else if(obj==bt_confirm){
				confirm();
			}
			
	}


	

}