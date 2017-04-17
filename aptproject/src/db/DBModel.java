package db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;

public abstract class DBModel {
	protected Connection conn;
	protected PreparedStatement pstmt;
	protected ResultSet rs;
	
	protected String colName[];
	protected String sql;
	protected ArrayList arrList = new ArrayList();
	
	//���ε� �������� ������ �ϴ� �޼���
	protected abstract void setSQL() throws SQLException;
	
	//dto�� ���� �����͸� �޾ƿ��� �޼���
	protected abstract void setTable(ResultSet rs, int count_col) throws SQLException;

	protected void init(String[] colName, String sql) {
		this.colName = colName;
		this.sql = sql;
		setData();
	}
	
	//select �� insert, update, delete ���� //���� ������ �޼���
	public void setData() {
		String[] sqlArr = sql.split("\\s");
		if (sqlArr[0].equals("select")) {
			exeQuery();
		} else {
			exeUpdate();
		}
	}
	
	
	private void exeQuery() {
		try {
			setSQL();
			rs = pstmt.executeQuery();
			//�� ���̺� ���� setTable �޼��� ���� (��ӹ��� Ŭ��������)
			setTable(rs, colName.length);
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			disconnect();
		}
	}

	private void exeUpdate() {
		try {
			setSQL();	
			int res = pstmt.executeUpdate();
			if (res != 0) {
				System.out.println("�������༺��");
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			disconnect();
		}
	}
	
	//resultset�� preparedstatement ������ �ݴ� �޼���
	private void disconnect() {
		if (rs != null) {
			try {
				rs.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		if (pstmt != null) {
			try {
				pstmt.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	public String[] getColName() {
		return colName;
	}

	public ArrayList getData() {
		return arrList;
	}

}
