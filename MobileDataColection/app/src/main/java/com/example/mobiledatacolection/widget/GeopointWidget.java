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
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.location.Location;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;


import com.example.mobiledatacolection.R;
import com.example.mobiledatacolection.activities.FormActivity;
import com.example.mobiledatacolection.activities.MapsActivity;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMapOptions;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DatabaseReference;

import org.javarosa.core.model.QuestionDef;
import org.javarosa.core.util.Map;
import org.javarosa.form.api.FormEntryController;
import org.javarosa.form.api.FormEntryPrompt;

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
    private DatabaseReference databaseReference;
    private MapFragment map;

    private double accuracyThreshold;


    public GeopointWidget(Context context, LinearLayout screen, QuestionDef form, FormEntryPrompt fep, int version, DatabaseReference databaseReference) {
        //  determineMapProperties();
        this.questionDef = form;
        this.linearlayout = screen;
        this.context = context;
        this.databaseReference = databaseReference;
        String name = form.getLabelInnerText() == null ? form.getTextID().split("/")[2].split(":")[0] : form.getLabelInnerText();
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

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(context);

                 LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE); // or (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                // View viewMyLayout = inflater.inflate(R.layout.activity_maps, null);
                // LayoutInflater inflater = LayoutInflater.from(MapsActivity.this).inflate(R.layout.activity_maps,null);
                final View customLayout = inflater.inflate(R.layout.activity_maps, null);

                builder.setView(customLayout);



               // builder.setView(R.layout.activity_maps);

                builder.setTitle("Here");

                builder.show();
            }
        });
        linearLayout1.addView(textView);
        linearLayout1.addView(button);
        this.linearlayout.addView(linearLayout1);

       /* FragmentManager fm = ((FormActivity)context).getFragmentManager();
        map = (MapFragment) fm.findFragmentById(R.id.map);
        if (map == null) {
            map = MapFragment.newInstance();
            fm.beginTransaction().replace(R.id.map, map).commit();

        map.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    googleMap.setMyLocationEnabled(true);
                    MarkerOptions markerOptions = new MarkerOptions();
                    markerOptions.title("Location");

                    googleMap.addMarker(markerOptions);
                    return;
                }


            }

        });  }*/

}

    public LinearLayout getElement(){
        return this.linearlayout;
    }

  /*  private void determineMapProperties() {
        // Determine the accuracy threshold to use.
        String acc = questionDef.getAdditionalAttribute(null, ACCURACY_THRESHOLD);
        accuracyThreshold = acc != null && !acc.isEmpty() ? Double.parseDouble(acc) : DEFAULT_LOCATION_ACCURACY;

        // Determine whether to use the map and whether the point should be draggable.
        if (MapProvider.getConfigurator().isAvailable(getContext())) {
            if (hasAppearance(getFormEntryPrompt(), PLACEMENT_MAP)) {
                draggable = true;
                useMap = true;
            } else if (hasAppearance(getFormEntryPrompt(), MAPS)) {
                draggable = false;
                useMap = true;
            }
        }
    }

    public void updateButtonLabelsAndVisibility(boolean dataAvailable) {
        if (useMap) {
            if (readOnly) {
                startGeoButton.setText(R.string.geopoint_view_read_only);
            } else {
                startGeoButton.setText(
                        dataAvailable ? R.string.view_change_location : R.string.get_point);
            }
        } else {
            if (!readOnly) {
                startGeoButton.setText(
                        dataAvailable ? R.string.change_location : R.string.get_point);
            }
        }
    }*/
}
