/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.assignment.Client;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import javax.ws.rs.ProcessingException;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;
import com.mycompany.assignment.Server.SkiDataDao;
import com.mycompany.assignment.Server.MetricsOnServer;

/**
 *
 * @author chuzhan
 */
public class ThreadPoolExecutor {
    private Client client;
    private int POSTAMOUNT;
    private int VERTICALAMOUNT;
//    private String IPADDRESS = "http://localhost:8080/assignment-two/webapi";
//    private String IPADDRESS = "http://ec2-52-39-38-97.us-west-2.compute.amazonaws.com:8080/assignment-two/webapi";
//    private String IPADDRESS = "http://ec2-52-25-85-235.us-west-2.compute.amazonaws.com:8080/assignment-two/webapi";
//    private String IPADDRESS = "http://ec2-52-27-151-5.us-west-2.compute.amazonaws.com:8080/assignment-two/webapi"
    private String IPADDRESS = "MyLoadBalancer-869608936.us-west-2.elb.amazonaws.com:8080/assignment-two/webapi";
    private List<RFIDLiftData> data = LoadData.loadResortData();
    public Long[] postLatencies;
    public int postTime;
    public List<MetricsForPlot> verticalLatencies;
    public int verticalTime;
    public static int totalPost;
    public static int totalVertical;
    private int dayNum = 1;
    
    public ThreadPoolExecutor(int postAmount, int verticalAmount){
        this.POSTAMOUNT = postAmount;
        this.VERTICALAMOUNT = verticalAmount;
        this.client = javax.ws.rs.client.ClientBuilder.newClient();
        this.postLatencies = new Long[postAmount];
        this.verticalLatencies = new ArrayList<>();
    }
    
    public PersonalVertical getPersonalVertical(int skierID, int dayNum){
        String path = "myresource/getvertical/" + skierID + "And" + dayNum;
        WebTarget resource = client.target(IPADDRESS).path(path);
        Response response = null;
        PersonalVertical personalVertical = null;
        try{
            long getStart = System.currentTimeMillis();
            response = resource.request().get();
            personalVertical = response.readEntity(PersonalVertical.class);
            response.close();
            long getEnd = System.currentTimeMillis();
            verticalLatenctAdd(getStart, (getEnd - getStart));
            return personalVertical;
        } catch(ProcessingException e){
            e.printStackTrace();
        }
        return personalVertical;
    }
    
    public void insertAllVerticals(){
        List<InsertVerticalThreadOnClient> insertThreads = new ArrayList<>();
        List<PersonalVertical> allVerticals = new ArrayList<>();
        try{
            allVerticals = SkiDataDao.getDao().calculateVerticals();
        }catch(SQLException e){
            e.printStackTrace();
        }
        Metrics metrics = new Metrics(); 
        double startTime = System.currentTimeMillis()/1000.0;
        for(int i=0; i<allVerticals.size(); i++){
            insertThreads.add(new InsertVerticalThreadOnClient(client, IPADDRESS, allVerticals.get(i), metrics));
        }
        ExecutorService insertPool = Executors.newFixedThreadPool(400);
        try{
            insertPool.invokeAll(insertThreads);
            insertPool.shutdown();
        }catch (InterruptedException e) {
            e.printStackTrace();
        }
        
        while(true){
            if(insertPool.isTerminated()){
                double endTime = System.currentTimeMillis()/1000.0;
                List<MetricsForPlot> verticalPoints = metrics.getLatenciesForPlot();
                int insertTime = verticalPoints.size();
                Long[] latencies = new Long[insertTime];
                long totalLatency = 0;
//                PlotDrawer.createDemoPanel(verticalPoints, "InsertVerticalIndividually");
                for(int i=0; i<insertTime; i++){
                    latencies[i] = verticalPoints.get(i).getLatency();
                    totalLatency += latencies[i];
                }
                System.out.println("Test Wall Time: " + (endTime - startTime));
                System.out.println("Total number of skier figure be inserted  " + 10000);
                Arrays.sort(latencies);
                System.out.println("Median of all post letencies:  " + latencies[insertTime/2]);
                System.out.println("Mean of all post letencies:  " + totalLatency/insertTime);
                int nfprecentile = (int) (insertTime*0.95);
                int nnprecentile = (int) (insertTime*0.99);
                System.out.println("95 percentile latency is:  " + latencies[nfprecentile]);
                System.out.println("99 percentile latency is:  " + latencies[nnprecentile]);
                System.out.println("Throughput is   " + insertTime/(endTime - startTime));
                System.out.println("Finish Insert Verticals");             
                break;
            }
        }
        
    }
    
    public void deleteMetrics(){
        WebTarget resource = client.target(IPADDRESS).path("myresource/clearTable");
        Response response = null;
        try{
            response = resource.request(javax.ws.rs.core.MediaType.TEXT_PLAIN).post(Entity.entity(data,javax.ws.rs.core.MediaType.APPLICATION_JSON));
            System.out.println(response.readEntity(String.class));
            response.close();
        }catch(ProcessingException e){
            e.printStackTrace();
        }
    }
    
