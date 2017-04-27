package chat;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.Socket;

/* 
 * TreeMain에 화면 새로고침을 요청한다
 * 클라이언트에서 발생한 요청를 로그로 기록한다
 * 주민들에 의한 물품 입/출고
 * 회원정보 변경사항
 */
public class RequestUpdate {
	Socket socket;
	String ip;
	BufferedWriter buffw;
	
	public RequestUpdate(String ip) {
		try {
			// 관리자 ip를 얻어와서 접속한다
			socket = new Socket(ip, 7777);
			buffw = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void sendMsg(String user_id, String msg) {
		String message = ChatProtocol.toJSON("update", user_id, msg);
		try {
			buffw.write(message + "\n");
			buffw.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
