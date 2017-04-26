package Edit.calender;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseListener;
import java.io.File;
import java.net.URL;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Calendar;

import javax.imageio.ImageIO;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import Edit.RetunPan;

public class ReturnCal extends JFrame  {
	
	
	JLabel lb_cal;
	JLabel prev,next; //이전 이후 라벨

	JPanel p_cal,p_day,p_cal2,p_dayin;
	Calendar cal;
	ArrayList<DayPan> dplist=new ArrayList<DayPan>();
	
	int year;
	int month;
	int startday;
	int lastday;
	
	int date;
	String dataData;
   ImageIcon i_next;
   Todaypan tp;
   
	RetunPan rp;
	URL url;
	
public ReturnCal(RetunPan rp) {

	tp=new Todaypan();
	this.rp=rp;
	p_cal=new JPanel();
	p_cal2=new JPanel(new BorderLayout());
	p_dayin=new JPanel();
	p_cal.setPreferredSize(new Dimension(400, 40));
	p_day=new JPanel(new GridLayout(6, 7));
	
	prev=new JLabel("◀");
	prev.setPreferredSize(new Dimension(30, 30));

	
	next=new JLabel("▶");
	next.setPreferredSize(new Dimension(30, 30));
	
	
	cal=Calendar.getInstance();
	
	year=cal.get(Calendar.YEAR);
	month=cal.get(Calendar.MONTH);
	
	lastDate();
	cal.set(year, month,1);
	startday=cal.get(Calendar.DAY_OF_WEEK);
	lb_cal=new JLabel(year+"년 "+(month+1)+"월");
	lb_cal.setFont(new Font("고딕체", Font.BOLD, 20));
	
	
	
	
	add(p_cal2,BorderLayout.NORTH);
	//p_cal.setBackground(Color.cyan);
	p_cal2.add(p_cal,BorderLayout.NORTH);
	p_cal.add(prev);
	p_cal.add(lb_cal);
	p_cal.add(next);
	p_cal2.add(p_dayin);
	p_cal2.add(tp);
	
	add(p_day);
	p_day.setBackground(Color.yellow);
	createDay(rp);
	prev.addMouseListener(lbA);
	next.addMouseListener(lbA);
	

	
	
	setVisible(true);
	setBounds(1200, 300, 300,300);

	
	
	
	  
	  
  }
  
  //라벨 클릭 이벤트 
	MouseListener lbA=new MouseAdapter() {
		public void mouseClicked(java.awt.event.MouseEvent e) {
		Object obj=e.getSource();
			if(obj==prev){
				//System.out.println("이전");
				month--;
				if(month<1){
					year--;
					month=12;
			
				}
			}
			else if(obj==next){
				//System.out.println("다음");
				month++;
				if(month>12){
					year++;
					month=1;
				}
			}
			
			
			//System.out.println(startday);
			lbUpdate();
		};
	};
 //달력 라벨 갱신
 public void lbUpdate(){
	 cal.set(year, month,1);
	 startday=cal.get(Calendar.DAY_OF_WEEK);
	 System.out.println(startday);
	 lb_cal.setText(year+"년 "+(month+1)+"월");
	 
	 lastDate();
	 createDay(rp);
 }
 
 public void lastDate(){
	 cal.set(year, month+1,0);
	 lastday=cal.get(Calendar.DATE);
	 //System.out.println(lastday);
 }
 
 //달력 데이 초기화
 public void createDay(RetunPan rp){
	 
	 dplist.removeAll(dplist);
	 this.p_day.removeAll();
	 date=0;
	 for(int i=1;i<=42;i++){
		 DayPan day=new DayPan(ReturnCal.this,rp.tf_takeTime);
		 if(i>=startday&&date<lastday){
			date++;
			 day.lb_d.setText(Integer.toString(i-startday+1));
			
		}
		 dplist.add(day);
		 p_day.add(day);
	 }
 }
 

}
