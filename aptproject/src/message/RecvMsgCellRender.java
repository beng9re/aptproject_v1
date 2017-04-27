/*
 * 쪽지 수신함 (RecieveMessage) table 의 Cell 에 대한 Color, Font, Alignment 조절
 */

package message;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.util.ArrayList;

import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

public class RecvMsgCellRender extends DefaultTableCellRenderer{
	
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
			int row, int column) {
		super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
		
		// 확인여부 column Index
		int chkColumn = table.getColumn("확인여부").getModelIndex();
		
		if (row==table.getSelectedRow()){
			// 선택한 row 는 연한 푸른색
			this.setBackground(new Color(204, 229, 255));
		} else if( ((String)table.getValueAt(row, chkColumn)).equals("N")){
			// 확인여부=N 인 경우는 연한노란색, 글자 굵게
			this.setFont(new Font("Default", Font.BOLD, this.getFont().getSize()));
			this.setBackground(new Color(255, 255, 204));
		} else {
			// 일반 cell 은 하얀색
			this.setBackground(Color.WHITE);
		}
		
		// Column Text Alignment
		if (column==chkColumn){
			// 확인여부 컬럼 , text 정렬 center
			this.setHorizontalAlignment(JLabel.CENTER);			
		} else {
			this.setHorizontalAlignment(JLabel.LEFT);
		}
		
		return this;
	}

}
