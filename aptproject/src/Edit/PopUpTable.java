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
	
	public PopUpTable(RetunPan rp,Connection con,String ck) {
		this.con=con;
		this.rp=rp;
		String sql="select invoice_id as ����ID, invoice_barcode as ������ڵ�, invoice_arrtime as ��Ͻð�, invoice_taker as ������, invoice_taketime as ���ɽð�, invoice_takeflag as ���ɿ���, aptuser_id as ȸ��ID "
				+" from view_acis "+" where aptuser_id="+"'"+ck+"'";
		

		ad=new AdminModel(con);
		ad.getList(sql);
		ta=new JTable(ad);
		sp=new JScrollPane(ta);
		
		add(sp);
		
		ta.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				JTable a=(JTable)e.getSource();
				
				
				String value=ad.getValueAt(a.getSelectedRow(), 0).toString();
			
				
				
				int result=JOptionPane.showConfirmDialog(PopUpTable.this, "������ �����ȣ\n"+value+"�� �½��ϱ�?",
						"�����ȣ ����",JOptionPane.YES_NO_OPTION);
				
				
				if(result==0){
					
					rp.tf_id.setText(value);
					setVisible(false);
				}else{
					return;
				}
				
				
				
				
			}
		});
		setLocationRelativeTo(rp);
		setLocation(20, 20);
	
		setBounds(100, 100, 400, 400);
	
	}

	
}
