package com.example.mateg.sqltest;

import android.annotation.SuppressLint;
import android.os.StrictMode;
import android.util.Log;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Created by mateg on 11.11.2016.
 */

public class ConnectionClass {

    String ip = "DESKTOP-CKKP70U//SQLEXPRESS"; //DESKTOP-CKKP70U\SQLEXPRESS
    String classs = "net.sourceforge.jtds.jdbc.Driver";
    String dbName = "Northwind";
    String user = "android_login ";
    String password = "password";
    String port;
    String instance = "SQLEXPRESS";

    public ConnectionClass(String ip, String dbName, String user, String password, String port){
        this.ip = ip;
        this.dbName = dbName;
        this.user = user;
        this.password = password;
        this.port = port;
       // this.instance = instance;
    }

    @SuppressLint("NewApi")
    public Connection CONN() {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                .permitAll().build();
        StrictMode.setThreadPolicy(policy);
        Connection conn = null;
        String ConnURL = null;
        try {

            Class.forName(classs);
            ConnURL = "jdbc:jtds:sqlserver://"+ip+":"+port+"/"+dbName+";instance=SQLEXPRESS;user="+user+";password="+password;
            conn = DriverManager.getConnection(ConnURL);
        } catch (SQLException se) {
            Log.e("ERRO 1", se.getMessage());
        } catch (ClassNotFoundException e) {
            Log.e("ERRO 2", e.getMessage());
        } catch (Exception e) {
            Log.e("ERRO 3", e.getMessage());
        }
        return conn;
    }
}
