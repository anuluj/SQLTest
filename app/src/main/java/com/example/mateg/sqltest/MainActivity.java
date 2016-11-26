package com.example.mateg.sqltest;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class MainActivity extends AppCompatActivity {

    ConnectionClass connectionClass;
    EditText edtuserid, edtpass;
    Button btnlogin;
    ProgressBar pbbar;
    Intent i;
    ConnectionHandler myConn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



      //  connectionClass = new ConnectionClass();
        edtuserid = (EditText) findViewById(R.id.edtuserid);
        edtpass = (EditText) findViewById(R.id.edtpass);
        btnlogin = (Button) findViewById(R.id.btnlogin);
        pbbar = (ProgressBar) findViewById(R.id.pbbar);
        pbbar.setVisibility(View.GONE);

        edtuserid.setText("andro_login");
        edtpass.setText("password");


        i = new Intent(this,QueryActivity.class);
        btnlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DoLogin doLogin = new DoLogin();// this is the Asynctask
                doLogin.execute("");

            }
        });


    }

    public class DoLogin extends AsyncTask<String, String, String> {
        String z = "";
        Boolean isSuccess = false;
        String userid = edtuserid.getText().toString();
        String password = edtpass.getText().toString();
        Connection con;

        @Override
        protected void onPreExecute() {
            pbbar.setVisibility(View.VISIBLE);
            edtpass.setEnabled(false);
            edtuserid.setEnabled(false);
            btnlogin.setEnabled(false);
        }

        @Override
        protected void onPostExecute(String r) {
            pbbar.setVisibility(View.GONE);
            edtpass.setEnabled(true);
            edtuserid.setEnabled(true);
            btnlogin.setEnabled(true);
            Toast.makeText(MainActivity.this, r, Toast.LENGTH_SHORT).show();



            if (isSuccess) {
                startActivity(i);
            }
        }

        @Override
        protected String doInBackground(String... params) {
            if (userid.trim().equals("") || password.trim().equals(""))
                z = "Please enter User Id and Password";
            else {
                try {
                    con = connectionClass.CONN();
                    if (con == null) {
                        z = "Error in connection with SQL server";
                    } else {
//                        String query = "select EmployeeID from Employees where EmployeeID=1";
                        Statement stmt = con.createStatement();
//                        ResultSet rs = stmt.executeQuery(query);
//                        if (rs.next()) {
//                            z = "Login Succesful";
//                            isSuccess = true;
//                        } else {
//                            z = "Invalid Credentials";
//                            isSuccess = false;
//                        }
                        isSuccess = true;
                        z = "Login Succesful";
                    }
                } catch (Exception ex) {
                    isSuccess = false;
                    z = "Exceptions";
                }
            }
            return z;
        }
    }
}
