package com.example.mateg.sqltest;

import java.sql.Connection;

/**
 * Created by mateg on 11.11.2016.
 */

public class ConnectionHandler {
    public Connection getCon() {
        return con;
    }

    public void setCon(Connection con) {
        this.con = con;
    }

    Connection con;
    ConnectionHandler(Connection con){
        this.con = con;
    }
}
