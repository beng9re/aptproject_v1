package chat;

import java.awt.Dimension;

import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.text.BadLocationException;
import javax.swing.text.Utilities;

public class ChatMessage extends JPanel {
	ChatClient client;
	JLabel user_id;
	JTextArea content;
	
	public ChatMessage(ChatClient client, String msg) {
		this.client = client;
		user_id = new JLabel(client.id);
		content = new JTextArea(msg);
		
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		user_id.setAlignmentX(JComponent.RIGHT_ALIGNMENT);
		content.setAlignmentX(JComponent.RIGHT_ALIGNMENT);
				
		//글자수에 따라 크기 다르게
		int height = (int)((Math.ceil(msg.getBytes().length/28)+1)*20);
		content.setPreferredSize(new Dimension(170, height));
		content.setMaximumSize(content.getPreferredSize());
		content.setEditable(false);
		content.setLineWrap(true);
		
		add(user_id);
		add(content);
	}
	
}
