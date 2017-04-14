package chat.client;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class ChatClient extends JFrame {
	JPanel pnl_south;
	JTextArea txa;
	JScrollPane scroll;
	JTextField txf_input;
	JButton btn_send;
	
	public ChatClient() {
		pnl_south = new JPanel();
		txa = new JTextArea();
		scroll = new JScrollPane(txa);
		txf_input = new JTextField();
		btn_send = new JButton("전송");
		
		//스크롤 상세설정 (글자 추가시 자동으로 내려오게)
		
		pnl_south.add(txf_input);
		pnl_south.add(btn_send);
		
		add(scroll);
		add(pnl_south, BorderLayout.SOUTH);
		
		txf_input.setPreferredSize(new Dimension(210, 30));
		btn_send.setBackground(Color.LIGHT_GRAY);
		
		//텍스트필드에서 엔터키와 전송연결
		txf_input.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode()==KeyEvent.VK_ENTER) {
					send();
				}
			}
		});
		//전송버튼과 전송 연결
		btn_send.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				send();
			}
		});
		//소켓연결, 쓰레드 종료를 위해 윈도우리스너 사용
		this.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				//접속종료				
				//쓰레드 종료
				System.exit(0);
			}
		});
		
		setTitle("대화");
		setBounds(100,100, 300, 400);
		setResizable(false);
		setVisible(true);
	}
	
	public void send() {
		System.out.println("전송");
		txf_input.setText("");
	}
	
	public static void main(String[] args) {
		new ChatClient();
	}

}
