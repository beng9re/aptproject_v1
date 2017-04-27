package statistics;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.sql.Connection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

import javax.swing.JPanel;

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

import db.DBManager;
import db.InvoiceModel;

public class ChartMain extends JPanel {
	Connection conn = DBManager.getInstance().getConnection();
	InvoiceModel Dmodel;
	InvoiceModel Pmodel;
	ArrayList<String[]> byDay;
	ArrayList<String[]> byPer;
	HashMap<String, Integer> byPerMap;
	SimpleDateFormat dateFormat;
	Font font;
	
	public ChartMain() {
		// 날짜정보를 입력한다
		dateFormat = new SimpleDateFormat("yyyyMMdd");
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DATE, 1);
		String eDay = dateFormat.format(cal.getTime());
		cal.add(Calendar.DATE, -8);
		String bDay = dateFormat.format(cal.getTime());
		
		font = new Font("맑은 고딕", Font.BOLD, 16);
		Dmodel = new InvoiceModel(conn);
		Pmodel = new InvoiceModel(conn);
		byDay = new ArrayList<String[]>();
		byPer = new ArrayList<String[]>();
		byPerMap = new HashMap<String, Integer>();
		
		setData(bDay, eDay);
		
		// 차트 생성
		CategoryDataset dataset1 = createDataset1();
		
		JFreeChart chart = ChartFactory.createBarChart("최근 일주일간 택배 통계",
				"날짜", // X축
				"처리량", // Y축 1번
				dataset1, // 1번 데이터
				PlotOrientation.VERTICAL, true, // 범주 표시
				true, // 툴팁
				false // URL
				);

		CategoryPlot plot = chart.getCategoryPlot();
		plot.setBackgroundPaint(new Color(0xEE, 0xEE, 0xFF));
		plot.setDomainAxisLocation(AxisLocation.BOTTOM_OR_RIGHT);

		CategoryDataset dataset2 = createDataset2();
		plot.setDataset(1, dataset2);
		plot.mapDatasetToRangeAxis(1, 1);

		CategoryAxis domainAxis = plot.getDomainAxis();
		domainAxis.setCategoryLabelPositions(CategoryLabelPositions.DOWN_45);
		ValueAxis axis2 = new NumberAxis("일일 처리율");
		axis2.setLabelFont(font);
		plot.setRangeAxis(1, axis2);
		
		LineAndShapeRenderer d2Renderer = new LineAndShapeRenderer();
		d2Renderer.setBaseToolTipGenerator(new StandardCategoryToolTipGenerator());
		plot.setRenderer(1, d2Renderer);
		plot.setDatasetRenderingOrder(DatasetRenderingOrder.FORWARD);

		chart.setBackgroundPaint(Color.white);
		chart.getTitle().setFont(new Font("맑은 고딕", Font.BOLD, 20));
		chart.getLegend().setItemFont(font);
		chart.getCategoryPlot().getRangeAxis().setLabelFont(font);
		chart.getCategoryPlot().getDomainAxis().setLabelFont(font);
		
		add(new ChartPanel(chart));
		setPreferredSize(new Dimension(700,700));
		setBackground(Color.CYAN);
	}
	
	private void setData(String bDay, String eDay) {
		Dmodel.selectByDAY(bDay, eDay);
		byDay = Dmodel.getData();
		Pmodel.selectProportion(bDay, eDay);
		byPer = Pmodel.getData();
	}

	private CategoryDataset createDataset1() {
		// row keys...
		String series1 = "전체 물품";
		String series2 = "당일 처리량";

		// create the dataset...
		DefaultCategoryDataset dataset = new DefaultCategoryDataset();

		// column keys...
		for (String[] s1 : byDay) {
			dataset.addValue(Integer.parseInt(s1[1]), series1, s1[0]);
		}
		
		for (String[] s2 : byPer) {
			dataset.addValue(Integer.parseInt(s2[1]), series2, s2[0]);
			byPerMap.put(s2[0], Integer.parseInt(s2[1]));
		}

		return dataset;
	}

	private CategoryDataset createDataset2() {
		// row keys...
		String series1 = "당일 처리비율";

		// create the dataset...
		DefaultCategoryDataset dataset = new DefaultCategoryDataset();

		// column keys...
		for (String[] list : byDay) {
			int numer = (byPerMap.get(list[0])==null) ? 0 : byPerMap.get(list[0]);
			dataset.addValue(numer*100/Integer.parseInt(list[1]), series1, list[0]);
		}
		
		return dataset;
	}
}