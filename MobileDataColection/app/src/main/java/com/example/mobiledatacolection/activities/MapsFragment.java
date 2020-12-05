package com.example.mobiledatacolection.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mobiledatacolection.R;
import com.example.mobiledatacolection.utils.TouchableWrapper;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;

import java.util.prefs.Preferences;

import static android.content.Context.MODE_PRIVATE;

public class MapsFragment extends SupportMapFragment {

    private GoogleMap mMap;
    private Location currLocation;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private static final int REQUEST_CODE = 101;
    public View mOriginalContentView;
    public TouchableWrapper mTouchView;
    public static final int LOCATION_UPDATE_MIN_DISTANCE = 10;
    public static final int LOCATION_UPDATE_MIN_TIME = 5000;
    private final int REQUEST_PERMISSION_PHONE_STATE=1;
    private LocationManager mLocationManager;
    TextView tv1, tv2;
    Bundle savedInstanceState;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        mOriginalContentView = super.onCreateView(inflater, parent, savedInstanceState);

       // mOriginalContentView =inflater.inflate(R.layout.activity_maps,parent,false);
                mTouchView = new TouchableWrapper(getActivity());
        mTouchView.addView(mOriginalContentView);
        mLocationManager =  (LocationManager) getContext().getSystemService(Context.LOCATION_SERVICE);

