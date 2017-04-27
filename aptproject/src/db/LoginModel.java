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
	
	// ID�� PW�� �Է¹޾� ȸ�����θ� �Ǵ��Ѵ�
	public boolean loginChk(String id, char[] pw) {
		this.id = id;
		this.pw = String.valueOf(pw);
		mode = "login";
		sql = "select * from aptuser where aptuser_id = ? and aptuser_pw = ?";
		super.appendix = false;
		init(super.colName, sql);
		return doChk();
	}

	// ���ڵ带 �Է¹޾� ȸ�����θ� Ȯ���Ѵ�
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
	
	// ȸ�� ���翩�θ� �Ǵ��ϴ� �޼���
	private boolean doChk() {
		if (arrList.size() == 1) {
			setIPAddress();
			return true;
		}
		return false;
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
		super.appendix = false;
		init(super.colName, sql);
	}

}
