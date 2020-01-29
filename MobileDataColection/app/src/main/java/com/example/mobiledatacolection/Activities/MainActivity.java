package com.example.mobiledatacolection.Activities;

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


       // Intent intent = new Intent(Intent.ACTION_VIEW);
      //  intent.setType("vnd.android.cursor.dir/vnd.odk.form");
     //   startActivity(intent);
        //startActivityForResult(intent, PICK_FORM_REQUEST);

      /*  Intent intent1 = new Intent(Intent.ACTION_PICK);
        intent1.setType("vnd.android.cursor.dir/vnd.odk.form");

        intent1.setType("vnd.android.cursor.dir/vnd.odk.instance");

        Intent intent2 = new Intent(Intent.ACTION_EDIT);
        intent2.setData("content://org.odk.collect.android.provider.odk.forms/forms/2");
        startActivity(intent2);*/
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Check which request we're responding to
        if (requestCode == PICK_FORM_REQUEST) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {
                // The Intent's data URI identifies which form was selected.
                Uri formUri = data.getData();
                // Do something with the form here
            }
        }
    }

    private View.OnClickListener login(View v){
        Editable email = emailText.getText();
        Editable password = passwordText.getText();
        // passar para outra activity
        Intent intent = new Intent(this, MenuActivity.class);
        startActivity(intent);
        return null;
    }
}
