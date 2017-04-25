/* 
 * 채팅창 외형, 특별한 기능은 없다
 * 텍스트area에 글이 스크롤 범위 밖으로 나가면
 * 자동으로 스크롤되게, 방법이 없으면 캔버스로 전환
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
	
	// 연결을 받아오지 않으므로 윈도우 리스너로 종료기능만 추가
	@Override
	public void connect() {
		this.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				// 채팅창 종료
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
