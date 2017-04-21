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
	JButton bt_excel,bt_reset;
	JFileChooser chooser=new JFileChooser("C:/Users/jm/Desktop/새 폴더(2)");
	FileOutputStream fos;
	
	String dd;
	String sql;//테이블 업데이트를 하기 위한 변수
	
	public TableController(){
		con=manager.getConnection();
		table=new JTable();
		db_table=new db_Table(con);
		scroll=new JScrollPane(table);
		p_north=new JPanel();
		bt_excel=new JButton("excel로 저장");
		bt_reset=new JButton("초기화");
		
		
		table.setModel(db_table);
		
		/*테이블 변경 이벤트
		 * table.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				JTable t=(JTable)e.getSource();
				int row=t.getSelectedRow();
				int col=t.getSelectedColumn();
				sql=(String) t.getValueAt(row, col);
				System.out.println(sql);
				
			}
		});
		*/
		
		//패널 디자인
		p_north.setLayout(null);
		p_north.setBackground(Color.orange);
		p_north.setPreferredSize(new Dimension(500, 70));
		
		//버튼 디자인
		
		bt_excel.setBounds(180, 20, 150, 30);
		bt_excel.setPreferredSize(new Dimension(100, 50));
		bt_excel.setBorder(new LineBorder(Color.black, 3));
		//bt_excel.setFont(new Font("굴림", Font.PLAIN, 20));
		bt_excel.setBackground(Color.pink);
		bt_excel.setFocusPainted(false);

		bt_reset.setBounds(350, 20, 100, 30);
		bt_reset.setPreferredSize(new Dimension(100, 50));
		bt_reset.setBorder(new LineBorder(Color.black, 3));
		//bt_excel.setFont(new Font("굴림", Font.PLAIN, 20));
		bt_reset.setBackground(Color.pink);
		bt_reset.setFocusPainted(false);
		
	
		//버튼에 이벤트 부여
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
	//엑셀을 저장하는 메서드
	public void saveExcell(){
		HSSFWorkbook book=new HSSFWorkbook();
		HSSFSheet sheet=book.createSheet("동호수");
		WritableWorkbook workbook=null;
		
		
		//WritableSheet sheet=workbook.createSheet("동호수",5);
		//writble
		
		for (int i = 0; i < table.getRowCount(); i++) {
			HSSFRow row=sheet.createRow(i);
			
			for (int j = 0; j < table.getColumnCount(); j++) {
				HSSFCell cell=row.createCell(j);
				System.out.println(table.getValueAt(i, j));
				System.out.println("이건 컬럼카운트"+cell);
				cell.setCellValue((String)table.getValueAt(i, j));
			}
		}
		
		int result=chooser.showSaveDialog(this);
		if(result==JFileChooser.APPROVE_OPTION){
			File file=chooser.getSelectedFile();
			String path=file.getAbsolutePath();
			String [] xml=path.split("\\.");//확장자 .안붙이면 if문에서 에러남
			
			try {		
				if(xml.length==1){
					file=new File(path+".xls");			
				}
				
				fos=new FileOutputStream(file);	
				book.write(fos);
				JOptionPane.showMessageDialog(this, "저장에 성공하셧습니다");
				
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
	
	//초기화 시키는 메서드
	public void reset(){
		PreparedStatement pstmt=null;
		StringBuffer sql=new StringBuffer();	
		//유닛지우기
		int really=JOptionPane.showConfirmDialog(this, "초기화하시겠습니까?");
		if(really==JOptionPane.OK_OPTION){
		sql.append("delete from unit");
		
		try {
			pstmt=con.prepareStatement(sql.toString());
			pstmt.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		//컴플렉스 지우기
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
	
	//테이블의 업데이트를 실현시키는 메서드
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
			reset();
		}
		
	}

	public static void main(String[] args) {
		new TableController();

	}
	



}
