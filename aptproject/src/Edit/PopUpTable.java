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
		
		
	
			sql.append("select invoice_id as ����ID, invoice_barcode as ������ڵ�, invoice_arrtime as ��Ͻð�,");
			sql.append("invoice_taker as ������, invoice_taketime as ���ɽð�, invoice_takeflag as ���ɿ���, aptuser_id as ȸ��ID ");
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
					int result=JOptionPane.showConfirmDialog(rp, "������ �����ȣ\n"+value+"�� �½��ϱ�?",
							"�����ȣ ����",JOptionPane.YES_NO_OPTION);
					
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
