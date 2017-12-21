/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package software2;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author SFFPC
 */
public class EditCustomerController implements Initializable {
    @FXML
    private Button editBtn; 
    @FXML
    private Button cancelBtn;
    @FXML
    private TableView<Customer> customerTable;
    @FXML
    private TableColumn idCol = new TableColumn();
    @FXML
    private TableColumn customerNameCol = new TableColumn();
    @FXML
    private TableColumn addressCol = new TableColumn();
    @FXML
    private TableColumn address2Col = new TableColumn();
    @FXML
    private TableColumn cityCol = new TableColumn();
    @FXML
    private TableColumn postalCol = new TableColumn();
    @FXML
    private TableColumn phoneCol = new TableColumn();
    
    public static Integer idToEdit;
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
            try {
                idCol.setCellValueFactory(new PropertyValueFactory<Customer, Integer>("idCol"));
                customerNameCol.setCellValueFactory(new PropertyValueFactory<Customer, String>("nameCol"));
                addressCol.setCellValueFactory(new PropertyValueFactory<Customer, String>("addressCol"));
                address2Col.setCellValueFactory(new PropertyValueFactory<Customer, String>("address2Col"));
                cityCol.setCellValueFactory(new PropertyValueFactory<Customer, String>("cityCol"));
                postalCol.setCellValueFactory(new PropertyValueFactory<Customer, String>("postalCol"));
                phoneCol.setCellValueFactory(new PropertyValueFactory<Customer, String>("phoneCol"));
                customerTable.getItems().setAll(Customer.getAllcustomerInfo());
            } catch (SQLException ex) {
                Logger.getLogger(EditCustomerController.class.getName()).log(Level.SEVERE, null, ex);
            }
       
            
        editBtn.setOnAction((ActionEvent e) -> {
            Parent window;
            Customer selCustomer = customerTable.getSelectionModel().getSelectedItem();
            idToEdit = selCustomer.getIdCol();
            try {
                window = FXMLLoader.load(getClass().getResource("EditSelectedCustomer.fxml"));
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
