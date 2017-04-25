package Edit;

import java.awt.ScrollPane;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Connection;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import viewer.AdminModel;

public class PopUpTable extends JFrame{
	
	
	
	JScrollPane sp;
	JTable ta;
	AdminModel ad;
	Connection con;
	
	public PopUpTable(Connection con,String ck) {
		this.con=con;
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
				
				System.out.println(a.getSelectedRow());
			}
		});
		
		setVisible(true);
		setBounds(100, 100, 400, 400);
	
	}

	
}
