package com.example.mobiledatacolection.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.mobiledatacolection.R;

public class MainActivity extends AppCompatActivity {

    private Button loginButton;

    static final int PICK_FORM_REQUEST = 1;  // The request code
    private EditText emailText;
    private EditText passwordText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        emailText   = (EditText)findViewById(R.id.emailText);
        passwordText   = (EditText)findViewById(R.id.passwordText);

        loginButton = (Button) findViewById(R.id.loginbutton) ;
        loginButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                login(v);
            }
        });

    }


    private void login(View v){
        Editable email = emailText.getText();
        Editable password = passwordText.getText();
        // passar para outra activity
        Intent intent = new Intent(this, MenuActivity.class);
        startActivity(intent);
    }
}
