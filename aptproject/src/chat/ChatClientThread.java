/* 
 * ä��â�� ������ �޴� ����� ���� thread�� �����ϰ�
 * �������ݿ� ���� ���� �۽ſ��θ� Ȯ���Ͽ� ������ ����
 */
package chat;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;

import javax.swing.Box;

import org.json.simple.parser.JSONParser;

public class ChatClientThread extends Thread {
	Socket socket;
	ChatClient client;
	boolean flag = true;

	BufferedReader buffR;
	BufferedWriter buffW;
	JSONParser parser;

	public ChatClientThread(Socket socket, ChatClient client) {
		this.socket = socket;
		this.client = client;
		try {
			buffR = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			buffW = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
			parser = new JSONParser();
		} catch (IOException e) {
			e.printStackTrace();
		}
		start();
	}

	private void listen() {
		try {
			client.scrollFlag = true;
			String msg = buffR.readLine();

			ChatMessage chatbox = new ChatMessage(client, msg);
			client.pnl_chat.add(chatbox);
			client.pnl_chat.add(Box.createHorizontalGlue());
			client.pnl_chat.revalidate();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void send(String type, String msg) {
		try {
			msg = ChatProtocol.toJSON("chat", client.id, msg);
			buffW.write(msg + "\n");
			buffW.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void disconnect() {
		send("disconnect", "���� ������ ����");
		// ��������
		if (socket != null) {
			try {
				socket.close();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
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
	}

	@Override
	public void run() {
		while (flag) {
			listen();
		}
		disconnect();
	}
}
