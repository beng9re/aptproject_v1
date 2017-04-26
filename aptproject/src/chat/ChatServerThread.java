package chat;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;

import javax.swing.JOptionPane;

import org.json.simple.JSONObject;

public class ChatServerThread extends Thread {
	ChatServer server;
	Socket socket;
	ServerSideChatClient serverChat;
	boolean serverChatStart = true;

	boolean flag = true;
	String req_id;

	BufferedReader buffR;
	BufferedWriter buffW;

	public ChatServerThread(ChatServer server, Socket socket, ServerSideChatClient serverChat) {
		this.server = server;
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
			String user_id = jsonObj.get("user_id").toString();
			String message = jsonObj.get("message").toString();

			if (reqType.equals("chat")) {
				// �ٽ� �������� �޼����� �����Ѵ�
				send(user_id, message);
				
				// ������ ä��Ŭ���̾�Ʈ���� �����͸� ������
				activateServerChat(user_id);
				serverChat.mkMsg(user_id, message, false);

			} else if (reqType.equals("disconnect")) {
				req_id = user_id;
				disconnect();
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void activateServerChat(String user_id) {
		// �޼����� �������� ������ Ŭ���̾�Ʈ�� �ʱ�ȭ�� �����Ѵ�
		if (serverChatStart) {
			serverChatStart = false;
			serverChat.setServerThread(this);
			serverChat.setTitle(user_id + "�� ���� ��ȭ");
			// ������ ������� �����ϴ� �迭�� ������ �߰��Ѵ�
			server.addList(user_id, serverChat);
		}
		// â�� �ݾ��� ���� �ٽ� ���̰� �Ѵ�
		serverChat.setVisible(true);
	}

	public void send(String user_id, String msg) {
		try {
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
		flag = false;

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
		
		serverChat.mkMsg(req_id, req_id+"���� ��ȭâ���� �������ϴ�", false);
		serverChat.txf_input.setEnabled(false);
		serverChat.btn_send.setEnabled(false);
	}

	@Override
	public void run() {
		while (flag) {
			listen();
		}
	}

}
