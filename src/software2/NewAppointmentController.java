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
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
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
    private TextField url;
    @FXML
    private ComboBox selectedCustomer;
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
        setData();
        
        saveBtn.setOnAction((ActionEvent e) -> {
            // TO DO
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
}
