package chat;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.Socket;

/* 
 * TreeMain�� ȭ�� ���ΰ�ħ�� ��û�Ѵ�
 * Ŭ���̾�Ʈ���� �߻��� ��û�� �α׷� ����Ѵ�
 * �ֹε鿡 ���� ��ǰ ��/���
 * ȸ������ �������
 */
public class RequestUpdate {
	Socket socket;
	String ip;
	BufferedWriter buffw;
	
	public RequestUpdate(String ip) {
		try {
			// ������ ip�� ���ͼ� �����Ѵ�
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
