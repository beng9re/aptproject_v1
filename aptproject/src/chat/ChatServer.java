/* 
 * 채팅서버는 메인윈도우 백그라운드에서 작동함
 * 여기서는 접속자만 받고 접속자가 생기면 관리자에게도 ChatClient를 띄우면서
 * 추가 thread를 가동한다
 */

package chat;

import java.awt.Color;
import java.awt.Dimension;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import javax.swing.JPanel;

import main.TreeMain;

public class ChatServer extends JPanel implements Runnable {
	private TreeMain main;
	private Thread thread;
	private ServerSocket server;
	private Socket socket;
	private int port = 7777;
	private boolean flag = true;

	public ChatServer(TreeMain main) {
		this.main = main;
		thread = new Thread();
		
		setBackground(Color.GRAY);
		setPreferredSize(new Dimension(700, 700));
		
		// 서버기능을 시작한다
		mkServer();
	}

	public void mkServer() {
		try {
			server = new ServerSocket(port);
			System.out.println("서버생성");
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
				System.out.println("접속자 확인");
				ServerSideChatClient serverChat = new ServerSideChatClient();
				ChatServerThread thread = new ChatServerThread(socket, serverChat);
				
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
}
