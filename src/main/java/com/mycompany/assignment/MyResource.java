package com.mycompany.assignment;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import com.mycompany.assignment.Server.ThreadController;
import com.mycompany.assignment.Client.PersonalVertical;
import com.mycompany.assignment.Client.RFIDLiftData;
import com.mycompany.assignment.Server.SkiDataDao;
import com.mycompany.assignment.Server.MetricsController;
import com.mycompany.assignment.Server.MetricsOnServer;
import com.mycompany.assignment.Server.MetricsSao;
import java.sql.SQLException;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.PathParam;

/**
 * Root resource (exposed at "myresource" path)
 */
@Path("myresource")
public class MyResource {

    ThreadController threadController;
    
    public MyResource(){
        threadController = ThreadController.getThreadController();
    }

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String getIt() {
        return "Got it!";
    }
    
    @GET
    @Path("getvertical/{skierID}And{dayNum}")
    @Produces(MediaType.APPLICATION_JSON)
    public PersonalVertical getMyVertical(
            @PathParam("skierID") int skierID,
            @PathParam("dayNum") int dayNum) throws SQLException{  
        try{
            return SkiDataDao.getDao().selectPersonalVertical(skierID, dayNum);
        }catch(SQLException e){
            e.printStackTrace();
            throw e;
        }      
    }
    
    @GET
    @Path("getAllVerticals")
    @Produces(MediaType.APPLICATION_JSON)
    public void getTotcalVertical() throws SQLException {
        try{
            SkiDataDao.getDao().calculateVerticals();
        } catch(SQLException e){
            e.printStackTrace();
        }
    }
    
    @GET
    @Path("getResponseTimeMetrics")
    @Produces(MediaType.APPLICATION_JSON)
    public MetricsOnServer getMetrics() throws SQLException {
            return MetricsController.getThreadController().getMetrics();
    }
    
    @POST
    @Path("clearTable")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN)
    public String clearTable(){
        try{
            MetricsSao.getDao().truncateTable();
        } catch(SQLException e){
            e.printStackTrace();
        }
        return "clear table responseTime";
    }
    
    @POST
    @Path("insertVertical")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN)
    public String insertVertical(PersonalVertical personalVertical){
        threadController.insertVerticalThreadAdd(personalVertical);
        return "Inserting  " + personalVertical.getSkierID();
    }
    
    @POST
    @Path("loadData")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN)
    public String loadData(RFIDLiftData data) {
        threadController.postResortThreadAdd(data);
        return "Loading  " + data.getResortID();
    }
}
