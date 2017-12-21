/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package software2;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.TimeZone;
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
            }
            if ("French".equals(t1) || Locale.getDefault() == Locale.FRANCE) {
                usernameText.setText("Nom d'utilisateur");
                passwordText.setText("Mot de passe");
                submit.setText("Soumettre");
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
                    //Connect to Database, check user/pass
                    PreparedStatement prepstate = dbConnection.dbConnect().prepareStatement("Select userName,password from user where userName=? and password=?");
                    prepstate.setString(1, usernameBox.getText());
                    prepstate.setString(2, passwordBox.getText());
                    ResultSet resultSet = prepstate.executeQuery();
                    if (resultSet.next()) { //Correct User/Pass
                        System.out.println("Correct login credentials");
                        User = usernameBox.getText();
                        userLoginToFile();
                        checkForAppointments();
                        Parent window;
                        window = FXMLLoader.load(getClass().getResource("CalendarView.fxml"));
                        Stage mainStage;
                        mainStage = Main.parentWindow;
                        mainStage.getScene().setRoot(window);
                        mainStage.sizeToScene();
                    } else { //Incorrect User/Pass
                        if (languageBox.getValue().toString().equals("English")){
                            System.out.println("Incorrect login credentials");
                            Alert error = new Alert(AlertType.WARNING);
                            error.setTitle("Error");
                            error.setHeaderText("Incorrect Username/Password.");
                            error.setContentText("Please try again.");
                            error.showAndWait();
                        } else {
                            System.out.println("FRENCH");
                            Alert error = new Alert(AlertType.WARNING);
                            error.setTitle("Erreur");
                            error.setHeaderText("Nom d'utilisateur / Mot de passe incorrect.");
                            error.setContentText("Veuillez réessayer.");
                            error.showAndWait();
                        }
                    }
    }

    public static String getUser() {
        return User;
    }
    
    // Writing user log
    public void userLoginToFile() {
        Date date = new Date();
        try(FileWriter fw = new FileWriter("userLogins.txt", true);
            BufferedWriter bw = new BufferedWriter(fw);
            PrintWriter out = new PrintWriter(bw))
        {
            out.println(getUser() + " logged in at: " + date.toString());
        } catch (IOException ex) {
            Logger.getLogger(LoginPageController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    // Checking for appointments within 15 minutes
    public void checkForAppointments() throws SQLException {
        ResultSet rs;
        Statement st = dbConnection.dbConnect().createStatement();
        Date now = new Date(); 
        
        String getAppointmentTimes = ("Select customerName, title, location, start from appointment" +
            " Inner Join customer ON appointment.customerId = customer.customerId" +
            " WHERE appointment.createdBy = " + "\"" + LoginPageController.getUser() + "\"" + ";");
        
        try {
            rs = st.executeQuery(getAppointmentTimes);
            while(rs.next()){
                String customer = rs.getString(1);
                String title = rs.getString(2);
                String location = rs.getString(3);
                long startTime = rs.getTimestamp(4).getTime();
                long nowTime = now.getTime();
                
                Date startDate = new Date(startTime + TimeZone.getDefault().getOffset(startTime));
                long millis = startDate.getTime();
                SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a");
                String startString = sdf.format(startDate);
                
                long timeLeft = millis-nowTime;
                if (timeLeft <= 900000 && timeLeft >= 0 ) {
                    Alert error = new Alert(Alert.AlertType.WARNING);
                    error.setTitle("Appointment is within 15 minutes");
                    error.setHeaderText("Appointment Reminder");
                    error.setContentText("You have a " + title.toUpperCase() + 
                            " with " + customer.toUpperCase() +
                            " at " + startString +
                            " located at " + location);
                    error.showAndWait();
                    return;
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(NewAppointmentController.class.getName()).log(Level.SEVERE, null, ex);
        }        
    }
}
    
