/*
 * n.2
 * 
 * manager를 두는 이유
 * 
 * 1.정보를 한곳에 두기
 * 데이터 베이스 계정정보를 중복해서 기재하지 않기 위함
 * (db연동을 하는 각각의 클래스에서)
 * 
 * 2. 인스턴스의 갯수를 한개만 둬보기
 * -어플리케이션 가동중 생성되는 connection 객체를 하나로 통일 하기 위함
 * 
 * */

package db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBManager {
	static private DBManager instance;
	private String diriver = "oracle.jdbc.driver.OracleDriver";
	private String url = "jdbc:oracle:thin:@localhost:1521:XE";
	private String user = "apt";
	private String password ="aptapt";
	private Connection con;

	private DBManager() {
		// n.3생성자 막기
		/*
		 * 1.드라이버 로드 2.접속 3.쿼리문 4.반납,해제
		 * 
		 */
		try {
			Class.forName(diriver);// Class는 하나의 자료형, 남이준 바이너리명 추출가능, 클래스에 대한 정보
									// 클래스
			con = DriverManager.getConnection(url, user, password);

		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	static public DBManager getInstance() {
		if (instance == null) {
			instance = new DBManager();

		}
		return instance;
	}

	public void setInstance(DBManager instance) {
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
