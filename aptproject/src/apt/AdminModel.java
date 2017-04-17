package apt;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Vector;

import javax.swing.table.AbstractTableModel;

public class AdminModel extends AbstractTableModel{
	Vector<String> columnName = new Vector<String>();
	Vector<Vector> data = new Vector<Vector>();
	Connection con;

	public AdminModel(Connection con) {
		this.con = con;
		getList("select * from aptuser");
	}

	public void getList(String sql) {
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			pstmt = con.prepareStatement(sql.toString());
			rs = pstmt.executeQuery();

			columnName.removeAll(columnName);
			data.removeAll(data);
			
			ResultSetMetaData meta = rs.getMetaData();
			for (int i = 1; i <=meta.getColumnCount(); i++) {
				columnName.add(meta.getColumnName(i));
			}

			while (rs.next()) {
				Vector vec = new Vector();
				for (int i = 1; i <= meta.getColumnCount(); i++) {
					vec.add(rs.getString(i));
				}
				data.add(vec);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public int getColumnCount() {
	
		return columnName.size();
	}

	public int getRowCount() {
	
		return data.size();
	}

	public Object getValueAt(int row, int col) {
		return data.elementAt(row).elementAt(col);
	}
	
	public String getColumnName(int col) {
		return columnName.get(col);
	}
	
}
