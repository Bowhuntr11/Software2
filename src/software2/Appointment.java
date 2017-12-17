/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package software2;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

/**
 *
 * @author Christopher Sherrill
 */
public class Appointment {
    private int appointmentId;
    private int customerId;
    private String title;
    private String description;
    private String location;
    private String contact;
    private String url;
    private Timestamp start;
    private Timestamp end;
    private Timestamp createDate;
    private String createdBy;
    private Timestamp lastUpdate;
    private String lastUpdateBy;

    private final SimpleDateFormat dateCol;
    private final SimpleStringProperty NameCol;
    private final SimpleStringProperty AddressCol;
    private final SimpleStringProperty Address2Col;
    private final SimpleStringProperty CityCol;
    private final SimpleStringProperty CountryCol;
    private final SimpleStringProperty PostalCol;
    private final SimpleStringProperty PhoneCol;

    public Appointment(Timestamp date, Timestamp start, Timestamp end, String title, String customer){
        this.dateCol = new SimpleDateFormat(date);
        this.startTimeCol = new SimpleStringProperty(start);
        this.endTimeCol = new SimpleStringProperty(end);
        this.apptTypeCol = new SimpleStringProperty(title);
        this.customerCol = new SimpleStringProperty(customer);
      }  
      public Integer getIdCol(){
          return IdCol.get();
      }
      public void setIdCol(Integer id){
          IdCol.set(id);      
      }
      public String getNameCol(){
          return NameCol.get();
      }
      public void setNameCol(String name){
          NameCol.set(name);
      }
      public String getAddressCol(){
          return AddressCol.get();
      }
      public void setAddressCol(String address){
          AddressCol.set(address);
      }
      public String getAddress2Col(){
          return Address2Col.get();
      }
      public void setAddress2Col(String address2){
          Address2Col.set(address2);
      }  
      public String getCityCol(){
          return CityCol.get();
      }
      public void setCityCol(String city){
          CityCol.set(city);
      }    
      public String getCountryCol(){
          return CountryCol.get();
      }
      public void setCountyCol(String country){
          CountryCol.set(country);
      }   
      public String getPostalCol(){
          return PostalCol.get();
      }
      public void setPostalCol(String postal){
          PostalCol.set(postal);
      }   
      public String getPhoneCol(){
          return PhoneCol.get();
      }
      public void setPhoneCol(String phone){
          PhoneCol.set(phone);
      }     

public static List<Appointment> getAllcustomerInfo() throws SQLException{
    Connection connection = null;
    String driver = "com.mysql.jdbc.Driver";
    String db = "U03lvi";
    String url = "jdbc:mysql://52.206.157.109/" + db;
    String user = "U03lvi";
    String pass = "53688016219";
    List ll = new LinkedList();
    Statement st;
    ResultSet rs;

        try {
            Class.forName(driver);
            connection = DriverManager.getConnection(url,user,pass);
            System.out.println("Connected to database : " + db);
            st = connection.createStatement();
            String recordQuery = ("Select * from customer" +
                    " Inner Join address ON customer.addressId = address.addressId" +
                    " Inner Join city ON address.cityId = city.cityId" +
                    " Inner Join country ON city.countryId = country.countryId");    
            rs = st.executeQuery(recordQuery);
            while(rs.next()){                
                Integer id = rs.getInt("customerId");
                String name = rs.getString("customerName");
                String address = rs.getString("address");
                String address2 = rs.getString("address2");
                String city = rs.getString("city");
                String country = rs.getString("country");
                String postal = rs.getString("postalCode");
                String phone = rs.getString("phone");
                ll.add(new Customer(id, name, address, address2, city, country, postal, phone));
            }            
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(EditCustomerController.class.getName()).log(Level.SEVERE, null, ex);
        }
    return ll;        
    }
    
}