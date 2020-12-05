package com.example.mobiledatacolection.widget.utils;

import android.app.ActivityManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.widget.Toast;
import com.example.mobiledatacolection.R;
import com.example.mobiledatacolection.fragmentos.widget.MapPoint;
import com.example.mobiledatacolection.widget.geo.MapFragment;
import com.example.mobiledatacolection.widget.geo.MapboxMapFragment;
import com.google.common.collect.ImmutableSet;

import java.io.File;
import java.util.List;
import java.util.Set;

import static com.example.mobiledatacolection.fragmentos.widget.MapFragment.KEY_REFERENCE_LAYER;
import static com.google.android.gms.common.GooglePlayServicesUtil.isGooglePlayServicesAvailable;

public class GoogleMapConfigurator implements MapConfigurator {

      private  String prefKey;
        private  int sourceLabelId;
        private  GoogleMapTypeOption[] options;

        /** Constructs a configurator with a few Google map type options to choose from. */
        public GoogleMapConfigurator(String prefKey, int sourceLabelId, String keyGoogleMapStyle, int basemap_source_google, com.example.mobiledatacolection.widget.geo.GoogleMapConfigurator.GoogleMapTypeOption option, com.example.mobiledatacolection.widget.geo.GoogleMapConfigurator.GoogleMapTypeOption typeOption, com.example.mobiledatacolection.widget.geo.GoogleMapConfigurator.GoogleMapTypeOption mapTypeOption, com.example.mobiledatacolection.widget.geo.GoogleMapConfigurator.GoogleMapTypeOption googleMapTypeOption, GoogleMapTypeOption... options) {
            this.prefKey = prefKey;
            this.sourceLabelId = sourceLabelId;
            this.options = options;
        }

    public GoogleMapConfigurator(String keyGoogleMapStyle, int basemap_source_google, com.example.mobiledatacolection.widget.geo.GoogleMapConfigurator.GoogleMapTypeOption googleMapTypeOption, com.example.mobiledatacolection.widget.geo.GoogleMapConfigurator.GoogleMapTypeOption googleMapTypeOption1, com.example.mobiledatacolection.widget.geo.GoogleMapConfigurator.GoogleMapTypeOption googleMapTypeOption2, com.example.mobiledatacolection.widget.geo.GoogleMapConfigurator.GoogleMapTypeOption googleMapTypeOption3) {

    }

    @Override public boolean isAvailable(Context context) {
            return isGoogleMapsSdkAvailable(context) ;
        }

        @Override public void showUnavailableMessage(Context context) {
            if (!isGoogleMapsSdkAvailable(context)) {
                Toast.makeText(context, context.getString(R.string.googleMapsSdkAvailable) ,Toast.LENGTH_LONG);
            }
        }

        private boolean isGoogleMapsSdkAvailable(Context context) {
            // The Google Maps SDK for Android requires OpenGL ES version 2.
            // See https://developers.google.com/maps/documentation/android-sdk/config
            return ((ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE))
                    .getDeviceConfigurationInfo().reqGlEsVersion >= 0x20000;
        }



        @Override public MapFragment createMapFragment(Context context) {
            return new MapboxMapFragment();
        }

        public List<Preference> createPrefs(Context context) {
            return null;
        }

        @Override public Set<String> getPrefKeys() {
            return prefKey.isEmpty() ? ImmutableSet.of(KEY_REFERENCE_LAYER) :
                    ImmutableSet.of(prefKey, KEY_REFERENCE_LAYER);
        }

        @Override public Bundle buildConfig(SharedPreferences prefs) {
            Bundle config = new Bundle();
            config.putString("REFERENCE_LAYER",
                    prefs.getString(KEY_REFERENCE_LAYER, null));
            return config;
        }

        @Override public boolean supportsLayer(File file) {
            // GoogleMapFragment supports only raster tiles.
            return true;
        }

        @Override public String getDisplayName(File file) {
            return file.getName();
        }

        static class GoogleMapTypeOption {
            final int mapType;
            final int labelId;

            GoogleMapTypeOption(int mapType, int labelId) {
                this.mapType = mapType;
                this.labelId = labelId;
            }
        }
    }

