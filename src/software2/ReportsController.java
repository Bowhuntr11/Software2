/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package software2;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormatSymbols;
import java.util.Date;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author SFFPC
 */
public class ReportsController implements Initializable {

    @FXML
    private Button NumberOfApptTypesPerMonth;
    @FXML
    private Button ScheduleForEachConsultant;
    @FXML
    private Button NamesOfConsultants;
    @FXML
    private Button cancelBtn;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        NumberOfApptTypesPerMonth.setOnAction((ActionEvent e) -> {
            try {
                Counter();
                ReportCreated();
            } catch (SQLException ex) {
                Logger.getLogger(ReportsController.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
        
        ScheduleForEachConsultant.setOnAction((ActionEvent e) -> {
            ScheduleForEachUser();
            ReportCreated();
        });
        
        NamesOfConsultants.setOnAction((ActionEvent e) -> {
            getConsultants();
            ReportCreated();
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
    
    public void Counter() throws SQLException {
            ResultSet rs;
            Statement st;
            String getTitles = "SELECT  MONTH(start) AS MONTH, " +
                                "title AS TITLE, " +
                                "count(title) AS COUNT " +
                                "FROM appointment " +
                                "GROUP BY title,MONTH(start);";
            st = dbConnection.dbConnect().createStatement();
            rs = st.executeQuery(getTitles);
            
            File file = new File("NumberOfApptTypesPerMonth.txt");
            try (Writer writer = new BufferedWriter(new FileWriter(file))) {
                writer.write("****Number of Appointment Types Per Month****\n");
                writer.write("*********************************************\n");
                writer.write("Month -> Appointment Type  -> How many Times\n");
                while (rs.next()) {
                    String Month = rs.getString(1);
                    String Months = getMonth(Month);
                    String AppointmentType = rs.getString(2);
                    String Times = rs.getString(3);
                    writer.write(Months + "  - >  " + AppointmentType.toUpperCase() + "  ->   " + Times + "\n");
                }
                writer.write("*********************************************");
                writer.close();
            } catch (IOException ex) {
                Logger.getLogger(ReportsController.class.getName()).log(Level.SEVERE, null, ex);
            }    
    }
    
    public void ReportCreated() {
        Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle(null);
            alert.setHeaderText(null);
            alert.setContentText("Report Created in Folder");
            Optional<ButtonType> result = alert.showAndWait();
    }
    
    public void ScheduleForEachUser() {
        ResultSet rs;
        Statement st;
        String getSchedules = "SELECT createdBy AS Consultant, title, start FROM appointment GROUP BY createdBy,start;";
        try {
            st = dbConnection.dbConnect().createStatement();
            rs = st.executeQuery(getSchedules);
            File file = new File("SchedulesForEachConsultant.txt");
            try (Writer writer = new BufferedWriter(new FileWriter(file))) {
                writer.write("********Schedules For Each Consultant********\n");
                writer.write("*********************************************\n");
                while (rs.next()) {
                    String User = rs.getString(1);
                    String AppointmentType = rs.getString(2);
                    Date Dates = rs.getDate(3);
                    writer.write(User + "  - >  " + AppointmentType.toUpperCase() + "  ->   " + Dates + "\n");
                }
                writer.write("*********************************************");
                writer.close();
            } catch (IOException ex) {
                Logger.getLogger(ReportsController.class.getName()).log(Level.SEVERE, null, ex);
            }    
        } catch (SQLException ex) {
            Logger.getLogger(ReportsController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void getConsultants() {
        ResultSet rs;
        Statement st;
        String getUsers = "SELECT userName FROM user";
        try {
            st = dbConnection.dbConnect().createStatement();
            rs = st.executeQuery(getUsers);
            File file = new File("Consultants.txt");
            try (Writer writer = new BufferedWriter(new FileWriter(file))) {
                writer.write("***********Names of Each Consultant**********\n");
                writer.write("*********************************************\n");
                while (rs.next()) {
                    String User = rs.getString(1);
                    writer.write(User + "\n");
                }
                writer.write("*********************************************");
                writer.close();
            } catch (IOException ex) {
                Logger.getLogger(ReportsController.class.getName()).log(Level.SEVERE, null, ex);
            }    
        } catch (SQLException ex) {
            Logger.getLogger(ReportsController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
     
    public String getMonth(String month) {
        int months = Integer.parseInt(month);
        return new DateFormatSymbols().getMonths()[months-1];
    }
}