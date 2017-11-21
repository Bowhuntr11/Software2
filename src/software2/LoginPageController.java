/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package software2;

import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TextField;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

/**
 * FXML Controller class
 *
 * @author SFFPC
 */
public class LoginPageController implements Initializable {
    @FXML
    private TextField usernameBox;
    @FXML
    private PasswordField passwordBox;
    @FXML
    private Button submit;
    @FXML
    private StackPane root;
    @FXML
    private VBox bx;
    @FXML
    private ComboBox languageBox;
    
    private final String[] languages = {"English","Spanish"};

    /**
     * Initializes the controller class.
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //Setting Up Layout
        setData();
		submit.setOnAction((ActionEvent e) -> {
                    ProgressIndicator pi = new ProgressIndicator();
                    VBox box = new VBox(pi);
                    box.setAlignment(Pos.CENTER);
                    // Grey Background
                    bx.setDisable(true);
                    root.getChildren().add(box);
                    try {
                        dbconnect();
                    } catch (ClassNotFoundException | SQLException ex) {
                        Logger.getLogger(LoginPageController.class.getName()).log(Level.SEVERE, null, ex);
                    }
                });
                
    }    
    
    public void setData()  {
        languageBox.setItems(FXCollections.observableArrayList(languages));
    }
    
    public void dbconnect() throws ClassNotFoundException, SQLException {
                    Connection conn = null;
                    Statement st = null;
                    
                    String driver = "com.mysql.jdbc.Driver";
                    String db = "U03lvi";
                    String url = "jdbc:mysql://52.206.157.109/" + db;
                    String user = "U03lvi";
                    String pass = "53688016219";
                        try {
                            Class.forName(driver);
                            conn = DriverManager.getConnection(url,user,pass);
                            System.out.println("Connected to database : " + db);
                        } catch (SQLException e) {
                            System.out.println("SQLException: "+e.getMessage());
                            System.out.println("SQLState: "+e.getSQLState());
                            System.out.println("VendorError: "+e.getErrorCode());
                        }
                    
                    st = conn.createStatement();
                    String query="DROP TABLE ITEM";      
                    st.executeUpdate(query);
                    System.out.println("TABle ITEM Created Successfully");
                    
    }
}
