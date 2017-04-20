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
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
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

public class ChatClient extends JFrame {
	JPanel pnl_south, pnl_chat;
	JTextArea txa;
	JScrollPane scroll;
	JTextField txf_input;
	JButton btn_send;
	boolean scrollFlag = true;
	
	Socket socket;
	ChatClientThread thread;
	String id = "admin";

	public ChatClient() {
		pnl_south = new JPanel();
		pnl_chat = new JPanel();
		txa = new JTextArea();
		scroll = new JScrollPane(pnl_chat,JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		txf_input = new JTextField();
		btn_send = new JButton("전송");
		
		pnl_south.add(txf_input);
		pnl_south.add(btn_send);
		
		add(scroll);
		add(pnl_south, BorderLayout.SOUTH);
		
		scroll.getVerticalScrollBar().setUnitIncrement(15);
		pnl_chat.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
		pnl_chat.setLayout(new BoxLayout(pnl_chat, BoxLayout.PAGE_AXIS));
//		pnl_chat.setMaximumSize(new Dimension(270, Integer.MAX_VALUE));
		txf_input.setPreferredSize(new Dimension(210, 30));
		btn_send.setBackground(Color.LIGHT_GRAY);
		
		//스크롤 항상 최하단 고정
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
				// 쓰레드 종료
				thread.flag = false;
				// 접속종료
				if (socket != null) {
					try {
						socket.close();
					} catch (IOException e1) {
						e1.printStackTrace();
					}
				}
				//채팅창 종료 (전체 프로세스가 꺼질 수 있으므로 다른방법 찾아볼것)
				System.exit(0);
			}
		});

		connect();

		setTitle("대화");
		setBounds(100, 100, 300, 400);
		setResizable(false);
		setVisible(true);
	}

	public void connect() {
		try {
			socket = new Socket("localhost", 7777);
			thread = new ChatClientThread(socket, this);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public void send() {
		thread.send(txf_input.getText());
		txf_input.setText("");
	}

	public static void main(String[] args) {
		new ChatClient();
	}

}
