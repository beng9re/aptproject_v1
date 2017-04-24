package chat;

import java.awt.Color;
import java.awt.Dimension;

import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;

public class ChatMessage extends JPanel {
	ChatClient client;
	JLabel user_id;
	JTextArea content;
	float alignf;

	public ChatMessage(ChatClient client, String msg, boolean isEcho) {
		this.client = client;
		user_id = new JLabel(client.id);
		content = new JTextArea(msg);
		
		if (!isEcho) {
			alignf = JComponent.LEFT_ALIGNMENT;
		} else if (isEcho) {
			alignf = JComponent.RIGHT_ALIGNMENT;
			content.setBackground(Color.YELLOW);
		}

		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		user_id.setAlignmentX(alignf);
		content.setAlignmentX(alignf);

		// 글자수에 따라 크기 다르게
		int height = (int) ((Math.ceil(msg.getBytes().length / 28) + 1) * 20);
		content.setPreferredSize(new Dimension(170, height));
		content.setMaximumSize(content.getPreferredSize());
		content.setEditable(false);
		content.setLineWrap(true);

		add(user_id);
		add(content);
	}

}
