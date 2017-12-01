/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.assignment.Server;

import java.sql.SQLException;
import com.mycompany.assignment.Client.RFIDLiftData;

/**
 *
 * @author chuzhan
 */
public class PostResortThread implements Runnable{
    public RFIDLiftData data;
    
    public PostResortThread(RFIDLiftData data){
        this.data = data;
    }
    
    @Override
    public void run(){
        long startTime = System.currentTimeMillis();
        try{
            SkiDataDao.getDao().insertResorts(data);
            System.out.println("inserting data");
            long endTime = System.currentTimeMillis();
            MetricsController.getThreadController().responseTimeAdd(endTime - startTime);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
