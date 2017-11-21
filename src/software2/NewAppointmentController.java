/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package software2;

import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
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
    private Button save;
    @FXML
    private Button cancel;
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
        
        save.setOnAction((ActionEvent e) -> {
            // TO DO
        });
        cancel.setOnAction((ActionEvent e) -> {
            Platform.exit();
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
