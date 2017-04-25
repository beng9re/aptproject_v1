package chat;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;

import org.json.simple.JSONObject;

public class ChatServerThread extends Thread {
	Socket socket;
	boolean flag = true;
	
	ServerSideChatClient serverChat;
	boolean serverChatStart = true;

	BufferedReader buffR;
	BufferedWriter buffW;

	public ChatServerThread(Socket socket, ServerSideChatClient serverChat) {
		this.socket = socket;
		this.serverChat = serverChat;
		try {
			buffR = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			buffW = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
		} catch (IOException e) {
			e.printStackTrace();
		}
		start();
	}

	public void listen() {
		try {
			String msg = buffR.readLine();

			// �޼��� ������ �з��Ͽ� ó��
			JSONObject jsonObj = ChatProtocol.parsing(msg);
			String reqType = jsonObj.get("requestType").toString();
			
			if (reqType.equals("chat")) {
				String user_id = jsonObj.get("user_id").toString();
				String message = jsonObj.get("message").toString();
				send(user_id, message);
				serverChat.mkMsg(user_id, message, false);
				
			} else if (reqType.equals("disconnect")) {
				flag = false;
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void send(String user_id, String msg) {
		try {
			//ä�õ��� �����ڰ� â�� �ݾƹ����� ���� >> �����ʿ�
			//�޼����� �������� ������ Ŭ���̾�Ʈ�� �ʱ�ȭ�� �����Ѵ�
			if (serverChatStart) {
				serverChatStart = false;
				serverChat.setServerThread(this);
				serverChat.setTitle(user_id+"�� ���� ��ȭ");
				serverChat.setVisible(true);
			}
			
			// output��Ʈ������ �޼����� ������
			buffW.write(ChatProtocol.toJSON("chat", user_id, msg) + "\n");
			buffW.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void sendEcho(String user_id, String msg, boolean isEcho) {
		// ������ Ŭ���̾�Ʈ���� �޼����� ������
		serverChat.mkMsg(user_id, msg, true);
		// ��ȭ ���濡�Ե� �޼����� ������
		send(user_id, msg);
	}

	private void disconnect() {
		// ������ ä�� Ŭ���̾�Ʈ�� �����Ѵ�
		serverChat.dispose();
		
		if (buffR != null) {
			try {
				buffR.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		if (buffW != null) {
			try {
				buffW.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		if (socket != null) {
			try {
				socket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void run() {
		while (flag) {
			listen();
		}
		disconnect();
	}

}
