package chat;

import java.awt.Color;
import java.awt.Dimension;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;

public class ChatMessage extends JPanel {
	ChatClient client;
	JPanel pnl_id, pnl_content;
	JLabel user_id;
	JTextArea content;

	public ChatMessage(ChatClient client, String id, String msg, boolean isEcho) {
		this.client = client;
		pnl_id = new JPanel();
		pnl_content = new JPanel();
		user_id = new JLabel(id);
		content = new JTextArea(msg);
		
		pnl_id.setLayout(new BoxLayout(pnl_id, BoxLayout.X_AXIS));
		pnl_content.setLayout(new BoxLayout(pnl_content, BoxLayout.X_AXIS));
		setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
		
		if (!isEcho) {
			pnl_id.add(user_id);
			pnl_id.add(Box.createHorizontalGlue());
			pnl_content.add(content);
			pnl_content.add(Box.createHorizontalGlue());
			
		} else if (isEcho) {
			pnl_id.add(Box.createHorizontalGlue());
			pnl_id.add(user_id);
			pnl_content.add(Box.createHorizontalGlue());
			pnl_content.add(content);
			content.setBackground(Color.YELLOW);
		}

		// 글자수에 따라 크기 다르게
		int height = (int) ((Math.ceil(msg.getBytes().length / 28) + 1) * 20);
		content.setPreferredSize(new Dimension(170, height));
		content.setMaximumSize(content.getPreferredSize());
		content.setEditable(false);
		content.setLineWrap(true);

		add(pnl_id);
		add(pnl_content);
	}

}
