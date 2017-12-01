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
public class GetVerticalThread implements Runnable{
    public int skierID;
    public int dayNum;
    
    public GetVerticalThread(int skierID, int dayNum){
        this.skierID = skierID;
        this.dayNum = dayNum;
    }
    
    @Override
    public void run(){
        try{
            SkiDataDao.getDao().selectPersonalVertical(skierID, dayNum);
        }catch(SQLException e){
            e.printStackTrace();
        }
    }
}
