/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package software2;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author SFFPC
 */
public class EditCustomerController implements Initializable {
    @FXML
    private Button editBtn; 
    @FXML
    private Button cancelBtn;
    @FXML
    private TableView<Customers> customerTable;
    @FXML
    private TableColumn idCol = new TableColumn();
    @FXML
    private TableColumn customerNameCol = new TableColumn();
    @FXML
    private TableColumn addressCol = new TableColumn();
    @FXML
    private TableColumn address2Col = new TableColumn();
    @FXML
    private TableColumn cityCol = new TableColumn();
    @FXML
    private TableColumn postalCol = new TableColumn();
    @FXML
    private TableColumn phoneCol = new TableColumn();
    
    public static Integer idToEdit;
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
            try {
                idCol.setCellValueFactory(new PropertyValueFactory<Customers, Integer>("idCol"));
                customerNameCol.setCellValueFactory(new PropertyValueFactory<Customers, String>("nameCol"));
                addressCol.setCellValueFactory(new PropertyValueFactory<Customers, String>("addressCol"));
                address2Col.setCellValueFactory(new PropertyValueFactory<Customers, String>("address2Col"));
                cityCol.setCellValueFactory(new PropertyValueFactory<Customers, String>("cityCol"));
                postalCol.setCellValueFactory(new PropertyValueFactory<Customers, String>("postalCol"));
                phoneCol.setCellValueFactory(new PropertyValueFactory<Customers, String>("phoneCol"));
                customerTable.getItems().setAll(getAllcustomerInfo());
            } catch (SQLException ex) {
                Logger.getLogger(EditCustomerController.class.getName()).log(Level.SEVERE, null, ex);
            }
       
            
        editBtn.setOnAction((ActionEvent e) -> {
            Parent window;
            Customers selCustomer = customerTable.getSelectionModel().getSelectedItem();
            idToEdit = selCustomer.getIdCol();
            try {
                window = FXMLLoader.load(getClass().getResource("EditSelectedCustomer.fxml"));
                Stage mainStage;
                mainStage = Main.parentWindow;
                mainStage.getScene().setRoot(window);
                mainStage.sizeToScene();
            } catch (IOException ex) {
                Logger.getLogger(CalendarViewController.class.getName()).log(Level.SEVERE, null, ex);
            }
            System.out.println(selCustomer.getIdCol());
        });
        
        cancelBtn.setOnAction((ActionEvent e) -> {
            Parent window;
            try {
                window = FXMLLoader.load(getClass().getResource("CalendarView.fxml"));
                Stage mainStage;
                mainStage = Main.parentWindow;
                mainStage.getScene().setRoot(window);
                mainStage.sizeToScene();
            } catch (IOException ex) {
                Logger.getLogger(CalendarViewController.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
    }    
    public class Customers{
        private final SimpleIntegerProperty IdCol;
        private final SimpleStringProperty NameCol;
        private final SimpleStringProperty AddressCol;
        private final SimpleStringProperty Address2Col;
        private final SimpleStringProperty CityCol;
        private final SimpleStringProperty CountryCol;
        private final SimpleStringProperty PostalCol;
        private final SimpleStringProperty PhoneCol;
    
        public Customers(Integer id, String name, String address, String address2, String city, String country, String postal, String phone){
            this.IdCol = new SimpleIntegerProperty(id);
            this.NameCol = new SimpleStringProperty(name);
            this.AddressCol = new SimpleStringProperty(address);
            this.Address2Col = new SimpleStringProperty(address2);
            this.CityCol = new SimpleStringProperty(city);
            this.CountryCol = new SimpleStringProperty(country);
            this.PostalCol = new SimpleStringProperty(postal);
            this.PhoneCol = new SimpleStringProperty(phone);
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
    }   
public List<Customers> getAllcustomerInfo() throws SQLException{
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
                ll.add(new Customers(id, name, address, address2, city, country, postal, phone));
            }            
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(EditCustomerController.class.getName()).log(Level.SEVERE, null, ex);
        }
    return ll;        
    }
}
