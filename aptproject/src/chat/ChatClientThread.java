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
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

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

	private String parsing(String msg) {
		try {
			JSONObject msgObj = (JSONObject)parser.parse(msg);
			if (msgObj.get("requestType").toString().equals("chat")) {
				msg = msgObj.get("msg").toString();
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return msg;
	}
	
	private void listen() {
		try {
			client.scrollFlag = true;
			String msg = buffR.readLine();
			//�޾ƿ� ä���� �м��Ѵ�(JSON�Ľ�)
			msg = parsing(msg);
			
			//ä�� ������ Ŭ���̾�Ʈ�� �����ش�
			ChatMessage chatbox = new ChatMessage(client, msg);
			client.pnl_chat.add(chatbox);
			client.pnl_chat.add(Box.createHorizontalGlue());
			client.pnl_chat.revalidate();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void send(String msg) {
		try {
			StringBuffer sb = new StringBuffer();
			sb.append("{");
			sb.append("\"requestType\":\"chat\",");
			sb.append("\"user_id\":\""+client.id+"\",");
			sb.append("\"msg\":\""+msg+"\"");
			sb.append("}");
			
			buffW.write(sb + "\n");
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
	}

	@Override
	public void run() {
		while (flag) {
			listen();
		}
		disconnect();
	}
}
