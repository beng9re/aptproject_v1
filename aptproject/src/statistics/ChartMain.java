package statistics;

import java.awt.Color;

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

public class ChartMain extends JFrame {

	public ChartMain() {
		// 차트 생성
		CategoryDataset dataset1 = createDataset1();
		JFreeChart chart = ChartFactory.createBarChart("택배 처리 통계",
				"날짜", // X축
				"처리량", // Y축 1번
				dataset1, // 1번 데이터
				PlotOrientation.VERTICAL, true, // 범주 표시
				true, // 툴팁
				false // URL
		);

		chart.setBackgroundPaint(Color.white);

		CategoryPlot plot = chart.getCategoryPlot();
		plot.setBackgroundPaint(new Color(0xEE, 0xEE, 0xFF));
		plot.setDomainAxisLocation(AxisLocation.BOTTOM_OR_RIGHT);

		CategoryDataset dataset2 = createDataset2();
		plot.setDataset(1, dataset2);
		plot.mapDatasetToRangeAxis(1, 1);

		CategoryAxis domainAxis = plot.getDomainAxis();
		domainAxis.setCategoryLabelPositions(CategoryLabelPositions.DOWN_45);
		ValueAxis axis2 = new NumberAxis("일일 처리율");
		plot.setRangeAxis(1, axis2);

		LineAndShapeRenderer renderer2 = new LineAndShapeRenderer();
		renderer2.setBaseToolTipGenerator(new StandardCategoryToolTipGenerator());
		plot.setRenderer(1, renderer2);
		plot.setDatasetRenderingOrder(DatasetRenderingOrder.REVERSE);

		ChartPanel chartPanel = new ChartPanel(chart);
		chartPanel.setPreferredSize(new java.awt.Dimension(500, 270));
		setContentPane(chartPanel);
	}
	
	private void setData() {
		// select * from invoice where invoice_arrtime < to_date('20170426','YYYYMMDD') and invoice_arrtime > to_date('20170425','YYYYMMDD')
		// select group by to_date??
	}

	private CategoryDataset createDataset1() {
		// row keys...
		final String series1 = "First";

		// column keys...
		final String category1 = "Category 1";
		final String category2 = "Category 2";
		final String category3 = "Category 3";
		final String category4 = "Category 4";
		final String category5 = "Category 5";
		final String category6 = "Category 6";
		final String category7 = "Category 7";
		final String category8 = "Category 8";

		// create the dataset...
		final DefaultCategoryDataset dataset = new DefaultCategoryDataset();

		dataset.addValue(1.0, series1, category1);
		dataset.addValue(4.0, series1, category2);
		dataset.addValue(3.0, series1, category3);
		dataset.addValue(5.0, series1, category4);
		dataset.addValue(5.0, series1, category5);
		dataset.addValue(7.0, series1, category6);
		dataset.addValue(7.0, series1, category7);
		dataset.addValue(8.0, series1, category8);

		return dataset;

	}

	private CategoryDataset createDataset2() {

		// row keys...
		final String series1 = "Fourth";

		// column keys...
		final String category1 = "Category 1";
		final String category2 = "Category 2";
		final String category3 = "Category 3";
		final String category4 = "Category 4";
		final String category5 = "Category 5";
		final String category6 = "Category 6";
		final String category7 = "Category 7";
		final String category8 = "Category 8";

		// create the dataset...
		final DefaultCategoryDataset dataset = new DefaultCategoryDataset();

		dataset.addValue(15.0, series1, category1);
		dataset.addValue(24.0, series1, category2);
		dataset.addValue(31.0, series1, category3);
		dataset.addValue(25.0, series1, category4);
		dataset.addValue(56.0, series1, category5);
		dataset.addValue(37.0, series1, category6);
		dataset.addValue(77.0, series1, category7);
		dataset.addValue(18.0, series1, category8);

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