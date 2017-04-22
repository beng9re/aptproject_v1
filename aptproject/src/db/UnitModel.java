package db;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

import dto.Unit;

public class UnitModel extends DBModel {
	private String[] colName = { "unit_id", "unit_name", "complex_id" };
	private String sql = "select * from unit order by unit_name asc";

	// 상속을 위한 기본 생성자
	public UnitModel() {
	}
	
	public UnitModel(Connection conn) {
		this.conn = conn;
		init(colName, sql);
	}
	
	protected void setTable(ResultSet rs) throws SQLException {
		arrList.clear();
		while (rs.next()) {
			Unit unit = new Unit(); 
			unit.setUnit_id(rs.getInt(colName[0]));
			unit.setUnit_name(rs.getString(colName[1]).replaceAll("\\D", ""));
			unit.setComplex_id(rs.getInt(colName[2]));
			arrList.add(unit);
		}
	}
}
