package message;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

import javax.swing.table.AbstractTableModel;

public class RecieveUserListModel extends AbstractTableModel{
	
	Connection con;
	Vector<String> columnName;
	Vector<Vector> data = new Vector<Vector>();
	
	public RecieveUserListModel(Connection con, int msg_send_id ) {
		System.out.println("RecieveUserLIstModel");
		this.con = con;
		
		columnName = new Vector<String>();
		columnName.add("��");
		columnName.add("ȣ��");
		columnName.add("������");
		columnName.add("Ȯ�ο���");
		columnName.add("Ȯ�νð�");
		columnName.add("msg_recieve_id");
		
		getList(msg_send_id);
	}
	
	public void getList(int msg_send_id){
		System.out.println("RecieveUserListModel -getList - msg_send_id : "+msg_send_id);
		PreparedStatement pstmt=null;
		ResultSet  rs=null;
		
		StringBuffer  sql = new StringBuffer();
		sql.append(" select com.complex_name ��, unt.unit_name ȣ��, usr.aptuser_name ������ \n");
		sql.append("          ,nvl(rcv.msg_confirm_flag,'N') Ȯ�ο���, rcv.msg_confirm_time Ȯ�νð� \n");
		sql.append("          ,rcv.msg_recieve_id \n");
		sql.append(" from   recieve_message rcv \n");
		sql.append("          ,aptuser                usr \n");
		sql.append("          ,unit                      unt \n");
		sql.append("          ,complex               com \n");
		sql.append(" where  usr.aptuser_id    = rcv.msg_recv_user_id \n");
		sql.append(" and      unt.unit_id         = usr.unit_id \n");
		sql.append(" and     com.complex_id = unt.complex_id \n");
		sql.append(" and     rcv.msg_send_id = ? \n");
		sql.append(" order by 1,2 \n");
		
		System.out.println(sql.toString());
		
		try {
			pstmt = con.prepareStatement(sql.toString());
			pstmt.setInt(1, msg_send_id);
			rs = pstmt.executeQuery();

			data.removeAll(data);
			while (rs.next()){
				Vector vec = new Vector();
				vec.add(rs.getString("��"));
				vec.add(rs.getString("ȣ��"));
				vec.add(rs.getString("������"));
				vec.add(rs.getString("Ȯ�ο���"));
				vec.add(rs.getString("Ȯ�νð�"));
				vec.add(rs.getInt("msg_recieve_id"));
				
				data.add(vec);
			}
			System.out.println("RecieveMsgModel -> getList -> OK");
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
