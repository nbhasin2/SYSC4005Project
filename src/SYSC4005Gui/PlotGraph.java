package SYSC4005Gui;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Arrays;

import javax.swing.JFrame;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.LogarithmicAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.ui.ApplicationFrame;
import org.jfree.ui.RefineryUtilities;

/**
 * A demo showing the use of log axes.
 *
 */
public class PlotGraph extends ApplicationFrame {

	kFrame framek2;
    /**
     * Creates a new demo.
     *
     * @param title  the frame title.
     * 
     */
    public PlotGraph(final String title, kFrame kf) {

        super(title);
        framek2=kf;

        final XYSeries s1 = new XYSeries("Series 1");
		String temp = kf.getTF14().getText();

		String a[] = temp.split(",");
		System.out.println(Arrays.toString(a));


		for(int j=1;j<=a.length;j++)
		{

			s1.add(Math.exp(j / 5.0),Double.parseDouble(a[j-1]));
			
		}
		


        final XYSeriesCollection dataset = new XYSeriesCollection();
        dataset.addSeries(s1);
       // dataset.addSeries(s2);
        //dataset.addSeries(s3);

        final JFreeChart chart = ChartFactory.createXYLineChart(
            "Log Axis Demo",          // chart title
            "Category",               // domain axis label
            "Value",                  // range axis label
            dataset,                  // data
            PlotOrientation.VERTICAL,
            true,                     // include legend
            true,
            false
        );

        final XYPlot plot = chart.getXYPlot();
        final NumberAxis domainAxis = new NumberAxis("x");
        final NumberAxis rangeAxis = new LogarithmicAxis("Log(y)");
        plot.setDomainAxis(domainAxis);
        plot.setRangeAxis(rangeAxis);
        chart.setBackgroundPaint(Color.white);
        plot.setOutlinePaint(Color.black);
        final ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new java.awt.Dimension(500, 270));
        setContentPane(chartPanel);
      
    }


}
