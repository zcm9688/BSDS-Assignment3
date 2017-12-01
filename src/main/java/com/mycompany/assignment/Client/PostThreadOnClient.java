/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.assignment.Client;

import java.util.concurrent.Callable;
import javax.ws.rs.ProcessingException;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;

/**
 *
 * @author chuzhan
 */
public class PostThreadOnClient implements Callable<String>{
    Client client;
    String ip;
    RFIDLiftData data;
    Metrics metrics;   
    
    public PostThreadOnClient(Client client, String ip, RFIDLiftData data, Metrics metrics){
        this.client = client;
        this.ip = ip;
        this.data = data;
        this.metrics = metrics;
    }
    
    public String call(){
        WebTarget resource = client.target(ip).path("myresource/loadData");
        Response response = null;
        long startTime = System.currentTimeMillis();
        try{                                                                                                                             
            response = resource.request(javax.ws.rs.core.MediaType.TEXT_PLAIN).post(Entity.entity(data,javax.ws.rs.core.MediaType.APPLICATION_JSON));
            System.out.println(response.readEntity(String.class));
            response.close();
            long endTime = System.currentTimeMillis();
            metrics.threadLatencyForPlotAdd(startTime, endTime - startTime);
        } catch(ProcessingException e){
            e.printStackTrace();
        }
        return "Posting Data";
    }  
}
