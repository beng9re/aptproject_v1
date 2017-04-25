/* 
 * ä�ü����� ���������� ��׶��忡�� �۵���
 * ���⼭�� �����ڸ� �ް� �����ڰ� ����� �����ڿ��Ե� ChatClient�� ���鼭
 * �߰� thread�� �����Ѵ�
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
	
	public void addUser(String user_id, ServerSideChatClient serverChat) {
		// ��ȭ���� �ֹθ���� �����ִ� �г��� �����ڰ� ���涧 ���� �ٿ��ִ´�
		JPanel pnl = new JPanel();
		pnl.add(new JLabel(user_id+"�԰��� ��ȭ"));
		pnl.add(new JLabel("�̰��� �����ø� ��ȭ�� �ٽ� �����մϴ�"));
		pnl.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				// ä��â�� �ٽ� �����ش�
				userList.get(user_id).setVisible(true);
			}
		});
		pnl.setBackground(Color.GRAY);
		pnl.setPreferredSize(new Dimension(500,100));
		add(pnl);
		
		//��ȭ����Ʈ ��ϸ� �����Ѵ�
		userList.put(user_id, serverChat);
		pnlList.put(user_id, pnl);
	}
	
	public void close()	{
		// �����ڸ� �޴� thread�� �ݴ´�
		flag = false;
	}

}
