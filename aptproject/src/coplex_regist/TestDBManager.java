/*
 * n.2
 * 
 * manager�� �δ� ����
 * 
 * 1.������ �Ѱ��� �α�
 * ������ ���̽� ���������� �ߺ��ؼ� �������� �ʱ� ����
 * (db������ �ϴ� ������ Ŭ��������)
 * 
 * 2. �ν��Ͻ��� ������ �Ѱ��� �ֺ���
 * -���ø����̼� ������ �����Ǵ� connection ��ü�� �ϳ��� ���� �ϱ� ����
 * 
 * */

package coplex_regist;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class TestDBManager {
	static private TestDBManager instance;
	private String diriver = "oracle.jdbc.driver.OracleDriver";
	private String url = "jdbc:oracle:thin:@localhost:1521:XE";
	private String user = "apt";
	private String password ="aptapt";
	private Connection con;

	private TestDBManager() {
		// n.3������ ����
		/*
		 * 1.����̹� �ε� 2.���� 3.������ 4.�ݳ�,����
		 * 
		 */
		try {
			Class.forName(diriver);// Class�� �ϳ��� �ڷ���, ������ ���̳ʸ��� ���Ⱑ��, Ŭ������ ���� ����
									// Ŭ����
			con = DriverManager.getConnection(url, user, password);

		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	static public TestDBManager getInstance() {
		if (instance == null) {
			instance = new TestDBManager();

		}
		return instance;
	}

	public void setInstance(TestDBManager instance) {
		this.instance = instance;
	}

	public Connection getConnection() {
		return con;
	}

	public void disConnect(Connection con) {
		if (con != null) {
			try {
				con.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

}
