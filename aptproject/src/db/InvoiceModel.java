package db;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class InvoiceModel extends DBModel {
	private String colName[];
	private String sql;
	private String bDay, eDay;

	//
	public InvoiceModel(Connection conn) {
		this.conn = conn;
	}

	// 바인드 변수 날짜 입력은 YYYYMMDD 형식으로
	public void selectByDAY(String bDay, String eDay) {
		this.bDay = bDay;
		this.eDay = eDay;
		
		StringBuffer sb = new StringBuffer();
		sb.append("select to_char(invoice_arrtime,'YYYYMMDD') as 날짜, count(invoice_id) as 합계");
		sb.append("	from invoice where invoice_arrtime");
		sb.append("	between to_date(?,'YYYYMMDD') and to_date(?,'YYYYMMDD')");
		sb.append("	group by to_char(invoice_arrtime, 'YYYYMMDD')");
		sql = sb.toString();
		init(colName, sql);
	}

	public void selectProportion(String bDay, String eDay) {
		this.bDay = bDay;
		this.eDay = eDay;
		
		StringBuffer sb = new StringBuffer();
		sb.append("select to_char(invoice_arrtime,'YYYYMMDD') as 날짜, count(invoice_id) as 합계");
		sb.append("	from invoice where invoice_arrtime");
		sb.append("	between to_date(?,'YYYYMMDD') and to_date(?,'YYYYMMDD')");
		sb.append("	and invoice_taketime is not null");
		sb.append("	group by to_char(invoice_arrtime, 'YYYYMMDD')");
		sql = sb.toString();
		init(colName, sql);
	}

	protected void setSQL() throws SQLException {
		pstmt = conn.prepareStatement(sql);
		pstmt.setString(1, bDay);
		pstmt.setString(2, eDay);
	}

	@Override
	protected void setTable(ResultSet rs) throws SQLException {
		arrList.clear();
		while (rs.next()) {
			System.out.println(rs.getString(1) + rs.getInt(2));
			String[] dto = new String[2];
			dto[0] = rs.getString(1);
			dto[1] = Integer.toString(rs.getInt(2));
			arrList.add(dto);
		}
	}
	
}
