package Edit;
import java.awt.BorderLayout;
import java.awt.Choice;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import Edit.calender.ReturnCal;




public class RetunPan extends JPanel implements ActionListener{
	
		
	JLabel title;
	
	JLabel lb_id,lb_code,lb_day,lb_taker,lb_outTime,lb_Time,lb_txt;
	
	
	JTextField tf_id,tf_code;

	public JTextField tf_takeTime;

	JTextField tf_re;
	
	//날짜는 달력으로 할지 고민중
	
	JTextArea rple;
	
	ReturnCal calender;
	
	
	JPanel p_info;
	JPanel p_up;
	JPanel p_down;
	JPanel p_emp; //여백용 패널
	JPanel p_txt;
	JButton bt_reset;
	JButton bt_regist;
	GridBagLayout gbl;
	GridBagConstraints gdc;
	
	
	String date;
	/**
	 * 
	 */
	public RetunPan() {
		
		
		gbl=new GridBagLayout();
		gdc=new GridBagConstraints();
		
		setLayout(new BorderLayout());
		
		
		
		p_up=new JPanel();
		p_info=new JPanel(gbl);
		p_down=new JPanel();
		p_emp=new JPanel();
		p_txt=new JPanel();
		//p_info.setPreferredSize(new Dimension(700, 500));
		
		p_up.setPreferredSize(new Dimension(700, 100));
		
		title=new JLabel("RETRUN");
		title.setFont(new  Font("고딕",Font.BOLD , 60));
		title.setForeground(Color.white);
		
		lb_id=new JLabel("송장ID"); //회원 id
		lb_code=new JLabel("바코드"); //바코드
		lb_taker=new JLabel("수거예정일");//입고시간
		
	
		lb_outTime=new JLabel("출고시간");
		lb_Time =new JLabel("110:33:33");
		lb_txt=new JLabel("보네는 말");
		tf_id=new JTextField(20);
		
		tf_code=new JTextField(20);//바코드
		tf_takeTime=new JTextField("    클릭하세요",20);//수거예정일
		
		tf_id.setPreferredSize(new Dimension(20,30));
		tf_code.setPreferredSize(new Dimension(20,30));

		tf_takeTime.setPreferredSize(new Dimension(20,30));
		
		tf_re=new JTextField(20);
		tf_re.setPreferredSize(new Dimension(20,100));
		
		
		bt_regist=new JButton("입력");
		bt_reset=new JButton("초기화");
		rple=new JTextArea();//남김글
		rple.setPreferredSize(new Dimension(220, 100));
		rple.setBorder(BorderFactory.createLineBorder(Color.pink,2));
		p_txt.add(rple);
		
		
		add(p_up,BorderLayout.NORTH);
		p_up.setBackground(Color.pink);
		p_up.add(title);
		
		add(p_info);
		p_info.setBackground(Color.white);
		
		print();
		
		
		tf_id.setBorder(BorderFactory.createLineBorder(Color.PINK, 2));
		tf_code.setBorder(BorderFactory.createLineBorder(Color.PINK, 2));
		tf_takeTime.setBorder(BorderFactory.createLineBorder(Color.PINK, 2));
		
		
		add(p_down,BorderLayout.SOUTH);
		p_down.setPreferredSize(new Dimension(700, 200));
		p_down.setBackground(Color.pink);
		p_down.add(p_emp);
		p_emp.setPreferredSize(new Dimension(700, 50));
		//p_emp.add(rple);
		p_emp.setBackground(Color.pink);
		p_down.add(bt_reset);
		bt_reset.setPreferredSize(new Dimension(180, 50));
		bt_reset.setBackground(new Color(137, 210, 245));
		bt_reset.setBorder(BorderFactory.createLineBorder(Color.white,2));
		
		p_down.add(bt_regist);
		bt_regist.setPreferredSize(new Dimension(180, 50));
		bt_regist.setBackground(new Color(137, 210, 245));
		bt_regist.setBorder(BorderFactory.createLineBorder(Color.white,2));
		
	
		/////////////////////////////////////////////////////////////////////////////
		//이벤트 구현부분///////////////////////////////////////////////////////////
		
		
		tf_takeTime.addMouseListener(TakerClick);
		
		//////////////////////////////////////////////////////////////////
		
		
		
		
		
		
	
		
		setSize(700,700);
	}
	public void print(){

		GridCom g_t1=new GridCom(p_info, gbl, gdc, lb_id, 				0, 0, 1,1,0, 0);
		GridCom g_l1=new GridCom(p_info, gbl, gdc, tf_id, 				1, 0, 1,1,0, 0);
		GridCom g_t2=new GridCom(p_info, gbl, gdc, lb_code,			0, 1, 1,1,0, 0);
		GridCom g_l2=new GridCom(p_info, gbl, gdc, tf_code, 			1, 1, 1,1,0, 0);
		GridCom g_t3=new GridCom(p_info, gbl, gdc, lb_taker,			0, 2, 1,1,0, 0);
		GridCom g_l3=new GridCom(p_info, gbl, gdc, tf_takeTime, 	1, 2, 1,1,0, 0);
		GridCom g_commentl=new GridCom(p_info, gbl, gdc,lb_txt, 0, 3, 1,1, 0, 0);
		GridCom g_comment=new GridCom(p_info, gbl, gdc,rple, 1, 3, 1,1, 0, 0);
		GridCom g_timeT=new GridCom(p_info, gbl, gdc, lb_outTime,    3, 7, 1, 1, 0, 0);
		GridCom g_time=new GridCom(p_info, gbl, gdc, lb_Time,   		3, 8, 1, 1, 0, 0);
		
		
	}
	
	MouseListener TakerClick =new MouseAdapter() {
		public void mouseClicked(java.awt.event.MouseEvent e) {
			if(calender==null){
				calender=new ReturnCal(RetunPan.this);
				
			}
			else{
				calender.setVisible(true);
			}
		};
	};
	
	
	
	
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		
	}
	
}
