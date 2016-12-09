package com.example.mateg.sqltest;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.inputmethodservice.Keyboard;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;

import static java.sql.Types.NULL;

public class QueryActivity extends AppCompatActivity {

    TableLayout tableLayout;
    ProgressBar progressBar;
    Button btExecuteQuery;
    EditText queryText;
    GridView gridView;
    Connection conn;
    ConnectionClass connectionClass;
    String queryString;
    ArrayList<String> arrayListAllResult;
    String[] resultOfQuery;
    ArrayAdapter<String> adapter;
    int rowNumber, columnNumber;
    ScrollView scrollView;
    HorizontalScrollView horizontalScrollView;
    String user, name, password, port, host;

    Context context = this.getApplication();
    Intent test_intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_query_v2);

        Bundle b = getIntent().getExtras();
        user = b.getString("user");
        name = b.getString("name");
        password = b.getString("password");
        port = b.getString("port");
        host = b.getString("host");


        setTitle("Northwind");
        horizontalScrollView = (HorizontalScrollView) findViewById(R.id.horizontalScrollView);
        scrollView = (ScrollView) findViewById(R.id.scrollViewv2);
       // connectionClass = new ConnectionClass();
        queryText = (EditText) findViewById(R.id.queryText);
        gridView = (GridView) findViewById(R.id.dataGrid);
        btExecuteQuery = (Button) findViewById(R.id.btExecuteQuery);
        progressBar = (ProgressBar) findViewById(R.id.pbbar_query);
        progressBar.setVisibility(View.GONE);
        tableLayout = (TableLayout) findViewById(R.id.table);

        test_intent = new Intent(this, LoginActivity.class);
        //conn = connectionClass.CONN();

        queryText.setText("select * from Employees");
        connectionClass = new ConnectionClass(host, name, user, password, port);
        CreateConnection createConnection = new CreateConnection();
        createConnection.execute();

        btExecuteQuery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Stopwatch sw2 = new Stopwatch();
                sw2.start();
//                if (conn == null) {
//                    CreateConnection createConnection = new CreateConnection();
//                    createConnection.execute();
           //     } else {
                    queryString = queryText.getText().toString();
                    ExecuteQuery exeQuer = new ExecuteQuery();
                    exeQuer.execute();
           //     }
                sw2.stop();
                Log.d("TIMER BUTTON", " " + sw2.getElapsedTime());
            }
        });
        arrayListAllResult = new ArrayList<String>();
