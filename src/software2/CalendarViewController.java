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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.ResourceBundle;
import java.util.TimeZone;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author SFFPC
 */
public class CalendarViewController implements Initializable {
    @FXML
    private ComboBox SortByBox;
    @FXML
    private Button NewCustomer;
    @FXML
    private Button EditCustomer;
    @FXML
    private Button ScheduleAppointment;
    @FXML
    private Button EditAppointment;
    @FXML
    private Button Reports;
    @FXML
    private ToggleGroup togglegroup;
    @FXML
    private RadioButton local;
    @FXML
    private RadioButton GMT;
    @FXML
    private TableView<Appointment> AppointmentTable;
    @FXML
    private TableColumn apptIdCol = new TableColumn();
    @FXML
    private TableColumn dateCol = new TableColumn();
    @FXML
    private TableColumn startCol = new TableColumn();
    @FXML
    private TableColumn endCol = new TableColumn();
    @FXML
    private TableColumn apptTypeCol = new TableColumn();
    @FXML
    private TableColumn customerCol = new TableColumn();
    
    public static Integer apptToEdit;
    
    private String timeZ = "Local";
    private String WeekMonth = "Month";
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        AppointmentTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        apptIdCol.setStyle( "-fx-alignment: CENTER;");
        dateCol.setStyle( "-fx-alignment: CENTER;");
        startCol.setStyle( "-fx-alignment: CENTER;");
        endCol.setStyle( "-fx-alignment: CENTER;");
        apptTypeCol.setStyle( "-fx-alignment: CENTER;");
        customerCol.setStyle( "-fx-alignment: CENTER;");
        apptIdCol.setCellValueFactory(new PropertyValueFactory<Appointment, Integer>("apptIdCol"));
        dateCol.setCellValueFactory(new PropertyValueFactory<Appointment, String>("dateCol"));
        startCol.setCellValueFactory(new PropertyValueFactory<Appointment, String>("startCol"));
        endCol.setCellValueFactory(new PropertyValueFactory<Appointment, String>("endCol"));
        apptTypeCol.setCellValueFactory(new PropertyValueFactory<Appointment, String>("apptTypeCol"));
        customerCol.setCellValueFactory(new PropertyValueFactory<Appointment, String>("customerCol"));
        
        try {
            AppointmentTable.getItems().setAll(Appointment.getAppointmentInfo(timeZ, WeekMonth));
        } catch (SQLException | ParseException ex) {
            Logger.getLogger(CalendarViewController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        SortByBox.valueProperty().addListener(new ChangeListener<String>() {
          @Override public void changed(ObservableValue ov, String t, String t1) {
            if ("Month".equals(t1)) {
                try {
                    WeekMonth = "Month";
                    AppointmentTable.getItems().setAll(Appointment.getAppointmentInfo(timeZ, WeekMonth));
                } catch (SQLException ex) {
                    Logger.getLogger(EditCustomerController.class.getName()).log(Level.SEVERE, null, ex);
                } catch (ParseException ex) {
                    Logger.getLogger(CalendarViewController.class.getName()).log(Level.SEVERE, null, ex);
                }
                
            }
            if ("Week".equals(t1)) {
                try {
                    WeekMonth = "Week";
                    AppointmentTable.getItems().setAll(Appointment.getAppointmentInfo(timeZ, WeekMonth));
                } catch (SQLException ex) {     
                    Logger.getLogger(EditCustomerController.class.getName()).log(Level.SEVERE, null, ex);
                } catch (ParseException ex) {
                    Logger.getLogger(CalendarViewController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }    
        });
        
        ToggleGroup group = new ToggleGroup();
        local.setToggleGroup(group);
        GMT.setToggleGroup(group);
        
        local.setOnAction((ActionEvent e) -> {
            timeZ = "Local";
            try {
                AppointmentTable.getItems().setAll(Appointment.getAppointmentInfo(timeZ, WeekMonth));
            } catch (SQLException | ParseException ex) {
                Logger.getLogger(CalendarViewController.class.getName()).log(Level.SEVERE, null, ex);
            }
        });  
        
        GMT.setOnAction((ActionEvent e) -> {
            timeZ = "GMT";
            try {
                AppointmentTable.getItems().setAll(Appointment.getAppointmentInfo(timeZ, WeekMonth));
            } catch (SQLException | ParseException ex) {
                Logger.getLogger(CalendarViewController.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
        
        //Setting up Button Listeners
        NewCustomer.setOnAction((ActionEvent e) -> {
            Parent window1;
            try {
                window1 = FXMLLoader.load(getClass().getResource("NewCustomer.fxml"));
                Stage mainStage;
                mainStage = Main.parentWindow;
                mainStage.getScene().setRoot(window1);
                mainStage.sizeToScene();
            } catch (IOException ex) {
                Logger.getLogger(CalendarViewController.class.getName()).log(Level.SEVERE, null, ex);
            }
        });   
        
        EditCustomer.setOnAction((ActionEvent e) -> {
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
        
        ScheduleAppointment.setOnAction((ActionEvent e) -> {
            Parent window1;
            try {
                window1 = FXMLLoader.load(getClass().getResource("NewAppointment.fxml"));
                Stage mainStage;
                mainStage = Main.parentWindow;
                mainStage.getScene().setRoot(window1);
                mainStage.sizeToScene();
            } catch (IOException ex) {
                Logger.getLogger(CalendarViewController.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
        
        EditAppointment.setOnAction((ActionEvent e) -> {
            Appointment selAppointment = AppointmentTable.getSelectionModel().getSelectedItem();
            apptToEdit = selAppointment.getApptIdCol();
            Parent window;
            try {
                window = FXMLLoader.load(getClass().getResource("EditAppointment.fxml"));
                Stage mainStage;
                mainStage = Main.parentWindow;
                mainStage.getScene().setRoot(window);
                mainStage.sizeToScene();
            } catch (IOException ex) {
                Logger.getLogger(CalendarViewController.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
        
        Reports.setOnAction((ActionEvent e) -> {
            Parent window;
            try {
                window = FXMLLoader.load(getClass().getResource("Reports.fxml"));
                Stage mainStage;
                mainStage = Main.parentWindow;
                mainStage.getScene().setRoot(window);
                mainStage.sizeToScene();
            } catch (IOException ex) {
                Logger.getLogger(CalendarViewController.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
    }    
    
    
    
}
