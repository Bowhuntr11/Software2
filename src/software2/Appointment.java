/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package software2;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.TimeZone;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

/**
 *
 * @author Christopher Sherrill
 */
public class Appointment {
    private final SimpleIntegerProperty apptIdCol;
    private final SimpleStringProperty dateCol;
    private final SimpleStringProperty startCol;
    private final SimpleStringProperty endCol;
    private final SimpleStringProperty apptTypeCol;
    private final SimpleStringProperty customerCol;
    
    public Appointment(Integer id, String date, String start, String end, String apptType, String customer){
        this.apptIdCol = new SimpleIntegerProperty(id);
        this.dateCol = new SimpleStringProperty(date);
        this.startCol = new SimpleStringProperty(start);
        this.endCol = new SimpleStringProperty(end);
        this.apptTypeCol = new SimpleStringProperty(apptType);
        this.customerCol = new SimpleStringProperty(customer);
    }  
    
    public Integer getApptIdCol(){
        return apptIdCol.get();
    }
    public String getDateCol(){
        return dateCol.get();
    }
    public String getStartCol(){
        return startCol.get();
    }
    public String getEndCol(){
        return endCol.get();
    }
    public String getApptTypeCol(){
        return apptTypeCol.get();
    }
    public String getCustomerCol(){
      return customerCol.get();
    }     

public static List<Appointment> getAppointmentInfo(String timeZ, String WeekMonth) throws SQLException, ParseException{
    ResultSet rs;
    Statement st = dbConnection.dbConnect().createStatement();
    List ll = new LinkedList();
    Date now = new Date();
    Calendar cal = Calendar.getInstance(); 
    cal.setTime(now);
    Calendar cal2 = Calendar.getInstance(); 
    
    if ("Month".equals(WeekMonth)) {
        cal2.setTime(now);
        cal2.add(Calendar.MONTH, 1);
    } else {
        cal2.setTime(now);
        cal2.add(Calendar.DAY_OF_YEAR, 7);
    }
    
    
    String recordQuery = ("Select * from appointment" +
            " Inner Join customer ON appointment.customerId = customer.customerId" +
            " WHERE appointment.createdBy = " + "\"" + LoginPageController.getUser() + "\"" + ";");
    rs = st.executeQuery(recordQuery);
    while(rs.next()){
        Integer id = rs.getInt("appointmentId");
        long startTime = rs.getTimestamp("start").getTime();
        long endTime = rs.getTimestamp("end").getTime();
        Date startDate;
        Date endDate;
        
        if ("GMT".equals(timeZ)) {
            TimeZone tzGMT = TimeZone.getTimeZone("GMT");
            startDate = new Date(startTime + tzGMT.getOffset(startTime));
            endDate = new Date(endTime + tzGMT.getOffset(endTime));
        } else {
            startDate = new Date(startTime + TimeZone.getDefault().getOffset(startTime));
            endDate = new Date(endTime + TimeZone.getDefault().getOffset(endTime));
        }
        
        
        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a");
        String dateString = new SimpleDateFormat("MM/dd/yyyy").format(startDate);
        String startString = sdf.format(startDate);
        String endString = sdf.format(endDate);
        String apptType = rs.getString("title");
        String customer = rs.getString("customerName");
        
        if (startDate.after(now) && startDate.before(cal2.getTime())) {
            ll.add(new Appointment(id, dateString, startString, endString, apptType, customer));
        }
    }
    return ll;        
    }
    
}