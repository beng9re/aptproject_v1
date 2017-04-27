package message;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import main.TreeMain;

public class MessageAutoInsertThread extends  Thread{
	
	TreeMain treeMain;
	Connection con;
	String userID;
	String adminUserID;
	boolean threadFlag=false;
	Thread  thread;
	int runCount=0;
	
	
	public MessageAutoInsertThread(TreeMain treeMain) {
		this.treeMain = treeMain;
		this.con = this.treeMain.getConnection();
		this.userID = this.treeMain.getUserID();
		this.adminUserID = this.treeMain.getAdminUserID();
		
		if (adminUserID.trim().length()==0){
			adminUserID="admin";
		}
		
		threadFlag=true;
		thread = new Thread(this);
		thread.start();
		System.out.println("MessageInsertThread 생성");
	}
	
	public void setThreadFlag(boolean threadFlag){
		this.threadFlag = threadFlag;
		System.out.println("MessageAutoInsertThread -setThreadFlag : threadFlag="+threadFlag);
	}
	
	// 송장 check
	public void InvoiceCheck(){
		
		if (threadFlag==false ) return;
		
		if (con==null) return;
		
		PreparedStatement pstmt=null;
		ResultSet rs=null;
		StringBuffer sql=new StringBuffer();
		
		int msg_send_id;
		int invoice_id;
		String invoice_barcode;
		String company_name;
		String recv_user_name;
		String invoice_arrtime;
		String complex_name;
		String unit_name;
		StringBuffer title = new StringBuffer();
		StringBuffer  msgContent=new StringBuffer();
		
		// 현재 유저가 포함된 가족의 신규 송장 조회
		sql.append(" select ivc.invoice_id, ivc.invoice_barcode , com.company_name , usr.aptuser_name \n");
		sql.append("        , ivc.invoice_arrtime, cpl.complex_name, unt.unit_name \n");
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
		sql.append(" 						      where smg.invoice_id = ivc.invoice_id  \n");
		sql.append(" 						      and     smg.returninv_id is null ) \n");
		
		
		try {
			// Connection Auto Commit 잠시 false
			con.setAutoCommit(false);

			pstmt = con.prepareStatement(sql.toString());
			pstmt.setString(1, this.userID);
			rs=pstmt.executeQuery();
			
			while (rs.next()){
				invoice_id = rs.getInt("invoice_id");
				invoice_barcode = rs.getString("invoice_barcode");
				company_name = rs.getString("company_name");
				recv_user_name = rs.getString("aptuser_name");
				invoice_arrtime   = rs.getString("invoice_arrtime");
				complex_name   = rs.getString("complex_name");
				unit_name          = rs.getString("unit_name");
				
				title.delete(0, title.length());
				title.append("택배도착 (수신자 : "+recv_user_name+")");
				
				msgContent.delete(0, msgContent.length());
				msgContent.append("수 신 자 : "+recv_user_name + "\n");
				msgContent.append("동, 호수 : "+complex_name + " - " + unit_name + "\n");
				msgContent.append("송장번호 : "+invoice_barcode + "\n");
				msgContent.append("운 송 사 : "+company_name + "\n");
				msgContent.append("도착시간 : "+invoice_arrtime );
				
				// next seq_send_message check
				sql.delete(0, sql.length());
				sql.append(" select  seq_send_message.nextval msg_send_id from dual");
				pstmt = con.prepareStatement(sql.toString());
				rs=pstmt.executeQuery();
				
				if (rs.next()){
					msg_send_id=rs.getInt("msg_send_id");

					// 송신 메세지 Insert  /////////////////////////
					sql.delete(0, sql.length());
					sql.append(" insert into send_message (msg_send_id, msg_send_user_id, msg_send_title, msg_send_content, msg_sendtime, invoice_id) ");
					sql.append(" values (?, ?, ?, ?, sysdate, ?) ");
					
					pstmt = con.prepareStatement(sql.toString());
					pstmt.setInt(1, msg_send_id);
					pstmt.setString(2, adminUserID);
					pstmt.setString(3, title.toString());
					pstmt.setString(4, msgContent.toString());
					pstmt.setInt(5, invoice_id);
					int result1 = pstmt.executeUpdate();
					
					// 송신 메세지가 생성된 경우.
					if (result1!=0){
						// 수신 메세지 Insert : 해당 유저의 가족들 모두에게 보낸다.
						System.out.println("insert send_message count : "+result1);
						sql.delete(0, sql.length());
						sql.append("insert into recieve_message (msg_recieve_id, msg_send_id, msg_recv_user_id, msg_recieve_time) ");
						sql.append(" select seq_recieve_message.nextval msg_recieve_id, ? msg_send_id \n");
						sql.append("           ,fml.aptuser_id msg_recv_user_id, sysdate msg_recieve_time \n");
						sql.append(" from aptuser usr, aptuser fml \n");
						sql.append(" where     fml.unit_id = usr.unit_id \n");
						sql.append(" and usr.aptuser_id = ? \n");
						sql.append(" and not exists (select 0 \n");
						sql.append("                      from   send_message s \n");
						sql.append("                              ,recieve_message r \n");
						sql.append("                      where s.msg_send_id = ? \n");
						sql.append("                      and    s.invoice_id = ? \n");
						sql.append("                      and    r.msg_send_id = s.msg_send_id \n");
						sql.append("                      and    r.msg_recv_user_id = fml.aptuser_id) \n");
						
						System.out.println(sql.toString());
						
						pstmt = con.prepareStatement(sql.toString());
						pstmt.setInt(1, msg_send_id);
						pstmt.setString(2, userID);
						pstmt.setInt(3, msg_send_id);
						pstmt.setInt(4, invoice_id);
						int result2 = pstmt.executeUpdate();
						System.out.println("insert recieve_message count : "+result2);
					}
				}				
			}
			
		} catch (SQLException e) {
			try {
				con.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			e.printStackTrace();
			System.out.println("MessageAutoInsertThread SQLException Error : "+e.getErrorCode() + "-"+ e.getMessage());
		} finally {
			
			// Commit;
			try {
				con.commit();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			// rs close
			if (rs!=null)
				try {
					rs.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			// pstmt close
			if (pstmt!=null)
				try {
					pstmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			// Connection AutoCommit true 로 다시 지정
			try {
				con.setAutoCommit(true);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
				
	}
	
	// 반품 check
	public void ReturnCheck(){
		
		if (threadFlag==false ) return;
		
		if (con==null) return;
		
		PreparedStatement pstmt=null;
		ResultSet rs=null;
		ResultSet rsSub=null;
		
		StringBuffer sql=new StringBuffer();
		String user_name;
		String complex_name;
		String unit_name;
		String returninv_time;
		String returninv_barcode;
		String returninv_date;
		String returninv_comment;
		int returninv_id;
		String returninv_msg_type;
		StringBuffer title = new StringBuffer();
		StringBuffer  msgContent=new StringBuffer();
		
		// 유저가 등록한 반품정보
		sql.append(" select usr.aptuser_name \n");
        sql.append("          ,cpl.complex_name \n");
        sql.append(" 	        ,unt.unit_name \n");
        sql.append(" 	        ,rtn.returninv_time \n"); 
        sql.append(" 	        ,rtn.returninv_barcode \n");
        sql.append(" 	        ,rtn.returninv_date \n");
        sql.append(" 	        ,rtn.returninv_comment \n");
        sql.append(" 	        ,rtn.returninv_id \n");
        sql.append(" 	        ,'R' returninv_msg_type \n");
        sql.append(" from   returninv  rtn \n");
        sql.append("           ,invoice    ivc \n");
        sql.append("           ,aptuser    usr \n");
        sql.append(" 	         ,unit       unt \n");
        sql.append(" 	         ,complex    cpl \n");
        sql.append(" where  ivc.invoice_id = rtn.invoice_id \n");
        sql.append(" and     usr.aptuser_id = ivc.aptuser_id \n");
        sql.append(" and     unt.unit_id = usr.unit_id \n");
        sql.append(" and     cpl.complex_id = unt.complex_id \n");
        sql.append(" and     ivc.aptuser_id = ? \n");
        sql.append(" and     rtn.returninv_arr is null \n");
        sql.append(" and     rtn.returninv_dep is null \n");
        sql.append(" and     not exists (select 0 \n");
        sql.append("                            from   recieve_message rm \n");
        sql.append(" 				                        ,send_message    sm \n");
        sql.append(" 				              where  sm.msg_send_id = rm.msg_send_id \n");
        sql.append(" 				              and    sm.returninv_id = rtn.returninv_id \n");
        sql.append(" 				              and    sm.returninv_msg_type = 'R') \n");
        
		try {
	        // Connection Auto Commit 잠시 false
			con.setAutoCommit(false);
			
			pstmt = con.prepareStatement(sql.toString());
			pstmt.setString(1, userID);
			rs=pstmt.executeQuery();
			
			while (rs.next()){
				user_name 				= rs.getString("user_name");
				complex_name 		= rs.getString("user_name");
				unit_name 				= rs.getString("user_name");
				returninv_time 			= rs.getString("user_name");
				returninv_barcode 	= rs.getString("user_name");
				returninv_date 			= rs.getString("user_name");
				returninv_comment 	= rs.getString("user_name");
				returninv_id 				= rs.getInt("returninv_id");
				returninv_msg_type 	= rs.getString("user_name");
				
				title.delete(0, title.length());
				title.append("반품요청등록 ("+user_name+", "+returninv_barcode+")");
				
				msgContent.delete(0, msgContent.length());
				msgContent.append("이     름 : "+user_name + "\n");
				msgContent.append("동, 호수 : "+complex_name + " - " + unit_name + "\n");
				msgContent.append("바 코 드 : "+returninv_barcode + "\n");
				msgContent.append("수거예정일 : "+returninv_date + "\n");
				msgContent.append("Comment : "+returninv_comment );
				
				// next seq_send_message check
				sql.delete(0, sql.length());
				sql.append(" select  seq_send_message.nextval msg_send_id from dual");
				pstmt = con.prepareStatement(sql.toString());
				rsSub=pstmt.executeQuery();
				
			}
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
	
	public void run() {
		while (threadFlag){
			try {
				runCount++;
				if (runCount==1){
					System.out.println("MessageAutoInsertThread  start");
				}
				thread.sleep(1000);
				//System.out.println("MessageThread start");
				InvoiceCheck();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

}
