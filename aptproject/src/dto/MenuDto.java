package dto;

import java.util.Date;

public class MenuDto {
	
	int menu_id;
	int menu_level;
	int menu_up_id;
	String menu_name;
	String menu_class_name;
	String menu_use_flag;
	String admin_role_flag;
	String user_role_flag;
	Date menu_regdate;
	
	public int getMenu_id() {
		return menu_id;
	}
	public void setMenu_id(int menu_id) {
		this.menu_id = menu_id;
	}
	public int getMenu_level() {
		return menu_level;
	}
	public void setMenu_level(int menu_level) {
		this.menu_level = menu_level;
	}
	public int getMenu_up_id() {
		return menu_up_id;
	}
	public void setMenu_up_id(int menu_up_id) {
		this.menu_up_id = menu_up_id;
	}
	public String getMenu_name() {
		return menu_name;
	}
	public void setMenu_name(String menu_name) {
		this.menu_name = menu_name;
	}
	public String getMenu_class_name() {
		return menu_class_name;
	}
	public void setMenu_class_name(String menu_class_name) {
		this.menu_class_name = menu_class_name;
	}
	public String getMenu_use_flag() {
		return menu_use_flag;
	}
	public void setMenu_use_flag(String menu_use_flag) {
		this.menu_use_flag = menu_use_flag;
	}
	public String getAdmin_role_flag() {
		return admin_role_flag;
	}
	public void setAdmin_role_flag(String admin_role_flag) {
		this.admin_role_flag = admin_role_flag;
	}
	public String getUser_role_flag() {
		return user_role_flag;
	}
	public void setUser_role_flag(String user_role_flag) {
		this.user_role_flag = user_role_flag;
	}
	public Date getMenu_regdate() {
		return menu_regdate;
	}
	public void setMenu_regdate(Date menu_regdate) {
		this.menu_regdate = menu_regdate;
	}
	
	

}