    public void getMetrics(){
        MetricsOnServer metricsOnServer = new MetricsOnServer();
        WebTarget resource = client.target(IPADDRESS).path("myresource/getResponseTimeMetrics");
        Response response = null;
        try{
            response = resource.request().get();
            metricsOnServer = response.readEntity(MetricsOnServer.class);
            response.close();
        } catch(ProcessingException e){
            e.printStackTrace();
        }
        System.out.println("Total number of success response:  " + metricsOnServer.getRequestAmount());
        System.out.println("Median of all latencies:  " + metricsOnServer.getMedian());
        System.out.println("Mean of all letencies:   " + metricsOnServer.getMean());
        System.out.println("95 percentile latency is:  " + metricsOnServer.getNfPercentile());
        System.out.println("99 percentile latency is:  " + metricsOnServer.getNnPercentile());
        System.out.println("Total number of failed response:  " + metricsOnServer.getFailedRequestAmount());
    }
    
    public void postResorts(){
        List<PostThreadOnClient> postThreads = new ArrayList<>();
        Metrics metrics = new Metrics(); 
        double startTime = System.currentTimeMillis()/1000.0;
        for(int i=180000; i<200000; i++){
            data.get(i).setResortID(i);
            postThreads.add(new PostThreadOnClient(client, IPADDRESS, data.get(i), metrics));
        }
        ExecutorService postPool = Executors.newFixedThreadPool(400);
        try {
            postPool.invokeAll(postThreads);
            postPool.shutdown();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        while(true){
            if(postPool.isTerminated()){
                double endTime = System.currentTimeMillis()/1000.0;
                System.out.println("Test Wall Time: " + (endTime - startTime));
                List<MetricsForPlot> verticalPoints = metrics.getLatenciesForPlot();
                int postTime = verticalPoints.size();
                System.out.println("Total number of resorts be posted  " + verticalPoints.size());
                List<Long> latencies = new ArrayList<>();
                long totalLatency = 0;
//                PlotDrawer.createDemoPanel(verticalPoints, "PostResortsForDay999Individually");
                for(int i=0; i<postTime; i++){
                    latencies.add(verticalPoints.get(i).getLatency());
                    totalLatency += latencies.get(i);
                }
                Collections.sort(latencies);
                System.out.println("Median of all post letencies:  " + latencies.get(postTime/2));
                System.out.println("Mean of all post letencies:  " + totalLatency/postTime);
                int nfprecentile = (int) (postTime*0.95);
                int nnprecentile = (int) (postTime*0.99);
                System.out.println("95 percentile latency is:  " + latencies.get(nfprecentile));
                System.out.println("99 percentile latency is:  " + latencies.get(nnprecentile)); 
                System.out.println("Throughput is   " + verticalPoints.size()/(endTime - startTime));
                System.out.println("Finish Post Resorts");
                break;
            }
        }
    }

    
    public void postData() {
        ExecutorService fixedThreadPool = Executors.newFixedThreadPool(POSTAMOUNT);
        double startTime = System.currentTimeMillis()/1000.0;
        for(int i=0; i<POSTAMOUNT; i++){
            data.get(i).setResortID(i);
            final RFIDLiftData curData = data.get(i);
            Future<String> future = fixedThreadPool.submit(new Callable<String>(){
                @Override
                public String call() throws Exception{
                    WebTarget resource = client.target(IPADDRESS).path("myresource/loadData");
                    Response response = null;
                    long postStart = System.currentTimeMillis();
                    try{                                                                                                                             
                        response = resource.request(javax.ws.rs.core.MediaType.TEXT_PLAIN).post(Entity.entity(curData,javax.ws.rs.core.MediaType.APPLICATION_JSON));
                        System.out.println(response.readEntity(String.class));
                        response.close();
                        totalPostAdd();
                    } catch(ProcessingException e){
                        e.printStackTrace();
                    }
                    long postEnd = System.currentTimeMillis();
                    postLatenctAdd(postEnd - postStart);
                    return ("Insert" + curData.getResortID());
                }
            });
        }        
        fixedThreadPool.shutdown();
        
        while(true){
            if(fixedThreadPool.isTerminated()){
                double endTime = System.currentTimeMillis()/1000.0;
                System.out.println("Total number of resorts be posted   " + totalPost);
                System.out.println("Test Wall Time: " + (endTime - startTime));
                Arrays.sort(postLatencies);
                System.out.println("Median of all post letencies: " + postLatencies[postTime/2] + "   mileseconds");
                long totalLatencyTime = 0;
                for(long lantencyy : postLatencies){
                    totalLatencyTime += lantencyy;
                }
                System.out.println("Mean of all letencies:  " + totalLatencyTime/(postLatencies.length) + "   mileseconds");
                int nfprecentile = (int) (postTime*0.95);
                int nnprecentile = (int) (postTime*0.99);
                System.out.println("95 percentile latency is:  " + postLatencies[nfprecentile]);
                System.out.println("99 percentile latency is:  " + postLatencies[nnprecentile]);
                System.out.println("Finish Post Resorts");             
                break;
            }
        }
    }
    
    private synchronized void postLatenctAdd(long latencyTime) {
        postLatencies[postTime++] = latencyTime;
    }
    
    private synchronized void verticalLatenctAdd(long startTime, long latencyTime) {
        verticalLatencies.add(new MetricsForPlot(startTime, latencyTime));
    }
    
    private synchronized void totalPostAdd() {
        totalPost++;
    }
    
    private synchronized void totalVerticalAdd() {
        totalVertical++;
    }
}
