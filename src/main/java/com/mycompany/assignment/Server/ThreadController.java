/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.assignment.Server;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import com.mycompany.assignment.Client.RFIDLiftData;
import com.mycompany.assignment.Client.PersonalVertical;

/**
 *
 * @author chuzhan
 */
public class ThreadController {
    ExecutorService postThreadPoolExecutor;
    ExecutorService insertVerticalThreadPoolExecutor;
    
    private static ThreadController threadController = null;
    
    public static synchronized ThreadController getThreadController() {
        if(threadController == null) {
            threadController = new ThreadController();
        }
        return threadController;
    }
    
    public ThreadController(){
        postThreadPoolExecutor = Executors.newFixedThreadPool(100);
        insertVerticalThreadPoolExecutor = Executors.newFixedThreadPool(100);
    }
    
     public synchronized int postResortThreadAdd(RFIDLiftData data){
         postThreadPoolExecutor.submit(new PostResortThread(data));
         return data.getResortID();
     }
     
     public synchronized void insertVerticalThreadAdd(PersonalVertical personalVertical){
         insertVerticalThreadPoolExecutor.submit(new InsertVerticalThread(personalVertical));
     }
}
