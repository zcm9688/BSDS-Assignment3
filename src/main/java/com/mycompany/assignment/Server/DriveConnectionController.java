/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.assignment.Server;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author chuzhan
 */
public class DriveConnectionController {
        private static final String driver_class="com.mysql.jdbc.Driver";
    //private static final String url="jdbc:mysql://localhost:3306/RFIDLiftData";
    private static final String url="jdbc:mysql://rfidliftdata.c1bd36ibdr18.us-west-2.rds.amazonaws.com:3306/RFIDLiftData";
    //private static final String url="rfidliftdata.c1bd36ibdr18.us-west-2.rds.amazonaws.com:3306/RFIDLiftData";
    
    public static Connection getConnection(){
        Connection conn=null;
        try {
            try{
            Class.forName(driver_class);}
            catch(ClassNotFoundException e){
                e.printStackTrace();
            }
            conn = DriverManager.getConnection(url, "root", "Zxs936300");
            System.out.println("连接ok");
        } catch(SQLException e){
            e.printStackTrace();
          } 
        return conn;
    }
    
    public static void closeConnection(Connection conn){
        try {
            if(conn!=null&&!(conn.isClosed())){
                conn.close();               
             }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public static void releaseDB(ResultSet resultSet, PreparedStatement statement,
            Connection connection) {

        if (resultSet != null) {
            try {
                resultSet.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        if (statement != null) {
            try {
                statement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

    }
}
