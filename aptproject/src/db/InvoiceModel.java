package db;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

import dto.Invoice;

public class InvoiceModel extends DBModel {
	private String[] colName = { "invoice_id", "invoice_arrtime", "invoce_barcode", "invoice_takeflag",
			"invoice_taker", "invoice_taketime", "aptuser_id" };
	private String sql;
	
	public InvoiceModel(Connection conn) {
		this.conn = conn;
		sql = "select * from invoice where~~";
		init(colName, sql);
	}

	protected void setSQL() throws SQLException {
		pstmt = conn.prepareStatement(sql);
	}
	
	@Override
	protected void setTable(ResultSet rs) throws SQLException {
		arrList.clear();
		while (rs.next()) {
			Invoice inv = new Invoice();
			
			arrList.add(inv);
		}
	}
}
