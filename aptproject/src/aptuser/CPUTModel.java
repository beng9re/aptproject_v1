package aptuser;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

import db.DBModel;
import dto.ViewCPUT;

public class CPUTModel extends DBModel {
	private String[] colName = { "complex_name", "complex_id", "unit_name", "unit_id" };
	private String sql = "select c.COMPLEX_NAME as 동,c.COMPLEX_id as 동_id,u.UNIT_NAME as 호수,u.UNIT_id as 호수_id "
			+ "FROM complex c,UNIT u WHERE c.COMPLEX_ID=u.COMPLEX_ID ORDER BY c.COMPLEX_ID,u.UNIT_id asc";

	public CPUTModel(Connection conn) {
		this.conn = conn;
		init(colName, sql);
	}

	protected void setTable(ResultSet rs) throws SQLException {
		arrList.clear();
		while (rs.next()) {
			ViewCPUT cput = new ViewCPUT(); 
			cput.setComplex_id(rs.getInt("동_id"));
			cput.setComplex_name(rs.getString("동"));
			cput.setUnit_id(rs.getInt("호수_id"));
			cput.setUnit_name(rs.getString("호수"));
			arrList.add(cput);
		}
	}
}
