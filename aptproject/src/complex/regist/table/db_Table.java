package complex.regist.table;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

import javax.swing.table.AbstractTableModel;

public class db_Table extends AbstractTableModel{
	Vector<String>colmn=new Vector<String>();
	Vector<Vector> data=new Vector<Vector>();
	Connection con;
	
	
	public db_Table(Connection con) {
		this.con=con;
		colmn=new Vector<String>();
		colmn.add("��");
		colmn.add("ȣ��");
		
			
		getList();
	
	}
	//��,ȣ���� �������� sql �� 
	public void getList(){
		PreparedStatement pstmt=null;
		ResultSet rs=null;
		
		String sql="select  COMPLEX_NAME as ��,UNIT_NAME as ȣ�� FROM complex,UNIT u WHERE complex.COMPLEX_ID=u.COMPLEX_ID";
		
		try {
			pstmt=con.prepareStatement(sql);
			rs=pstmt.executeQuery();
			
			while(rs.next()){
				Vector vec=new Vector();
				vec.add(rs.getString("��"));
				vec.add(rs.getString("ȣ��"));
		
				
				data.add(vec);		
			}
		
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			if(pstmt!=null){
				try {
					pstmt.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if(rs!=null){
				try {
					rs.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		
		
	}
	
	
	@Override
	public int getColumnCount() {
		// TODO Auto-generated method stub
		return colmn.size();
	}

	@Override
	public int getRowCount() {
		// TODO Auto-generated method stub
		return data.size();
	}

	@Override
	public String getColumnName(int col) {
		
		
		return colmn.get(col);
	}
	@Override
	public Object getValueAt(int row, int col) {
		
		return data.elementAt(row).elementAt(col);
	}
	

}
