package message;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

import javax.swing.table.AbstractTableModel;

public class RecieveMsgModel extends AbstractTableModel{
	
	Connection con;
	String userId;
	Vector<String> columnName;
	Vector<Vector> data = new Vector<Vector>();
	
	public RecieveMsgModel(Connection con, String userId ) {
		//System.out.println("CompUnitModel");
		this.con = con;
		this.userId = userId;
		
		columnName = new Vector<String>();
		columnName.add("송신자명");
		columnName.add("제목");
		columnName.add("수신시간");
		columnName.add("확인여부");
		columnName.add("확인시간");
		columnName.add("msg_recieve_id");
		columnName.add("msg_send_user_id");
		columnName.add("msg_send_content");
		
		getList("");
	}
	
	public void getList(String search){
		//System.out.println("CompUnitModel - getList : "+search);
		PreparedStatement pstmt=null;
		ResultSet  rs=null;
		
		StringBuffer  sql = new StringBuffer();
		sql.append(" select u.aptuser_name 송신자명, s.msg_send_title 제목, r.msg_recieve_time 수신시간 \n");
		sql.append("         , nvl(r.msg_confirm_flag,'N') 확인여부, r.msg_confirm_time 확인시간 \n");
		sql.append("         , r.msg_recieve_id, s.msg_send_user_id, s.msg_send_content \n");  // 
		sql.append(" from  recieve_message r \n");
		sql.append("         ,send_message    s \n");
		sql.append("         ,aptuser              u \n");
		sql.append(" where r.msg_send_id = s.msg_send_id \n");
		sql.append(" and    u.aptuser_id     = s.msg_send_user_id \n");
		sql.append(" and    r.msg_recv_user_id = ? \n");
		sql.append(" and   (u.aptuser_name like ? or  \n");
		sql.append("            s.msg_send_title like ? )  \n");
		sql.append(" order by r.msg_recieve_time desc ");
		
		//System.out.println(sql.toString());
		//System.out.println("userId = "+userId);
		
		try {
			pstmt = con.prepareStatement(sql.toString());
			pstmt.setString(1, userId);
			pstmt.setString(2, "%"+search+"%");
			pstmt.setString(3, "%"+search+"%");
			rs = pstmt.executeQuery();

			data.removeAll(data);
			while (rs.next()){
				Vector vec = new Vector();
				vec.add(rs.getString("송신자명"));
				vec.add(rs.getString("제목"));
				vec.add(rs.getString("수신시간"));
				vec.add(rs.getString("확인여부"));
				vec.add(rs.getString("확인시간"));
				vec.add(rs.getInt("msg_recieve_id"));
				vec.add(rs.getString("msg_send_user_id"));
				vec.add(rs.getString("msg_send_content"));
				
				data.add(vec);
			}
			//System.out.println("RecieveMsgModel -> getList -> OK");
		} catch (SQLException e) {
			System.out.println("RecieveMsgModel -> getList -> SQLException : "+e.getMessage());
			e.printStackTrace();
		} finally {
			if (rs!=null)
				try {
					rs.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			if (pstmt!=null)
				try {
					pstmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
		}
	}
	
	public boolean isCellEditable(int row, int col) {
		return false;
	}

	public int getColumnCount() {
		//columnName.
		//System.out.println("CompUnitModel - getColumnCount");
		return columnName.size();
	}
	
	public String getColumnName(int col) {
		//System.out.println("CompUnitModel - getColumnName");
		return columnName.elementAt(col);
	}
	
	public int findColumn(String colName) {
		int col = -1;
		for (int i=0; i<columnName.size();i++){
			String name = (String)columnName.elementAt(i);
			//System.out.println("name = "+name);
			if (name.toUpperCase().equals(colName.toUpperCase())){
				col=i;
				break;
			}			
		}
		return col;
	}

	public int getRowCount() {
		//System.out.println("CompUnitModel - getRowCount");
		return data.size();
	}

	public Object getValueAt(int row, int col) {
		//System.out.println("CompUnitModel - getValueAt");
		return data.elementAt(row).elementAt(col);
	}

}
