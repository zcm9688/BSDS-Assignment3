/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.assignment.Client;

import java.awt.Color;
import java.io.File;
import java.io.IOException;
import java.util.List;
import javax.swing.JPanel;
import javax.swing.JPanel;  
import java.io.File;  
import org.jfree.chart.ChartFactory; 
import java.awt.Color; 
import org.jfree.chart.ChartPanel;  
import org.jfree.chart.ChartUtilities;  
import org.jfree.chart.JFreeChart;  
import org.jfree.chart.axis.CategoryAxis;  
import org.jfree.chart.axis.CategoryLabelPositions;  
import org.jfree.chart.axis.NumberAxis;  
import org.jfree.chart.plot.CategoryPlot;  
import org.jfree.chart.plot.PlotOrientation;  
import org.jfree.data.category.CategoryDataset;  
import java.util.List;
import org.jfree.data.category.DefaultCategoryDataset;
import java.io.IOException;  

/**
 *
 * @author chuzhan
 */
public class PlotDrawer {
    private static List<MetricsForPlot> points;
    
    private static CategoryDataset createDataset(List<MetricsForPlot> points){
        DefaultCategoryDataset defaultcategorydataset = new DefaultCategoryDataset();
        for(MetricsForPlot point : points){
            defaultcategorydataset.addValue((double)point.getLatency(), "First", String.valueOf(point.getStartTime()));
        }
        return defaultcategorydataset;
    }
    
    private static JFreeChart createChart(CategoryDataset categorydataset){
        JFreeChart jfreechart = ChartFactory.createLineChart(  
                "Thread's Latency VS StartTime",// 图表标题  
                "StartTime", // 主轴标签（x轴）  
                "Latency",// 范围轴标签（y轴）  
                categorydataset, // 数据集  
                PlotOrientation.VERTICAL,// 方向  
                false, // 是否包含图例  
                true, // 提示信息是否显示  
                false);// 是否使用urls 
        jfreechart.setBackgroundPaint(Color.white);  
          
        CategoryPlot categoryplot = (CategoryPlot) jfreechart.getPlot();  
        categoryplot.setBackgroundPaint(Color.lightGray);  
        categoryplot.setRangeGridlinePaint(Color.white);  
        categoryplot.setRangeGridlinesVisible(false); 
        NumberAxis numberaxis = (NumberAxis) categoryplot.getRangeAxis();  
        numberaxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits()); 
        CategoryAxis domainAxis = categoryplot.getDomainAxis();  
        domainAxis.setCategoryLabelPositions(CategoryLabelPositions.UP_45); // 设置X轴
        domainAxis.setLowerMargin(0.0);   // 设置距离图片左端距离   
        domainAxis.setUpperMargin(0.0);  // 设置距离图片右端距离 
        
        return jfreechart;  
    }
    
     public static JPanel createDemoPanel(List<MetricsForPlot> points, String chartName) {  
        JFreeChart jfreechart = createChart(createDataset(points));  
  
        try {  
            ChartUtilities.saveChartAsJPEG(  
                    new File("/Users/chuzhan/Downloads/" + chartName + ".png"), //文件保存物理路径包括路径和文件名   
                 //    1.0f,    //图片质量 ，0.0f~1.0f  
                     jfreechart, //图表对象   
                    1024,   //图像宽度 ，这个将决定图表的横坐标值是否能完全显示还是显示省略号  
                    768);  
        } catch (IOException e) {  
            // TODO Auto-generated catch block  
            e.printStackTrace();  
        }    //图像高度   
                   
        return new ChartPanel(jfreechart);  
    } 
}
