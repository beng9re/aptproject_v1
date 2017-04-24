/* 
 * ä�ü����� ���������� ��׶��忡�� �۵���
 * ���⼭�� �����ڸ� �ް� �����ڰ� ����� �����ڿ��Ե� ChatClient�� ���鼭
 * �߰� thread�� �����Ѵ�
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
			System.out.println("��������");
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
				System.out.println("������ Ȯ��");
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
