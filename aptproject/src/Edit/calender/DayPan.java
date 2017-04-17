package Edit.calender;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.LayoutManager;
import java.awt.event.MouseAdapter;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class DayPan extends JPanel{
	
	JLabel lb_d;
	ReturnCal returnCal;
	int year;
	int month;
	int day;
	JTextField taker;
	int clickCount;
	
public DayPan(ReturnCal returnCal,JTextField taker) {
	
	
		this.taker=taker;
		this.returnCal=returnCal;
		setLayout(new BorderLayout());
		
		
		lb_d=new JLabel("");
		add(lb_d,BorderLayout.NORTH);
	
		
		this.addMouseListener(new MouseAdapter() {
		
			public void mouseClicked(java.awt.event.MouseEvent e) {
				
				clickPan();
				
			};
		} );
		
		
		setBackground(Color.white);
		setBorder(BorderFactory.createLineBorder(Color.pink));
		setPreferredSize(new Dimension(400/6, 400/7));
			
	}
	//클릭할때 반응
	public void clickPan(){
		
		//System.out.println(returnCal.startday+returnCal.date);
		
		year=returnCal.year;
		month=returnCal.month+1;
		try {
			day=Integer.parseInt(lb_d.getText());
		} catch (NumberFormatException e) {
			calPanReSet();
			return;
		}
		
		
		
		
		
		
	
		
		
		
		//System.out.println(returnCal.dataData);
		int s=returnCal.startday+returnCal.date-1;
		for(int i=0;i<returnCal.dplist.size();i++){
			
			
			if(returnCal.dplist.get(i)==this){
				if(returnCal.startday-1<=i&&s>i){
					returnCal.dplist.get(i).setBackground(new Color(255, 255, 150));
					returnCal.dplist.get(i).clickCount++;
				}
			
			
				
			}
			else{
				returnCal.dplist.get(i).setBackground(Color.white);
				returnCal.dplist.get(i).clickCount=0;
				//System.out.println(i+"번쨰");
			}
			
		}
		showYes();
		
	}
	
	public void showYes(){
		if(clickCount==2){
			//제이 옵션페인으로 유효성 체크
			
			int result=JOptionPane.showConfirmDialog(this, "선택하신 수거 예정 날짜가\n"+selectedDay()+"가 맞습니까?",
					"수거예정일",JOptionPane.YES_NO_OPTION);
			if(result==0){
				returnCal.dataData=selectedDay();
			}else{
				return;
			}
			
			
			taker.setText(selectedDay());
			returnCal.setVisible(false);
			}
		
	}
	public void calPanReSet(){
		for(int i=0;i<returnCal.dplist.size();i++){
			returnCal.dplist.get(i).setBackground(Color.white);
		}
	}
	public String selectedDay(){
		StringBuffer bf=new StringBuffer();
		bf.append(year);
		bf.append("/");
		bf.append(month);
		bf.append("/");
		bf.append(day);
		
		return bf.toString();
	}

}
