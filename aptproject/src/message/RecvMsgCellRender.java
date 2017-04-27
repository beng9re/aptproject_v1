package message;

import java.awt.Color;
import java.awt.Component;
import java.util.ArrayList;

import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

public class RecvMsgCellRender extends DefaultTableCellRenderer{
	
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
			int row, int column) {
		super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
		
		//if (((String)value).equals("Y")){
			//setBackground(Color.BLUE);
		//} else {
			//setBackground(Color.BLACK);
		//}
		
		setBackground(Color.BLUE);
		
		return this;
	}

}
