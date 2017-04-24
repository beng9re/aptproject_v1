package message;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

import javax.swing.table.AbstractTableModel;

public class SendMsgListModel extends AbstractTableModel{
	
	Connection con;
	Vector<String> columnName;
	Vector<Vector> data = new Vector<Vector>();
	
	public SendMsgListModel(Connection con, String send_user_id, String search ) {
		System.out.println("SendMsgListModel");
		this.con = con;
		
		columnName = new Vector<String>();
		columnName.add("송신자명");
		columnName.add("제목");
		columnName.add("송신시간");
		columnName.add("msg_send_content");
		columnName.add("msg_send_id");
		columnName.add("msg_send_user_id");
		
		getList(send_user_id, search);
	}
	
	public void getList(String send_user_id, String search){
		System.out.println("CompUnitModel - getList : "+send_user_id);
		PreparedStatement pstmt=null;
		ResultSet  rs=null;
		
		StringBuffer  sql = new StringBuffer();
		sql.append(" select u.aptuser_name 송신자명, s.msg_send_title 제목, s.msg_sendtime 송신시간 \n");
		sql.append("          ,s.msg_send_content, s.msg_send_id, s.msg_send_user_id \n");
		sql.append(" from   send_message s \n");
		sql.append("          ,aptuser      u \n");
		sql.append(" where s.msg_send_user_id = u.aptuser_id \n");
		sql.append(" and     s.msg_send_user_id = ? \n");
		sql.append(" and     (s.msg_send_title like ? or \n");
		sql.append("             s.msg_send_content like ?) \n");
		sql.append(" order by s.msg_send_id desc \n");		
		System.out.println("SendMsgListModel : \n"+sql.toString());
		
		try {
			pstmt = con.prepareStatement(sql.toString());
			pstmt.setString(1, send_user_id);
			pstmt.setString(2, "%"+search+"%");
			pstmt.setString(3, "%"+search+"%");
			rs = pstmt.executeQuery();

			data.removeAll(data);
			while (rs.next()){
				Vector vec = new Vector();
				vec.add(rs.getString("송신자명"));
				vec.add(rs.getString("제목"));
				vec.add(rs.getString("송신시간"));
				vec.add(rs.getString("msg_send_content"));
				vec.add(rs.getInt("msg_send_id"));
				vec.add(rs.getString("msg_send_user_id"));
				
				data.add(vec);
			}
			System.out.println("SendMsgListModel -> getList -> OK : size = "+data.size());
		} catch (SQLException e) {
			System.out.println("SendMsgListModel -> getList -> SQLException : "+e.getMessage());
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
