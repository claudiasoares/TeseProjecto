package com.example.mobiledatacolection.activities;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.Preference;
import androidx.preference.PreferenceDataStore;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceManager;

import com.example.mobiledatacolection.R;

public class SettingsActivity extends AppCompatActivity {

    private String server;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_activity);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.settings, new SettingsFragment())
                .commit();
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);

        }



    }



    public static class SettingsFragment extends PreferenceFragmentCompat {
        private String server;
        private SharedPreferences preferencesServer;

        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            setPreferencesFromResource(R.xml.root_preferences, rootKey);
            Context context;
            preferencesServer = getContext().getSharedPreferences("com.example.mobiledatacolection_preferences", MODE_PRIVATE);


        }


        @Override
        public void onResume() {
            super.onResume();
        }

        @Override
        public void onPause() {
            super.onPause();
        }

        @Override
        public void onDestroy() {
            super.onDestroy();

            SharedPreferences.Editor editor = preferencesServer.edit();
            editor.putString("server", preferencesServer.getString("server",""));
            editor.commit();
            Toast.makeText(getContext(), preferencesServer.getString("server",""),Toast.LENGTH_SHORT).show();
        }
    }
}

