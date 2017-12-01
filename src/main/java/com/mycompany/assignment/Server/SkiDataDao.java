/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.assignment.Server;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import com.mycompany.assignment.Client.PersonalVertical;
import com.mycompany.assignment.Client.RFIDLiftData;

/**
 *
 * @author chuzhan
 */
public class SkiDataDao {
    protected DriveConnectionController connectionManager;
    protected Connection connection = null;
//    protected ThreadController threadController;
    
    private static SkiDataDao skiDataDao = null;
    
    protected SkiDataDao(){
        connectionManager = new DriveConnectionController();
        connection = connectionManager.getConnection();
    }
    
    public static SkiDataDao getDao(){
        if(skiDataDao == null){
            return new SkiDataDao();
        }
        return skiDataDao;
    }
    
    public void insertVertical(PersonalVertical vertical) throws SQLException{
        //Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try{
            //connection = connectionManager.getConnection();
            String sql = "INSERT INTO Vertical(skierID, dayNum, lifts, vertical) VALUES (?,?,?,?)";
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, vertical.getSkierID());
            preparedStatement.setInt(2, vertical.getDayNum());
            preparedStatement.setInt(3, vertical.getLifts());
            preparedStatement.setInt(4, vertical.getVertical());
            preparedStatement.executeUpdate();
        } catch(SQLException e){
            e.printStackTrace();
            throw e;
        }
        finally{
            connectionManager.releaseDB(resultSet, preparedStatement, connection);
        }
    }
    
    public RFIDLiftData insertResorts (RFIDLiftData data) throws SQLException{
        //Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try{
            //connection = connectionManager.getConnection();
            String sql = "INSERT INTO Resort(resortID, dayNum, skierID, liftID, time) VALUES (?,?,?,?,?)";
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, data.getResortID());
            preparedStatement.setInt(2, data.getDayNum());
            preparedStatement.setInt(3, data.getSkierID());
            preparedStatement.setInt(4, CalculateLiftAmount.verticalCalculation(data.getLiftID()));
            preparedStatement.setInt(5, data.getTime());
            preparedStatement.executeUpdate();
            System.out.println("success");
        } catch(SQLException e){
            e.printStackTrace();
            System.out.println("failure");
            throw e;
        }
        finally{
            connectionManager.releaseDB(resultSet, preparedStatement, connection);
        }
        return data;
    }
    
    public String truncateResort() throws SQLException{
        //Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try{
            //connection = connectionManager.getConnection();
            String sql = "TRUNCATE TABLE Resort";
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.executeUpdate();
            return "Clear All Resorts";
        } catch(SQLException e){
            e.printStackTrace();
            throw e;
        }
        finally{
            connectionManager.releaseDB(resultSet, preparedStatement, connection);
        }
    }
    
    public String deleteResort(int resortID) throws SQLException{
        //Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try{
            //connection = connectionManager.getConnection();
            String sql = "DELETE FROM Resort WHERE ResortID=?";
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, resortID);
            preparedStatement.executeUpdate();
            return ("delete resortID =" + resortID);
        } catch(SQLException e){
            e.printStackTrace();
            throw e;
        }
        finally{
            connectionManager.releaseDB(resultSet, preparedStatement, connection);
        }
    }
    
    public PersonalVertical getPersonalVerticals(int skierID, int dayNum) throws SQLException{
        //Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        PersonalVertical matchVertical = new PersonalVertical(0,0,0,0);
        try{
            //connection = connectionManager.getConnection();
            String sql = "SELECT * FROM Vertical WHERE dayNum=? AND skierID=? ";
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1,dayNum);
            preparedStatement.setInt(2,skierID);
            resultSet = preparedStatement.executeQuery();
            while(resultSet.next()){
                int lifts = resultSet.getInt("lifts");
                int vertical = resultSet.getInt("vertical");
                matchVertical = new PersonalVertical(skierID, dayNum, lifts, vertical);
                //System.out.print("lifts is:  " + matchVertical.getLifts() + "   vertical is:    " + matchVertical.getVertical());
            }
        } catch(SQLException e){
            e.printStackTrace();
            throw e;
        }
        finally{
            connectionManager.releaseDB(resultSet, preparedStatement, connection);
        }
        if(matchVertical.getLifts() != 0)
            return matchVertical;
        else
            return null;
    }
    
    public PersonalVertical selectPersonalVertical(int skierID, int dayNum) throws SQLException{
        //Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        int lifts = 0, vertical = 0;
        try{
            //connection = connectionManager.getConnection();
            String sql = "SELECT * FROM Resort WHERE dayNum=? AND skierID=? ";
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1,dayNum);
            preparedStatement.setInt(2,skierID);
            resultSet = preparedStatement.executeQuery();
            while(resultSet.next()){
                vertical += resultSet.getInt("liftID");
                lifts++;
            }
        } catch(SQLException e){
            e.printStackTrace();
            throw e;
        }
        finally{
            connectionManager.releaseDB(resultSet, preparedStatement, connection);
        }
        PersonalVertical result = new PersonalVertical(skierID, dayNum, lifts, vertical);
        if(result.getLifts() != 0){
            System.out.println("Result : SkierID is:  " + result.getSkierID() + "     dayNum is:   " + result.getDayNum() + "     lifts amount is :  " + result.getLifts() + "      vertical is:  " + result.getVertical());
            return result;
//            threadController.insertVerticalThreadAdd(result);
        }
        return null;
    }
    
    public void insertVerticals(List<PersonalVertical> verticals) throws SQLException{
        //Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try{
            //connection = connectionManager.getConnection();
            for(int i=0; i<verticals.size(); i++){
            String sql = "INSERT INTO Vertical(skierID, dayNum, lifts, vertical) VALUES (?,?,?,?)";
            preparedStatement = connection.prepareStatement(sql);
            PersonalVertical curVertical = verticals.get(i);
            preparedStatement.setInt(1, curVertical.getSkierID());
            preparedStatement.setInt(2, curVertical.getDayNum());
            preparedStatement.setInt(3, curVertical.getLifts());
            preparedStatement.setInt(4, curVertical.getVertical());
            preparedStatement.executeUpdate();
            }
        }catch(SQLException e){
            e.printStackTrace();
            System.out.println("failure");
            throw e;
        }
        finally{
            connectionManager.releaseDB(resultSet, preparedStatement, connection);
        }
        return;
    }
    
    public List<PersonalVertical> calculateVerticals() throws SQLException{
        List<PersonalVertical> verticals = new ArrayList<>();
        List<Long> verticalLatencies = new ArrayList<>();
        //Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        double startTime = System.currentTimeMillis()/1000.0;
        try{
            //connection = connectionManager.getConnection();
            //String sql = "INSERT INTO Vertical(skierID, dayNum, lifts, vertical) SELECT skierID, dayNum, COUNT(*), SUM(liftID) from Resort GROUP BY skierID, dayNum";
            String sql = "SELECT COUNT(*), SUM(liftID), skierID, dayNum FROM Resort GROUP BY skierID, dayNum";
            preparedStatement = connection.prepareStatement(sql);
            resultSet = preparedStatement.executeQuery();
            while(resultSet.next()){
                int count = resultSet.getInt(1);
                int vertical = resultSet.getInt(2);
                int skierID = resultSet.getInt(3);
                int dayNum = resultSet.getInt(4);
                //System.out.println(resultSet.getInt(4));
                PersonalVertical personalVertical = new PersonalVertical(skierID, dayNum, count, vertical);
//                insertVertical(personalVertical);
                verticals.add(personalVertical);
            }
        } catch(SQLException e){
            e.printStackTrace();
            throw e;
        }
        finally{
            connectionManager.releaseDB(resultSet, preparedStatement, connection);
        }
//        double endTime = System.currentTimeMillis()/1000.0;
//        System.out.println("Test Wall Time: " + (endTime - startTime));
        return verticals;
    }
}
