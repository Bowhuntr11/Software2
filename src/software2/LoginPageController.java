/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package software2;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author SFFPC
 */
public class LoginPageController implements Initializable {
    @FXML
    private PasswordField passwordBox;
    @FXML
    private TextField usernameBox;
    @FXML
    private Button submit;
    @FXML
    private StackPane root;
    @FXML
    private VBox bx;
    @FXML
    private ComboBox languageBox;
    @FXML
    private Text usernameText;
    @FXML
    private Text passwordText;

    private static String User;
    /**
     * Initializes the controller class.
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //Setting Up Layout
        Platform.runLater(()->usernameBox.requestFocus());
        if (Locale.getDefault() == Locale.US ||
                Locale.getDefault() == null) {
            languageBox.setValue("English");
            usernameText.setText("Username");
            passwordText.setText("Password");
            submit.setText("Submit");
        }
        if (Locale.getDefault() == Locale.FRANCE) {
            languageBox.setValue("French");
            usernameText.setText("Nom d'utilisateur");
            passwordText.setText("Mot de passe");
            submit.setText("Soumettre");
        }
        // Language Button Action
        languageBox.valueProperty().addListener(new ChangeListener<String>() {
          @Override public void changed(ObservableValue ov, String t, String t1) {
            if ("English".equals(t1) || Locale.getDefault() == Locale.US) {
                usernameText.setText("Username");
                passwordText.setText("Password");
                submit.setText("Submit");
                System.out.println("English");
            }
            if ("French".equals(t1) || Locale.getDefault() == Locale.FRANCE) {
                usernameText.setText("Nom d'utilisateur");
                passwordText.setText("Mot de passe");
                submit.setText("Soumettre");
                System.out.println("French");
            }
          }    
        });
        
        // Submit Button Action
        submit.setOnAction((ActionEvent e) -> {
            try {
                dbconnect();
            } catch (ClassNotFoundException | SQLException | IOException | InterruptedException ex) {
                Logger.getLogger(LoginPageController.class.getName()).log(Level.SEVERE, null, ex);
            }
        });   
    }    
    
    public void dbconnect() throws ClassNotFoundException, SQLException, IOException, InterruptedException {
                    Connection connection = null;
                    String driver = "com.mysql.jdbc.Driver";
                    String db = "U03lvi";
                    String url = "jdbc:mysql://52.206.157.109/" + db;
                    String user = "U03lvi";
                    String pass = "53688016219";
                        try {
                            //Connect to Database, check user/pass
                            Class.forName(driver);
                            connection = DriverManager.getConnection(url,user,pass);
                            System.out.println("Connected to database : " + db);
                            PreparedStatement prepstate = connection.prepareStatement("Select userName,password from user where userName=? and password=?");
                            prepstate.setString(1, usernameBox.getText());
                            prepstate.setString(2, passwordBox.getText());
                            ResultSet resultSet = prepstate.executeQuery();
                            if (resultSet.next()) { //Correct User/Pass
                                System.out.println("Correct login credentials");
                                User = usernameBox.getText();
                                Parent window;
                                window = FXMLLoader.load(getClass().getResource("CalendarView.fxml"));
                                Stage mainStage;
                                mainStage = Main.parentWindow;
                                mainStage.getScene().setRoot(window);
                                mainStage.sizeToScene();
                            } 
                            else { //Incorrect User/Pass
                                // root = FXMLLoader.load(getClass().getResource("LoginPage.fxml"));
                                System.out.println("Incorrect login credentials");
                                Alert error = new Alert(AlertType.WARNING);
                                error.setTitle("Error");
                                error.setContentText("Incorrect Username/Password. Please try again.");
                                error.showAndWait();
                            }
                        } catch (SQLException e) {
                            System.out.println("SQLException: "+e.getMessage());
                            System.out.println("SQLState: "+e.getSQLState());
                            System.out.println("VendorError: "+e.getErrorCode());
                        }
                    
                    System.out.println("Done");
                    
    }

    public static String getUser() {
        return User;
    }
}
