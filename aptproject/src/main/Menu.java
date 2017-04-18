package main;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;

public class Menu extends JPanel implements TreeSelectionListener {
	Main main;
	JTree tree;
	DefaultMutableTreeNode root;
	JPanel pnl_bot;
	JLabel lb_id, lb_txt;
	JButton btn_exit;
	
	//�޴��� �̸� ������
	String[] menu = {"�ù�", "ȸ��", "���", "ä��", "����", "ȸ����������"};
	
	//�����޴��� �����޴��� ���� �迭
	DefaultMutableTreeNode[] menuArr = new DefaultMutableTreeNode[menu.length];
	ArrayList<DefaultMutableTreeNode> subMenuArr = new ArrayList<DefaultMutableTreeNode>();
	
	public Menu(Main main) {
		this.main = main;
		setTree();
		tree = new JTree(root);
		pnl_bot = new JPanel();
		lb_txt = new JLabel("OO���� �湮�� ȯ���մϴ�");
		btn_exit = new JButton("����");
		
		pnl_bot.setLayout(new FlowLayout(FlowLayout.CENTER));
		setLayout(new BorderLayout());
		tree.expandRow(1);
		
		pnl_bot.add(lb_txt);
		pnl_bot.add(btn_exit);
		
		add(tree);
		add(pnl_bot, BorderLayout.SOUTH);
		
		pnl_bot.setPreferredSize(new Dimension(200, 150));
		
		tree.addTreeSelectionListener(this);
		btn_exit.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				main.exit();
			}
		});
		
		setPreferredSize(new Dimension(200, 700));
	}
	
	public void setTree() {
		root = new DefaultMutableTreeNode("�޴�");
		for (int i=0; i<menu.length; i++) {
			DefaultMutableTreeNode subTree = new DefaultMutableTreeNode(menu[i]);
			root.add(subTree);
			menuArr[i] = subTree;
		}
		DefaultMutableTreeNode menu1_1 = new DefaultMutableTreeNode("��ǰ���");
		DefaultMutableTreeNode menu1_2 = new DefaultMutableTreeNode("��ǰ���");
		menuArr[0].add(menu1_1);
		menuArr[0].add(menu1_2);
		
	}

	@Override
	public void valueChanged(TreeSelectionEvent e) {
		if (e.getPath().getLastPathComponent()==menuArr[0]) {
			System.out.println("����");
			main.list();
		}
	}
}
