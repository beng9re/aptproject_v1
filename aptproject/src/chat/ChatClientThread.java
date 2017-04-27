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

import org.json.simple.JSONObject;

public class ChatClientThread extends Thread {
	Socket socket;
	ChatClient client;
	boolean flag = true;

	BufferedReader buffR;
	BufferedWriter buffW;

	public ChatClientThread(Socket socket, ChatClient client) {
		this.socket = socket;
		this.client = client;
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
			client.scrollFlag = true;
			String msg = buffR.readLine();
			
			if (msg != null) {
				// ���� �޽����� �Ľ��Ͽ� �ڽ��� �޽����� Ÿ���� �޽����� �����Ͽ� ���
				JSONObject jsonObj = ChatProtocol.parsing(msg);
				msg = jsonObj.get("message").toString();
				String user_id = jsonObj.get("user_id").toString();
				ChatMessage chatbox = null;
				
				if (user_id.equals(client.id)) {
					chatbox = new ChatMessage(client, user_id, msg, true);
				} else {
					chatbox = new ChatMessage(client, user_id, msg, false);
				}
				client.pnl_chat.add(chatbox);
				client.pnl_chat.revalidate();
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void send(String type, String msg) {
		try {
			msg = ChatProtocol.toJSON(type, client.id, msg);
			buffW.write(msg + "\n");
			buffW.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void disconnect() {
		flag = false;
		
		// ��������
		send("disconnect", "exit");
		System.out.println("����ä������");
		
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
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
	}

	@Override
	public void run() {
		while (flag) {
			listen();
		}
	}
}
