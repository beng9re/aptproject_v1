package complex.regist.table;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.HeadlessException;
import java.awt.Label;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.WritableByteChannel;
import java.sql.Connection;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.LineBorder;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import db.DBManager;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

public class ShowTable extends JFrame implements ActionListener{
	Connection con;
	DBManager manager=DBManager.getInstance();
	JPanel p_north;
	JTable table;
	JScrollPane scroll;
	db_Table db_table;
	JButton bt_excel,bt_reset;
	JFileChooser chooser=new JFileChooser("C:/Users/jm/Desktop/�� ����(2)");
	FileOutputStream fos;
	
	
	
	public ShowTable(){
		con=manager.getConnection();
		table=new JTable(db_table=new db_Table(con));
		scroll=new JScrollPane(table);
		p_north=new JPanel();
		bt_excel=new JButton("excel�� ����");
		bt_reset=new JButton("�ʱ�ȭ");
		
		//�г� ������
		p_north.setLayout(null);
		p_north.setBackground(Color.orange);
		p_north.setPreferredSize(new Dimension(500, 70));
		
		//��ư ������
		
		bt_excel.setBounds(180, 20, 150, 30);
		bt_excel.setPreferredSize(new Dimension(100, 50));
		bt_excel.setBorder(new LineBorder(Color.black, 3));
		//bt_excel.setFont(new Font("����", Font.PLAIN, 20));
		bt_excel.setBackground(Color.pink);
		bt_excel.setFocusPainted(false);

		bt_reset.setBounds(350, 20, 100, 30);
		bt_reset.setPreferredSize(new Dimension(100, 50));
		bt_reset.setBorder(new LineBorder(Color.black, 3));
		//bt_excel.setFont(new Font("����", Font.PLAIN, 20));
		bt_reset.setBackground(Color.pink);
		bt_reset.setFocusPainted(false);
		
	
		//��ư�� �̺�Ʈ �ο�
		bt_excel.addActionListener(this);
		bt_reset.addActionListener(this);
		
		
		chooser.setFileFilter(new FileNameExtensionFilter("xls", "xls"));
		//chooser.addChoosableFileFilter(new FileNameExtensionFilter("xls", "xls"));
		
		p_north.add(bt_excel);
		p_north.add(bt_reset);
		
		
		
		add(p_north,BorderLayout.NORTH);
		add(scroll);
		
		setBounds(1000, 50, 500, 700);
		setVisible(true);
		
		
	}
	//������ �����ϴ� �޼���
	public void saveExcell(){
		HSSFWorkbook book=new HSSFWorkbook();
		HSSFSheet sheet=book.createSheet("��ȣ��");
		WritableWorkbook workbook=null;
		
		
		//WritableSheet sheet=workbook.createSheet("��ȣ��",5);
		//writble
		
		for (int i = 0; i < table.getRowCount(); i++) {
			HSSFRow row=sheet.createRow(i);
			
			for (int j = 0; j < table.getColumnCount(); j++) {
				HSSFCell cell=row.createCell(j);
				System.out.println(table.getValueAt(i, j));
				System.out.println("�̰� �÷�ī��Ʈ"+cell);
				cell.setCellValue((String)table.getValueAt(i, j));
			}
		}
		
		int result=chooser.showSaveDialog(this);
		if(result==JFileChooser.APPROVE_OPTION){
			File file=chooser.getSelectedFile();
			String path=file.getAbsolutePath();
			String [] xml=path.split("\\.");//Ȯ���� .�Ⱥ��̸� if������ ������
			
			try {
				fos=new FileOutputStream(file);	
				book.write(fos);
				JOptionPane.showMessageDialog(this, "�߸��� ���� ����Դϴ�");	
				
				if(xml[1].equals("xls")){					
					JOptionPane.showMessageDialog(this, "���忡 �����ϼ˽��ϴ�");
				}
					
				
			}catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				
			} catch (IOException e) {
				// TODO Auto-generated catch block		
			}finally{
				if(fos!=null){
					try {
						fos.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}	
		}
	
	}
	
	//�ʱ�ȭ ��Ű�� �޼���
	public void reset(){
		
	}
	
	
	
	@Override
	public void actionPerformed(ActionEvent e) {
		Object obj=e.getSource();
		if(obj==bt_excel){
			saveExcell();
		}else if(obj==bt_reset){
			reset();
		}
		
	}

	public static void main(String[] args) {
		new ShowTable();

	}



}
