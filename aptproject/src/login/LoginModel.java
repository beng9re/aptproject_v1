package login;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

import db.AptuserModel;

public class LoginModel extends AptuserModel {
	private String sql = "select * from aptuser where aptuser_id = ? and aptuser_pw = ?";
	private String id, pw;
	
	public LoginModel(Connection conn) {
		this.conn = conn;
		aptuserModel(sql, false);
	}
	
	protected void setSQL() throws SQLException {
		pstmt = conn.prepareStatement(sql);
		pstmt.setString(1, id);
		pstmt.setString(2, pw);
	}
	
	public boolean loginChk(String id, char[] pw) {
		this.id = id;
		this.pw = String.valueOf(pw);
		setData();
		boolean result = false;
		if (arrList.size() == 1) {
			result = true;
		}
		return result;
	}
	
}
