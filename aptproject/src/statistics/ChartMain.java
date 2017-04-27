package statistics;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.sql.Connection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import javax.swing.JFrame;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.AxisLocation;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.CategoryLabelPositions;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.labels.StandardCategoryToolTipGenerator;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.DatasetRenderingOrder;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.LineAndShapeRenderer;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.ui.RefineryUtilities;

import db.DBManager;
import db.InvoiceModel;

public class ChartMain extends JFrame {
	Connection conn = DBManager.getInstance().getConnection();
	InvoiceModel model;
	ArrayList<ArrayList> byDay;
	ArrayList<ArrayList> byPer;
	SimpleDateFormat dateFormat;
	Font font;
	
	public ChartMain() {
		font = new Font("���� ���", Font.PLAIN, 14);
		// ��¥������ �Է��Ѵ�
		dateFormat = new SimpleDateFormat("yyyyMMdd");
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DATE, 1);
		String eDay = dateFormat.format(cal.getTime());
		cal.add(Calendar.DATE, -8);
		String bDay = dateFormat.format(cal.getTime());
		
		model = new InvoiceModel(conn);
		byDay = new ArrayList<ArrayList>();
		byPer = new ArrayList<ArrayList>();
		
		setData(bDay, eDay);
		
		System.out.println(byDay);
		System.out.println(byPer);
		
		// ��Ʈ ����
		CategoryDataset dataset1 = createDataset1();
		JFreeChart chart = ChartFactory.createBarChart("�ù� ó�� ���",
				"��¥", // X��
				"ó����", // Y�� 1��
				dataset1, // 1�� ������
				PlotOrientation.VERTICAL, true, // ���� ǥ��
				true, // ����
				false // URL
		);

		chart.setBackgroundPaint(Color.white);
		chart.getTitle().setFont(new Font("���� ���", Font.BOLD, 20));
		chart.getLegend().setItemFont(font);
		
		CategoryPlot plot = chart.getCategoryPlot();
		plot.setBackgroundPaint(new Color(0xEE, 0xEE, 0xFF));
		plot.setDomainAxisLocation(AxisLocation.BOTTOM_OR_RIGHT);

		CategoryDataset dataset2 = createDataset2();
		plot.setDataset(1, dataset2);
		plot.mapDatasetToRangeAxis(1, 1);

		CategoryAxis domainAxis = plot.getDomainAxis();
		domainAxis.setCategoryLabelPositions(CategoryLabelPositions.DOWN_45);
		ValueAxis axis2 = new NumberAxis("���� ó����");
		axis2.setLabelFont(font);
		plot.setRangeAxis(1, axis2);

		LineAndShapeRenderer renderer2 = new LineAndShapeRenderer();
		renderer2.setBaseToolTipGenerator(new StandardCategoryToolTipGenerator());
		plot.setRenderer(1, renderer2);
		plot.setDatasetRenderingOrder(DatasetRenderingOrder.REVERSE);

		ChartPanel chartPanel = new ChartPanel(chart);
//		chartPanel.setPreferredSize(new Dimension(500, 270));
		setContentPane(chartPanel);
		
		setPreferredSize(new Dimension(700,700));
		
	}
	
	private void setData(String bDay, String eDay) {
		model.selectByDAY(bDay, eDay);
		byDay = model.getData();
		model.selectProportion(bDay, eDay);
		byPer = model.getData();
		
	}

	private CategoryDataset createDataset1() {
		// row keys...
		String series1 = "�� ó����";

		// create the dataset...
		DefaultCategoryDataset dataset = new DefaultCategoryDataset();

		// column keys...
		for (ArrayList list : byDay) {
			dataset.addValue(Integer.parseInt(list.get(0).toString()), series1, list.get(0).toString());
		}

		return dataset;
	}

	private CategoryDataset createDataset2() {
		// row keys...
		String series1 = "���� ó������";

		// create the dataset...
		DefaultCategoryDataset dataset = new DefaultCategoryDataset();

		// column keys...
		
		for (ArrayList list : byPer) {
			dataset.addValue(Integer.parseInt(list.get(0).toString()), series1, list.get(0).toString());
		}
		
		return dataset;

	}

	public static void main(String[] args) {
		ChartMain demo = new ChartMain();
		demo.pack();
		RefineryUtilities.centerFrameOnScreen(demo);
		demo.setVisible(true);
		demo.setDefaultCloseOperation(EXIT_ON_CLOSE);

	}

}