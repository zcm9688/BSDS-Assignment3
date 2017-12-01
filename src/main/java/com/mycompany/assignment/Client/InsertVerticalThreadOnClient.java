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
public class InsertVerticalThreadOnClient implements Callable<String>{
    Client client;
    String ip;
    PersonalVertical vertical;
    Metrics metrics; 
    
    public InsertVerticalThreadOnClient(Client client, String ip, PersonalVertical vertical, Metrics metrics){
        this.client = client;
        this.ip = ip;
        this.vertical = vertical;
        this.metrics = metrics;
    }
    
    public String call(){
        WebTarget resource = client.target(ip).path("myresource/insertVertical");
        Response response = null;
        long verticalStart = System.currentTimeMillis();
        try{
            response = resource.request(javax.ws.rs.core.MediaType.TEXT_PLAIN).post(Entity.entity(vertical,javax.ws.rs.core.MediaType.APPLICATION_JSON));
            System.out.println(response.readEntity(String.class));
            response.close();
            long verticalEnd = System.currentTimeMillis();
            metrics.threadLatencyForPlotAdd(verticalStart, verticalEnd - verticalStart);
        }catch(ProcessingException e){
            e.printStackTrace();
        }
        return "Inserting Data";
    }
}
