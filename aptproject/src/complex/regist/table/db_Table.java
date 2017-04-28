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
	Vector vec;
	Connection con;
	
	
	public db_Table(Connection con) {
		this.con=con;
		colmn=new Vector<String>();
		colmn.add("id");
		colmn.add("동");
		colmn.add("호수");
		
			
		getList();
		
	
	}
	//동,호수를 가져오는 sql 문 
	public void getList(){
		data.removeAll(data);//다지워야 업데이트가 되기에 

		PreparedStatement pstmt=null;
		ResultSet rs=null;
		
		String sql="select * FROM VIEW_CONFIRMTABLE v ORDER BY id ASC";
				
		
		try {
			pstmt=con.prepareStatement(sql);
			rs=pstmt.executeQuery();
			
			
			while(rs.next()){
				Vector vec=new Vector();
				vec.add(rs.getString("id"));
				vec.add(rs.getString("동"));
				vec.add(rs.getString("호수"));
				data.add(vec);
				//System.out.print(vec.size());
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
public boolean isCellEditable(int row, int col) {
		boolean flag=false;
		if(col==0){
			flag=false;
		}else{
			flag=true;
		}
		
		return false;

}

	
	@Override
	public void setValueAt(Object Value, int row, int col) {
		Vector vec=data.elementAt(row);
		vec.set(col, Value);
		
		fireTableDataChanged();
		
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
