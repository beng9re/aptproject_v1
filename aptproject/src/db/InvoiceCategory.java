package db;

public class InvoiceCategory {
	private String invoice_id;
	private String invoice_arrtime;
	private String invoice_code;
	private String invoice_takeflag;
	private String invoice_taker;
	private String invoice_taketime;
	
	public String getInvoice_id() {
		return invoice_id;
	}
	public void setInvoice_id(String invoice_id) {
		this.invoice_id = invoice_id;
	}
	public String getInvoice_arrtime() {
		return invoice_arrtime;
	}
	public void setInvoice_arrtime(String invoice_arrtime) {
		this.invoice_arrtime = invoice_arrtime;
	}
	public String getInvoice_code() {
		return invoice_code;
	}
	public void setInvoice_code(String invoice_code) {
		this.invoice_code = invoice_code;
	}
	public String getInvoice_takeflag() {
		return invoice_takeflag;
	}
	public void setInvoice_takeflag(String invoice_takeflag) {
		this.invoice_takeflag = invoice_takeflag;
	}
	public String getInvoice_taker() {
		return invoice_taker;
	}
	public void setInvoice_taker(String invoice_taker) {
		this.invoice_taker = invoice_taker;
	}
	public String getInvoice_taketime() {
		return invoice_taketime;
	}
	public void setInvoice_taketime(String invoice_taketime) {
		this.invoice_taketime = invoice_taketime;
	}
}
