/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.assignment.Server;

import java.sql.SQLException;
import com.mycompany.assignment.Client.PersonalVertical;

/**
 *
 * @author chuzhan
 */
public class InsertVerticalThread implements Runnable{
    PersonalVertical personalVertical;
    
    public InsertVerticalThread(PersonalVertical personalVertical){
        this.personalVertical = personalVertical;
    }
    
    @Override
    public void run(){
        long startTime = System.currentTimeMillis();
        try{
            SkiDataDao.getDao().insertVertical(personalVertical);
            System.out.println("inserting data");
            long endTime = System.currentTimeMillis();
            MetricsController.getThreadController().responseTimeAdd(endTime - startTime);
            System.out.println("response time is:  "  + (endTime - startTime));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
