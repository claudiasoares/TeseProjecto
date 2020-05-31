package com.example.mobiledatacolection.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.Preference;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.mobiledatacolection.R;
import com.example.mobiledatacolection.http.HttpGetRequest;
import com.example.mobiledatacolection.model.User;
import com.example.mobiledatacolection.sqlLite.SQLLiteDBHelper;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity {

    private static final String DEBUG_TAG = "";
    private Button loginButton;

    static final int PICK_FORM_REQUEST = 1;  // The request code
    private EditText emailText;
    private EditText passwordText;
    private Button buttonTest;
    private SharedPreferences preferencesServer;
    private EditText companyText;
    private String server;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        preferencesServer = this.getSharedPreferences("com.example.mobiledatacolection_preferences", MODE_PRIVATE);
        server = preferencesServer.getString("server","");

        emailText   = (EditText)findViewById(R.id.emailText);
        passwordText   = (EditText)findViewById(R.id.passwordText);
        companyText = (EditText)findViewById(R.id.companyText);

        loginButton = (Button) findViewById(R.id.loginbutton) ;
        loginButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                login(v);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.settings:
                Intent intent = new Intent(this,SettingsActivity.class );
                startActivity(intent);
                break;
                default:  Toast.makeText(getApplicationContext(),"Settings Default",Toast.LENGTH_SHORT).show();

        }

        return super.onOptionsItemSelected(item);
    }

    private void login(View v){
        Editable username = emailText.getText();
        Editable password = passwordText.getText();
        Editable company = companyText.getText();
        if(existUser(new User(username.toString(), password.toString(), company.toString()))){
            // passar para outra activity
            Intent intent = new Intent(this, MenuActivity.class);
            intent.putExtra("COMPANY",companyText.getText().toString());
            intent.putExtra("USERNAME",emailText.getText().toString());
            startActivity(intent);
        }
        else {
            if(hasConectivity()){
                if(getUserFromServer()){
                    // passar para outra activity
                    Intent intent = new Intent(this, MenuActivity.class);
                    intent.putExtra("COMPANY",companyText.getText().toString());
                    intent.putExtra("USERNAME",emailText.getText().toString());
                    startActivity(intent);
                }
            }else{

            }
        }


    }

    private boolean getUserFromServer() {
        String username = emailText.getText().toString();
        String password = passwordText.getText().toString();
        String company = companyText.getText().toString();
        boolean existsUser = false;
        // Instantiate the RequestQueue.
        //Some url endpoint that you may have
        String myUrl = server + "api/allusers";
        //String to place our result in
        String result;
        //Instantiate new instance of our class
        HttpGetRequest getRequest = new HttpGetRequest();
        //Perform the doInBackground method, passing in our url
        try {
            result = getRequest.execute(myUrl).get();
            JSONObject obj = new JSONObject(result);
            String state = obj.getJSONObject("obj").getString("state");
            if(state.equals("OK")){
                JSONArray users = obj.getJSONObject("obj").getJSONArray("users");
                List<User> listUsers = User.parseUserData(users);
                saveToDB(listUsers);
                existsUser = listUsers.contains(new User(username, password, company));

            }else{
                Toast.makeText(this, "Connection Failed on Server!",Toast.LENGTH_SHORT).show();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return existsUser;
    }

    private boolean hasConectivity() {
        ConnectivityManager connMgr =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        boolean isWifiConn = false;
        boolean isMobileConn = false;
        for (Network network : connMgr.getAllNetworks()) {
            NetworkInfo networkInfo = connMgr.getNetworkInfo(network);
            if (networkInfo.getType() == ConnectivityManager.TYPE_WIFI) {
                isWifiConn |= networkInfo.isConnected();
            }
            if (networkInfo.getType() == ConnectivityManager.TYPE_MOBILE) {
                isMobileConn |= networkInfo.isConnected();
            }
        }
        Log.d(DEBUG_TAG, "Wifi connected: " + isWifiConn);
        Log.d(DEBUG_TAG, "Mobile connected: " + isMobileConn);
        return isMobileConn || isWifiConn;
    }

    private boolean existUser(User user) {
        int exists = readFromDB();
        if(exists == 0 ) return false;
        return true;
    }

    private void saveToDB(List<User> user) {
        SQLiteDatabase database = new SQLLiteDBHelper(this).getWritableDatabase();
        for (int i =0; i < user.size(); ++i){
            ContentValues values = new ContentValues();
            values.put(SQLLiteDBHelper.USER_COLUMN_USERNAME, user.get(i).getUsername());
            values.put(SQLLiteDBHelper.USER_COLUMN_COMPANY, user.get(i).getCompany());
            values.put(SQLLiteDBHelper.USER_COLUMN_PASSWORD, user.get(i).getPassword());
            long newRowId = database.insert(SQLLiteDBHelper.USER_TABLE_NAME, null, values);

            Toast.makeText(this, "The new Row Id is " + newRowId, Toast.LENGTH_LONG).show();
        }

    }

    private int readFromDB() {
        String username =  emailText.getText().toString();
        String company = companyText.getText().toString();
        String password = passwordText.getText().toString();

        SQLiteDatabase database = new SQLLiteDBHelper(this).getReadableDatabase();

        String[] projection = {
                SQLLiteDBHelper.USER_COLUMN_ID,
                SQLLiteDBHelper.USER_COLUMN_USERNAME,
                SQLLiteDBHelper.USER_COLUMN_COMPANY,
                SQLLiteDBHelper.USER_COLUMN_PASSWORD
        };

        String selection =
                SQLLiteDBHelper.USER_COLUMN_USERNAME + " like ? and " +
                        SQLLiteDBHelper.USER_COLUMN_COMPANY + " like ? and " +
                        SQLLiteDBHelper.USER_COLUMN_PASSWORD + " like ?";

        String[] selectionArgs = {"%" + username + "%", company, "%" + password + "%"};

        Cursor cursor = database.query(
                SQLLiteDBHelper.USER_TABLE_NAME,   // The table to query
                projection,                               // The columns to return
                selection,                                // The columns for the WHERE clause
                selectionArgs,                            // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                null                                      // don't sort
        );

        Log.d("TAG", "The total cursor count is " + cursor.getCount());
        return  cursor.getCount();
    }
}
