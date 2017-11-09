/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package software2;

import java.net.URL;
import java.time.LocalTime;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Spinner;

/**
 * FXML Controller class
 *
 * @author SFFPC
 */
public class NewAppointmentController implements Initializable {
    @FXML
    private Spinner startTime;
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        startTime = new SpinnerTime();
    }
}
 