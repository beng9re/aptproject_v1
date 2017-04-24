/* 
 * aptuser테이블에서 아이디로
 * 조회 : selectData() 
 * 추가 : insertData()
 * 수정 : updateData()
 * 삭제 : deleteData()
 * 의 기능을 담당한다
 */
package db;

import java.sql.Connection;
import java.sql.SQLException;

import dto.Aptuser;

public class AptuserModelByID extends AptuserModel {
	private String dfSQL = "select * from aptuser A, (select * from complex C inner join unit U on C.complex_id = U.complex_id)"
			+ " B where A.unit_id = B.unit_id and aptuser_id = ?";
	private String sql = dfSQL;
	private String id;
	private String mode = "select";

	public AptuserModelByID(Connection conn, String id) {
		this.conn = conn;
		this.id = id;
		aptuserModel(sql, true);
	}

	protected void setSQL() throws SQLException {
		// 바인드 변수는 조건문으로 분류
		switch (mode) {
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

	public void selectData(String id) {
		this.id = id;
		mode = "select";
		sql = dfSQL;
		aptuserModel(sql, true);
	}

	public void insertData() {
		mode = "insert";
		sql = "insert into aptuser(aptuser_id,aptuser_code,aptuser_pw,aptuser_name,aptuser_phone,aptuser_perm,unit_id) "
				+ "values(?,?,?,?,?,?,?)";
		aptuserModel(sql, true);
	}

	public void updateData() {
		mode = "update";
		sql = "update aptuser set aptuser_code=?,aptuser_pw=?,aptuser_name=?,aptuser_phone=? where aptuser_id=?";
		aptuserModel(sql, true);
	}

	public void deleteData() {
		mode = "delete";
		sql = "delete from aptuser where aptuser_id = ?";
		aptuserModel(sql, true);
	}

}
