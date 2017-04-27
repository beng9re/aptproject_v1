package db;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

import dto.Aptuser;

public class AptuserModel extends DBModel {
	protected String[] colName = { "aptuser_id", "aptuser_pw", "aptuser_code", "aptuser_name", "aptuser_phone",
			"aptuser_regdate", "aptuser_live", "aptuser_perm", "aptuser_ip", "complex_id", "complex_name", "unit_id",
			"unit_name" };
	private String mode;
	private String sql;
	private String id;
	protected boolean appendix;

	// 상속을 위한 생성자
	public AptuserModel() {
	}

	// 전체를 조회할 때의 생성자
	public AptuserModel(Connection conn) {
		this.conn = conn;
		selectData();
	}

	// 아이디를 입력할 때의 생성자
	public AptuserModel(Connection conn, String id) {
		this.conn = conn;
		this.id = id;
		
		selectDataByID(id);
	}

	protected void setSQL() throws SQLException {
		// 바인드 변수는 조건문으로 분류
		switch (mode) {
		case "default":
			pstmt = conn.prepareStatement(sql);
			break;
		case "select":
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, id);
			break;
		case "insert":
			Aptuser insert_user = (Aptuser) arrList.get(0);
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, insert_user.getAptuser_id());
			pstmt.setString(2, insert_user.getAptuser_code());
			pstmt.setString(3, insert_user.getAptuser_pw());
			pstmt.setString(4, insert_user.getAptuser_name());
			pstmt.setString(5, insert_user.getAptuser_phone());
			pstmt.setInt(6, insert_user.getAptuser_perm());
			pstmt.setInt(7, insert_user.getUnit_id());

			break;
		case "update":
			Aptuser update_user = (Aptuser) arrList.get(0);
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, update_user.getAptuser_code());
			pstmt.setString(2, update_user.getAptuser_pw());
			pstmt.setString(3, update_user.getAptuser_name());
			pstmt.setString(4, update_user.getAptuser_phone());
			pstmt.setString(5, id);
			break;
		case "delete":
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, id);
			break;
		}
	}

	public void selectData() {
		mode = "default";
		sql = "select * from aptuser A, (select * from complex C inner join unit U on C.complex_id = U.complex_id)"
				+ " B where A.unit_id = B.unit_id(+)";
		appendix = true;
		init(colName, sql);
	}
	
	public void selectDataByID(String id) {
		this.id = id;
		mode = "select";
		sql = "select * from aptuser A, (select * from complex C inner join unit U on C.complex_id = U.complex_id)"
				+ " B where A.unit_id = B.unit_id(+) and aptuser_id = ?";
		appendix = true;
		init(colName, sql);
	}

	public void insertData() {
		mode = "insert";
		sql = "insert into aptuser(aptuser_id,aptuser_code,aptuser_pw,aptuser_name,aptuser_phone,aptuser_perm,unit_id) "
				+ "values(?,?,?,?,?,?,?)";
		appendix = false;
		init(colName, sql);
	}

	public void updateData() {
		mode = "update";
		sql = "update aptuser set aptuser_code=?,aptuser_pw=?,aptuser_name=?,aptuser_phone=? where aptuser_id=?";
		appendix = false;
		init(colName, sql);
	}

	public void deleteData() {
		mode = "delete";
		sql = "delete from aptuser where aptuser_id = ?";
		appendix = false;
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
			// 주소 관련정보
			if (appendix) {
				dto.setComplex_id(rs.getInt(colName[9]));
				dto.setComplex_name(rs.getString(colName[10]));
				dto.setUnit_name(rs.getString(colName[12]));
			}
			arrList.add(dto);
		}
	}
}
