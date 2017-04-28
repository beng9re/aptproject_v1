package Edit;

import java.awt.ScrollPane;
import java.awt.Window;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Connection;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import viewer.AdminModel;

public class PopUpTable extends JFrame{
	
	
	
	JScrollPane sp;
	JTable ta;
	AdminModel ad;
	Connection con;
	RetunPan rp;
	StringBuffer sql;
	int clickCount=-1;
	int selectrow;
	
	public PopUpTable(RetunPan rp,Connection con) {
		this.con=con;
		this.rp=rp;
		 sql =new StringBuffer();
		
		
		String name=rp.userList.get(0).getAptuser_id();
		
		//System.out.println(rp.userList.get(0).getAptuser_perm());
		
		
	
			sql.append("select invoice_id as 송장ID, invoice_barcode as 송장바코드, invoice_arrtime as 등록시간,");
			sql.append("invoice_taker as 수령인, invoice_taketime as 수령시간, invoice_takeflag as 수령여부, aptuser_id as 회원ID ");
			sql.append(" from view_acis where invoice_id in");
			sql.append("(select v.invoice_id  from view_acis v where  aptuser_id="+"'"+name+"') and (");
			sql.append(" invoice_id not in(");
			sql.append("select invoice_id  from RETURNINV r where r.INVOICE_ID in(");
			sql.append("select v.invoice_id  from view_acis v where  aptuser_id=");
			sql.append("'");
			sql.append(name);
			sql.append("')))  ");
			//sql.append("(select v.invoice_id  from view_acis v where  aptuser_id="+"'"+name+"')");
			
		System.out.println(sql.toString());

		ad=new AdminModel(con);
		System.out.println(sql.toString());
		ad.getList(sql.toString());
		
		ta=new JTable(ad);
		ta.setRowHeight(20);
		sp=new JScrollPane(ta);
		
		add(sp);
		
		ta.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				JTable a=(JTable)e.getSource();
				
				
				if(selectrow==a.getSelectedRow()&&clickCount==1){
				
					String value=ad.getValueAt(a.getSelectedRow(), 0).toString();
					int result=JOptionPane.showConfirmDialog(rp, "선택한 송장번호\n"+value+"가 맞습니까?",
							"송장번호 선택",JOptionPane.YES_NO_OPTION);
					
					if(result==0){
						
						rp.tf_id.setText(value);
						setVisible(false);
					}else{
						return;
					}
					ad.getList(sql.toString());
				}
				else if(clickCount==0){
					
					selectrow=a.getSelectedRow();
					clickCount=1;
				}
				else{
					clickCount=0;
					selectrow=-1;
				}
				
				
				
				
				
				
				
			}
		});
		
		setLocationRelativeTo(rp);
		
	
		setBounds(505, 860, 900, 120);
	
	}

	
}
