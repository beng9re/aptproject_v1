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
import java.awt.event.MouseListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.PseudoColumnUsage;
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

import Edit.locker.PostBoxMain;
import apt.test.NewAptPanel;
import db.DBManager;
import dto.Company;
import dto.ComplexDto;
import dto.ViewCPUT;

public class InvEditPan extends JPanel implements ActionListener,ItemListener{
	
	JLabel lb_block,lb_class,lb_code,lb_com,lb_id,lb_takerTime,lb_Time,lb_box,title;
	
	public JTextField tf_code,tf_taker,tf_box;
	Choice ch_block,ch_class,ch_com,ch_id;
	
	
	JPanel p_info,p_up,p_down,p_emp; //여백용 패널
	JButton bt_reset,bt_regist;
	GridBagLayout gbl;
	GridBagConstraints gdc;
	
	Connection con;
	PostBoxMain pm;
	
	String initba="스캐너로 바코드를 읽어주세요";
	
	Vector <String> u_id=new Vector<String>();
	Vector<Vector> cput=new  Vector<Vector>();
	//뷰의의 정보를 가진 벡터
	Vector<String> checkv=new Vector<String>();
	//빈벡터 판단용벡터
	Vector<Vector> company=new  Vector<Vector>();
	//운송사 테이블 정보를 가진 벡터

