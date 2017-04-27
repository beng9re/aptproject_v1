/*
 * ���� ������ (RecieveMessage) table �� Cell �� ���� Color, Font, Alignment ����
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
		
		// Ȯ�ο��� column Index
		int chkColumn = table.getColumn("Ȯ�ο���").getModelIndex();
		
		if (row==table.getSelectedRow()){
			// ������ row �� ���� Ǫ����
			this.setBackground(new Color(204, 229, 255));
		} else if( ((String)table.getValueAt(row, chkColumn)).equals("N")){
			// Ȯ�ο���=N �� ���� ���ѳ����, ���� ����
			this.setFont(new Font("Default", Font.BOLD, this.getFont().getSize()));
			this.setBackground(new Color(255, 255, 204));
		} else {
			// �Ϲ� cell �� �Ͼ��
			this.setBackground(Color.WHITE);
		}
		
		// Column Text Alignment
		if (column==chkColumn){
			// Ȯ�ο��� �÷� , text ���� center
			this.setHorizontalAlignment(JLabel.CENTER);			
		} else {
			this.setHorizontalAlignment(JLabel.LEFT);
		}
		
		return this;
	}

}
