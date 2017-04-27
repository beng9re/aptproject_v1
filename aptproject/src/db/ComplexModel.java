package db;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.TreeMap;

public class ComplexModel extends DBModel {
	private String[] colName = { "complex_id", "complex_name" };
	private String sql = "select * from complex order by complex_name desc";
	private int cpid;
	private TreeMap<Integer, Integer> arrList = new TreeMap<Integer, Integer>();

	public ComplexModel(Connection conn) {
		this.conn = conn;
		init(colName, sql);
	}

	public void getComplexData(int cpid) {
		this.cpid = cpid;
		init(colName, sql);
	}

	protected void setTable(ResultSet rs) throws SQLException {
		arrList.clear();
		while (rs.next()) {
			arrList.put(Integer.parseInt(rs.getString(colName[1]).replaceAll("\\D", "")), rs.getInt(colName[0]));
		}
	}

	public TreeMap<Integer, Integer> getMap() {
		return arrList;
	}
}
