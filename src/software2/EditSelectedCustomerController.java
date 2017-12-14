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
import java.sql.Timestamp;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author SFFPC
 */
public class EditSelectedCustomerController implements Initializable {

    @FXML
    private TextField customername;
    @FXML
    private TextField address1;
    @FXML
    private TextField address2;
    @FXML
    private TextField city;
    @FXML
    private TextField country;
    @FXML
    private TextField postalcode;
    @FXML
    private TextField phone;
    @FXML
    private Button saveBtn;
    @FXML
    private Button cancelBtn;

    private Integer idToEdit2;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        idToEdit2 = EditCustomerController.idToEdit;
        try {
            dbconnect();
        } catch (ClassNotFoundException | SQLException | IOException | InterruptedException ex) {
            Logger.getLogger(EditSelectedCustomerController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        saveBtn.setOnAction((ActionEvent e) -> {
            try {
                if(customername.getLength() == 0 || address1.getLength() == 0 || address2.getLength() == 0 || city.getLength() == 0 || country.getLength() == 0
                        || postalcode.getLength() == 0 || phone.getLength() == 0) {
                            System.out.println("Fields can't be blank");
                            Alert error = new Alert(Alert.AlertType.WARNING);
                            error.setTitle("Error");
                            error.setContentText("Did you fill out all the fields?");
                            error.showAndWait();
                }
                else {
                dbupdate();
                System.out.println("Customer Updated");
                Parent window1;
                try {
                    window1 = FXMLLoader.load(getClass().getResource("EditCustomer.fxml"));
                    Stage mainStage;
                    mainStage = Main.parentWindow;
                    mainStage.getScene().setRoot(window1);
                    mainStage.sizeToScene();
                } catch (IOException ex) {
                    Logger.getLogger(CalendarViewController.class.getName()).log(Level.SEVERE, null, ex);
                }
                        
                }
            } catch (ClassNotFoundException | SQLException | IOException | InterruptedException ex) {
                Logger.getLogger(NewCustomerController.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
        
        cancelBtn.setOnAction((ActionEvent e) -> {
            Parent window1;
            try {
                window1 = FXMLLoader.load(getClass().getResource("EditCustomer.fxml"));
                Stage mainStage;
                mainStage = Main.parentWindow;
                mainStage.getScene().setRoot(window1);
                mainStage.sizeToScene();
            } catch (IOException ex) {
                Logger.getLogger(CalendarViewController.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
    }    
    
    public void dbconnect() throws ClassNotFoundException, SQLException, IOException, InterruptedException {
                    Connection connection = null;
                    Statement st = null;
                    String driver = "com.mysql.jdbc.Driver";
                    String db = "U03lvi";
                    String url = "jdbc:mysql://52.206.157.109/" + db;
                    String user = "U03lvi";
                    String pass = "53688016219";
                    ResultSet rs;
                        try {
                            Class.forName(driver);
                            connection = DriverManager.getConnection(url,user,pass);
                            System.out.println("Connected to database : " + db);
                            st=connection.createStatement();
                            String recordQuery = ("Select * from customer" +
                                " Inner Join address ON customer.addressId = address.addressId" +
                                " Inner Join city ON address.cityId = city.cityId" +
                                " Inner Join country ON city.countryId = country.countryId" +
                                " WHERE customerId = " + idToEdit2 + ";"); 
                            rs = st.executeQuery(recordQuery);
                            while(rs.next()){                
                                Integer id = rs.getInt("customerId");
                                customername.setText(rs.getString("customerName"));
                                address1.setText(rs.getString("address"));
                                address2.setText(rs.getString("address2"));
                                city.setText(rs.getString("city"));
                                country.setText(rs.getString("country"));
                                postalcode.setText(rs.getString("postalCode"));
                                phone.setText(rs.getString("phone"));
                                System.out.println(id +","+ customername +","+ address1 +","+ address2 +","+ city +","+ country +","+ postalcode +","+ phone + " added.");
                            }            
                        }catch (SQLException e) {
                            System.out.println("SQLException: "+e.getMessage());
                            System.out.println("SQLState: "+e.getSQLState());
                            System.out.println("VendorError: "+e.getErrorCode());
                        }
    
    }
    
    
    public void dbupdate() throws ClassNotFoundException, SQLException, IOException, InterruptedException {
                    Connection connection = null;
                    Statement statement = null;
                    String driver = "com.mysql.jdbc.Driver";
                    String db = "U03lvi";
                    String url = "jdbc:mysql://52.206.157.109/" + db;
                    String user = "U03lvi";
                    String pass = "53688016219";
                    Timestamp date = new java.sql.Timestamp(new java.util.Date().getTime());
                    Statement st;
                        try {
                            Class.forName(driver);
                            connection = DriverManager.getConnection(url,user,pass);
                            System.out.println("Connected to database : " + db);
                            st=connection.createStatement();
                            
                            String recordUpdate = ("UPDATE customer" +
                                    " Inner Join address ON customer.addressId = address.addressId" +
                                    " Inner Join city ON address.cityId = city.cityId" +
                                    " Inner Join country ON city.countryId = country.countryId" +
                                    " SET customerName = '" + customername.getText() + "'" +
                                    ", address = '" + address1.getText() + "'" +
                                    ", address2 = '" + address2.getText() + "'" +
                                    ", city = '" + city.getText() + "'" +
                                    ", country = '" + country.getText() + "'" +
                                    ", postalCode = '" + postalcode.getText() + "'" +
                                    ", customer.lastUpdateBy = '" + LoginPageController.getUser() + "'" +
                                    ", customer.lastUpdate = '" + date + "'" +
                                    ", address.lastUpdateBy = '" + LoginPageController.getUser() + "'" +
                                    ", address.lastUpdate = '" + date + "'" +
                                    ", city.lastUpdateBy = '" + LoginPageController.getUser() + "'" +
                                    ", city.lastUpdate = '" + date + "'" +
                                    ", country.lastUpdateBy = '" + LoginPageController.getUser() + "'" +
                                    ", country.lastUpdate = '" + date + "'" +
                                    " WHERE customerID = " + idToEdit2 + ";");
                            st.executeUpdate(recordUpdate);
                            
                            
                        } catch (SQLException e) {
                            System.out.println("SQLException: "+e.getMessage());
                            System.out.println("SQLState: "+e.getSQLState());
                            System.out.println("VendorError: "+e.getErrorCode());
                        }
    }
}
