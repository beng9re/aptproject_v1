package chat;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class ChatUserList extends JPanel {
	ChatServer server;
	String user_id;
	JLabel id, comment;
	JButton btn_close;
	
	public ChatUserList(ChatServer server, String user_id) {
		this.server = server;
		this.user_id = user_id;
		btn_close = new JButton("대화기록 삭제");
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		setAlignmentX(Component.CENTER_ALIGNMENT);
		Font font = new Font("맑은 고딕", Font.BOLD, 16);
		
		add(id =new JLabel(user_id+"님과의 대화"));
		add(Box.createVerticalStrut(5));
		add(comment = new JLabel("클릭하시면 대화기록을 볼 수 있습니다"));
		add(Box.createVerticalStrut(5));
		add(btn_close);
		
		btn_close.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int res =JOptionPane.showConfirmDialog(ChatUserList.this,
						"대화기록을 삭제하시겠습니까?", "대화기록 삭제", JOptionPane.YES_NO_OPTION);
				if (res == JOptionPane.OK_OPTION) {
					server.removeList(user_id);
				}
			}
		});
		addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				// 채팅창을 다시 보여준다
				server.userList.get(user_id).setVisible(true);
			}
		});
		
		id.setFont(font);
		comment.setFont(font);
		btn_close.setFont(font);
		btn_close.setBackground(Color.WHITE);
		setBorder(BorderFactory.createEmptyBorder(5,50,5,50));
		setBackground(Color.PINK);
		setPreferredSize(new Dimension(500,100));
	}
}
