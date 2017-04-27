package db;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

import dto.Unit;

public class UnitModel extends DBModel {
	private String[] colName = { "unit_id", "unit_name", "complex_id" };
	private String mode;
	private String sql;
	private int cpid;

	// 전체 unit을 조회
	public UnitModel(Connection conn) {
		this.conn = conn;
		mode = "default";
		sql = "select * from unit order by unit_name asc";
		init(colName, sql);
	}

	// 특정 complex의 unit을 조회
	public UnitModel(Connection conn, int cpid) {
		this.conn = conn;
		this.cpid = cpid;
		mode = "cpid";
		sql = "select * from unit where complex_id=? order by unit_name asc";
		init(colName, sql);
	}

	public void getList(int cpid) {
		this.cpid = cpid;
		init(colName, sql);
	}

	protected void setSQL() throws SQLException {
		switch (mode) {
		case "default":
			pstmt = conn.prepareStatement(sql);
			break;

		case "cpid":
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, cpid);
			break;
		}
	}

	protected void setTable(ResultSet rs) throws SQLException {
		arrList.clear();
		while (rs.next()) {
			Unit unit = new Unit();
			unit.setUnit_id(rs.getInt(colName[0]));
			unit.setUnit_name(Integer.parseInt(rs.getString(colName[1]).replaceAll("\\D", "")));
			unit.setComplex_id(rs.getInt(colName[2]));
			arrList.add(unit);
		}
	}
}
