package aptuser;

import java.awt.Choice;
import java.awt.Component;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.TreeMap;
import java.util.TreeSet;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

import db.AptuserModel;
import db.ComplexModel;
import db.UnitModel;
import dto.Aptuser;
import dto.Unit;

public class RegistUser extends UserInfo implements ItemListener {
	// ȸ��ID, ���ڵ�, ��й�ȣ, ��й�ȣȮ��, �̸�, ����ó, �����, �ּ�
	String[][] isEdit = { { "ȸ��ID", "Y" }, { "���ڵ�", "N" }, { "��й�ȣ", "Y" }, { "��й�ȣ Ȯ��", "Y" }, { "�̸�", "Y" },
			{ "����ó", "Y" }, { "�����", "Y" }, { "�ּ�", "Y" } };
	String titleStr = "ȸ�����";
	String btnTxt = "�� ��";
	// �ߺ��Ǵ� ID�� �ִ��� Ȯ���ϴµ� ����ϰ�, �ߺ��Ǵ� ID�� ������ null���� �ǹǷ� �Է¿� ��Ȱ��
	AptuserModel duChkModel;
	TreeMap<Integer, Integer> complex;
	TreeMap<Integer, TreeSet<Integer>> unitTotal;
	
	Choice ch_complex, ch_floor, ch_unit;

	public RegistUser(Connection conn, String id) {
		this.conn = conn;
		this.id = id;
		ch_complex = ((Choice) fieldData.get("��"));
		ch_floor = ((Choice) fieldData.get("��"));
		ch_unit = ((Choice) fieldData.get("ȣ"));
		
		init(isEdit, titleStr, btnTxt, false);
		
		ComplexModel cpModel = new ComplexModel(conn);
		complex = cpModel.getMap();
		setComplex();
		
		ch_complex.addItemListener(this);
		ch_floor.addItemListener(this);
		ch_unit.addItemListener(this);
	}

	protected void mkBottomFields(JPanel pan, int i) {
		String lbTxt = null;
		JPanel pnl_sub = new JPanel();
		pnl_sub.setLayout(new BoxLayout(pnl_sub, BoxLayout.LINE_AXIS));
		pnl_sub.setAlignmentX(Component.LEFT_ALIGNMENT);
		
		if (isEdit[i][0].equals("�����")) {
			lbTxt = "����";
			JLabel desc = new JLabel("�����ڸ� üũ���ּ���");
			desc.setFont(font);
			pnl_sub.add(fieldData.get("����"));
			pnl_sub.add(desc);
		} else {
			lbTxt = "�ּ��Է�";
			pnl_sub.add(ch_complex);
			pnl_sub.add(Box.createHorizontalStrut(5));
			pnl_sub.add(ch_floor);
			pnl_sub.add(Box.createHorizontalStrut(5));
			pnl_sub.add(ch_unit);
		}

		lbArr[i] = new JLabel(lbTxt);
		lbArr[i].setFont(font);
		pan.add(lbArr[i]);
		pan.add(Box.createVerticalGlue());
		pan.add(pnl_sub);
	}

	// �ּ��Է��� ���ؼ� ���̽�������Ʈ�� ��ϵ� �ּҵ� ����� ����
	private void setComplex() {
		ch_complex.removeAll();
		ch_complex.add("��");
		for (Integer item : complex.keySet()) {
			ch_complex.add(item.toString() + "��");
		}
	}

	private void setFloor(String selectedItem) {
		UnitModel utModel = new UnitModel(conn, complex.get(conversion(selectedItem)));
		ArrayList unit = utModel.getData();
		ch_floor.removeAll();
		ch_floor.add("��");
		ch_unit.removeAll();
		ch_unit.add("ȣ");

		// �ʿ� ���� ȣ�� ��´�
		unitTotal = new TreeMap<Integer, TreeSet<Integer>>();
		int res = 0;
		TreeSet<Integer> unitList = null;
		for (int i = 0; i < unit.size(); i++) {
			res = ((Unit) unit.get(i)).getUnit_name();
			int floor = res / 100;
			if (unitTotal.get(floor) == null) {
				unitTotal.put(floor, new TreeSet<Integer>());
			}
			unitList = unitTotal.get(Integer.valueOf(floor));
			unitList.add(Integer.valueOf(res % 100));
		}

		// ���̽� ������Ʈ�� �߰��Ѵ�
		for (Integer item : unitTotal.keySet()) {
			ch_floor.add(item.toString() + "��");
		}
	}

	private void setUnit(String selectedItem) {
		ch_unit.removeAll();
		ch_unit.add("ȣ");
		
		// ���̽� ������Ʈ�� �߰��Ѵ�
		for (Integer item : unitTotal.get(conversion(selectedItem))) {
			ch_unit.add(item + "ȣ");
		}
	}
	
