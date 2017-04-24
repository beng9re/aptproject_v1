/* 
 * 채팅창 외형, 특별한 기능은 없다
 * 텍스트area에 글이 스크롤 범위 밖으로 나가면
 * 자동으로 스크롤되게, 방법이 없으면 캔버스로 전환
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

	// 생성할 때 접속중인 회원의 id를 받아온다
	public ChatClient(TreeMain main) {
		this.main = main;
		this.id = main.getUserID();
		
		pnl_south = new JPanel();
		pnl_chat = new JPanel();
		txa = new JTextArea();
		scroll = new JScrollPane(pnl_chat, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
				JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		txf_input = new JTextField();
		btn_send = new JButton("전송");

		pnl_south.add(txf_input);
		pnl_south.add(btn_send);

		add(scroll);
		add(pnl_south, BorderLayout.SOUTH);

		scroll.getVerticalScrollBar().setUnitIncrement(15);
		pnl_chat.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
		pnl_chat.setLayout(new BoxLayout(pnl_chat, BoxLayout.PAGE_AXIS));
		txf_input.setPreferredSize(new Dimension(210, 30));
		btn_send.setBackground(Color.LIGHT_GRAY);

		// 스크롤 항상 최하단 고정
		scroll.getVerticalScrollBar().addAdjustmentListener(new AdjustmentListener() {
			public void adjustmentValueChanged(AdjustmentEvent e) {
				if (scrollFlag) {
					e.getAdjustable().setValue(e.getAdjustable().getMaximum());
					scrollFlag = false;
				}
			}
		});
		// 텍스트필드에서 엔터키와 전송연결
		txf_input.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ENTER) {
					send();
				}
			}
		});
		// 전송버튼과 전송 연결
		btn_send.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				send();
			}
		});
		// 소켓연결, 쓰레드 종료를 위해 윈도우리스너 사용
		this.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				// flag 값으로 쓰레드 종료하고, 동시에 서버의 쓰레드와 스트림버퍼도 닫는다
				thread.flag = false;
				// 채팅창 종료
				main.removeMenuOpenList(ChatClient.this);
				ChatClient.this.dispose();
			}
		});

		connect();

		setTitle("대화");
		setBounds(100, 100, 300, 400);
		setResizable(false);
		setVisible(true);
	}

	// 접속유형 (관리자 답변용, 회원 문의용)에 따라 소켓생성여부를 다르게하고
	// 관리자를 위한 별도의 ChatClient를 생성한다(받아온 socket으로 생성함)
	public void connect() {
		try {
			// aptuser테이블에 있는 관리자 ip를 얻어와서 접속한다
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
