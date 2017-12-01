/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.assignment.Client;

/**
 *
 * @author chuzhan
 */
public class MetricsForPlot {
    private long startTime;
    private long latency;
    
    public MetricsForPlot(){
    }
    
    public MetricsForPlot(long startTime, long latency){
        this.startTime = startTime;
        this.latency = latency;
    }
    
    public long getStartTime(){
        return startTime;
    }
    
    public void setStartTime(long startTime){
        this.startTime = startTime;
    }
    
    public long getLatency(){
        return latency;
    }
    
    public void setLatency(long latency){
        this.latency = latency;
    }
}
