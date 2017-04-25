/* 
 * ä�ü����� ���������� ��׶��忡�� �۵���
 * ���⼭�� �����ڸ� �ް� �����ڰ� ����� �����ڿ��Ե� ChatClient�� ���鼭
 * �߰� thread�� �����Ѵ�
 */

package chat;

import java.awt.Color;
import java.awt.Dimension;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import javax.swing.JFrame;

import main.TreeMain;

public class ChatServer extends JFrame implements Runnable {
	private TreeMain main;
	
	Thread thread;
	private ServerSocket server;
	private Socket socket;
	private int port = 7777;
	private boolean flag = true;

	public ChatServer(TreeMain main) {
		this.main = main;
		thread = new Thread();
		
		setBackground(Color.GRAY);
		setPreferredSize(new Dimension(700, 700));
		
		// ��������� �����Ѵ�
		mkServer();
	}

	public void mkServer() {
		try {
			server = new ServerSocket(port);
			System.out.println("��������");
			thread.start();

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
				ServerSideChatClient serverChat = new ServerSideChatClient();
				ChatServerThread thread = new ChatServerThread(socket, serverChat);
				
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
}
