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

	BufferedReader buffR;
	BufferedWriter buffW;

	public ChatServerThread(Socket socket) {
		this.socket = socket;
		try {
			buffR = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			buffW = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
		} catch (IOException e) {
			e.printStackTrace();
		}
		start();
	}

	private void listen() {
		try {
			String msg = buffR.readLine();

			// 메세지 종류를 분류하여 처리 (채팅, 접속종료 요청, 쪽지알림, 목록갱신요청)
			JSONObject jsonObj = ChatProtocol.parsing(msg);
			String reqType = jsonObj.get("requestType").toString();
			if (reqType.equals("chat")) {
				msg = jsonObj.get("message").toString();
				send(msg);
			} else if (reqType.equals("disconnect")) {
				flag = false;
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void send(String msg) {
		try {
			buffW.write(msg + "\n");
			buffW.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void disconnect() {
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
