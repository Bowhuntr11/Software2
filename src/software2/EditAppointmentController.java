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
    
    private Integer apptToEdit;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        apptToEdit = CalendarViewController.apptToEdit;
        setData();
        try {
            getCustomerNames();
        } catch (SQLException ex) {
            Logger.getLogger(NewAppointmentController.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            dbconnect();
        } catch (ClassNotFoundException | SQLException | IOException | InterruptedException ex) {
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
            sdf.setTimeZone(TimeZone.getDefault());
            Parent window;
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
                            updateAppointment();
                            window = FXMLLoader.load(getClass().getResource("CalendarView.fxml"));
                            Stage mainStage;
                            mainStage = Main.parentWindow;
                            mainStage.getScene().setRoot(window);
                            mainStage.sizeToScene();
                        } catch (SQLException ex) {
                            Logger.getLogger(NewAppointmentController.class.getName()).log(Level.SEVERE, null, ex);
                        } catch (IOException ex) {
                            Logger.getLogger(EditAppointmentController.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                } catch (ParseException ex) {
                    Logger.getLogger(NewAppointmentController.class.getName()).log(Level.SEVERE, null, ex);
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
    
    public void setData(){
        startHour.setItems(FXCollections.observableArrayList(hours));
        startMin.setItems(FXCollections.observableArrayList(mins));
        endHour.setItems(FXCollections.observableArrayList(hours));
        endMin.setItems(FXCollections.observableArrayList(mins));
        startAMPM.setItems(FXCollections.observableArrayList(ampm));
        endAMPM.setItems(FXCollections.observableArrayList(ampm));
    }
    
    public void dbconnect() throws ClassNotFoundException, SQLException, IOException, InterruptedException {
                    Statement st;
                    ResultSet rs;
                    st = dbConnection.dbConnect().createStatement();
                    String recordQuery = ("Select * from appointment" +
                        " Inner Join customer ON appointment.customerId = customer.customerId" +
                        " WHERE appointmentId = " + apptToEdit + ";"); 
                            rs = st.executeQuery(recordQuery);
                            while(rs.next()){
                                title.setText(rs.getString("title"));
                                description.setText(rs.getString("description"));
                                location.setText(rs.getString("location")); 
                                urlText.setText(rs.getString("url"));
                                java.sql.Date startDate = rs.getDate("start");
                                datePicker.setValue(startDate.toLocalDate());
                                
                                long startTimeMilli = rs.getTimestamp("start").getTime();
                                Date startDate2 = new Date(startTimeMilli + TimeZone.getDefault().getOffset(startTimeMilli));
                                startHour.setValue(new SimpleDateFormat("hh").format(startDate2));
                                startMin.setValue(new SimpleDateFormat("mm").format(startDate2));
                                startAMPM.setValue(new SimpleDateFormat("a").format(startDate2));
                                
                                long endTimeMilli = rs.getTimestamp("end").getTime();
                                Date endDate = new Date(endTimeMilli + TimeZone.getDefault().getOffset(endTimeMilli));
                                endHour.setValue(new SimpleDateFormat("hh").format(endDate));
                                endMin.setValue(new SimpleDateFormat("mm").format(endDate));
                                endAMPM.setValue(new SimpleDateFormat("a").format(endDate));
                                
                                customers.setValue(rs.getString("customerName"));
                            }            
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
    
    public void updateAppointment() throws SQLException, ParseException {
        ResultSet rs;
        Statement st = dbConnection.dbConnect().createStatement();
        String getCustomerId= ("SELECT customerId FROM customer WHERE customerName = '" + selectedCustomer + "';");
        rs = st.executeQuery(getCustomerId);
        rs.next();
        
        // Editing Appointment
        String query= ("UPDATE appointment" +
            " Inner Join customer ON appointment.customerId = customer.customerId" +
            " SET appointment.customerId = '" + rs.getString("customerId") + "'" +
            ", title = '" + title.getText() + "'" +
            ", description = '" + description.getText() + "'" +
            ", location = '" + location.getText() + "'" +
            ", url = '" + urlText.getText() + "'" +
            ", start = '" + appointmentStarts + "'" +
            ", end = '" + appointmentEnds + "'" +
            ", appointment.lastUpdateBy = '" + LoginPageController.getUser() + "'" +
            " WHERE appointmentID = " + apptToEdit + ";");
        st.executeUpdate(query);
        
    }
    
    
}

