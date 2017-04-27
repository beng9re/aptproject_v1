package complex.regist.table;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.HeadlessException;
import java.awt.Label;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.WritableByteChannel;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.LineBorder;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.DataFormatter;

import db.DBManager;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

public class TableController extends JFrame implements ActionListener{
	Connection con;
	DBManager manager=DBManager.getInstance();
	JPanel p_north;
	public JTable table;
	JScrollPane scroll;
	db_Table db_table;
	JButton bt_excel,bt_reset,bt_bring;
	JFileChooser chooser=new JFileChooser("C:/Users/jm/Desktop/�� ����(2)");
	FileOutputStream fos;
	
	String dd;
	//������ ���̵� ��
	String value;
	
	
	public TableController(){
		con=manager.getConnection();
		table=new JTable();
		db_table=new db_Table(con);
		scroll=new JScrollPane(table);
		p_north=new JPanel();
		bt_excel=new JButton("excel�� ����");
		bt_reset=new JButton("����");
		//bt_bring=new JButton("excel�� ��������");
		
	
		table.setModel(db_table);
		
		//���̺� �����!!!!
		table.getColumn("id").setWidth(0);
		table.getColumn("id").setMinWidth(0);
		table.getColumn("id").setMaxWidth(0);
		table.setRowHeight(20);
		
		
		
		
		//���̺� ���� �̺�Ʈ
		  table.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				JTable t=(JTable)e.getSource();
				int row=t.getSelectedRow();
				int col=0;
				value=(String) t.getValueAt(row, col);
				
				
			}
		});
		
		
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
		
		/*
		bt_bring.setBounds(0, 20, 150, 30);
		bt_bring.setPreferredSize(new Dimension(100, 50));
		bt_bring.setBorder(new LineBorder(Color.black, 3));
		//bt_excel.setFont(new Font("����", Font.PLAIN, 20));
		bt_bring.setBackground(Color.pink);
		bt_bring.setFocusPainted(false);
		*/
	
		//��ư�� �̺�Ʈ �ο�
		bt_excel.addActionListener(this);
		bt_reset.addActionListener(this);
		//bt_bring.addActionListener(this);
		
		
		chooser.setFileFilter(new FileNameExtensionFilter("xls", "xls"));
		//chooser.addChoosableFileFilter(new FileNameExtensionFilter("xls", "xls"));
		
		p_north.add(bt_excel);
		p_north.add(bt_reset);
		//p_north.add(bt_bring);
		
		
		
		
		add(p_north,BorderLayout.NORTH);
		add(scroll);
		
		setBounds(1000, 50, 500, 700);
		setVisible(true);
		
		
	}
	
	/*
	//������ �ҷ��ͼ� db�� �����Ű�� �޼���
	public void loadExcell(){
		
			int result = chooser.showOpenDialog(this);
			if (result == JFileChooser.APPROVE_OPTION) {
				File file = chooser.getSelectedFile();
				FileInputStream fis = null;// try�� ������ �Ⱥ��� �� ����
				// 28_3
				StringBuffer cols = new StringBuffer();
				StringBuffer data = new StringBuffer();

				try {
					fis = new FileInputStream(file);

					HSSFWorkbook book = null;
					book = new HSSFWorkbook(fis);// stream�� ��� �Ծ��

					HSSFSheet sheet = null;
					sheet = book.getSheet("��ȣ��");

					int total = sheet.getLastRowNum();// sheet�� �մ� ���� ���� ���ϱ�
					int first = sheet.getFirstRowNum();
					
					HSSFRow firstRow = sheet.getRow(first);// row ������ ������

					cols.delete(0, cols.length());
					
					firstRow.getLastCellNum();// ù��° ���� ������ cell ��ȣ ������
					for (int i = 0; i < firstRow.getLastCellNum(); i++) {

						HSSFCell cell = firstRow.getCell(i);
						if (i < firstRow.getLastCellNum() - 1) {
							
							cols.append(cell.getStringCellValue());
	
						} else
							
							cols.append(cell.getStringCellValue());
					
					}

					DataFormatter df = new DataFormatter();// ������ �����͸� �д� �޼���
					
					for (int a = 1; a <= total; a++) {
						HSSFRow row = sheet.getRow(a);
						
						int columnCount = row.getLastCellNum();// �÷��� ���� �����´�
						

						data.delete(0, data.length());// data ��Ʈ������ ����� 28_4

						for (int i = 0; i < columnCount; i++) {
							HSSFCell cell = row.getCell(i);
							String value = df.formatCellValue(cell);
			
							data.append(value);
							
						}
			
					}
					JOptionPane.showMessageDialog(this, "�����̵���");
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
			

		
	}
	*/
	
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
				if(xml.length==1){
					file=new File(path+".xls");			
				}
				
				fos=new FileOutputStream(file);	
				book.write(fos);
				JOptionPane.showMessageDialog(this, "���忡 �����ϼ˽��ϴ�");
				
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
	public void delete(){
		int ans=JOptionPane.showConfirmDialog(this, "���� �����Ͻðڽ��ϱ�?");
		if(ans==JOptionPane.OK_OPTION){
		
		PreparedStatement pstmt=null;
		String sql="delete FROM unit WHERE UNIT_ID=?";
			
		try {
			pstmt=con.prepareStatement(sql);
			pstmt.setString(1, value);
			int result=pstmt.executeUpdate();
			if(result!=0){
				JOptionPane.showMessageDialog(this, "�����Ϸ�");
				//table.updateUI();//���̺��� ���� ���־����
				
				upDate();
				
			}
			
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		}
		
	}
	
	/*
	//�ʱ�ȭ ��Ű�� �޼���
	public void reset(){
		PreparedStatement pstmt=null;
		StringBuffer sql=new StringBuffer();	
		//���������
		int really=JOptionPane.showConfirmDialog(this, "�ʱ�ȭ�Ͻðڽ��ϱ�?");
		if(really==JOptionPane.OK_OPTION){
		sql.append("delete from unit");
		
		try {
			pstmt=con.prepareStatement(sql.toString());
			pstmt.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		//���÷��� �����
		sql.delete(0, sql.length());
		sql.append("delete from complex");
		System.out.println(sql.toString());
		
		try {
			pstmt=con.prepareStatement(sql.toString());
			pstmt.executeUpdate();
			//table.updateUI();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			if(pstmt!=null){
				try {
					pstmt.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		upDate();
		}
	}
	*/
	
	
	
	//���̺��� ������Ʈ�� ������Ű�� �޼���
	public void upDate(){
		
		db_table.getList();
		table.setModel(db_table);
		table.updateUI();
		
		
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		Object obj=e.getSource();
		if(obj==bt_excel){
			saveExcell();
		}else if(obj==bt_reset){
			delete();
		}else if(obj==bt_bring){
			//loadExcell();
		}
		
	}

	public static void main(String[] args) {
		new TableController();

	}

}
