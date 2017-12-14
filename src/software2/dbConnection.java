/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package software2;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author SFFPC
 */
public class dbConnection {
    
    public static Connection dbConnect()throws SQLException {
        Connection connection = null;
        String driver = "com.mysql.jdbc.Driver";
        String db = "U03lvi";
        String url = "jdbc:mysql://52.206.157.109/" + db;
        String user = "U03lvi";
        String pass = "53688016219";
        try {
            Class.forName(driver);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(dbConnection.class.getName()).log(Level.SEVERE, null, ex);
        }
        connection = DriverManager.getConnection(url,user,pass);
        System.out.println("Connected to database : " + db);
        return connection;
    }
}
