/* 
 * aptuser���̺��� ���̵��
 * ��ȸ : selectData() 
 * �߰� : insertData()
 * ���� : updateData()
 * ���� : deleteData()
 * �� ����� ����Ѵ�
 */
package aptuser;

import java.sql.Connection;
import java.sql.SQLException;

import dto.Aptuser;

public class AptuserByIDModel extends AptuserModel {
	private String dfSQL = "select * from aptuser A, (select * from complex C inner join unit U on C.complex_id = U.complex_id)"
			+ " B where A.unit_id = B.unit_id and aptuser_id = ?";
	private String sql = dfSQL;
	private String id;
	private String mode = "select";
	
	public AptuserByIDModel(Connection conn, String id) {
		this.conn = conn;
		this.id = id;
		setQuery(sql, true);
	}
	
	protected void setSQL() throws SQLException {
		//���ε� ������ ���ǹ����� �з�
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
				pstmt.setString(6, id);
				break;
			case "delete":
				pstmt = conn.prepareStatement(sql);
				pstmt.setString(1, id);
				break;
		}
	}
	
	public void selectData() {
		mode = "select";
		sql = dfSQL;
		setQuery(sql, true);
	}
	
	public void insertData() {
		mode = "insert";
		sql = "insert into aptuser values(aptuser_id,aptuser_pw,aptuser_name,aptuser_phone,�ּ�ó��)";
		setQuery(sql, true);
	}
	
	public void updateData() {
		mode = "update";
		sql = "update aptuser set aptuser_pw=?,aptuser_code=?,aptuser_name=?,aptuser_phone=?,aptuser_live=? where aptuser_id=?";
		setQuery(sql, true);
	}
	
	public void deleteData() {
		mode = "delete";
		sql = "delete from aptuser where aptuser_id = ?";
		setQuery(sql, true);
	}
	
}
