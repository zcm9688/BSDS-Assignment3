/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.assignment.Server;

import java.sql.SQLException;

/**
 *
 * @author chuzhan
 */
public class PostMetricOnServer implements Runnable{
    public long latency;
    
    public PostMetricOnServer(long latency){
        this.latency = latency;
    }
    
    @Override
    public void run(){
        try{
            MetricsSao.getDao().insertResponseTime(latency);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
