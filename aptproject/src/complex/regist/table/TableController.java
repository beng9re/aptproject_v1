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
	JFileChooser chooser=new JFileChooser("C:/Users/jm/Desktop/새 폴더(2)");
	FileOutputStream fos;
	
	String dd;
	//유닛의 아이디 값
	String value;
	
	
	public TableController(){
		con=manager.getConnection();
		table=new JTable();
		db_table=new db_Table(con);
		scroll=new JScrollPane(table);
		p_north=new JPanel();
		bt_excel=new JButton("excel로 저장");
		bt_reset=new JButton("삭제");
		//bt_bring=new JButton("excel로 가져오기");
		
	
		table.setModel(db_table);
		
		//테이블 숨기기!!!!
		table.getColumn("id").setWidth(0);
		table.getColumn("id").setMinWidth(0);
		table.getColumn("id").setMaxWidth(0);
		table.setRowHeight(20);
		
		
		
		
		//테이블 변경 이벤트
		  table.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				JTable t=(JTable)e.getSource();
				int row=t.getSelectedRow();
				int col=0;
				value=(String) t.getValueAt(row, col);
				
				
			}
		});
		
		
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
		
		/*
		bt_bring.setBounds(0, 20, 150, 30);
		bt_bring.setPreferredSize(new Dimension(100, 50));
		bt_bring.setBorder(new LineBorder(Color.black, 3));
		//bt_excel.setFont(new Font("굴림", Font.PLAIN, 20));
		bt_bring.setBackground(Color.pink);
		bt_bring.setFocusPainted(false);
		*/
	
		//버튼에 이벤트 부여
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
	//엑셀을 불러와서 db에 저장시키는 메서드
	public void loadExcell(){
		
			int result = chooser.showOpenDialog(this);
			if (result == JFileChooser.APPROVE_OPTION) {
				File file = chooser.getSelectedFile();
				FileInputStream fis = null;// try문 닫을떄 안보일 수 있음
				// 28_3
				StringBuffer cols = new StringBuffer();
				StringBuffer data = new StringBuffer();

				try {
					fis = new FileInputStream(file);

					HSSFWorkbook book = null;
					book = new HSSFWorkbook(fis);// stream을 잡아 먹어라

					HSSFSheet sheet = null;
					sheet = book.getSheet("동호수");

					int total = sheet.getLastRowNum();// sheet에 잇는 열의 갯수 구하기
					int first = sheet.getFirstRowNum();
					
					HSSFRow firstRow = sheet.getRow(first);// row 한줄을 얻어왔음

					cols.delete(0, cols.length());
					
					firstRow.getLastCellNum();// 첫번째 행의 마지막 cell 번호 얻어오기
					for (int i = 0; i < firstRow.getLastCellNum(); i++) {

						HSSFCell cell = firstRow.getCell(i);
						if (i < firstRow.getLastCellNum() - 1) {
							
							cols.append(cell.getStringCellValue());
	
						} else
							
							cols.append(cell.getStringCellValue());
					
					}

					DataFormatter df = new DataFormatter();// 엑셀의 데이터를 읽는 메서드
					
					for (int a = 1; a <= total; a++) {
						HSSFRow row = sheet.getRow(a);
						
						int columnCount = row.getLastCellNum();// 컬럼의 수를 가져온다
						

						data.delete(0, data.length());// data 스트링버퍼 지우기 28_4

						for (int i = 0; i < columnCount; i++) {
							HSSFCell cell = row.getCell(i);
							String value = df.formatCellValue(cell);
			
							data.append(value);
							
						}
			
					}
					JOptionPane.showMessageDialog(this, "파일이동중");
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
	public void delete(){
		int ans=JOptionPane.showConfirmDialog(this, "정말 삭제하시겠습니까?");
		if(ans==JOptionPane.OK_OPTION){
		
		PreparedStatement pstmt=null;
		String sql="delete FROM unit WHERE UNIT_ID=?";
			
		try {
			pstmt=con.prepareStatement(sql);
			pstmt.setString(1, value);
			int result=pstmt.executeUpdate();
			if(result!=0){
				JOptionPane.showMessageDialog(this, "삭제완료");
				//table.updateUI();//테이블을 갱신 해주어야함
				
				upDate();
				
			}
			
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		}
		
	}
	
	/*
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
	*/
	
	
	
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
			delete();
		}else if(obj==bt_bring){
			//loadExcell();
		}
		
	}

	public static void main(String[] args) {
		new TableController();

	}

}
