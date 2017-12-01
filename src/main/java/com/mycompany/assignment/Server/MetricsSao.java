/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.assignment.Server;

import com.mysql.jdbc.Statement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author chuzhan
 */
public class MetricsSao {
    protected DriveConnectionController connectionManager;
    protected Connection connection = null;
    
    private static MetricsSao metricsSao = null;
    
    protected MetricsSao(){
        connectionManager = new DriveConnectionController();
        connection = connectionManager.getConnection();
    }
    
    public static synchronized MetricsSao getDao(){
        if(metricsSao == null){
            return new MetricsSao();
        }
        return metricsSao;
    }
    
    public void insertResponseTime(long responseTime) throws SQLException{
        PreparedStatement insertStatement = null;
        ResultSet resultSet = null;
        try{
            //connection = connectionManager.getConnection();
            String sql = "INSERT INTO ResponseTime(responseTime) VALUES (?)";
            insertStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            insertStatement.setLong(1, responseTime);
            insertStatement.executeUpdate();
        } catch(SQLException e){
            e.printStackTrace();
            throw e;
        }
        finally{
            connectionManager.releaseDB(resultSet, insertStatement, connection);
        }
    }
    
    public MetricsOnServer getMetrics(int sum) throws SQLException{
        MetricsOnServer metricsOnServer = new MetricsOnServer();
        int count = 0;
        List<Integer> per = new ArrayList<>();
        try{
            count = getAmount();
            per = getPercentile(count);
            metricsOnServer.setMean(getMean());
            metricsOnServer.setRequestAmount(count);
            metricsOnServer.setFailedRequestAmount(sum - count);
            metricsOnServer.setMedian(per.get(0));
            metricsOnServer.setNfPercentile(per.get(1));
            metricsOnServer.setNnPercentile(per.get(2));
        }catch(SQLException e){
            e.printStackTrace();
            throw e;
        }
        return metricsOnServer;
    }
    
    public List<Integer> getPercentile(int count) throws SQLException{
        int cur = 0;
        int medianPosition = (int) Math.floor(count * 0.5);
        int nfPosition = (int) Math.floor(count * 0.95);
        int nnPosition = (int) Math.floor(count * 0.99);
        List<Integer> result = new ArrayList<>(); 
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try{
            String sql = "SELECT responseTime FROM ResponseTime ORDER BY responseTime";
            preparedStatement = connection.prepareStatement(sql);
            resultSet = preparedStatement.executeQuery();
             while(resultSet.next()){
                 cur++;
                 if(cur == medianPosition)
                     result.add(resultSet.getInt(1));
                 if(cur == nfPosition)
                     result.add(resultSet.getInt(1));
                 if(cur == nnPosition)
                     result.add(resultSet.getInt(1));
            }
        } catch(SQLException e){
            e.printStackTrace();
            throw e;
        }
        finally{
            connectionManager.releaseDB(resultSet, preparedStatement, connection);
        }
        return result;
    }
    
    public int getAmount() throws SQLException{
        int amount = 0;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try{
            String sql = "SELECT COUNT(*) FROM ResponseTime";
            preparedStatement = connection.prepareStatement(sql);
            resultSet = preparedStatement.executeQuery();
            while(resultSet.next()){
                 amount = resultSet.getInt(1);
            }
        } catch(SQLException e){
            e.printStackTrace();
            throw e;
        }
        finally{
            preparedStatement.close();
        }
        return amount;
    }
    
    public long getMean() throws SQLException{
        long average = 0;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try{
            String sql = "SELECT AVG(responseTime) FROM ResponseTime";
            preparedStatement = connection.prepareStatement(sql);
            resultSet = preparedStatement.executeQuery();
            while(resultSet.next()){
                 average = resultSet.getLong(1);
            }
        } catch(SQLException e){
            e.printStackTrace();
            throw e;
        }
        finally{
            preparedStatement.close();
        }
        return average;
    }
    
    public String truncateTable() throws SQLException{
        //Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try{
            //connection = connectionManager.getConnection();
            String sql = "TRUNCATE TABLE ResponseTime";
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.executeUpdate();
            return "Clear All ResponseTimes";
        } catch(SQLException e){
            e.printStackTrace();
            throw e;
        }
        finally{
            connectionManager.releaseDB(resultSet, preparedStatement, connection);
        }
    }
}
