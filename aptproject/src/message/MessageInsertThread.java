package message;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import main.TreeMain;

public class MessageInsertThread extends  Thread{
	
	TreeMain treeMain;
	Connection con;
	String userID;
	boolean execFlag=false;
	Thread  thread;
	
	
	public MessageInsertThread(TreeMain treeMain) {
		this.treeMain = treeMain;
		this.con = this.treeMain.getConnection();
		this.userID = this.treeMain.getUserID();
		thread = new Thread(this);
		thread.start();
		
	}
	
	// 송장 check
	public void InvoiceCheck(){
		PreparedStatement pstmt=null;
		ResultSet rs=null;
		PreparedStatement subPstmt=null;
		ResultSet  subRs=null;
		StringBuffer sql=new StringBuffer();
		StringBuffer subSql=new StringBuffer();
		int invoice_id;
		String invoice_barcode;
		String company_name;
		String recv_user_name;
		String invoice_arrtime;
		StringBuffer title = new StringBuffer();
		StringBuffer  msgContent=new StringBuffer();
		
		// 현재 유저가 포함된 가족의 신규 송장 조회
		sql.append(" select ivc.invoice_id, ivc.invoice_barcode , com.company_name , usr.aptuser_name \n");
		sql.append("        , ivc.invoice_arrtime, usr.unit_id, ivc.invoice_id, usr.aptuser_id \n");
		sql.append(" from  aptuser usr \n");
		sql.append("          ,unit      unt \n");
		sql.append(" 	        ,complex cpl \n");
		sql.append(" 	        ,invoice   ivc \n");
		sql.append(" 	        ,company com \n");
		sql.append(" where unt.unit_id       = usr.unit_id \n");
		sql.append(" and    cpl.complex_id = unt.complex_id \n");
		sql.append(" and    ivc.aptuser_id = usr.aptuser_id \n");
		sql.append(" and    com.company_id = ivc.company_id \n");
		sql.append(" and    nvl(ivc.invoice_takeflag,'N') = 'N' \n");
		sql.append(" and    usr.unit_id = (select unit_id from aptuser fml where fml.aptuser_id = ?) \n");
		sql.append(" and    not exists (select 0 \n");
		sql.append("                           from   send_message smg \n");
		sql.append(" 						      where smg.invoice_id = ivc.invoice_id) \n");
		
		try {
			pstmt = con.prepareStatement(sql.toString());
			pstmt.setString(1, this.userID);
			rs=pstmt.executeQuery();
			
			while (rs.next()){
				invoice_id = rs.getInt("invoice_id");
				invoice_barcode = rs.getString("invoice_barcode");
				company_name = rs.getString("company_name");
				recv_user_name = rs.getString("aptuser_name");
				invoice_arrtime   = rs.getString("invoice_arrtime");
				
				title.delete(0, title.length());
				title.append("택배도착 (수신자 : "+recv_user_name+")");
				
				msgContent.delete(0, msgContent.length());
				msgContent.append("수신자 : "+recv_user_name + "\n");
				msgContent.append("송장번호 : "+invoice_barcode + "\n");
				msgContent.append("운송사 : "+company_name + "\n");
				msgContent.append("도착시간 : "+invoice_arrtime + "\n");
				
				// 송신 메세지 
				subSql.delete(0, subSql.length());
				subSql.append(" insert into send_message (msg_send_id, msg_send_user_id, msg_send_title, msg_send_content, msg_sendtime, invoice_id, returninv_id) ");
				subSql.append(" values (seq_send_message.nextval, 'system', ?, ?, sysdate, ?, null) ");
				
				subPstmt = con.prepareStatement(subSql.toString());
				subPstmt.setString(1, title.toString());
				subPstmt.setString(2, msgContent.toString());
				subPstmt.setInt(3, invoice_id);
				int result = subPstmt.executeUpdate();
				
				if (result != 0){
					// 수신 메세지
					
				}
				
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	// 반품 check
	public void ReturnCheck(){
		
	}
	
	public void run() {
		while (execFlag){
			try {
				thread.sleep(1000);
				System.out.println("MessageThread start");
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

}
