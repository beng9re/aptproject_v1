package db;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

import dto.Unit;

public class UnitModelByID extends UnitModel {
	private String[] colName;
	private String sql = "select * from unit where complex_id=? order by unit_name asc";
	private int cpid;

	public UnitModelByID(Connection conn, int cpid) {
		this.conn = conn;
		this.cpid =cpid;
		init(colName, sql);
	}
	
	public void getList(int cpid) {
		this.cpid = cpid;
		init(colName, sql);
	}
	
	protected void setSQL() throws SQLException {
		pstmt = conn.prepareStatement(sql);
		pstmt.setInt(1, cpid);
	}

}
