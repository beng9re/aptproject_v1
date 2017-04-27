package db;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.sql.Connection;
import java.sql.SQLException;

import dto.Aptuser;

public class LoginModel extends AptuserModel {
	private String sql;
	private String id, pw, barcode, ip;
	private String mode;
	private String condition;

	public LoginModel(Connection conn) {
		this.conn = conn;
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
	
	// ID와 PW를 입력받아 회원여부를 판단한다
	public boolean loginChk(String id, char[] pw) {
		this.id = id;
		this.pw = String.valueOf(pw);
		mode = "login";
		sql = "select * from aptuser where aptuser_id = ? and aptuser_pw = ?";
		super.appendix = false;
		init(super.colName, sql);
		return doChk();
	}

	// 바코드를 입력받아 회원여부를 확인한다
	public boolean barcodeChk(String barcode) {
		this.barcode = barcode;
		mode = "barcode";
		sql = "select * from aptuser where aptuser_code = ?";
		super.appendix = false;
		init(super.colName, sql);
		return doChk();
	}
	
	public String getVerifiedID() {
		if (arrList.size() == 1) {
			return ((Aptuser)arrList.get(0)).getAptuser_id(); 
		}
		return null;
	}
	
	// 회원 존재여부를 판단하는 메서드
	private boolean doChk() {
		if (arrList.size() == 1) {
			setIPAddress();
			return true;
		}
		return false;
	}

	// 회원 ip정보를 db에 입력한다
	private void setIPAddress() {
		try {
			// 리눅스환경에서는 loopback이 반환됨 (해결책은???)
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
		super.appendix = false;
		init(super.colName, sql);
	}

}
