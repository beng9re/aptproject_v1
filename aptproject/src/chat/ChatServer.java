/* 
 * ä�ü����� ���������� ��׶��忡�� �۵���
 * ���⼭�� �����ڸ� �ް� �����ڰ� ����� �����ڿ��Ե� ChatClient�� ���鼭
 * �߰� thread�� �����Ѵ�
 */

package chat;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import main.TreeMain;

public class ChatServer extends Thread {
	private TreeMain main;
	
	private ServerSocket server;
	private Socket socket;
	private int port = 7777;
	private boolean flag = true;

	public ChatServer(TreeMain main) {
		this.main = main;
		
		try {
			server = new ServerSocket(port);
			System.out.println("��������");
			start();

		} catch (IOException e) {
			e.printStackTrace();
		}

	}
	
	public void close()	{
		flag = false;
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

}
