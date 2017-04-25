package Edit;
import java.awt.BorderLayout;
import java.awt.Choice;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;
import java.util.concurrent.SynchronousQueue;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.Border;

import db.DBManager;
import dto.Company;
import dto.ComplexDto;
import dto.ViewCPUT;

public class InvEditPan extends JPanel implements ActionListener,ItemListener{
	
	JLabel lb_block,lb_class,lb_code,lb_com,lb_taker,lb_takerTime,lb_Time;
				//동			//호수
	JLabel title;
	
	JTextField tf_code,tf_taker;
	Choice ch_block,ch_class,ch_com;
	
	String userid; //회원아이디 값이 들어갈것
	
	JPanel p_info;
	JPanel p_up;
	JPanel p_down;
	JPanel p_emp; //여백용 패널
	JButton bt_reset;
	JButton bt_regist;
	GridBagLayout gbl;
	GridBagConstraints gdc;
	
	Connection con;
	
	Vector<Vector> cput=new  Vector<Vector>();
	//뷰의의 정보를 가진 벡터
	Vector<String> checkv=new Vector<String>();
	//빈벡터 판단용벡터
	Vector<Vector> company=new  Vector<Vector>();
	//운송사 테이블 정보를 가진 벡터
	

	
	public InvEditPan(Connection con) {
		this.con=con;
	
		
		
		title=new JLabel("INVOICE");
		title.setFont(new  Font("고딕",Font.BOLD , 60));
		title.setForeground(Color.white);
		
		gbl=new GridBagLayout();
		gdc=new GridBagConstraints();
		
		setLayout(new BorderLayout());
		
		p_up=new JPanel();
		p_info=new JPanel(gbl);
		p_down=new JPanel();
		p_emp=new JPanel();
		//p_info.setPreferredSize(new Dimension(700, 500));
		
		p_up.setPreferredSize(new Dimension(700, 100));
		p_up.add(title);
		lb_block=new JLabel("동"); //동 id
		lb_class=new JLabel("호수");
		lb_code=new JLabel("바코드"); //바코드
		
		lb_com=new JLabel("운송사"); //운송사
		lb_taker=new JLabel("수령인");//수령인
	
		
		ch_block=new Choice();
		ch_class=new  Choice();
		ch_com=new Choice();
		
		tf_code=new JTextField(20);
		tf_taker=new JTextField(20);
		
		ch_block.setPreferredSize(new Dimension(220,30));
		ch_class.setPreferredSize(new Dimension(220,30));
		tf_code.setPreferredSize(new Dimension(20,30));
		ch_com.setPreferredSize(new Dimension(220,30));
		tf_taker.setPreferredSize(new Dimension(20,30));
		
		bt_regist=new JButton("입력");
		bt_reset=new JButton("초기화");
		
		add(p_up,BorderLayout.NORTH);
		p_up.setBackground(Color.pink);
		
		add(p_info);
		p_info.setBackground(Color.white);
		
		
		print();
	
		ch_block.setBackground(Color.PINK);
		ch_class.setBackground(Color.pink);
		ch_com.setBackground(Color.pink);
		//ch_block.setBorder(BorderFactory.createLineBorder(Color.PINK, 2));
		//ch_class.setBorder(BorderFactory.createLineBorder(Color.PINK, 2));
		tf_code.setBorder(BorderFactory.createLineBorder(Color.PINK, 2));
		
		tf_taker.setBorder(BorderFactory.createLineBorder(Color.PINK, 2));
		
		
		add(p_down,BorderLayout.SOUTH);
		p_down.setPreferredSize(new Dimension(700, 180));
		p_down.setBackground(Color.pink);
		p_down.add(p_emp);
		p_emp.setPreferredSize(new Dimension(700, 20));
		p_emp.setBackground(new Color(255,255,128));
		p_down.add(bt_reset);
		bt_reset.setPreferredSize(new Dimension(180, 50));
		bt_reset.setBackground(new Color(137, 210, 245));
		bt_reset.setBorder(BorderFactory.createLineBorder(Color.white,2));
		
		p_down.add(bt_regist);
		bt_regist.setPreferredSize(new Dimension(180, 50));
		bt_regist.setBackground(new Color(137, 210, 245));
		bt_regist.setBorder(BorderFactory.createLineBorder(Color.white,2));
		
		
		complexSet();
		listadd();
		
		//이벤트 리스너 연결 부분------------------------------------------------------------------------------//
		tf_code.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				JOptionPane.showMessageDialog(tf_code, "스캐너를 사용해주세요");
				
			}
		});
		
		ch_block.addItemListener(this);
	
		
		bt_regist.addActionListener(this);
		bt_reset.addActionListener(this);
		
	
		
		
		setSize(700,700);
	}
	
	
	
	//화면에 센터영역에 뿌리는 것
	public void print(){

		GridCom g_t1=new GridCom(p_info, gbl, gdc,lb_block, 				0, 0, 1,1,0, 0);
		GridCom g_l1=new GridCom(p_info, gbl, gdc, ch_block, 				1, 0, 1,1,0, 0);
		GridCom g_t2=new GridCom(p_info, gbl, gdc,lb_class, 				0, 1, 1,1,0, 0);
		GridCom g_l2=new GridCom(p_info, gbl, gdc, ch_class, 				1, 1, 1,1,0, 0);
		GridCom g_t3=new GridCom(p_info, gbl, gdc, lb_code,			    0, 2, 1,1,0, 0);
		GridCom g_l3=new GridCom(p_info, gbl, gdc, tf_code, 			    1, 2, 1,1,0, 0);
		GridCom g_t4=new GridCom(p_info, gbl, gdc, lb_com,				0, 3, 1,1,0, 0);
		GridCom g_l4=new GridCom(p_info, gbl, gdc, ch_com, 			    1, 3, 1,1,0, 0);
		GridCom g_t5=new GridCom(p_info, gbl, gdc, lb_taker, 			    0, 4, 1,1,0, 0);
		GridCom g_l5=new GridCom(p_info, gbl, gdc, tf_taker, 		     	1, 4, 1,1,0, 0);
		//GridCom g_timeT=new GridCom(p_info, gbl, gdc, lb_takerTime,    3, 7, 1, 1, 0, 0);
		//GridCom g_time=new GridCom(p_info, gbl, gdc, lb_Time,   		3, 8, 1, 1, 0, 0);
		
		
	}
	
	
	
	////////////////////////////////////버튼 메서드/////////////////////////////
	
	//동 테이블 벡터값으로 가져옴
	public void complexSet(){
		PreparedStatement pstmt=null;
		ResultSet rs=null;
		String sql="select * from view_cput";
		ViewCPUT dto=new ViewCPUT();
		try {
			pstmt=con.prepareStatement(sql);
			rs=pstmt.executeQuery();
			while (rs.next()) {
				Vector<String> vec=new  Vector<String>();
				dto.setComplex_id(rs.getInt("complex_id"));
				dto.setComplex_name(rs.getString("complex_name"));
				dto.setUnit_id(rs.getInt("unit_id"));
				dto.setUnit_name(rs.getString("unit_name"));
				vec.add(Integer.toString(dto.getComplex_id()));
				vec.add(dto.getComplex_name());
				vec.add(Integer.toString(dto.getUnit_id()));
				vec.add(dto.getUnit_name());
				cput.add(vec);
				
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			try {
				if(rs!=null)
					rs.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			try {
				if(pstmt!=null)
					pstmt.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}
	
	//동수 및 운송사 
	public void listadd(){
		checkv.add(cput.get(0).get(1).toString());
		ch_block.add(cput.get(0).get(1).toString());
		String listval=null;
		boolean flag =false;
		
			for(int i=1;i<cput.size();i++){
					
				String a=cput.get(i).get(1).toString(); //2
				int s=checkv.size(); //첫번째값 들어감
				
				for(int j=0;j<s;j++){
				
					listval=checkv.get(j).toString();
					if(listval.equals(a)){
						flag=false;
						break;
					}else if(!listval.equals(a)){
						flag=true;
						
					}
				}
				
				if(flag){
					 checkv.add(a);
					 ch_block.add(a);
					 flag=false;
				}
			}
			comListAdd();
	}
	public void comListAdd(){  //운송테이블 추가
		PreparedStatement pstmt=null;
		ResultSet rs=null;
		String sql="select * from company";
		try {
			pstmt=con.prepareStatement(sql);
			rs=pstmt.executeQuery();
			while(rs.next()){
				Company dto=new Company();
				Vector vec=new Vector();
				dto.setCompany_id(rs.getInt("company_id"));
				dto.setCompany_name(rs.getString("company_name"));
				vec.add(dto.getCompany_id());
				vec.add(dto.getCompany_name());
				ch_com.add(dto.getCompany_name());
				company.add(vec);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			try {
				if(pstmt!=null)pstmt.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				if(rs!=null)rs.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		
		
	}
	public void addClasslist(){ // 클레스 리스트 추가
		
		ch_class.removeAll();
		PreparedStatement pstmt=null;
		ResultSet rs=null;
	
		for(int i=0;i<cput.size();i++){
			if(cput.get(i).get(1).toString().equals(ch_block.getSelectedItem().toString())){
			   ch_class.add(cput.get(i).get(3).toString());
			}
		}
	}
	
	
	//등록
	public void regist(){
		userSelect();
		
		
		String a=ch_com.getSelectedItem();
		String companyid=null;
		for(int i=0;i<company.size();i++){
			if(a.equals(company.get(i).get(1))){
				companyid=company.get(i).get(0).toString();
			}
		}
		
	
		
		
		PreparedStatement pstmt=null;
		StringBuffer sql =new StringBuffer();
		
		sql.append("insert into INVOICE");
		sql.append("(INVOICE_ID, INVOICE_BARCODE, INVOICE_TAKER,  APTUSER_ID, COMPANY_ID) values ");
		sql.append("(seq_invoice.nextval,");
		sql.append("?,?,?,?)");
			
		
	
		
		try {
			pstmt=con.prepareStatement(sql.toString());
		
			pstmt.setString(1,tf_code.getText());
			pstmt.setString(2,tf_taker.getText());
			pstmt.setString(3,userid); //나중에 아이디 값뽑아야됨
			pstmt.setInt(4,Integer.parseInt(companyid));

			
			int reset=pstmt.executeUpdate();	
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			try {
				if(pstmt!=null)pstmt.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	
	}
	///리셋
	public void reset(){
		
		tf_code.setText(null);
		tf_taker.setText(null);
		
	}
	
	public void userSelect(){
		
		
		int unit=1;
		for(int i=0;i<cput.size();i++){
		
			
			if(cput.get(i).get(1).toString().equals(ch_block.getSelectedItem())){
				System.out.println("검출");
				if(cput.get(i).get(3).toString().equals(ch_class.getSelectedItem())){
					System.out.println("검출2");
					unit=Integer.parseInt(cput.get(i).get(2).toString());
				}
			}
		}
		
		System.out.println(unit);
		PreparedStatement pstmt=null;
		ResultSet rs=null;
		
		String sql="select aptuser_id from aptuser where unit_id=?";
		try {
			pstmt=con.prepareStatement(sql);
			pstmt.setInt(1,unit);
			rs=pstmt.executeQuery();
			
			
			rs.next();
			userid=rs.getString("aptuser_id");
		} catch (SQLException e) {
			try {
				pstmt=con.prepareStatement(sql);
				pstmt.setInt(1,1);
				rs=pstmt.executeQuery();
				rs.next();
				userid=rs.getString("aptuser_id");
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
			
			}
			
		}
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		Object bt=e.getSource();
		if(bt==bt_regist){
			System.out.println("등록");
			regist();
		}
		else if(bt==bt_reset){
			System.out.println("초기화");
			reset();
		}
		
	}
	



	
	public void itemStateChanged(ItemEvent e) {
		Object obj=e.getSource();
		if(obj==ch_block){
			addClasslist();
		}
		
	}
	
	
	
}
