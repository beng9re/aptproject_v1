package aptuser;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

import db.DBModel;
import dto.Aptuser;

public class AptuserModel extends DBModel {
	private String[] colName = { "aptuser_id", "aptuser_pw", "aptuser_code", "aptuser_name", "aptuser_phone",
			"aptuser_regdate", "aptuser_live", "aptuser_perm", "aptuser_ip", "complex_id", "complex_name", "unit_id",
			"unit_name" };
	private String sql = "select * from aptuser A, (select * from complex C inner join unit U on C.complex_id = U.complex_id)"
			+ " B where A.unit_id = B.unit_id";
	boolean appendix = true;

	public AptuserModel() {
	}

	public AptuserModel(Connection conn) {
		this.conn = conn;
		init(colName, sql);
	}

	protected void setQuery(String sql, boolean appendix) {
		this.sql = sql;
		this.appendix = appendix;
		init(colName, sql);
	}

	protected void setTable(ResultSet rs) throws SQLException {
		arrList.clear();
		while (rs.next()) {
			Aptuser dto = new Aptuser();
			dto.setAptuser_id(rs.getString(colName[0]));
			dto.setAptuser_pw(rs.getString(colName[1]));
			dto.setAptuser_code(rs.getString(colName[2]));
			dto.setAptuser_name(rs.getString(colName[3]));
			dto.setAptuser_phone(rs.getString(colName[4]));
			dto.setAptuser_regdate(rs.getString(colName[5]));
			dto.setAptuser_live(rs.getString(colName[6]));
			dto.setAptuser_perm(rs.getInt(colName[7]));
			dto.setAptuser_ip(rs.getString(colName[8]));
			dto.setUnit_id(rs.getInt(colName[11]));
			//주소 관련정보
			if (appendix) {
				dto.setComplex_id(rs.getInt(colName[9]));
				dto.setComplex_name(rs.getString(colName[10]));
				dto.setUnit_name(rs.getString(colName[12]));
			}
			arrList.add(dto);
		}
	}
}
