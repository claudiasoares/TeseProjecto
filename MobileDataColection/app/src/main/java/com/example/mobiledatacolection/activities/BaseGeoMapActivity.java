package com.example.mobiledatacolection.activities;

import android.os.Bundle;
import android.widget.Toast;


import com.example.mobiledatacolection.R;

import static com.example.mobiledatacolection.utils.PermissionUtils.areLocationPermissionsGranted;

/**
 * Implementation details common to the geo activities.  (After the migration
 * to storing user selections in the preferences, there's not a lot left here,
 * though this will probably grow as we add more geospatial capabilities.)
 */
public abstract class BaseGeoMapActivity extends CollectAbstractActivity {
    protected Bundle previousState;

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        previousState = savedInstanceState;

        if (!areLocationPermissionsGranted(this)) {
            Toast.makeText(this, R.string.not_granted_permission,Toast.LENGTH_SHORT);
            finish();
        }
    }
}
