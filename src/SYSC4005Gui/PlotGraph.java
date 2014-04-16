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
        //Object[][][] data = new Object[3][50][2];
        final XYSeries s1 = new XYSeries("Series 1");
		String temp = kf.getTF14().getText();

		String a[] = temp.split(",");
		System.out.println(Arrays.toString(a));
		//ArrayList<String> tlist = (ArrayList<String>) Arrays.asList(temp.split(","));
//		
//		for(int j=1;j<tlist.size();j++)
//		{
//			//s1.add(Double.parseDouble(tlist.get(i)), 10 * Math.exp(i / 5.0));
//		System.out.println(j+"-"+Double.parseDouble(tlist.get(j)));
//		}
//		

		for(int j=1;j<=a.length;j++)
		{

			s1.add(Math.exp(j / 5.0),Double.parseDouble(a[j-1]));
			
		}
		
//        for (int i = 1; i <= 50; i++) {
//        	if(i%10==0)
//        	{
//            s1.add(i, 10 * Math.exp(i / 5.0));
//        	}
//        	
//            
//        }


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

    // ****************************************************************************
    // * JFREECHART DEVELOPER GUIDE                                               *
    // * The JFreeChart Developer Guide, written by David Gilbert, is available   *
    // * to purchase from Object Refinery Limited:                                *
    // *                                                                          *
    // * http://www.object-refinery.com/jfreechart/guide.html                     *
    // *                                                                          *
    // * Sales are used to provide funding for the JFreeChart project - please    * 
    // * support us so that we can continue developing free software.             *
    // ****************************************************************************
    
    /**
     * Starting point for the demonstration application.
     *
     * @param args  ignored.
     */
//    public static void main(final String[] args) {
//
//       
//
//    }

}