        return mTouchView;
    }

    @Override
    public void onCreate(Bundle bundle) {
        this.savedInstanceState = new Bundle();
        super.onCreate(bundle);
    }

    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay!
                    if (ActivityCompat.checkSelfPermission(getContext(),
                            Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                        mMap.setMyLocationEnabled(true);
                    }
                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Toast.makeText(getContext(), "permission denied", Toast.LENGTH_LONG).show();
                }
                return;
            }

        }
    }

    @Override
    public void onStart() {
        super.onStart();
        getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                mMap = googleMap;
                // TODO: Before enabling the My Location layer, you must request
                // location permission from the user. This sample does not include
                // a request for location permission.
                mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);

                if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    //User has previously accepted this permission

                    if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        // TODO: Consider calling
                        //    ActivityCompat#requestPermissions
                        // here to request the missing permissions, and then overriding
                        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                        //                                          int[] grantResults)
                        // to handle the case where the user grants the permission. See the documentation
                        // for ActivityCompat#requestPermissions for more details.
                        showPhoneStatePermission();
                        showLocationPermission();
                        showLocationFinePermission();

                    }
                    mMap.setMyLocationEnabled(true);
                    getCurrentLocation();
                } else {
                    //Not in api-23, no need to prompt
                    mMap.setMyLocationEnabled(true);
                    getCurrentLocation();

                }

                mMap.setOnMyLocationButtonClickListener(new GoogleMap.OnMyLocationButtonClickListener() {
                    @Override
                    public boolean onMyLocationButtonClick() {
                        getCurrentLocation();
                        return true;
                    }
                });
                mMap.setOnMyLocationClickListener(new GoogleMap.OnMyLocationClickListener() {
                    @Override
                    public void onMyLocationClick(@NonNull Location location) {

                    }
                });
            }
            private LocationListener mLocationListener = new LocationListener() {
                @Override
                public void onLocationChanged(Location location) {
                    if (location != null) {
                        Log.v("Location",String.format("%f, %f", location.getLatitude(), location.getLongitude()));
                        drawMarker(location);
                        mLocationManager.removeUpdates(mLocationListener);
                    } else {
                        Log.v("Location","Location is null");
                    }
                }

                @Override
                public void onStatusChanged(String s, int i, Bundle bundle) {

                }

                @Override
                public void onProviderEnabled(String s) {

                }

                @Override
                public void onProviderDisabled(String s) {

                }
            };

            private void showPhoneStatePermission() {
                int permissionCheck = ContextCompat.checkSelfPermission(
                        getContext(), Manifest.permission.READ_PHONE_STATE);
                if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
                    if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                            Manifest.permission.READ_PHONE_STATE)) {
                        showExplanation("Permission Needed", "Rationale", Manifest.permission.READ_PHONE_STATE, REQUEST_PERMISSION_PHONE_STATE);
                    } else {
                        requestPermission(Manifest.permission.READ_PHONE_STATE, REQUEST_PERMISSION_PHONE_STATE);
                    }
                } else {
                    Toast.makeText(getContext(), "Permission (already) Granted!", Toast.LENGTH_SHORT).show();
                }
            }

            private void showLocationPermission() {
                int permissionCheck = ContextCompat.checkSelfPermission(
                        getContext(), Manifest.permission.ACCESS_COARSE_LOCATION);
                if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
                    if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                            Manifest.permission.ACCESS_COARSE_LOCATION)) {
                        showExplanation("Permission Needed", "Rationale", Manifest.permission.ACCESS_COARSE_LOCATION, REQUEST_PERMISSION_PHONE_STATE);
                    } else {
                        requestPermission(Manifest.permission.ACCESS_COARSE_LOCATION, REQUEST_PERMISSION_PHONE_STATE);
                    }
                } else {
                    Toast.makeText(getContext(), "Permission (already) Granted!", Toast.LENGTH_SHORT).show();
                }
            }

            private void showLocationFinePermission() {
                int permissionCheck = ContextCompat.checkSelfPermission(
                        getContext(), Manifest.permission.ACCESS_FINE_LOCATION);
                if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
                    if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                            Manifest.permission.ACCESS_FINE_LOCATION)) {
                        showExplanation("Permission Needed", "Rationale", Manifest.permission.ACCESS_FINE_LOCATION, REQUEST_PERMISSION_PHONE_STATE);
                    } else {
                        requestPermission(Manifest.permission.ACCESS_FINE_LOCATION, REQUEST_PERMISSION_PHONE_STATE);
                    }
                } else {
                    Toast.makeText(getContext(), "Permission (already) Granted!", Toast.LENGTH_SHORT).show();
                }

            }

            private void showExplanation(String title,
                                         String message,
                                         final String permission,
                                         final int permissionRequestCode) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle(title)
                        .setMessage(message)
                        .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                requestPermission(permission, permissionRequestCode);
                            }
                        });
                builder.create().show();
            }

            private void requestPermission(String permissionName, int permissionRequestCode) {
                ActivityCompat.requestPermissions(getActivity(),
                        new String[]{permissionName}, permissionRequestCode);
            }
            private void drawMarker(Location location) {
                if (mMap != null) {
                    mMap.clear();
                    LatLng gps = new LatLng(location.getLatitude(), location.getLongitude());
                    mMap.addMarker(new MarkerOptions()
                            .position(gps)
                            .title("Current Position"));
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(gps, 12));
                }

            }

            private void getCurrentLocation() {
                boolean isGPSEnabled = mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
                boolean isNetworkEnabled = mLocationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

                Location location = null;
                if (!(isGPSEnabled || isNetworkEnabled))
                    Snackbar.make(mOriginalContentView, R.string.error_location_provider, Snackbar.LENGTH_INDEFINITE).show();
                else {
                    if (isNetworkEnabled && ActivityCompat.checkSelfPermission(getContext(),
                            Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                        mLocationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,
                                LOCATION_UPDATE_MIN_TIME, LOCATION_UPDATE_MIN_DISTANCE, mLocationListener);
                        location = mLocationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                    }

                    if (isGPSEnabled && ActivityCompat.checkSelfPermission(getContext(),
                            Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                        mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                                LOCATION_UPDATE_MIN_TIME, LOCATION_UPDATE_MIN_DISTANCE, mLocationListener);
                        location = mLocationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                    }
                }
                if (location != null) {
                    SharedPreferences preferencesServer = getContext().getSharedPreferences("com.example.mobiledatacolection_preferences", MODE_PRIVATE);
                    SharedPreferences.Editor editor = preferencesServer.edit();
                    editor.putLong("Latitude", Double.doubleToLongBits(location.getLatitude()));
                    editor.putLong("Longitude", Double.doubleToLongBits(location.getLongitude()));
                   editor.commit();
                    Log.v("Location", String.format("getCurrentLocation(%f, %f)", location.getLatitude(), location.getLongitude()));
                    drawMarker(location);
                    mMap.getUiSettings().setZoomControlsEnabled(true);
                    mMap.animateCamera(CameraUpdateFactory.zoomTo(15), 2000, null);
                }
            }
        });
    }

    @Override
    public View getView() {
        return mOriginalContentView;
    }

    @Override
    public void onDestroy() {
        System.out.println("onDestroyFragment");
        super.onDestroy();
    }

    @Override
    public void onDestroyView() {
        System.out.println("onDestroyFragment");
        super.onDestroyView();
    }
}