/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package software2;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

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

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        saveBtn.setOnAction((ActionEvent e) -> {
            // TO DO
        });
    }    
    
}
