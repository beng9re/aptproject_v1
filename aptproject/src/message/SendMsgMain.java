package message;

import java.awt.Choice;

import javax.swing.JButton;
import javax.swing.JTextArea;

import extend.InputFrame;
import extend.OutputFrame;

public class SendMsgMain extends InputFrame{
	
	JTextArea  area;
	Choice  ch_complex;
	JButton bt_send;
	
	public SendMsgMain() {
		area = new JTextArea();
		ch_complex = new Choice();
		bt_send = new JButton("º¸³»±â");
		
		p_center.add(area);
		p_center.add(ch_complex);
		p_center.add(bt_send);
	}


	public void reset() {
		
	}

	public void regist() {
		
	}

	public static void main(String[] args) {
		new SendMsgMain();
	}

}
