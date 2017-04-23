package db;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LoginModel extends AptuserModel {
	private String dfSQL = "select * from aptuser where aptuser_id = ? and aptuser_pw = ?";
	private String sql = dfSQL;
	private String id, pw, barcode, ip;
	private String mode = "login";
	private String condition;

	public LoginModel(Connection conn) {
		this.conn = conn;
		aptuserModel(sql, false);
	}

	protected void setSQL() throws SQLException {
		switch (mode) {
		case "login":
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, id);
			pstmt.setString(2, pw);
			break;

		case "barcode":
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, barcode);
			break;

		case "ip":
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, ip);
			
			switch (condition) {
			case "login":
				pstmt.setString(2, id);
				break;
			case "barcode":
				pstmt.setString(2, barcode);
				break;
			}
			break;
		}
	}
	
	// ID�� PW�� �Է¹޾� ȸ�����θ� �Ǵ��Ѵ�
	public boolean loginChk(String id, char[] pw) {
		this.id = id;
		this.pw = String.valueOf(pw);
		mode = "login";
		sql = dfSQL;
		aptuserModel(sql, false);
		return doChk();
	}

	// ���ڵ带 �Է¹޾� ȸ�����θ� Ȯ���Ѵ�
	public boolean barcodeChk(String barcode) {
		this.barcode = barcode;
		mode = "barcode";
		sql = "select * from aptuser where aptuser_code = ?";
		aptuserModel(sql, false);
		return doChk();
	}
	
	// ȸ�� ���翩�θ� �Ǵ��ϴ� �޼���
	private boolean doChk() {
		boolean result = false;
		if (arrList.size() == 1) {
			setIPAddress();
			result = true;
		}
		return result;
	}

	// ȸ�� ip������ db�� �Է��Ѵ�
	private void setIPAddress() {
		try {
			// ������ȯ�濡���� loopback�� ��ȯ�� (�ذ�å��???)
			ip = InetAddress.getLocalHost().getHostAddress();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		
		if (mode.equals("login")) {
			sql = "update aptuser set aptuser_ip = ? where aptuser_id = ?";
		} else if (mode.equals("barcode")) {
			sql = "update aptuser set aptuser_ip = ? where aptuser_code = ?";
		}
		
		condition = mode;
		mode = "ip";
		aptuserModel(sql, false);
	}

}
