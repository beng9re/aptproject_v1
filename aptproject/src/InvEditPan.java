import java.awt.BorderLayout;
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
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.Border;

public class InvEditPan extends JPanel {
	
	JLabel lb_id,lb_code,lb_com,lb_taker,lb_takerTime,lb_Time;
	
	JLabel title;
	
	JTextField tf_id,tf_code,tf_com,tf_taker;
	
	JPanel p_info;
	JPanel p_up;
	JPanel p_down;
	JPanel p_emp; //여백용 패널
	JButton bt_reset;
	JButton bt_regist;
	GridBagLayout gbl;
	GridBagConstraints gdc;
	
	
	/**
	 * 
	 */
	public InvEditPan() {
		
		
		
		title=new JLabel("SUBMIT");
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
		lb_id=new JLabel("회원ID"); //회원 id
		lb_code=new JLabel("바코드"); //바코드
		
		lb_com=new JLabel("운송사"); //운송사
		lb_taker=new JLabel("수령인");//수령인
		lb_takerTime=new JLabel("수거시간");
		lb_Time =new JLabel("110:33:33");
		
		tf_id=new JTextField(20);
		tf_code=new JTextField(20);
		tf_com=new JTextField(20);
		tf_taker=new JTextField(20);
		
		tf_id.setPreferredSize(new Dimension(20,30));
		tf_code.setPreferredSize(new Dimension(20,30));
		tf_com.setPreferredSize(new Dimension(20,30));
		tf_taker.setPreferredSize(new Dimension(20,30));
		
		bt_regist=new JButton("입력");
		bt_reset=new JButton("초기화");
		
		add(p_up,BorderLayout.NORTH);
		p_up.setBackground(Color.pink);
		
		add(p_info);
		p_info.setBackground(Color.white);
		
		
		print();
		tf_id.setBorder(BorderFactory.createLineBorder(Color.PINK, 2));
		tf_code.setBorder(BorderFactory.createLineBorder(Color.PINK, 2));
		tf_com.setBorder(BorderFactory.createLineBorder(Color.PINK, 2));
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
		
		
		//이벤트구현
		tf_code.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				
			}
		});
		
	
		
		
		
		
	
		
		
		
		
		
		
	
		
		setSize(700,700);
	}
	public void print(){

		GridCom g_t1=new GridCom(p_info, gbl, gdc, lb_id, 				0, 0, 1,1,0, 0);
		GridCom g_l1=new GridCom(p_info, gbl, gdc, tf_id, 				1, 0, 1,1,0, 0);
		GridCom g_t2=new GridCom(p_info, gbl, gdc, lb_code,			0, 2, 1,1,0, 0);
		GridCom g_l2=new GridCom(p_info, gbl, gdc, tf_code, 			1, 2, 1,1,0, 0);
		GridCom g_t3=new GridCom(p_info, gbl, gdc, lb_com,				0, 3, 1,1,0, 0);
		GridCom g_l3=new GridCom(p_info, gbl, gdc, tf_com, 			1, 3, 1,1,0, 0);
		GridCom g_t4=new GridCom(p_info, gbl, gdc, lb_taker, 			0, 4, 1,1,0, 0);
		GridCom g_l4=new GridCom(p_info, gbl, gdc, tf_taker, 			1, 4, 1,1,0, 0);
		GridCom g_timeT=new GridCom(p_info, gbl, gdc, lb_takerTime,    3, 7, 1, 1, 0, 0);
		GridCom g_time=new GridCom(p_info, gbl, gdc, lb_Time,   		3, 8, 1, 1, 0, 0);
		
		
	}
	
	
	
}