	private int conversion(String str) {
		return Integer.parseInt(str.replaceAll("\\D", ""));
	}

	@Override
	public void itemStateChanged(ItemEvent e) {
		if (e.getSource() == ch_complex && ((Choice) e.getSource()).getSelectedIndex() != 0) {
			setFloor(((Choice) e.getSource()).getSelectedItem());
		} else if (e.getSource() == ch_floor && ((Choice) e.getSource()).getSelectedIndex() != 0) {
			setUnit(((Choice) e.getSource()).getSelectedItem());
		}
	}

	// ȸ��ID �ߺ�Ȯ��, �ּ��Է� ���� Ȯ�� : ����ִ� �ʵ�Ȯ�ο��� �������� ���� ��
	protected boolean regFieldChk() {
		String new_id = ((JTextField) fieldData.get("ȸ��ID")).getText();
		duChkModel = new AptuserModel(conn, new_id);
		ArrayList<Aptuser> res_id = duChkModel.getData();
		// �ߺ��Ǵ� ID�� ������ true�� ��ȯ�Ѵ�
		if (res_id.size() != 0) {
			JOptionPane.showMessageDialog(this, new_id + "�� �̹� �����մϴ�. �ٸ� ID�� �Է����ּ���");
			return true;
		}
		
		// �ּ��ʵ� ���� �ִ��� Ȯ���Ѵ�
		if (((Choice)ch_unit).getSelectedItem()==null || ((Choice)ch_unit).getSelectedItem().equals("ȣ")) {
			JOptionPane.showMessageDialog(this, "��Ȯ�� �ּҸ� �Է��ϼ���");
			return true;
		}
		
		// ������ ��� �����ϸ� false ��ȯ
		return false;
	}

	// �Է��ʵ� �ʱ�ȭ
	private void clearForms() {
		for (int i = 0; i < isEdit.length; i++) {
			((JTextField) fieldData.get(isEdit[i][0])).setText("");
		}
		((JRadioButton)fieldData.get("����")).setSelected(false);
		setComplex();
		((Choice)ch_floor).removeAll();
		((Choice)ch_unit).removeAll();
	}
	
	// �Է��ʵ��� �ּҸ� ������ �ͼ� unit_id�� ��ȯ�Ѵ�
	private int searchAddress() {
		int complex_id = complex.get(conversion(ch_complex.getSelectedItem()));
		int floor = conversion(ch_floor.getSelectedItem());
		int unit = conversion(ch_unit.getSelectedItem());
		int unit_name = floor*100+unit;
		// Unit ���̺� ������ ������ �´�
		UnitModel unitModel = new UnitModel(conn);
		ArrayList<Unit> unitData = unitModel.getData();
		// ��ġ�ϴ� unit_id�� �˻��Ѵ�
		for (int i=0; i<unitData.size(); i++) {
			if(unitData.get(i).getComplex_id()==complex_id && unitData.get(i).getUnit_name()==unit_name) {
				return unitData.get(i).getUnit_id();
			}
		}
		System.out.println("�ּ� ��ȯ ����");
		return 0;
	}
	
	@Override
	// �ʵ峻���� ��� insert�Ѵ�
	protected void confirm() {
		ArrayList<Aptuser> insertData = duChkModel.getData();
		Aptuser insertUser = new Aptuser();
		insertUser.setAptuser_id(((JTextField) fieldData.get("ȸ��ID")).getText());
		insertUser.setAptuser_code(((JTextField) fieldData.get("���ڵ�")).getText());
		insertUser.setAptuser_pw(String.valueOf(((JPasswordField) fieldData.get("��й�ȣ")).getPassword()));
		insertUser.setAptuser_name(((JTextField) fieldData.get("�̸�")).getText());
		insertUser.setAptuser_phone(((JTextField) fieldData.get("����ó")).getText());
		
		// ���ѿ� ���õ� ������ư ��� ���� �ݿ��Ѵ�
		Object[] permission = ((JRadioButton) fieldData.get("����")).getSelectedObjects();
		if (permission!=null) {
			insertUser.setAptuser_perm(9); //checked�� ��� �����ڱ����� 9�� �ο�
		}
		
		// ���� �ּҸ� ��ȯ�ؼ� �Է��Ѵ�
		insertUser.setUnit_id(searchAddress());
		// DB�� �����͸� �Է��Ѵ�
		insertData.add(insertUser);
		duChkModel.insertData();
		
		// �Է��ʵ带 �ʱ�ȭ�Ѵ�
		JOptionPane.showMessageDialog(this, "ȸ����� �Ϸ�");
		clearForms();
	}

}
