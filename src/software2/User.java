/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package software2;

import java.sql.Timestamp;

/**
 *
 * @author Christopher Sherrill
 */
public class User {
    private int userId;
    private String userName;
    private String password;
    private byte active;
    private String createBy;
    private Timestamp createDate;
    private Timestamp lastUpdate;
    private String lastUpdatedBy;
}