/* 
 * ä��â ����, Ư���� ����� ����
 * �ؽ�Ʈarea�� ���� ��ũ�� ���� ������ ������
 * �ڵ����� ��ũ�ѵǰ�, ����� ������ ĵ������ ��ȯ
 */

package chat;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;

import javax.swing.Box;

import org.json.simple.JSONObject;

public class ServerSideChatClient extends ChatClient {
	String id;
	ChatServerThread serverThread;
	
	public ServerSideChatClient() {
		init();
	}
	
	public void initFrame() {
		setBounds(100, 100, 300, 500);
		setResizable(false);
		setVisible(false);
	}
	
	// ������ �޾ƿ��� �����Ƿ� ������ �����ʷ� �����ɸ� �߰�
	@Override
	public void connect() {
		this.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				// ä��â ����
				ServerSideChatClient.this.dispose();
			}
		});
	}
	
	@Override
	public void send() {
		String msg = txf_input.getText();
		serverThread.sendEcho("admin", msg, true);
		txf_input.setText("");
	}
	
	public void mkMsg(String user_id, String msg, boolean isEcho) {
		scrollFlag = true;
		ChatMessage chatbox = new ChatMessage(this, user_id, msg, isEcho);
		pnl_chat.add(chatbox);
		pnl_chat.add(Box.createHorizontalGlue());
		pnl_chat.revalidate();
	}
	
	public void setServerThread(ChatServerThread serverThread) {
		this.serverThread = serverThread;
	}
}
