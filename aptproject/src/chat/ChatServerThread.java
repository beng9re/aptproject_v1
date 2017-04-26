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

			// 메세지 종류를 분류하여 처리
			JSONObject jsonObj = ChatProtocol.parsing(msg);
			String reqType = jsonObj.get("requestType").toString();
			String user_id = jsonObj.get("user_id").toString();
			String message = jsonObj.get("message").toString();

			if (reqType.equals("chat")) {
				// 다시 유저에게 메세지를 전송한다
				send(user_id, message);
				
				// 서버측 채팅클라이언트에게 데이터를 보낸다
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
		// 메세지를 날리기전 서버측 클라이언트의 초기화를 실행한다
		if (serverChatStart) {
			serverChatStart = false;
			serverChat.setServerThread(this);
			serverChat.setTitle(user_id + "님 과의 대화");
			// 접속한 사람들을 관리하는 배열에 유저를 추가한다
			server.addList(user_id, serverChat);
		}
		// 창을 닫았을 때라도 다시 보이게 한다
		serverChat.setVisible(true);
	}

	public void send(String user_id, String msg) {
		try {
			// output스트림으로 메세지를 보낸다
			buffW.write(ChatProtocol.toJSON("chat", user_id, msg) + "\n");
			buffW.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void sendEcho(String user_id, String msg, boolean isEcho) {
		// 서버측 클라이언트에도 메세지를 보낸다
		serverChat.mkMsg(user_id, msg, true);
		// 대화 상대방에게도 메세지를 보낸다
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
		
		serverChat.mkMsg(req_id, req_id+"님이 대화창에서 나갔습니다", false);
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
