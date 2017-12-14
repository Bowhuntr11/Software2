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
public class NewCustomerController implements Initializable {

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
    
    private int addressId;
    private int cityId;
    private int countryId;
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
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
                dbconnect();
                }
            } catch (ClassNotFoundException | SQLException | IOException | InterruptedException ex) {
                Logger.getLogger(NewCustomerController.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
        
        cancelBtn.setOnAction((ActionEvent e) -> {
            Parent window1;
            try {
                window1 = FXMLLoader.load(getClass().getResource("CalendarView.fxml"));
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
                    ResultSet rs;
                    Statement st = dbConnection.dbConnect().createStatement();
                    countryId = countryId + 1;
                    Timestamp date = new java.sql.Timestamp(new java.util.Date().getTime());
                            String getaddressId="SELECT addressId FROM address ORDER BY addressId DESC LIMIT 1;";
                            ResultSet rset1 = st.executeQuery(getaddressId);
                            if (!rset1.isBeforeFirst() ) {  
                                System.out.println("No data");   
                                addressId = 1;
                            } 
                            else {
                                rset1.next();
                                System.out.println(rset1.getInt(1));
                                addressId = rset1.getInt(1) + 1;
                            }
                            
                            String getcityId="SELECT cityId FROM city ORDER BY cityId DESC LIMIT 1;";
                            ResultSet rset2 = st.executeQuery(getcityId);
                            if (!rset2.isBeforeFirst() ) {    
                                System.out.println("No data"); 
                                cityId = 1;
                            } 
                            else {
                                rset2.next();
                                System.out.println(rset2.getInt(1));
                                cityId =  rset2.getInt(1) + 1;
                            }
                            
                            String getcountryId="SELECT countryId FROM country ORDER BY countryId DESC LIMIT 1;";
                            ResultSet rset3 = st.executeQuery(getcountryId);
                            if (!rset3.isBeforeFirst() ) {    
                                System.out.println("No data"); 
                                countryId = 1;
                            } 
                            else {
                                rset3.next();
                                System.out.println(rset3.getInt(1));
                                countryId = rset3.getInt(1) + 1;
                            }
                            
                            // Adding to customer table
                            String query="INSERT INTO customer(customerName, addressId, active, createDate, createdBy, lastUpdateBy) VALUES('" 
                                    + customername.getText() + "', '"
                                    + addressId +  "', '"
                                    + 1 +  "', '"
                                    + date +  "', '"
                                    + LoginPageController.getUser() +  "', '"
                                    + LoginPageController.getUser() + "')";
                            st.executeUpdate(query);
                            
                            // Adding to address table
                            String query2="INSERT INTO address(addressId, address, address2, cityId, postalCode, phone, createDate, createdBy, lastUpdate, lastUpdateBy) VALUES('"
                                    + addressId + "', '"
                                    + address1.getText() + "', '"
                                    + address2.getText() + "', '"
                                    + cityId + "', '"
                                    + postalcode.getText() + "', '"
                                    + phone.getText() + "', '"
                                    + date + "', '"
                                    + LoginPageController.getUser() + "', '"
                                    + date + "', '"
                                    + LoginPageController.getUser() + "')";
                            st.executeUpdate(query2);
                            
                            // Adding to city Table
                            String query3="INSERT INTO city(cityId, city, countryId, createDate, createdBy, lastUpdate, lastUpdateBy) VALUES('"
                                    + cityId + "', '"
                                    + city.getText() + "', '"
                                    + countryId + "', '"
                                    + date + "', '"
                                    + LoginPageController.getUser() + "', '"
                                    + date + "', '"
                                    + LoginPageController.getUser() + "')";
                            st.executeUpdate(query3);
                            
                            // Adding to country Table
                            String query4="INSERT INTO country(countryId, country, createDate, createdBy, lastUpdate, lastUpdateBy) VALUES ('"
                                    + countryId + "', '"
                                    + country.getText() + "', '"
                                    + date + "', '"
                                    + LoginPageController.getUser() + "', '"
                                    + date + "', '"
                                    + LoginPageController.getUser() + "')";
                            st.executeUpdate(query4);
                            
                            
                        }
    }
