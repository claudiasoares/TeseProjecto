package com.example.mobiledatacolection.widget;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.location.Location;
import android.os.Build;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;


import com.example.mobiledatacolection.R;
import com.example.mobiledatacolection.activities.FormActivity;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMapOptions;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import org.javarosa.core.model.QuestionDef;
import org.javarosa.core.util.Map;
import org.javarosa.form.api.FormEntryController;
import org.javarosa.form.api.FormEntryPrompt;

import java.util.prefs.Preferences;

import static android.content.Context.MODE_PRIVATE;
import static java.security.AccessController.getContext;

public class GeopointWidget {
    private final Context context;
    private final LinearLayout linearlayout;
    private final QuestionDef questionDef;
    public static final String LOCATION = "gp";
    public static final String ACCURACY_THRESHOLD = "accuracyThreshold";
    public static final String READ_ONLY = "readOnly";
    public static final String DRAGGABLE_ONLY = "draggable";

    public static final double DEFAULT_LOCATION_ACCURACY = 5.0;
    private final TextView textView;
    private final Button button;
    private final TextView tv1res;
    private final TextView tv2res;
    private DatabaseReference databaseReference;
    private MapFragment map;
    private TextView tv2,tv1;
    long last_text_edit = 0;
    Handler handler = new Handler();
    long delay = 1000; // 1 seconds after user stops typing

    private double accuracyThreshold;


    public GeopointWidget(Context context, LinearLayout screen, QuestionDef form, FormEntryPrompt fep, int version,DatabaseReference databaseReference) {
        //  determineMapProperties();
        this.questionDef = form;
        this.linearlayout = screen;
        this.context = context;
        this.databaseReference = databaseReference;
        String name = fep.mTreeElement.getName();
                // form.getLabelInnerText() == null ? form.getTextID().split("/")[2].split(":")[0] : form.getLabelInnerText().split("/")[2].split(":")[0];
        LinearLayout linearLayout1 = new LinearLayout(context);
        linearLayout1.setOrientation(LinearLayout.HORIZONTAL);
        LinearLayout.LayoutParams params
                = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        linearLayout1.setLayoutParams(params);
        params.setMargins(0, 5, 5, 0);

        textView = new TextView(context);
        textView.setTextColor(Color.BLACK);
        textView.setTypeface(Typeface.DEFAULT_BOLD);
        textView.setText(name);
        LinearLayout.LayoutParams paramsTextView
                = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, 1);
        linearLayout1.setLayoutParams(params);
        textView.setLayoutParams(paramsTextView);

        button = new Button(context);
        LinearLayout.LayoutParams paramsButton
                = new LinearLayout.LayoutParams(40, 100, 1);
        linearLayout1.setLayoutParams(params);
        button.setLayoutParams(paramsButton);
        button.setBackgroundColor(Color.LTGRAY);
        button.setText("Select " + name);



        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE); // or (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        // View viewMyLayout = inflater.inflate(R.layout.activity_maps, null);
        // LayoutInflater inflater = LayoutInflater.from(MapsActivity.this).inflate(R.layout.activity_maps,null);

        final View customLayout = inflater.inflate(R.layout.activity_maps, null);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                builder.setTitle("Location")
                        .setView(customLayout)
                        .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                                SharedPreferences preferencesServer = context.getSharedPreferences("com.example.mobiledatacolection_preferences", MODE_PRIVATE);
                                long longitude = preferencesServer.getLong("Longitude", 0);
                                long latitude = preferencesServer.getLong("Latitude", 0);
                                tv2res.setText(Double.longBitsToDouble(longitude) + "");

                                tv1res.setText(Double.longBitsToDouble(latitude)+ "");
                                if(customLayout.getParent() != null) {
                                    ((ViewGroup)customLayout.getParent()).removeView(customLayout);
                                }
                                System.out.println(id);
                            }
                        });



                builder.create().show();

            }
        });
        if(textView.getParent() != null) {
            ((ViewGroup)textView.getParent()).removeView(textView); // <- fix
        }
        if(button.getParent() != null) {
            ((ViewGroup)button.getParent()).removeView(button); // <- fix
        }
        tv1 = new TextView(context);
        tv1.setText("Latitude: ");
        tv1res = new TextView(context);
        tv2 = new TextView(context);
        tv2.setText("Longitude: ");
        tv2res = new TextView(context);


        databaseReference.child(tv1.getText().toString()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                tv1res.setText(dataSnapshot.getValue() == null ? "" : dataSnapshot.getValue().toString());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        databaseReference.child(tv2.getText().toString()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                tv2res.setText(dataSnapshot.getValue() == null ? "" : dataSnapshot.getValue().toString());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        tv1res.addTextChangedListener(new TextWatcher() {
                   @Override
                   public void beforeTextChanged (CharSequence s,int start, int count,
                                                  int after){
                   }
                   @Override
                   public void onTextChanged ( final CharSequence s, int start, int before,
                                               int count){
                       //You need to remove this to run only once
                       handler.removeCallbacks(input_finish_checker);

                   }
                   @Override
                   public void afterTextChanged ( final Editable s){
                       //avoid triggering event when text is empty
                       if (s.length() > 0) {
                           last_text_edit = System.currentTimeMillis();
                           handler.postDelayed(input_finish_checker, delay);
                       } else {

                       }
                   }
               }
        );
        tv2res.addTextChangedListener(new TextWatcher() {
              @Override
              public void beforeTextChanged (CharSequence s,int start, int count,
                                             int after){
              }
              @Override
              public void onTextChanged ( final CharSequence s, int start, int before,
                                          int count){
                  //You need to remove this to run only once
                  handler.removeCallbacks(input_finish_checker2);

              }
              @Override
              public void afterTextChanged ( final Editable s){
                  //avoid triggering event when text is empty
                  if (s.length() > 0) {
                      last_text_edit = System.currentTimeMillis();
                      handler.postDelayed(input_finish_checker2, delay);
                  } else {

                  }
              }
          }
        );

        linearLayout1.addView(textView);
        linearLayout1.addView(tv1);
        linearLayout1.addView(tv1res);
        linearLayout1.addView(tv2);
        linearLayout1.addView(tv2res);
        linearLayout1.addView(button);
        this.linearlayout.addView(linearLayout1);

    }

    private Runnable input_finish_checker = new Runnable() {
        public void run() {
            if (System.currentTimeMillis() > (last_text_edit + delay - 500)) {
                final DatabaseReference connectedRef = databaseReference.getDatabase().getReference(".info/connected");
                connectedRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot snapshot) {
                        boolean connected = snapshot.getValue(Boolean.class);
                        if (connected) {
                            databaseReference.child(tv1.getText().toString()).onDisconnect().setValue(tv1res.getText().toString());
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                    }
                });
            }
        }
    };

    private Runnable input_finish_checker2 = new Runnable() {
        public void run() {
            if (System.currentTimeMillis() > (last_text_edit + delay - 500)) {
                final DatabaseReference connectedRef = databaseReference.getDatabase().getReference(".info/connected");
                connectedRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot snapshot) {
                        boolean connected = snapshot.getValue(Boolean.class);
                        if (connected) {
                            databaseReference.child(tv2.getText().toString()).onDisconnect().setValue(tv2res.getText().toString());
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                    }
                });
            }
        }
    };
    public LinearLayout getElement() {
        return this.linearlayout;
    }

}
