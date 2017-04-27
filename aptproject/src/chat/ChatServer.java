/* 
 * ä�ü����� ���������� ��׶��忡�� �۵���
 * ���⼭�� �����ڸ� �ް� �����ڰ� ����� �����ڿ��Ե� ChatClient�� ���鼭
 * �߰� thread�� �����Ѵ�
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
		
		title = new JLabel("[��ȭ ���]", JLabel.CENTER);
		setLayout(new BorderLayout());
		pnl_content = new JPanel();
		scroll = new JScrollPane(pnl_content, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		
		title.setFont(new Font("���� ���", Font.BOLD, 24));
		title.setBorder(BorderFactory.createLineBorder(Color.GRAY, 4));
		title.setPreferredSize(new Dimension(700,60));
		scroll.getVerticalScrollBar().setUnitIncrement(15);
		pnl_content.setBorder(BorderFactory.createEmptyBorder(20, 50, 20, 50));
		pnl_content.setLayout(new BoxLayout(pnl_content, BoxLayout.Y_AXIS));
		
		add(title, BorderLayout.NORTH);
		add(scroll, BorderLayout.CENTER);
		
		setSize(690, 670);
		
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
		pnl_content.add(listpan);
		
		//��ȭ����Ʈ ��ϸ� �ߺ����� �����Ѵ�
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
