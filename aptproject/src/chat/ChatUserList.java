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
		btn_close = new JButton("��ȭ��� ����");
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		setAlignmentX(Component.CENTER_ALIGNMENT);
		Font font = new Font("���� ���", Font.BOLD, 16);
		
		add(id =new JLabel(user_id+"�԰��� ��ȭ"));
		add(Box.createVerticalStrut(5));
		add(comment = new JLabel("Ŭ���Ͻø� ��ȭ����� �� �� �ֽ��ϴ�"));
		add(Box.createVerticalStrut(5));
		add(btn_close);
		
		btn_close.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int res =JOptionPane.showConfirmDialog(ChatUserList.this,
						"��ȭ����� �����Ͻðڽ��ϱ�?", "��ȭ��� ����", JOptionPane.YES_NO_OPTION);
				if (res == JOptionPane.OK_OPTION) {
					server.removeList(user_id);
				}
			}
		});
		addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				// ä��â�� �ٽ� �����ش�
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
