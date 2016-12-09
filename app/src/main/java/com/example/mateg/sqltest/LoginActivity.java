package com.example.mateg.sqltest;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import static java.sql.Types.NULL;

public class LoginActivity extends AppCompatActivity {
    EditText etUser, etName, etPassword, etPort, etHost;
    String user, name, password, port, host;
    Button btTestConnection, btNewQuery, btCancelTest;
    ProgressBar pbTestConnection;
    ConnectionClass connectionClass;
    CreateConnectionV2 createConnection;
    Context context = this.getApplication();
    Intent i;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        etUser = (EditText) findViewById(R.id.etUser);
        etName = (EditText) findViewById(R.id.etName);
        etPassword = (EditText) findViewById(R.id.etPassword);
        etPort = (EditText) findViewById(R.id.etPort);
        etHost = (EditText) findViewById(R.id.etHost);

        btTestConnection = (Button) findViewById(R.id.btTestConnection);
        btNewQuery = (Button) findViewById(R.id.btNewQuery);
       // btCancelTest = (Button) findViewById(R.id.btCancelTest);
      //  btCancelTest.setVisibility(View.INVISIBLE);
        btNewQuery.setVisibility(View.INVISIBLE);

        pbTestConnection = (ProgressBar) findViewById(R.id.pb_connectionTest);
        pbTestConnection.setVisibility(View.INVISIBLE);

        etUser.setText("andro_login");
        etName.setText("Northwind");
        etHost.setText("192.168.0.12");
        etPassword.setText("password");


        btTestConnection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                user = etUser.getText().toString();
                name = etName.getText().toString();
                password = etPassword.getText().toString();
                port = etPort.getText().toString();
                host = etHost.getText().toString();

                connectionClass = new ConnectionClass(host, name, user, password, port);
                createConnection = new CreateConnectionV2();
                createConnection.execute();
            }
        });
        i = new Intent(this, QueryActivity.class);
        btNewQuery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              //  i= new Intent(LoginActivity.this, QueryActivity.class);
                i.putExtra("host", host);
                i.putExtra("name", name);
                i.putExtra("user", user);
                i.putExtra("password", password);
                i.putExtra("port", port);
                startActivity(i);
            }
        });
    }

    public class CreateConnectionV2 extends AsyncTask<String, String, String> {
        Connection conn;
        Boolean isSuccess = false;

        @Override
        protected void onPreExecute() {
           // btCancelTest.setVisibility(View.VISIBLE);
            pbTestConnection.setVisibility(View.VISIBLE);
            btTestConnection.setEnabled(false);
            btNewQuery.setVisibility(View.INVISIBLE);
        }

        @Override
        protected void onPostExecute(String s) {
         //   btCancelTest.setVisibility(View.INVISIBLE);
            if (isSuccess) {
//                queryString = queryText.getText().toString();
//                ExecuteQuery exeQuer = new ExecuteQuery();
//                exeQuer.execute();
                Toast.makeText(LoginActivity.this, "Connected", Toast.LENGTH_LONG).show();
                pbTestConnection.setVisibility(View.INVISIBLE);
                btTestConnection.setEnabled(true);
                btNewQuery.setVisibility(View.VISIBLE);
                ConnectionHandler connectionHandler = (ConnectionHandler) getApplicationContext();
                connectionHandler.setConn(conn);
            }
        }

        @Override
        protected String doInBackground(String... strings) {
            String result;
            try {
                conn = connectionClass.CONN();
//                Statement stmt = conn.createStatement();
//                ResultSet rs = stmt.executeQuery("USE "+host+"\n" +
//                        "SELECT COUNT(*) from information_schema.tables \n" +
//                        "WHERE table_type = 'base table' ");
//                if(rs.getInt(0) >= 1) {
//                    result = "connected";
//                }
//                else
//                    result = "connection failed";
                result = "connected";
            } catch (Exception e) {
                Toast.makeText(LoginActivity.this, "ERROR: " + e.getMessage(), Toast.LENGTH_LONG).show();
                result = "error";
                conn = null;
                isSuccess = false;
            }
            //conn = null;
            isSuccess = true;
            return result;
        }
    }
}
