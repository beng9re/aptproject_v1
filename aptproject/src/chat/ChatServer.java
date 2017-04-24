/* 
 * 채팅서버는 메인윈도우 백그라운드에서 작동함
 * 여기서는 접속자만 받고 접속자가 생기면 관리자에게도 ChatClient를 띄우면서
 * 추가 thread를 가동한다
 */

package chat;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ChatServer extends Thread {
	ServerSocket server;
	Socket socket;
	int port = 7777;
	boolean flag = true;

	public ChatServer() {
		try {
			server = new ServerSocket(port);
			System.out.println("서버생성");
			start();

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	@Override
	public void run() {
		while (flag) {
			try {
				socket = server.accept();
				System.out.println("접속자 확인");
				ChatServerThread thread = new ChatServerThread(socket);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public static void main(String[] args) {
		new ChatServer();
	}

}
