package com.example.mobiledatacolection.widget.utils;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.provider.Settings;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.VisibleForTesting;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import com.example.mobiledatacolection.R;
import com.example.mobiledatacolection.adapters.LocationClient;
import com.example.mobiledatacolection.adapters.LocationClientProvider;
import com.example.mobiledatacolection.dragger.DaggerUtils;
import com.example.mobiledatacolection.fragmentos.widget.MapFragment;
import com.example.mobiledatacolection.fragmentos.widget.MapPoint;
import com.example.mobiledatacolection.utils.GoogleFusedLocationClient;
import com.example.mobiledatacolection.utils.IconUtils;
import com.example.mobiledatacolection.utils.MapProvider;
import com.example.mobiledatacolection.utils.PlayServicesChecker;
import com.example.mobiledatacolection.widget.geo.GoogleMapsMapBoxOfflineTileProvider;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.maps.model.TileOverlay;
import com.google.android.gms.maps.model.TileOverlayOptions;


import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import timber.log.Timber;

import static com.example.mobiledatacolection.fragmentos.widget.MapFragment.POINT_ZOOM;

public class GoogleMapFragment extends SupportMapFragment implements
        LocationListener, LocationClient.LocationClientListener,
        GoogleMap.OnMapClickListener, GoogleMap.OnMapLongClickListener,
        GoogleMap.OnMarkerClickListener, GoogleMap.OnMarkerDragListener,
        GoogleMap.OnPolylineClickListener {

    // Bundle keys understood by applyConfig().
    static final String KEY_MAP_TYPE = "MAP_TYPE";

    @Inject
    MapProvider mapProvider;
    private GoogleMap map;
    private Marker locationCrosshairs;
    private Circle accuracyCircle;
    private final List<com.example.mobiledatacolection.widget.geo.MapFragment.ReadyListener> gpsLocationReadyListeners = new ArrayList<>();
    private com.example.mobiledatacolection.widget.geo.MapFragment.PointListener clickListener;
    private com.example.mobiledatacolection.widget.geo.MapFragment.PointListener longPressListener;
    private com.example.mobiledatacolection.widget.geo.MapFragment.PointListener gpsLocationListener;
    private com.example.mobiledatacolection.widget.geo.MapFragment.FeatureListener featureClickListener;
    private com.example.mobiledatacolection.widget.geo.MapFragment.FeatureListener dragEndListener;

    private LocationClient locationClient;
    private boolean clientWantsLocationUpdates;
    private MapPoint lastLocationFix;
    private String lastLocationProvider;

    private int nextFeatureId = 1;
    private final Map<Integer, MapFeature> features = new HashMap<>();
    private int mapType;
    private File referenceLayerFile;
    private TileOverlay referenceOverlay;

    // During Robolectric tests, Google Play Services is unavailable; sadly, the
    // "map" field will be null and many operations will need to be stubbed out.
    @VisibleForTesting
    public static boolean testMode;


    public void addTo(
            @NonNull FragmentActivity activity, int containerId,
            @Nullable com.example.mobiledatacolection.widget.geo.MapFragment.ReadyListener readyListener, @Nullable com.example.mobiledatacolection.widget.geo.MapFragment.ErrorListener errorListener) {
        // If the containing activity is being re-created upon screen rotation,
        // the FragmentManager will have also re-created a copy of the previous
        // GoogleMapFragment.  We don't want these useless copies of old fragments
        // to linger, so the following line calls .replace() instead of .add().
        activity.getSupportFragmentManager()
                .beginTransaction().replace(containerId, this).commitNow();
        getMapAsync((GoogleMap map) -> {
            if (map == null) {
                Toast.makeText(getContext(), R.string.google_play_services_error_occured, Toast.LENGTH_SHORT);
                if (errorListener != null) {
                    errorListener.onError();
                }
                return;
            }
            this.map = map;
            map.setMapType(mapType);
            map.setOnMapClickListener(this);
            map.setOnMapLongClickListener(this);
            map.setOnMarkerClickListener(this);
            map.setOnPolylineClickListener(this);
            map.setOnMarkerDragListener(this);
            map.getUiSettings().setCompassEnabled(true);
            // Don't show the blue dot on the map; we'll draw crosshairs instead.
            if ((ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) && (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            map.setMyLocationEnabled(false);
            map.setMinZoomPreference(1);
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(
                    toLatLng(MapFragment.INITIAL_CENTER), MapFragment.INITIAL_ZOOM));
            loadReferenceOverlay();

            // If the screen is rotated before the map is ready, this fragment
            // could already be detached, which makes it unsafe to use.  Only
            // call the ReadyListener if this fragment is still attached.
            if (readyListener != null && getActivity() != null) {
                readyListener.onReady((com.example.mobiledatacolection.widget.geo.MapFragment) this);
            }
        });

        // In Robolectric tests, getMapAsync() never gets around to calling its
        // callback; we have to invoke the ready listener directly.
        if (testMode && readyListener != null) {
            readyListener.onReady((com.example.mobiledatacolection.widget.geo.MapFragment) this);
        }
    }

    @Override public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        DaggerUtils.getComponent(context).inject(this);
    }

    @Override public void onStart() {
        super.onStart();
        mapProvider.onMapFragmentStart((MapFragment) this);
        enableLocationUpdates(clientWantsLocationUpdates);
    }

    @Override public void onStop() {
        enableLocationUpdates(false);
        mapProvider.onMapFragmentStop((MapFragment) this);
        super.onStop();
    }




    @Override public void onLocationChanged(Location location) {
        Timber.i("onLocationChanged: location = %s", location);
        lastLocationFix = fromLocation(location);
        lastLocationProvider = location.getProvider();
        for (com.example.mobiledatacolection.widget.geo.MapFragment.ReadyListener listener : gpsLocationReadyListeners) {
            listener.onReady((com.example.mobiledatacolection.widget.geo.MapFragment) this);
        }
        gpsLocationReadyListeners.clear();
        if (gpsLocationListener != null) {
            gpsLocationListener.onPoint(lastLocationFix);
        }

        if (getActivity() != null) {
            updateLocationIndicator(toLatLng(lastLocationFix), location.getAccuracy());
        }
    }


    @Override public void onMapClick(LatLng latLng) {
        if (clickListener != null) {
            clickListener.onPoint(fromLatLng(latLng));
        }
    }

    @Override public void onMapLongClick(LatLng latLng) {
        if (longPressListener != null) {
            longPressListener.onPoint(fromLatLng(latLng));
        }
    }

    @Override public boolean onMarkerClick(Marker marker) {
        if (featureClickListener != null) { // FormMapActivity
            featureClickListener.onFeature(findFeature(marker));
        } else { // GeoWidget
            onMapClick(marker.getPosition());
        }
        return true;  // consume the event (no default zoom and popup behaviour)
    }

    @Override public void onPolylineClick(Polyline polyline) {
        if (featureClickListener != null) {
            featureClickListener.onFeature(findFeature(polyline));
        }
    }

    @Override public void onMarkerDragStart(Marker marker) {
        // When dragging starts, GoogleMap makes the marker jump up to move it
        // out from under the user's finger; whenever a marker moves, we have
        // to update its corresponding feature.
        updateFeature(findFeature(marker));
    }

    @Override public void onMarkerDrag(Marker marker) {
        // When a marker is manually dragged, the position is no longer
        // obtained from a GPS reading, so the altitude and standard deviation
        // fields are no longer meaningful; reset them to zero.
        marker.setSnippet("0;0");
        updateFeature(findFeature(marker));
    }

    @Override public void onMarkerDragEnd(Marker marker) {
        int featureId = findFeature(marker);
        updateFeature(featureId);
        if (dragEndListener != null && featureId != -1) {
            dragEndListener.onFeature(featureId);
        }
    }

    @Override public void onClientStart() {
        lastLocationFix = fromLocation(locationClient.getLastLocation());
        Timber.i("Requesting location updates (to %s)", this);
        locationClient.requestLocationUpdates(this);
        if (!locationClient.isLocationAvailable()) {
            showGpsDisabledAlert();
        }
    }

    @Override public void onClientStartFailure() {
        showGpsDisabledAlert();
    }

    @Override public void onClientStop() {
        Timber.i("Stopping location updates (to %s)", this);
        locationClient.stopLocationUpdates();
    }

    private static @NonNull MapPoint fromLatLng(@NonNull LatLng latLng) {
        return new MapPoint(latLng.latitude, latLng.longitude);
    }

    private static @Nullable MapPoint fromLocation(@Nullable Location location) {
        if (location == null) {
            return null;
        }
        return new MapPoint(location.getLatitude(), location.getLongitude(),
                location.getAltitude(), location.getAccuracy());
    }

    private static @NonNull MapPoint fromMarker(@NonNull Marker marker) {
        LatLng position = marker.getPosition();
        String snippet = marker.getSnippet();
        String[] parts = (snippet != null ? snippet : "").split(";");
        double alt = 0;
        double sd = 0;
        try {
            if (parts.length >= 1) {
                alt = Double.parseDouble(parts[0]);
            }
            if (parts.length >= 2) {
                sd = Double.parseDouble(parts[1]);
            }
        } catch (NumberFormatException e) {
            Timber.w("Marker.getSnippet() did not contain two numbers");
        }
        return new MapPoint(position.latitude, position.longitude, alt, sd);
    }

    private static @NonNull LatLng toLatLng(@NonNull MapPoint point) {
        return new LatLng(point.lat, point.lon);
    }

    /** Updates the map to reflect the value of referenceLayerFile. */
    private void loadReferenceOverlay() {
        if (referenceOverlay != null) {
            referenceOverlay.remove();
            referenceOverlay = null;
        }
        if (referenceLayerFile != null) {
            referenceOverlay = this.map.addTileOverlay(new TileOverlayOptions().tileProvider(
                    new GoogleMapsMapBoxOfflineTileProvider(referenceLayerFile)
            ));
            setLabelsVisibility("off");
        } else {
            setLabelsVisibility("on");
        }
    }

    private void setLabelsVisibility(String state) {
        String style = String.format(" [ { featureType: all, elementType: labels, stylers: [ { visibility: %s } ] } ]", state);
        map.setMapStyle(new MapStyleOptions(style));
    }

    private LatLngBounds expandBounds(LatLngBounds bounds, double factor) {
        double north = bounds.northeast.latitude;
        double south = bounds.southwest.latitude;
        double latCenter = (north + south) / 2;
        double latRadius = ((north - south) / 2) * factor;
        north = Math.min(90, latCenter + latRadius);
        south = Math.max(-90, latCenter - latRadius);

        double east = bounds.northeast.longitude;
        double west = bounds.southwest.longitude;
        while (east < west) {
            east += 360;
        }
        double lonCenter = (east + west) / 2;
        double lonRadius = Math.min(180 - 1e-6, ((east - west) / 2) * factor);
        east = lonCenter + lonRadius;
        west = lonCenter - lonRadius;

        return new LatLngBounds(new LatLng(south, west), new LatLng(north, east));
    }

    private void moveOrAnimateCamera(CameraUpdate movement, boolean animate) {
        if (animate) {
            map.animateCamera(movement);
        } else {
            map.moveCamera(movement);
        }
    }

    private void enableLocationUpdates(boolean enable) {
        if (locationClient == null) {
            locationClient = LocationClientProvider.getClient(getActivity(), new PlayServicesChecker(),
                    () -> new GoogleFusedLocationClient(getActivity().getApplication()));
            locationClient.setListener(this);
        }
        if (enable) {
            Timber.i("Starting LocationClient %s (for MapFragment %s)", locationClient, this);
            locationClient.start();
        } else {
            Timber.i("Stopping LocationClient %s (for MapFragment %s)", locationClient, this);
            locationClient.stop();
        }
    }

    private void updateLocationIndicator(LatLng loc, double radius) {
        if (map == null) {
            return;
        }
        if (locationCrosshairs == null) {
            locationCrosshairs = map.addMarker(new MarkerOptions()
                    .position(loc)
                    .icon(getBitmapDescriptor(R.drawable.ic_crosshairs))
                    .anchor(0.5f, 0.5f)  // center the crosshairs on the position
            );
        }
        if (accuracyCircle == null) {
            int stroke = getResources().getColor(R.color.locationAccuracyCircle);
            int fill = getResources().getColor(R.color.locationAccuracyFill);
            accuracyCircle = map.addCircle(new CircleOptions()
                    .center(loc)
                    .radius(radius)
                    .strokeWidth(1)
                    .strokeColor(stroke)
                    .fillColor(fill)
            );
        }

        locationCrosshairs.setPosition(loc);
        accuracyCircle.setCenter(loc);
        accuracyCircle.setRadius(radius);
    }

    /** Finds the feature to which the given marker belongs. */
    private int findFeature(Marker marker) {
        for (int featureId : features.keySet()) {
            if (features.get(featureId).ownsMarker(marker)) {
                return featureId;
            }
        }
        return -1;  // not found
    }

    /** Finds the feature to which the given polyline belongs. */
    private int findFeature(Polyline polyline) {
        for (int featureId : features.keySet()) {
            if (features.get(featureId).ownsPolyline(polyline)) {
                return featureId;
            }
        }
        return -1;  // not found
    }

    private void updateFeature(int featureId) {
        MapFeature feature = features.get(featureId);
        if (feature != null) {
            feature.update();
        }
    }

    private Marker createMarker(GoogleMap map, MapPoint point, boolean draggable, @com.example.mobiledatacolection.widget.geo.MapFragment.IconAnchor String iconAnchor) {
        if (map == null || getActivity() == null) {  // during Robolectric tests, map will be null
            return null;
        }
        // A Marker's position is a LatLng with just latitude and longitude
        // fields.  We need to store the point's altitude and standard
        // deviation values somewhere, so they go in the marker's snippet.
        return map.addMarker(new MarkerOptions()
                .position(toLatLng(point))
                .snippet(point.alt + ";" + point.sd)
                .draggable(draggable)
                .icon(getBitmapDescriptor(R.drawable.ic_map_point))
                .anchor(getIconAnchorValueX(iconAnchor), getIconAnchorValueY(iconAnchor))  // center the icon on the position
        );
    }

    private float getIconAnchorValueX(@com.example.mobiledatacolection.widget.geo.MapFragment.IconAnchor String iconAnchor) {
        switch (iconAnchor) {
            case com.example.mobiledatacolection.widget.geo.MapFragment.BOTTOM:
            default:
                return 0.5f;
        }
    }

    private float getIconAnchorValueY(@com.example.mobiledatacolection.widget.geo.MapFragment.IconAnchor String iconAnchor) {
        switch (iconAnchor) {
            case com.example.mobiledatacolection.widget.geo.MapFragment.BOTTOM:
                return 1.0f;
            default:
                return 0.5f;
        }
    }

    private BitmapDescriptor getBitmapDescriptor(int drawableId) {
        return BitmapDescriptorFactory.fromBitmap(
                IconUtils.getBitmap(getActivity(), drawableId));
    }

    private void showGpsDisabledAlert() {
        new AlertDialog.Builder(getActivity())
                .setMessage(getString(R.string.gps_enable_message))
                .setCancelable(false)
                .setPositiveButton(getString(R.string.enable_gps),
                        (dialog, id) -> startActivityForResult(
                                new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS), 0))
                .setNegativeButton(getString(R.string.cancel),
                        (dialog, id) -> dialog.cancel())
                .create()
                .show();
    }

    /**
     * A MapFeature is a physical feature on a map, such as a point, a road,
     * a building, a region, etc.  It is presented to the user as one editable
     * object, though its appearance may be constructed from multiple overlays
     * (e.g. geometric elements, handles for manipulation, etc.).
     */
    interface MapFeature {
        /** Returns true if the given marker belongs to this feature. */
        boolean ownsMarker(Marker marker);

        /** Returns true if the given polyline belongs to this feature. */
        boolean ownsPolyline(Polyline polyline);

        /** Updates the feature's geometry after any UI handles have moved. */
        void update();

        /** Removes the feature from the map, leaving it no longer usable. */
        void dispose();
    }

    private class MarkerFeature implements MapFeature {
        private Marker marker;

        MarkerFeature(GoogleMap map, MapPoint point, boolean draggable, @com.example.mobiledatacolection.widget.geo.MapFragment.IconAnchor String iconAnchor) {
            marker = createMarker(map, point, draggable, iconAnchor);
        }

        public void setIcon(int drawableId) {
            marker.setIcon(getBitmapDescriptor(drawableId));
        }

        public MapPoint getPoint() {
            return fromMarker(marker);
        }

        public boolean ownsMarker(Marker givenMarker) {
            return marker.equals(givenMarker);
        }

        public boolean ownsPolyline(Polyline givenPolyline) {
            return false;
        }

        public void update() { }

        public void dispose() {
            marker.remove();
            marker = null;
        }
    }

    /** A polyline or polygon that can be manipulated by dragging markers at its vertices. */
    private class PolyFeature implements MapFeature {
        public static final int STROKE_WIDTH = 5;

        private final GoogleMap map;
        private final List<Marker> markers = new ArrayList<>();
        private final boolean closedPolygon;
        private Polyline polyline;

        PolyFeature(GoogleMap map, Iterable<MapPoint> points, boolean closedPolygon) {
            this.map = map;
            this.closedPolygon = closedPolygon;
            if (map == null) {  // during Robolectric tests, map will be null
                return;
            }
            for (MapPoint point : points) {
                markers.add(createMarker(map, point, true, com.example.mobiledatacolection.widget.geo.MapFragment.CENTER));
            }
            update();
        }

        public boolean ownsMarker(Marker givenMarker) {
            return markers.contains(givenMarker);
        }

        public boolean ownsPolyline(Polyline givenPolyline) {
            return polyline.equals(givenPolyline);
        }

        public void update() {
            List<LatLng> latLngs = new ArrayList<>();
            for (Marker marker : markers) {
                latLngs.add(marker.getPosition());
            }
            if (closedPolygon && !latLngs.isEmpty()) {
                latLngs.add(latLngs.get(0));
            }
            if (markers.isEmpty()) {
                clearPolyline();
            } else if (polyline == null) {
                polyline = map.addPolyline(new PolylineOptions()
                        .color(getResources().getColor(R.color.mapLine))
                        .zIndex(1)
                        .width(STROKE_WIDTH)
                        .addAll(latLngs)
                        .clickable(true)
                );
            } else {
                polyline.setPoints(latLngs);
            }
        }

        public void dispose() {
            clearPolyline();
            for (Marker marker : markers) {
                marker.remove();
            }
            markers.clear();
        }

        public List<MapPoint> getPoints() {
            List<MapPoint> points = new ArrayList<>();
            for (Marker marker : markers) {
                points.add(fromMarker(marker));
            }
            return points;
        }

        public void addPoint(MapPoint point) {
            if (map == null) {  // during Robolectric tests, map will be null
                return;
            }
            markers.add(createMarker(map, point, true, com.example.mobiledatacolection.widget.geo.MapFragment.CENTER));
            update();
        }

        public void removeLastPoint() {
            if (!markers.isEmpty()) {
                int last = markers.size() - 1;
                markers.get(last).remove();
                markers.remove(last);
                update();
            }
        }

        private void clearPolyline() {
            if (polyline != null) {
                polyline.remove();
                polyline = null;
            }
        }
    }
}
