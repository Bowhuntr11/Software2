/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package software2;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.ResourceBundle;
import java.util.TimeZone;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author SFFPC
 */
public class EditAppointmentController implements Initializable {

    @FXML
    private TextField title;
    @FXML
    private TextField description;
    @FXML
    private TextField location;
    @FXML
    private TextField urlText;
    @FXML
    private ComboBox customers;
    @FXML
    private Button saveBtn;
    @FXML
    private Button cancelBtn;
    @FXML
    private ComboBox startMin;
    @FXML
    private ComboBox startHour;
    @FXML
    private ComboBox startAMPM;
    @FXML
    private ComboBox endMin;
    @FXML
    private ComboBox endHour;
    @FXML
    private ComboBox endAMPM;
    @FXML
    private DatePicker datePicker;
    
    
    private String selectedCustomer;
    private Integer selectedCustomerId;
    private Timestamp appointmentStarts;
    private Timestamp appointmentEnds; 
            
    private final String[] hours = {"1","2","3","4","5","6","7","8",
                                        "9","10","11","12"};
    private final String[] mins = {"00","05","10","15","20","25","30",
                                        "35","40","45","50","55"};
    private final String[] ampm = {"AM","PM"};

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        customers.setValue("Customer");
        setData();
        try {
            getCustomerNames();
        } catch (SQLException ex) {
            Logger.getLogger(EditAppointmentController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        saveBtn.setOnAction((ActionEvent e) -> {
            if ("Customer".equalsIgnoreCase(customers.getValue().toString()) || title.getLength() == 0 || description.getLength() == 0 
                    || location.getLength() == 0 || urlText.getLength() == 0 || startHour.getValue().toString() == null || startMin.getValue().toString() == null 
                    || endHour.getValue() == null || endMin.getValue() == null || startAMPM.getValue() == null || endAMPM.getValue() == null) {
                System.out.println("A field isn't filled out");
                Alert error = new Alert(Alert.AlertType.WARNING);
                error.setTitle("Error");
                error.setContentText("Make sure all fields are filled out.");
                error.showAndWait();
            } else {
            selectedCustomer = customers.getValue().toString();
            String selectedDate = datePicker.getValue().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            String startTime = startHour.getValue().toString() + ":" + startMin.getValue().toString();
            String endTime = endHour.getValue().toString() + ":" + endMin.getValue().toString();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd KK:mm a");
            TimeZone tz = TimeZone.getDefault();
            sdf.setTimeZone(tz);
                try {
                    Date appointmentStart = sdf.parse(selectedDate + " " + startTime + " " + startAMPM.getValue().toString());
                    Date appointmentEnd = sdf.parse(selectedDate + " " + endTime + " " + endAMPM.getValue().toString());
                    if(appointmentStart.after(appointmentEnd)){
                                System.out.println("End Time is before Start Time");
                                Alert error = new Alert(Alert.AlertType.WARNING);
                                error.setTitle("Error");
                                error.setContentText("End Time is before Start Time");
                                error.showAndWait();
                    } else {
                        try {
                            appointmentStarts = new Timestamp(appointmentStart.getTime());
                            appointmentEnds = new Timestamp(appointmentEnd.getTime());
                            setAppointment();
                        } catch (SQLException ex) {
                            Logger.getLogger(EditAppointmentController.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                } catch (ParseException ex) {
                    Logger.getLogger(EditAppointmentController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
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
    
    public void setData(){
        startHour.setItems(FXCollections.observableArrayList(hours));
        startMin.setItems(FXCollections.observableArrayList(mins));
        endHour.setItems(FXCollections.observableArrayList(hours));
        endMin.setItems(FXCollections.observableArrayList(mins));
        startAMPM.setItems(FXCollections.observableArrayList(ampm));
        endAMPM.setItems(FXCollections.observableArrayList(ampm));
    }
    
    public void getCustomerNames() throws SQLException {
        ResultSet rs; 
        Statement st = dbConnection.dbConnect().createStatement();
        String recordQuery = ("Select * from customer");
        rs = st.executeQuery(recordQuery);
        while(rs.next()){
            customers.getItems().add(rs.getString("customerName"));
        }
    }
    
    public void setAppointment() throws SQLException {
        ResultSet rs;
        Statement st = dbConnection.dbConnect().createStatement();
        Timestamp date = new java.sql.Timestamp(new java.util.Date().getTime());
        
        String getCustomerId="SELECT customerId FROM customer WHERE customerName = '" + selectedCustomer + "';";
        rs = st.executeQuery(getCustomerId);
        rs.next();
        selectedCustomerId = rs.getInt(1);
        
        // Adding to customer table
        String query="INSERT INTO appointment(customerId, title, description, location, url, start, end, createDate, createdBy, lastUpdateBy) VALUES('" 
                + selectedCustomerId + "', '"
                + title.getText() +  "', '"
                + description.getText() +  "', '"
                + location.getText() +  "', '"
                + urlText.getText() +  "', '"
                + appointmentStarts +  "', '"
                + appointmentEnds +  "', '"
                + date +  "', '"
                + LoginPageController.getUser() +  "', '"
                + LoginPageController.getUser() + "')";
        st.executeUpdate(query);
        
    }
    
    
}

