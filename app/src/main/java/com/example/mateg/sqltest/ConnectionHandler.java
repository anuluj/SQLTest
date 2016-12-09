package com.example.mateg.sqltest;

import android.app.Application;

import java.sql.Connection;

/**
 * Created by mateg on 11.11.2016.
 */

public class ConnectionHandler extends Application {
    public Connection getConn() {
        return conn;
    }

    public void setConn(Connection con) {
        this.conn = con;
    }

    Connection conn;

}