	String userid; //회원아이디 값이 들어갈것
	String err="해당 집에 사는 회원이 없음";
	boolean classflag=false;
	boolean userflag=false;
	int invoce_id;
	boolean adminflag=false;
	
	
	public InvEditPan(Connection con) {
		this.con=con;
		setLayout(new BorderLayout());
		
		//---------------------객체생성부분
		gbl=new GridBagLayout();
		gdc=new GridBagConstraints();
		
		p_up=new JPanel();
		p_info=new JPanel(gbl);
		p_down=new JPanel();
		p_emp=new JPanel();
		
		lb_box=new JLabel("박스번호");
		title=new JLabel("INVOICE");
		lb_block=new JLabel("동"); //동 id
		lb_class=new JLabel("호수");
		lb_id=new JLabel("회원ID");//수령인
		lb_code=new JLabel("바코드"); //바코드
		lb_com=new JLabel("운송사"); //운송사
		
		ch_block=new Choice();
		ch_class=new  Choice();
		ch_com=new Choice();
		ch_id=new Choice();
		
		tf_box=new JTextField(20);
		tf_taker=new JTextField(20);
		tf_code=new JTextField(initba,20);
		
		bt_regist=new JButton("입력");
		bt_reset=new JButton("초기화");
		//p_info.setPreferredSize(new Dimension(700, 500));
		//----------------------사이즈조정
		
		p_up.setPreferredSize(new Dimension(700, 100));
		p_down.setPreferredSize(new Dimension(700, 180));
		p_emp.setPreferredSize(new Dimension(700, 20));
		
		bt_reset.setPreferredSize(new Dimension(180, 50));
		bt_regist.setPreferredSize(new Dimension(180, 50));
		
		
		ch_id.setPreferredSize(new Dimension(220,30));
		ch_block.setPreferredSize(new Dimension(220,30));
		ch_class.setPreferredSize(new Dimension(220,30));
		ch_com.setPreferredSize(new Dimension(220,30));
		
		tf_code.setPreferredSize(new Dimension(20,30));
		tf_box.setPreferredSize(new Dimension(20,30));
		
		
		//--------------------------------------바탕색
		
		
		p_up.setBackground(new Color(247,146,30));
		p_info.setBackground(Color.white);
		p_emp.setBackground(new Color(247,146,30));
		p_down.setBackground(new Color(247,146,30));
		tf_box.setBackground(Color.white);
		bt_reset.setBackground(Color.white);
		bt_regist.setBackground(Color.white);
		
		//---------------------------------선색
		tf_box.setBorder(BorderFactory.createLineBorder(new Color(247,146,30), 2));
		tf_code.setBorder(BorderFactory.createLineBorder(new Color(247,146,30), 2));
		bt_reset.setBorder(BorderFactory.createLineBorder(new Color(247,146,30),2));
		bt_regist.setBorder(BorderFactory.createLineBorder(new Color(247,146,30),2));
		
		
		
		
		//----------------라벨 글자관련
		title.setFont(new  Font("고딕",Font.BOLD , 60));
		title.setForeground(Color.white);
		tf_code.setForeground(Color.gray);
		
		
		
		//---------------------------------------객체추가
		
		add(p_up,BorderLayout.NORTH);
		p_up.add(title);
		add(p_info);
		add(p_down,BorderLayout.SOUTH);
		p_down.add(p_emp);
		p_down.add(bt_reset);
		p_down.add(bt_regist);
		
		
		print();
	
		
		
		
		complexSet();
		listadd();
		//---------------------------------텍스트 에디트 속성
		tf_box.setEditable(false);
		
		//이벤트 리스너 연결 부분------------------------------------------------------------------------------//
		tf_code.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				tf_code.setText("");
				tf_code.setForeground(Color.BLACK);
				
			}
		});
		tf_box.addMouseListener(boxL);
		ch_block.addItemListener(this);
		ch_class.addItemListener(this);
		
		bt_regist.addActionListener(this);
		bt_reset.addActionListener(this);
		
	
		
		
		setSize(700,700);
		pm=new PostBoxMain(this);
	}
	
	
	
	//화면에 센터영역에 뿌리는 것
	public void print(){

		GridCom g_t1=new GridCom(p_info, gbl, gdc,lb_block, 				0, 0, 1,1,0, 0);
		GridCom g_l1=new GridCom(p_info, gbl, gdc, ch_block, 				1, 0, 1,1,0, 0);
		GridCom g_t2=new GridCom(p_info, gbl, gdc,lb_class, 				0, 1, 1,1,0, 0);
		GridCom g_l2=new GridCom(p_info, gbl, gdc, ch_class, 				1, 1, 1,1,0, 0);
		GridCom g_t3=new GridCom(p_info, gbl, gdc, lb_id, 			  	    0, 2, 1,1,0, 0);
		GridCom g_l3=new GridCom(p_info, gbl, gdc, ch_id, 		     		1, 2, 1,1,0, 0);
		GridCom g_t4=new GridCom(p_info, gbl, gdc, lb_code,			    0, 3, 1,1,0, 0);
		GridCom g_l4=new GridCom(p_info, gbl, gdc, tf_code, 			    1, 3, 1,1,0, 0);
		GridCom g_t5=new GridCom(p_info, gbl, gdc, lb_com,					0, 4, 1,1,0, 0);
		GridCom g_l5=new GridCom(p_info, gbl, gdc, ch_com, 			    1, 4, 1,1,0, 0);
		GridCom g_t6=new GridCom(p_info, gbl, gdc, lb_box, 			   		0, 5, 1,1,0, 0);
		GridCom g_l6=new GridCom(p_info, gbl, gdc, tf_box, 			    	1, 5, 1,1,0, 0);
		
		
	}
	
	
	
	////////////////////////////////////버튼 메서드/////////////////////////////
	
	//동 테이블 벡터값으로 가져옴
	public void complexSet(){
		PreparedStatement pstmt=null;
		ResultSet rs=null;
		String sql="select * from view_cput order by complex_name,unit_name asc ";
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
			System.out.println("조회된호수 x");
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
		ch_block.removeAll();
		checkv.removeAll(checkv);
		
		checkv.add(cput.get(0).get(1).toString());
		ch_block.add("▼ 동을 선택하세요");
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
		ch_com.removeAll();
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
		ch_class.add("▼호수를 선택해줘요");
		for(int i=0;i<cput.size();i++){
			if(cput.get(i).get(1).toString().equals(ch_block.getSelectedItem().toString())){
			
				if(cput.get(i).get(3)==null){
					ch_class.removeAll();
					ch_class.add("해당 동에 호수가 없음");
					classflag=false;
					return;
				}
				classflag=true;
				ch_class.add(cput.get(i).get(3).toString());
			}
		}
	}
	
	public void addIdlist(){
		ch_class.removeAll();
		pm.setVisible(true);
	}
	
	
	//등록
	public void selectId(){
		PreparedStatement pstmt=null;
		ResultSet rs=null;
		String sql="select invoice_id from invoice order by invoice_id desc";
		try {
			pstmt=con.prepareStatement(sql);
			rs=pstmt.executeQuery();
			rs.next();
			invoce_id=rs.getInt("invoice_id");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
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
	
	public void boxInsert(){
		PreparedStatement pstmt=null;
		String sql="update storagebox set invoice_id=?,box_use='Y' where box_num=?";
		
		try {
			pstmt=con.prepareStatement(sql);
			pstmt.setInt(1, invoce_id);
			pstmt.setInt(2, Integer.parseInt(tf_box.getText()));
			pstmt.executeUpdate();
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
	
	public void regist(){
		//userSelect();
		
		if(ch_block.getSelectedIndex()==0){
			JOptionPane.showMessageDialog(this, "동수를 골라주세요");
			return;
		}else if(ch_class.getSelectedIndex()==0){
			JOptionPane.showMessageDialog(this, "호수를 골라주세요");
			return;
		}
		
		
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
			
		String ch_idv="admin";
	
		
		try {
			pstmt=con.prepareStatement(sql.toString());
			
			pstmt.setString(1,tf_code.getText());
			pstmt.setString(2,tf_taker.getText());
			if(ch_id.getSelectedItem()!=null){
				ch_idv=ch_id.getSelectedItem();
				adminflag=false;
			}else{
				adminflag=true;
			}
			pstmt.setString(3,ch_idv); //나중에 아이디 값뽑아야됨
			pstmt.setInt(4,Integer.parseInt(companyid));

			
			int reset=pstmt.executeUpdate();
			con.commit();
			
		} catch (SQLException e) {
			try {
				con.rollback();
				JOptionPane.showMessageDialog(this, "해당바코드번호가 이미존재 함!!!!");
				return;
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
			e.printStackTrace();
		}finally{
			try {
				if(pstmt!=null)pstmt.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	
		selectId();//마지막 invoice_id 값가져옴 즉 방금입력한 invoice임
		boxInsert();//박스 선택!
		pm.p_south.removeAll();
		pm.setBox();
		pm.addList();
		if(adminflag==false){
		JOptionPane.showMessageDialog(this, "등록완료!");
		}else{
			JOptionPane.showMessageDialog(this, "해당호수에 사람이 없어서 \n 관리자로 등록완료!");
		}
	}
	///리셋
	public void reset(){
		ch_class.removeAll();
		ch_id.removeAll();
		listadd();
		tf_box.setText(null);
		tf_code.setText(initba);
		tf_code.setForeground(Color.gray);
		tf_taker.setText(null);
		
	}
	
	public void userSelect(){
		
		ch_id.removeAll();
	
		u_id.removeAll(u_id);
		
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
			
			while(rs.next()){
				userid=rs.getString("aptuser_id");
				u_id.add(userid);
				
			}
		} catch (SQLException e) {
			try {
				pstmt=con.prepareStatement(sql);
				pstmt.setInt(1,1);
				rs=pstmt.executeQuery();
				rs.next();
				userflag=true;
				userid=rs.getString("aptuser_id");  //해당사람이 없다면 관리자로 등록됨
				ch_id.add(err);
			} catch (SQLException e1) {
				System.out.println("조회된 사람 x");
			
			}finally{
				
				try {
					if(rs!=null)rs.close();
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				try {
					if(pstmt!=null)pstmt.close();
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
			
		}
		if(userflag){
			ch_id.add(err);
		}
		for(int i=0;i<u_id.size();i++){
			ch_id.add(u_id.get(i));
		}
	}
	
	MouseListener boxL=new MouseAdapter() {
		
		public void mouseClicked(MouseEvent e) {
			pm.setVisible(true);
			
		};
	};
	
	
	@Override
	public void actionPerformed(ActionEvent e) {
		Object bt=e.getSource();
		if(bt==bt_regist){
			if(initba.equals(tf_code.getText())){
				JOptionPane.showMessageDialog(this, "바코드 값을 설정해주세요");
				return;
			}
			else if(classflag==false){
				System.out.println("해당 값 x");
				
			}else{
			regist();
			System.out.println("등록");
			reset();
			}
		}
		else if(bt==bt_reset){
			
			reset();
		}
		
	}
	



	
	public void itemStateChanged(ItemEvent e) {
		Object obj=e.getSource();
		if(obj==ch_block){
			addClasslist();
		}
		if(obj==ch_class){
			userSelect();
		}
		
	}
	
	
	
}
