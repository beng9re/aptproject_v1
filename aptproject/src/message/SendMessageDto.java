package message;

import java.sql.Date;

public class SendMessageDto {

	int msg_send_id;
	String msg_send_content;
	Date msg_sendtime;
	public int getMsg_send_id() {
		return msg_send_id;
	}
	public void setMsg_send_id(int msg_send_id) {
		this.msg_send_id = msg_send_id;
	}
	public String getMsg_send_content() {
		return msg_send_content;
	}
	public void setMsg_send_content(String msg_send_content) {
		this.msg_send_content = msg_send_content;
	}
	public Date getMsg_sendtime() {
		return msg_sendtime;
	}
	public void setMsg_sendtime(Date msg_sendtime) {
		this.msg_sendtime = msg_sendtime;
	}
	
	
}
