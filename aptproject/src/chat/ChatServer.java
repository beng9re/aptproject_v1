/* 
 * 채팅서버는 메인윈도우 백그라운드에서 작동함
 * 여기서는 접속자만 받고 접속자가 생기면 관리자에게도 ChatClient를 띄우면서
 * 추가 thread를 가동한다
 */

package chat;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ConcurrentHashMap;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

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
	
	JLabel title;
	JPanel pnl_content;
	JScrollPane scroll;

	public ChatServer(TreeMain main) {
		this.main = main;
		userList = new ConcurrentHashMap<String, ServerSideChatClient>();
		pnlList = new ConcurrentHashMap<String, JPanel>();
		
		title = new JLabel("[대화 목록]", JLabel.CENTER);
		setLayout(new BorderLayout());
		pnl_content = new JPanel();
		scroll = new JScrollPane(pnl_content, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		
		title.setFont(new Font("맑은 고딕", Font.BOLD, 24));
		title.setBorder(BorderFactory.createLineBorder(Color.GRAY, 4));
		title.setPreferredSize(new Dimension(700,60));
		scroll.getVerticalScrollBar().setUnitIncrement(15);
		pnl_content.setBorder(BorderFactory.createEmptyBorder(20, 50, 20, 50));
		pnl_content.setLayout(new BoxLayout(pnl_content, BoxLayout.Y_AXIS));
		
		add(title, BorderLayout.NORTH);
		add(scroll, BorderLayout.CENTER);
		
		setSize(690, 670);
		
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
	
	public void addList(String user_id, ServerSideChatClient serverChat) {
		// 대화중인 주민목록을 보여주는 패널을 접속자가 생길때 마다 붙여넣는다
		ChatUserList list = new ChatUserList(this, user_id);
		JPanel listpan = new JPanel();
		listpan.add(list);
		listpan.add(Box.createVerticalStrut(5));
		pnl_content.add(listpan);
		
		//대화리스트 목록를 중복없이 관리한다
		for (String id : userList.keySet()) {
			if (id.equals(user_id)) {
				removeList(id);
			}
		}
		
		userList.put(user_id, serverChat);
		pnlList.put(user_id, listpan);
		updateUI();
	}
	
	public void removeList(String id) {
		// 서버측 채팅 클라이언트를 종료한다 >> 종료하지말고 목록에서 삭제 가능하게 한다
		pnl_content.remove(pnlList.get(id));
		userList.remove(id);
		pnlList.remove(id);
		updateUI();
	}
	
	public void close()	{
		// 접속자를 받는 thread를 닫는다
		flag = false;
	}

}
