package message;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

import javax.swing.table.AbstractTableModel;

public class CompUnitModel extends AbstractTableModel{
	
	Connection con;
	Vector<String> columnName;
	Vector<Vector> data = new Vector<Vector>();
	
	public CompUnitModel(Connection con ) {
		//System.out.println("CompUnitModel");
		this.con = con;
		
		columnName = new Vector<String>();
		columnName.add("동");
		columnName.add("호수");
		columnName.add("사용자명");
		columnName.add("사용자ID");
		
		getList("");
	}
	
	public void getList(String search){
		//System.out.println("CompUnitModel - getList : "+search);
		PreparedStatement pstmt=null;
		ResultSet  rs=null;
		
		StringBuffer  sql = new StringBuffer();
		sql.append(" select c.COMPLEX_NAME 동, u.UNIT_NAME 호수, a.APTUSER_NAME 사용자명, a.APTUSER_ID 사용자ID \n");
		sql.append(" from   COMPLEX c, UNIT u, APTUSER a \n");
		sql.append(" where  c.COMPLEX_ID = u.COMPLEX_ID \n");
		sql.append(" and    a.UNIT_ID = u.UNIT_ID \n");
		sql.append(" and (c.COMPLEX_NAME like ? or \n");
		sql.append("         u.UNIT_NAME like ? or \n");
		sql.append("         a.APTUSER_NAME like ? ) \n");
		sql.append(" order by 1, 2 ");
		
		//System.out.println(sql.toString());
		
		try {
			pstmt = con.prepareStatement(sql.toString());
			pstmt.setString(1, "%"+search+"%");
			pstmt.setString(2, "%"+search+"%");
			pstmt.setString(3, "%"+search+"%");
			rs = pstmt.executeQuery();

			data.removeAll(data);
			while (rs.next()){
				Vector vec = new Vector();
				vec.add(rs.getString("동"));
				vec.add(rs.getString("호수"));
				vec.add(rs.getString("사용자명"));
				vec.add(rs.getString("사용자ID"));
				
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
