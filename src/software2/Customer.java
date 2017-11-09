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
public class Customer {
    private int customerId;
    private String customerName;
    private int addressId;
    private byte active;
    private Timestamp createDate;
    private String createdBy;
    private Timestamp lastUpdate;
    private String lastUpdateby;
}
