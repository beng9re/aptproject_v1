/* 
 * ä�ü����� ���������� ��׶��忡�� �۵���
 * ���⼭�� �����ڸ� �ް� �����ڰ� ����� �����ڿ��Ե� ChatClient�� ���鼭
 * �߰� thread�� �����Ѵ�
 */

package chat;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ConcurrentHashMap;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
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
	
	JPanel pnl_content;
	JScrollPane scroll;

	public ChatServer(TreeMain main) {
		this.main = main;
		userList = new ConcurrentHashMap<String, ServerSideChatClient>();
		pnlList = new ConcurrentHashMap<String, JPanel>();
		
		setLayout(new BorderLayout());
		pnl_content = new JPanel();
		scroll = new JScrollPane(pnl_content, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		pnl_content.setLayout(new BoxLayout(pnl_content, BoxLayout.PAGE_AXIS));
		
		add(scroll, BorderLayout.CENTER);
		
		scroll.getVerticalScrollBar().setUnitIncrement(15);
		pnl_content.setBorder(BorderFactory.createEmptyBorder(20, 50, 20, 50));
		setSize(680, 655);
		
		// ��������� �����Ѵ�
		mkServer();
	}

	public void mkServer() {
		try {
			server = new ServerSocket(port);
			System.out.println("��������");
			
			// thread�� �� �� �����Ͽ� �������̽��� �����ϸ� �۵����ϹǷ� �����͸�����
			mainThread = new Thread() {
				@Override
				public void run() {
					while (flag) {
						try {
							System.out.println("���Ӵ��");
							socket = server.accept();
							System.out.println("������ Ȯ��");
							// �����ڰ� ���� ä�� Ŭ���̾�Ʈ�� �����Ѵ�
							ServerSideChatClient serverChat = new ServerSideChatClient();
							// �����ڸ� ���� thread�� �����Ѵ�
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
		// ��ȭ���� �ֹθ���� �����ִ� �г��� �����ڰ� ���涧 ���� �ٿ��ִ´�
		ChatUserList list = new ChatUserList(this, user_id);
		JPanel listpan = new JPanel();
		listpan.add(list);
		listpan.add(Box.createVerticalStrut(5));
		listpan.add(Box.createVerticalGlue());
		pnl_content.add(listpan);
		
		//��ȭ����Ʈ ��ϸ� �����Ѵ�
		userList.put(user_id, serverChat);
		pnlList.put(user_id, listpan);
		updateUI();
	}
	
	public void removeList(String id) {
		// ������ ä�� Ŭ���̾�Ʈ�� �����Ѵ� >> ������������ ��Ͽ��� ���� �����ϰ� �Ѵ�
		pnl_content.remove(pnlList.get(id));
		userList.remove(id);
		pnlList.remove(id);
		updateUI();
	}
	
	public void close()	{
		// �����ڸ� �޴� thread�� �ݴ´�
		flag = false;
	}

}
