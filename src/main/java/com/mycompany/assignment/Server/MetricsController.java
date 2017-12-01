/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.assignment.Server;

import java.sql.SQLException;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 *
 * @author chuzhan
 */
public class MetricsController {
    ExecutorService threadPoolExecutor;
    MetricsSao metricsSao;
    
    private static MetricsController metricsController = null;
    
    public static synchronized MetricsController getThreadController() {
        if(metricsController == null) {
            metricsController = new MetricsController();
        }
        return metricsController;
    }
    
    public MetricsController(){
        threadPoolExecutor = Executors.newFixedThreadPool(100);
        metricsSao = MetricsSao.getDao();
    }
    
    public synchronized void responseTimeAdd(long latency){
        threadPoolExecutor.submit(new PostMetricOnServer(latency));
    }
    
    public MetricsOnServer getMetrics(){
        MetricsOnServer metricsOnServer = new MetricsOnServer();
        try{
            int amount = metricsSao.getAmount();
            long mean = metricsSao.getMean();
            List<Integer> per = metricsSao.getPercentile(amount);
            metricsOnServer.setRequestAmount(amount);
            metricsOnServer.setMean(mean);
            metricsOnServer.setMedian(per.get(0));
            metricsOnServer.setNfPercentile(per.get(1));
            metricsOnServer.setNnPercentile(per.get(2));
        } catch(SQLException e){
            e.printStackTrace();
        }
        return metricsOnServer; 
    }
}
