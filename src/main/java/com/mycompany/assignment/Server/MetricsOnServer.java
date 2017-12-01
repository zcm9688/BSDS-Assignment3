/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.assignment.Server;

/**
 *
 * @author chuzhan
 */
public class MetricsOnServer {
    private long requestAmount;
    private long failedRequestAmount;
    private long mean;
    private long median;
    private long nfPercentile;
    private long nnPercentile;
    
    public MetricsOnServer(){
    }
    
    public MetricsOnServer(long requestAmount, long failedRequestAmount, long mean, long median, long nfPercentile, long nnPercentile){
        this.requestAmount = requestAmount;
        this.failedRequestAmount = failedRequestAmount;
        this.mean = mean;
        this.median = median;
        this.nfPercentile = nfPercentile;
        this.nnPercentile = nnPercentile;
    }
    
    public long getRequestAmount(){
        return requestAmount;
    }
    
    public void setRequestAmount(long requestAmount){
        this.requestAmount = requestAmount;
    }
    
    public long getFailedRequestAmount(){
        return failedRequestAmount;
    }
    
    public void setFailedRequestAmount(long failedRequestAmount){
        this.failedRequestAmount = failedRequestAmount;
    }
    
    public long getMean(){
        return mean;
    }
    
    public void setMean(long mean){
        this.mean = mean;
    }
    
    public long getMedian(){
        return median;
    }
    
    public void setMedian(long median){
        this.median = median;
    }
    
    public long getNfPercentile(){
        return nfPercentile;
    }
    
    public void setNfPercentile(long nfPercentile){
        this.nfPercentile = nfPercentile;
    }
    
    public long getNnPercentile(){
        return nnPercentile;
    }
    
    public void setNnPercentile(long nnPercentile){
        this.nnPercentile = nnPercentile;
    }
}
