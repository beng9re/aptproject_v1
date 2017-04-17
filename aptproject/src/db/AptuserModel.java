package db;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AptuserModel extends DBModel {
	private String[] colName = { "aptuser_id", "aptuser_pw", "aptuser_code", "unit_id", "aptuser_name",
			"aptuser_phone", "aptuser_regdate", "aptuser_live", "aptuser_perm", "aptuser_ip" };
	private String sql = "select * from aptuser";
	
	public AptuserModel() {
	}
	
	public AptuserModel(Connection conn) {
		this.conn = conn;
		init(colName, sql);
	}
	
	protected void setQuery(String sql) {
		this.sql = sql;
		init(colName, sql);
	}
	
	protected void setTable(ResultSet rs, int count_col) throws SQLException {
		arrList.clear();
		while (rs.next()) {
			Aptuser dto = new Aptuser();
			dto.setAptuser_id(rs.getString(colName[0]));
			dto.setAptuser_pw(rs.getString(colName[1]));
			dto.setAptuser_code(rs.getString(colName[2]));
			dto.setUnit_id(rs.getInt(colName[3]));
			dto.setAptuser_name(rs.getString(colName[4]));
			dto.setAptuser_phone(rs.getString(colName[5]));
			dto.setAptuser_regdate(rs.getString(colName[6]));
			dto.setAptuser_live(rs.getString(colName[7]));
			dto.setAptuser_perm(rs.getString(colName[8]));
			dto.setAptuser_ip(rs.getString(colName[9]));
			arrList.add(dto);
		}
	}
}
