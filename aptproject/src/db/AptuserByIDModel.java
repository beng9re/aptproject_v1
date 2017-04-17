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

public class AptuserByIDModel extends AptuserModel {
	private String sql = "select * from aptuser where aptuser_id = ?";
	private String id;
	private String mode = "select";
	
	public AptuserByIDModel(Connection conn, String id) {
		this.conn = conn;
		this.id = id;
		setQuery(sql);
	}
	
	protected void setSQL() throws SQLException {
		//바인드 변수는 조건문으로 분류
		switch (mode) {
			case "select":
				pstmt = conn.prepareStatement(sql);
				pstmt.setString(1, id);
				break;
			case "insert":
				pstmt = conn.prepareStatement(sql);
				break;
			case "update":
				Aptuser user = (Aptuser) arrList.get(0);
				pstmt = conn.prepareStatement(sql);
				pstmt.setString(1, user.getAptuser_pw());
				pstmt.setString(2, user.getAptuser_code());
				pstmt.setString(3, user.getAptuser_name());
				pstmt.setString(4, user.getAptuser_phone());
				pstmt.setString(5, user.getAptuser_live());
				pstmt.setString(5, id);
				break;
			case "delete":
				pstmt = conn.prepareStatement(sql);
				pstmt.setString(1, id);
				break;
		}
	}
	
	public void selectData() {
		mode = "select";
		sql = "select * from aptuser where aptuser_id = ?";
		setData();
	}
	
	public void insertData() {
		mode = "insert";
		sql = "insert into aptuser values()";
		setData();
	}
	
	public void updateData() {
		mode = "update";
		sql = "update aptuser set aptuser_pw=?,aptuser_code=?,aptuser_name=?,aptuser_phone?,aptuser_live=? where aptuser_id=?";
		setData();
	}
	
	public void deleteData() {
		mode = "delete";
		sql = "delete from aptuser where aptuser_id = ?";
		setData();
	}
	
}
