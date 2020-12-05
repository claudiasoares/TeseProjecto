package com.example.mobiledatacolection.widget.geo;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;

import androidx.preference.Preference;

import com.example.mobiledatacolection.R;
import com.google.common.collect.ImmutableSet;
import com.mapbox.mapboxsdk.maps.Style;

import java.io.File;
import java.util.List;
import java.util.Set;

import static com.example.mobiledatacolection.preferences.GeneralKeys.KEY_MAPBOX_MAP_STYLE;
import static com.example.mobiledatacolection.widget.geo.MapFragment.KEY_REFERENCE_LAYER;

public class MapboxMapConfigurator implements MapConfigurator, com.example.mobiledatacolection.widget.utils.MapConfigurator {
    private final String prefKey;
    private final int sourceLabelId;
    private final MapboxUrlOption[] options;

    /** Constructs a configurator with a few Mapbox style URL options to choose from. */
    public MapboxMapConfigurator(String prefKey, int sourceLabelId, MapboxUrlOption... options) {
        this.prefKey = prefKey;
        this.sourceLabelId = sourceLabelId;
        this.options = options;
    }

    @Override public boolean isAvailable(Context context) {
        return MapboxUtils.initMapbox() != null;
    }

    @Override public void showUnavailableMessage(Context context) {
        Toast.makeText(context,context.getString(
            R.string.basemap_source_unavailable, context.getString(sourceLabelId)),Toast.LENGTH_LONG);
    }

    @Override public MapFragment createMapFragment(Context context) {
        return MapboxUtils.initMapbox() != null ? new MapboxMapFragment() : null;
    }

    @Override
    public List<Preference> createPrefs(Context context) {
        return null;
    }


    @Override public Set<String> getPrefKeys() {
        return prefKey.isEmpty() ? ImmutableSet.of(KEY_REFERENCE_LAYER) :
            ImmutableSet.of(prefKey, KEY_REFERENCE_LAYER);
    }

    @Override public Bundle buildConfig(SharedPreferences prefs) {
        Bundle config = new Bundle();
        config.putString(MapboxMapFragment.KEY_STYLE_URL,
            prefs.getString(KEY_MAPBOX_MAP_STYLE, Style.MAPBOX_STREETS));
        config.putString(KEY_REFERENCE_LAYER,
            prefs.getString(KEY_REFERENCE_LAYER, null));
        return config;
    }

    @Override public boolean supportsLayer(File file) {
        // MapboxMapFragment supports any file that MbtilesFile can read.
        return MbtilesFile.readLayerType(file) != null;
    }

    @Override public String getDisplayName(File file) {
        String name = MbtilesFile.readName(file);
        return name != null ? name : file.getName();
    }

    public static class MapboxUrlOption {
        final String url;
        final int labelId;

        public MapboxUrlOption(String url, int labelId) {
            this.url = url;
            this.labelId = labelId;
        }
    }
}
