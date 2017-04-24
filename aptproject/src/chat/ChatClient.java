/* 
 * ä��â ����, Ư���� ����� ����
 * �ؽ�Ʈarea�� ���� ��ũ�� ���� ������ ������
 * �ڵ����� ��ũ�ѵǰ�, ����� ������ ĵ������ ��ȯ
 */

package chat;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.net.Socket;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import main.TreeMain;

public class ChatClient extends JFrame {
	JPanel pnl_south, pnl_chat;
	JTextArea txa;
	JScrollPane scroll;
	JTextField txf_input;
	JButton btn_send;
	boolean scrollFlag = true;

	Socket socket;
	ChatClientThread thread;
	TreeMain main;
	String id;

	// ������ �� �������� ȸ���� id�� �޾ƿ´�
	public ChatClient(TreeMain main) {
		this.main = main;
		this.id = main.getUserID();
		
		pnl_south = new JPanel();
		pnl_chat = new JPanel();
		txa = new JTextArea();
		scroll = new JScrollPane(pnl_chat, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
				JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		txf_input = new JTextField();
		btn_send = new JButton("����");

		pnl_south.add(txf_input);
		pnl_south.add(btn_send);

		add(scroll);
		add(pnl_south, BorderLayout.SOUTH);

		scroll.getVerticalScrollBar().setUnitIncrement(15);
		pnl_chat.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
		pnl_chat.setLayout(new BoxLayout(pnl_chat, BoxLayout.PAGE_AXIS));
		txf_input.setPreferredSize(new Dimension(210, 30));
		btn_send.setBackground(Color.LIGHT_GRAY);

		// ��ũ�� �׻� ���ϴ� ����
		scroll.getVerticalScrollBar().addAdjustmentListener(new AdjustmentListener() {
			public void adjustmentValueChanged(AdjustmentEvent e) {
				if (scrollFlag) {
					e.getAdjustable().setValue(e.getAdjustable().getMaximum());
					scrollFlag = false;
				}
			}
		});
		// �ؽ�Ʈ�ʵ忡�� ����Ű�� ���ۿ���
		txf_input.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ENTER) {
					send();
				}
			}
		});
		// ���۹�ư�� ���� ����
		btn_send.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				send();
			}
		});
		// ���Ͽ���, ������ ���Ḧ ���� �����츮���� ���
		this.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				// flag ������ ������ �����ϰ�, ���ÿ� ������ ������� ��Ʈ�����۵� �ݴ´�
				thread.flag = false;
				// ä��â ����
				main.removeMenuOpenList(ChatClient.this);
				ChatClient.this.dispose();
			}
		});

		connect();

		setTitle("��ȭ");
		setBounds(100, 100, 300, 400);
		setResizable(false);
		setVisible(true);
	}

	// �������� (������ �亯��, ȸ�� ���ǿ�)�� ���� ���ϻ������θ� �ٸ����ϰ�
	// �����ڸ� ���� ������ ChatClient�� �����Ѵ�(�޾ƿ� socket���� ������)
	public void connect() {
		try {
			// aptuser���̺� �ִ� ������ ip�� ���ͼ� �����Ѵ�
			socket = new Socket(main.getSeverIP(), 7777);
			thread = new ChatClientThread(socket, this);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void send() {
		thread.send("chat", txf_input.getText());
		txf_input.setText("");
	}

}
