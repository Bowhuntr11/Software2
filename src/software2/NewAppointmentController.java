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
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Optional;
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
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author SFFPC
 */
public class NewAppointmentController implements Initializable {

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
    private Date appointmentStart;
    private Date appointmentEnd;
            
    private final String[] hours = {"01","02","03","04","05","06","07","08",
                                        "09","10","11","12"};
    private final String[] mins = {"00","05","10","15","20","25","30",
                                        "35","40","45","50","55"};
    private final String[] ampm = {"AM","PM"};
    
    // The times that the business opens or closes locally
    private final LocalTime businessOpenTime = LocalTime.parse("06:00 AM", DateTimeFormatter.ofPattern("hh:mm a"));
    private final LocalTime businessCloseTime = LocalTime.parse("05:00 PM", DateTimeFormatter.ofPattern("hh:mm a"));
    
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
            Logger.getLogger(NewAppointmentController.class.getName()).log(Level.SEVERE, null, ex);
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
                return;
            } else {
            selectedCustomer = customers.getValue().toString();
            String selectedDate = datePicker.getValue().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            String startTime = startHour.getValue().toString() + ":" + startMin.getValue().toString();
            String endTime = endHour.getValue().toString() + ":" + endMin.getValue().toString();
            LocalTime startTimeParse = LocalTime.parse(startTime + " " + startAMPM.getValue().toString(), DateTimeFormatter.ofPattern("hh:mm a"));
            LocalTime endTimeParse = LocalTime.parse(endTime + " " + endAMPM.getValue().toString(), DateTimeFormatter.ofPattern("hh:mm a"));            
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm a");
            try {
                assert !startTimeParse.isBefore(businessOpenTime):"Not valid 1";
                assert !startTimeParse.isAfter(businessCloseTime):"Not valid 2";
                assert !endTimeParse.isBefore(businessOpenTime):"Not valid 3";
                assert !endTimeParse.isAfter(businessCloseTime):"Not valid 4";
                appointmentStart = sdf.parse(selectedDate + " " + startTime + " " + startAMPM.getValue().toString());
                appointmentEnd = sdf.parse(selectedDate + " " + endTime + " " + endAMPM.getValue().toString());
                appointmentStarts = new Timestamp(appointmentStart.getTime());
                appointmentEnds = new Timestamp(appointmentEnd.getTime());

                if (!checkForOverlappingTimes(appointmentStart, appointmentEnd) || 
                        appointmentStarts.after(appointmentEnds)) {
                    Alert error = new Alert(Alert.AlertType.WARNING);
                    error.setTitle("Error");
                    error.setContentText("The appointment times are either overlapping another appointment or"
                            + "the end time is before start time");
                    error.showAndWait();
                    return;
                } else {
                    try {
                        setAppointment();
                    } catch (SQLException ex) {
                        Logger.getLogger(NewAppointmentController.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            } catch (ParseException | SQLException ex) {
                Logger.getLogger(NewAppointmentController.class.getName()).log(Level.SEVERE, null, ex);
            } catch (AssertionError d) {
              String message = d.getMessage();
              System.out.println(message);
              Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle(null);
                alert.setHeaderText(null);
                alert.setContentText("Your appointment times are outside of business hours");
                Optional<ButtonType> result = alert.showAndWait();
                return;
            }
            }
            
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
    
    // Setting time options
    public void setData(){
        startHour.setItems(FXCollections.observableArrayList(hours));
        startMin.setItems(FXCollections.observableArrayList(mins));
        endHour.setItems(FXCollections.observableArrayList(hours));
        endMin.setItems(FXCollections.observableArrayList(mins));
        startAMPM.setItems(FXCollections.observableArrayList(ampm));
        endAMPM.setItems(FXCollections.observableArrayList(ampm));
    }
    
    // Getting customer Names for the drop down box
    public void getCustomerNames() throws SQLException {
        ResultSet rs; 
        Statement st = dbConnection.dbConnect().createStatement();
        String recordQuery = ("Select * from customer");
        rs = st.executeQuery(recordQuery);
        while(rs.next()){
            customers.getItems().add(rs.getString("customerName"));
        }
    }
    
    // Setting up appointment into Database
    public void setAppointment() throws SQLException {
        ResultSet rs;
        Statement st = dbConnection.dbConnect().createStatement();
        Timestamp date = new java.sql.Timestamp(new java.util.Date().getTime());
        
        String getCustomerId="SELECT customerId FROM customer WHERE customerName = '" + selectedCustomer + "';";
        rs = st.executeQuery(getCustomerId);
        rs.next();
        selectedCustomerId = rs.getInt(1);
        
        long startTime = appointmentStart.getTime();
        Date startDate = new Date(startTime - TimeZone.getDefault().getOffset(startTime));
        appointmentStarts = new Timestamp(startDate.getTime());
        
        long endTime = appointmentEnd.getTime();
        Date endDate = new Date(endTime - TimeZone.getDefault().getOffset(endTime));
        appointmentEnds = new Timestamp(endDate.getTime());
        
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
    
    public boolean checkForOverlappingTimes(Date start, Date end) throws SQLException {
        ResultSet rs;
        Statement st = dbConnection.dbConnect().createStatement();
        
        String getStartDates = ("SELECT start, end from appointment" +
        " WHERE createdBy = \"" + LoginPageController.getUser() + "\"" + ";");
        
        try {
            rs = st.executeQuery(getStartDates);
            while(rs.next()){
                long startTime = rs.getTimestamp(1).getTime();
                long endTime = rs.getTimestamp(2).getTime();
                Date startDate = new Date(startTime + TimeZone.getDefault().getOffset(startTime));
                Date endDate = new Date(endTime + TimeZone.getDefault().getOffset(endTime));
                
                if (start.after(startDate) && start.before(endDate) || end.after(startDate) && end.before(endDate)) {
                    System.out.println("Appointment is during another appointment time");
                    return false;
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(NewAppointmentController.class.getName()).log(Level.SEVERE, null, ex);
        }        
        return true;
    }
}

