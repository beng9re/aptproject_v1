package message;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

import javax.swing.table.AbstractTableModel;

public class RecieveMsgModel extends AbstractTableModel{
	
	Connection con;
	Vector<String> columnName;
	Vector<Vector> data = new Vector<Vector>();
	
	public RecieveMsgModel(Connection con ) {
		System.out.println("CompUnitModel");
		this.con = con;
		
		columnName = new Vector<String>();
		columnName.add("송신자명");
		columnName.add("제목");
		columnName.add("수신시간");
		columnName.add("확인여부");
		columnName.add("msg_recieve_id");
		columnName.add("send_user_id");
		columnName.add("msg_send_content");
		
		getList("");
	}
	
	public void getList(String search){
		System.out.println("CompUnitModel - getList : "+search);
		PreparedStatement pstmt=null;
		ResultSet  rs=null;
		
		StringBuffer  sql = new StringBuffer();
		sql.append(" select u.user_name 송신자명, s.msg_send_title 제목, r.msg_recieve_time 수신시간 \n");
		sql.append("        , msg_confirm_flag 확인여부, r.msg_recieve_id, s.send_user_id, s.msg_send_content \n");  // 
		sql.append(" from  recieve_message r \n");
		sql.append("         ,send_message    s \n");
		sql.append("         ,apt_user             u \n");
		sql.append(" where r.msg_send_id = s.msg_send_id \n");
		sql.append(" and    u.user_id        = s.send_user_id \n");
		sql.append(" and   (u.user_name like ? or  \n");
		sql.append("            s.msg_send_title like ? )  \n");
		sql.append(" order by r.msg_recieve_time desc ");
		
		System.out.println(sql.toString());
		
		try {
			pstmt = con.prepareStatement(sql.toString());
			pstmt.setString(1, "%"+search+"%");
			pstmt.setString(2, "%"+search+"%");
			rs = pstmt.executeQuery();

			data.removeAll(data);
			while (rs.next()){
				Vector vec = new Vector();
				vec.add(rs.getString("송신자명"));
				vec.add(rs.getString("제목"));
				vec.add(rs.getString("수신시간"));
				vec.add(rs.getString("확인여부"));
				vec.add(rs.getString("msg_recieve_id"));
				vec.add(rs.getString("send_user_id"));
				vec.add(rs.getString("msg_send_content"));
				
				data.add(vec);
			}
		} catch (SQLException e) {
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
