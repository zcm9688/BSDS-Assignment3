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
public class MyClientLocally {
    public static void main(String[] args){
        ThreadPoolExecutor executor = new ThreadPoolExecutor(1,1);
//        executor.postResorts();
//        executor.getMetrics();
//        executor.deleteMetrics();
        
        
        executor.insertAllVerticals();
        executor.getMetrics();
        executor.deleteMetrics();

    }
}
