package chat;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class ChatServerThread extends Thread {
	Socket socket;
	boolean flag = true;
	
	BufferedReader buffR;
	BufferedWriter buffW;
	JSONParser parser;
	
	public ChatServerThread(Socket socket) {
		this.socket = socket;
		try {
			buffR = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			buffW = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
			parser = new JSONParser();
		} catch (IOException e) {
			e.printStackTrace();
		}
		start();
	}
	
	//메세지 종류를 분류하여 처리 (채팅, 쪽지알림, 목록갱신요청)
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
			String msg = buffR.readLine();
//			msg = parsing(msg);
			send(msg);
			System.out.println(msg);
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
	}
	
	@Override
	public void run() {
		while (flag) {
			listen();
		}
		disconnect();
	}
	
}
