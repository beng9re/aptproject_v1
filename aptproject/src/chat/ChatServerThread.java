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

			// 메세지 종류를 분류하여 처리
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
			//채팅도중 관리자가 창을 닫아버리면 끝남 >> 수정필요
			//메세지를 날리기전 서버측 클라이언트의 초기화를 실행한다
			if (serverChatStart) {
				serverChatStart = false;
				serverChat.setServerThread(this);
				serverChat.setTitle(user_id+"님 과의 대화");
				serverChat.setVisible(true);
			}
			
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
		// 서버측 채팅 클라이언트를 종료한다
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
