/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.assignment.Client;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author chuzhan
 */
public class Metrics {
    private List<MetricsForPlot> latenciesForPlot = new ArrayList<MetricsForPlot>();
    
    public List<MetricsForPlot> getLatenciesForPlot(){
        return latenciesForPlot;
    }
    
    synchronized public void threadLatencyForPlotAdd(long startTime, long latency) {
        latenciesForPlot.add(new MetricsForPlot(startTime, latency));
    }
}
