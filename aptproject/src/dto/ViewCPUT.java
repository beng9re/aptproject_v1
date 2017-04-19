package dto;

public class ViewCPUT {
	public int getComplex_id() {
		return complex_id;
	}
	public void setComplex_id(int complex_id) {
		this.complex_id = complex_id;
	}
	public String getComplex_name() {
		return complex_name;
	}
	public void setComplex_name(String complex_name) {
		this.complex_name = complex_name;
	}
	public int getUnit_id() {
		return unit_id;
	}
	public void setUnit_id(int unit_id) {
		this.unit_id = unit_id;
	}
	public String getUnit_name() {
		return unit_name;
	}
	public void setUnit_name(String unit_name) {
		this.unit_name = unit_name;
	}
	private int complex_id;
	private String complex_name;
	private int unit_id;
	private String unit_name;
	
}
