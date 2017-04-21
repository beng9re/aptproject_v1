package aptuser;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

import db.DBModel;
import dto.ViewCPUT;

public class CPUTModel extends DBModel {
	private String[] colName = { "complex_name", "complex_id", "unit_name", "unit_id" };
	private String sql = "select c.COMPLEX_NAME as ��,c.COMPLEX_id as ��_id,u.UNIT_NAME as ȣ��,u.UNIT_id as ȣ��_id "
			+ "FROM complex c,UNIT u WHERE c.COMPLEX_ID=u.COMPLEX_ID ORDER BY c.COMPLEX_ID,u.UNIT_id asc";

	public CPUTModel(Connection conn) {
		this.conn = conn;
		init(colName, sql);
	}

	protected void setTable(ResultSet rs) throws SQLException {
		arrList.clear();
		while (rs.next()) {
			ViewCPUT cput = new ViewCPUT(); 
			cput.setComplex_id(rs.getInt("��_id"));
			cput.setComplex_name(rs.getString("��"));
			cput.setUnit_id(rs.getInt("ȣ��_id"));
			cput.setUnit_name(rs.getString("ȣ��"));
			arrList.add(cput);
		}
	}
}
