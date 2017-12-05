/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package software2;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Button;
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
    private Button NewCustomersThisMonth;
    @FXML
    private Button cancelBtn;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        NumberOfApptTypesPerMonth.setOnAction((ActionEvent e) -> {
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
        
        ScheduleForEachConsultant.setOnAction((ActionEvent e) -> {
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
        
        NewCustomersThisMonth.setOnAction((ActionEvent e) -> {
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
    
}
