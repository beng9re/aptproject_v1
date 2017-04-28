package apt.test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Vector;

import db.DBManager;

public class Test{
	
	Connection con;
	PreparedStatement pstm;
	ResultSet rs;
	HashMap<Integer, HashMap> maptop=new HashMap<Integer, HashMap>();
	HashMap<Integer,Integer> mapdown=new HashMap<Integer,Integer>();
	Vector<Integer> arrayList=new Vector<Integer>();
	Vector<Vector> aptvec=new Vector<Vector>();
	Vector vc;
	public Test(String name) {
		con=DBManager.getInstance().getConnection();
		String sql="select unit_name from view_cput where complex_name=? order by unit_name asc";
		try {
			pstm=con.prepareStatement(sql);
			pstm.setString(1, name);
			rs=pstm.executeQuery();
			
			
			while (rs.next()){
				
				int value=Integer.parseInt(rs.getString("unit_name").split("\\s")[0]);
				arrayList.add(value);
				
				
			/*	
				
				rs.next();
				
				
				try {
				} catch (Exception e) {
					// TODO Auto-generated catch block
					
				
					
				}
				*/
			
			
			
				
				
				
			
				
				
				
			}
			paint2();
			} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			}
		
	
	}
	public void paint2(){
		
		Descending des=new Descending();
		Collections.sort(arrayList,des);
		
		
		
		int count =1;
		int index=1;
		int result=0;

		int value2=arrayList.get(1);
		index=(int)value2/100;//15
		for(int i=1;i<arrayList.size();i++){
			try {
				vc=new Vector();
				value2=arrayList.get(i);	
				System.out.println(value2);
				vc.add(value2);
				result=((int)value2/(100*index));
			} catch (Exception e) {
			
				index--;
				aptvec.add(vc);
			
			}
			
		
		}
			for(int i=1;i<aptvec.size();i++){
				for(int j=0; j<aptvec.get(i).size();j++){
					System.out.print(aptvec.get(i).get(j)+"\t");
				}
				System.out.println();
			}
			
	}
		/*
		System.out.println(maptop.size());
		System.out.println(mapdown.size());
		for(int i=1;i<maptop.size();i++){
			for (int j=0;j<mapdown.size();j++){
				System.out.print(maptop.get(i).get(j)+"\t");
			}
			System.out.println();
		}
		
		System.out.println();System.out.println();
		for(int i=maptop.size()-1 ;i>0;i--){
			for (int j=0;j<mapdown.size();j++){
				System.out.print(maptop.get(i).get(j)+"\t");
			}
			System.out.println();
		}
		*/
		
		
	
	class Descending implements Comparator<Integer> {
		 
	    public int compare(Integer o1, Integer o2) {
	        return o2.compareTo(o1);
	    }
	 
	}
	 
	// 오름차순
	class Ascending implements Comparator<Integer> {
	 
	 
	    public int compare(Integer o1, Integer o2) {
	        return o1.compareTo(o2);
	    }
	 
	}


	public static void main(String[] args) {
		new Test("103 동");
	}
}
