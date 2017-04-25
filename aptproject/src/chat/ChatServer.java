/* 
 * 채팅서버는 메인윈도우 백그라운드에서 작동함
 * 여기서는 접속자만 받고 접속자가 생기면 관리자에게도 ChatClient를 띄우면서
 * 추가 thread를 가동한다
 */

package chat;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ConcurrentHashMap;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;

import main.TreeMain;

public class ChatServer extends JPanel {
	TreeMain main;
	Thread mainThread;
	ServerSocket server;
	Socket socket;
	int port = 7777;
	boolean flag = true;
	ConcurrentHashMap<String, ServerSideChatClient> userList;
	ConcurrentHashMap<String, JPanel> pnlList;

	public ChatServer(TreeMain main) {
		this.main = main;
		userList = new ConcurrentHashMap<String, ServerSideChatClient>();
		pnlList = new ConcurrentHashMap<String, JPanel>();
		
		setLayout(new FlowLayout(FlowLayout.CENTER));
		setBorder(BorderFactory.createEmptyBorder(50, 50, 50, 50));
		setBackground(Color.CYAN);
		setPreferredSize(new Dimension(700, 700));
		
		// 서버기능을 시작한다
		mkServer();
	}

	public void mkServer() {
		try {
			server = new ServerSocket(port);
			System.out.println("서버생성");
			
			// thread가 두 개 존재하여 인터페이스로 구현하면 작동안하므로 내부익명으로
			mainThread = new Thread() {
				@Override
				public void run() {
					while (flag) {
						try {
							System.out.println("접속대기");
							socket = server.accept();
							System.out.println("접속자 확인");
							// 관리자가 보는 채팅 클라이언트를 생성한다
							ServerSideChatClient serverChat = new ServerSideChatClient();
							// 접속자를 위한 thread를 생성한다
							ChatServerThread thread = new ChatServerThread(ChatServer.this, socket, serverChat);
							
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				}
			};
			mainThread.start();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void addUser(String user_id, ServerSideChatClient serverChat) {
		// 대화중인 주민목록을 보여주는 패널을 접속자가 생길때 마다 붙여넣는다
		JPanel pnl = new JPanel();
		pnl.add(new JLabel(user_id+"님과의 대화"));
		pnl.add(new JLabel("이곳을 누르시면 대화를 다시 시작합니다"));
		pnl.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				// 채팅창을 다시 보여준다
				userList.get(user_id).setVisible(true);
			}
		});
		pnl.setBackground(Color.GRAY);
		pnl.setPreferredSize(new Dimension(500,100));
		add(pnl);
		
		//대화리스트 목록를 관리한다
		userList.put(user_id, serverChat);
		pnlList.put(user_id, pnl);
	}
	
	public void close()	{
		// 접속자를 받는 thread를 닫는다
		flag = false;
	}

}