//        arrayListAllResult.add("item1");
//        arrayListAllResult.add("item1");
//        arrayListAllResult.add("item1");
//        arrayListAllResult.add("item1");
//        arrayListAllResult.add("item1");
//        arrayListAllResult.add("item1");
//        arrayListAllResult.add("item1");


        // experimental

        // experimental
        // adapter = new ArrayAdapter<String>(this,
        //        android.R.layout.simple_list_item_1, arrayListAllResult);
        //gridView.setAdapter(adapter);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putStringArrayList("list", arrayListAllResult);
        outState.putInt("row", rowNumber);
        outState.putInt("column", columnNumber);

    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        arrayListAllResult = savedInstanceState.getStringArrayList("list");
        rowNumber = savedInstanceState.getInt("row");
        columnNumber = savedInstanceState.getInt("column");
        this.CreateTable(rowNumber, columnNumber);
    }

    private void CreateTable(int rowsNumber, int columnsNumber) {
        TableRow newRow;
        int numerElementu = 0;
        if (tableLayout != null) {
            tableLayout.removeAllViewsInLayout();
        }

        try {
            for (int i = 0; i <= rowsNumber; i++) {  //dla kazdego wiersza
                newRow = new TableRow(this);
                newRow.setPadding(2, 2, 2, 2);
                if (i == 0)
                    newRow.setBackgroundColor(Color.parseColor("#b8da8b")); //#92C94A // base color #6F9C33
                else if (i % 2 == 0)
                    newRow.setBackgroundColor(Color.parseColor("#e7f3d8")); //#6F9C33

                for (int j = 0; j < columnsNumber; j++) {  //dla kazdej kolumny
                    TextView tv = new TextView(this);
                    if (j % 2 != 0 && i != 0) {
                        tv.setBackgroundColor(Color.parseColor("#dbecc5"));
                    }
                    TableRow.LayoutParams params = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.MATCH_PARENT, 1f);
                    tv.setLayoutParams(params);
                    //tv.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, 0.1f));
                    //tv.setLayoutParams(new TableLayout.LayoutParams(TableLayout.LayoutParams.WRAP_CONTENT, TableLayout.LayoutParams.WRAP_CONTENT));
                    tv.setMaxWidth(1500);
                    tv.setPadding(4, 2, 4, 2);
                    tv.setText(arrayListAllResult.get(numerElementu++));
                    //  Log.d("Dodanie :", tv.getText().toString());
                    newRow.addView(tv);
                }
                tableLayout.addView(newRow);
            }
        } catch (Exception e) {
            Log.d("ERROR: ", "Problem przy tworzeniu tabeli - " + e.getMessage());
        }
        scrollView.fullScroll(ScrollView.FOCUS_UP);
        horizontalScrollView.fullScroll(HorizontalScrollView.FOCUS_UP);
    }

    public class ExecuteQuery extends AsyncTask<String, String, ArrayList<String>> {
        String z = "";
        Boolean isSuccess = false;
        int rowsNumber;
        int columnsNumber;
        Connection con;
        Stopwatch sw = new Stopwatch();

        @Override
        protected void onPreExecute() {
            progressBar.setVisibility(View.VISIBLE);
            btExecuteQuery.setEnabled(false);
            queryText.setEnabled(false);
        }

        @Override
        protected void onPostExecute(ArrayList<String> arrayList) {
            progressBar.setVisibility(View.GONE);
            btExecuteQuery.setEnabled(true);
            queryText.setEnabled(true);
            rowNumber = rowsNumber;
            columnNumber = columnsNumber;
            //conn = con;

            if (isSuccess) {
                arrayListAllResult.clear();
                arrayListAllResult.addAll(arrayList);
                // Log.d("ERROR: ", "RowNumber " + rowsNumber + " columnnumber: " + columnsNumber);
                CreateTable(rowsNumber, (columnsNumber+1));
                Toast.makeText(QueryActivity.this, "Downloaded " + rowsNumber + " rows.", Toast.LENGTH_LONG).show();
                // adapter.notifyDataSetChanged();
            } else
                Toast.makeText(QueryActivity.this, z, Toast.LENGTH_LONG).show();
        }

        @Override
        protected ArrayList<String> doInBackground(String... params) {
            ArrayList<String> arrayListInBackgroun = new ArrayList<String>();


            try {
                sw.start();
                // if(conn == null)
                //   conn = connectionClass.CONN();
                sw.stop();
                Log.d("TIMER CONN", " " + sw.getElapsedTime());

                if (((ConnectionHandler) context).getConn() == null) {
                    z = "Error in connection with SQL server";
                } else {
                    String query = queryString;

                    sw.start();
                    Statement stmt = conn.createStatement();
                    sw.stop();
                    Log.d("TIMER CREATING STMT", " " + sw.getElapsedTime());

                    sw.start();
                    ResultSet rs = stmt.executeQuery(query);
                    sw.stop();
                    Log.d("TIMER GETTING RESULTS", " " + sw.getElapsedTime());
                    ResultSetMetaData rsmd = rs.getMetaData();
                    columnsNumber = rsmd.getColumnCount();
                    rowsNumber = 0;
                    // z = rsmd.getColumnName(1);
                    //  Log.d("ERROR: ", " powykonaniu query");
                    arrayListInBackgroun.add(""); // dodanie pierwszego pustego pola do numerow wierszy

                    for (int i = 1; i <= columnsNumber; i++) {
                        if (rsmd.getColumnName(i).isEmpty())
                            arrayListInBackgroun.add("NoName");
                        else
                            arrayListInBackgroun.add(rsmd.getColumnName(i));
                        //   Log.d("ERROR: ", " tworzenie wiersza naglowka");
                    }
                    while (rs.next()) {
                        //  rs.next();
                        //  Log.d("rs.next", " petla while");

                        rowsNumber++;
                        arrayListInBackgroun.add(String.valueOf(rowsNumber)); //do numeracji wierszy
                        for (int i = 1; i <= columnsNumber; i++) {
                            if (rsmd.getColumnType(i) == Types.BLOB)
                                arrayListInBackgroun.add("BLOB");
                            else if (rs.getString(i) == null)
                                arrayListInBackgroun.add("NULL");
                            else
                                arrayListInBackgroun.add(rs.getString(i));

                        }
                        //   Log.d("ERROR: ", " po wypelnieniu listy");
                    }
                    //  Log.d("ERROR: ", " powykonaniu query v 2");
                    //  rowsNumber = rs.getRow();
                    isSuccess = true;
                    //z = "Data downloaded";
                }
            } catch (Exception ex) {
                //conn=null;
                Log.d("ERROR: ", ex.getMessage());
                isSuccess = false;
                z = "Exceptions " + ex.getMessage();
            }
            return arrayListInBackgroun;
        }
    }

    public class CreateConnection extends AsyncTask<String, String, Boolean> {
      //  @Override
//        protected void onPostExecute(Boolean bool) {
//            if (bool == true) {
////                queryString = queryText.getText().toString();
////                ExecuteQuery exeQuer = new ExecuteQuery();
////                exeQuer.execute();
//            }
//        }

        @Override
        protected Boolean doInBackground(String... strings) {
            try {
                conn = connectionClass.CONN();
                ((ConnectionHandler) context).setConn(conn);
            } catch (Exception e) {
                Toast.makeText(QueryActivity.this, "ERROR: " + e.getMessage(), Toast.LENGTH_LONG).show();
                return false;
            }
            return true;
        }
    }

}

