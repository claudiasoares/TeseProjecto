package com.example.mobiledatacolection.widget.location.client;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.mobiledatacolection.utils.PlayServicesChecker;


import java.util.function.Supplier;


/** A static helper class for obtaining the appropriate LocationClient to use. */
public class LocationClientProvider {
    @Nullable private static LocationClient testClient;

    private LocationClientProvider() { }  // prevent instantiation of this utility class

    /** Returns a LocationClient appropriate for a given context. */
    // NOTE(ping): As of 2018-11-01, the GoogleFusedLocationClient never returns an
    // accuracy radius below 3m: https://issuetracker.google.com/issues/118789585
    public static LocationClient getClient(@NonNull Context context, @NonNull PlayServicesChecker playServicesChecker,
                                           @NonNull Supplier<GoogleFusedLocationClient> googleFusedLocationClientProvider) {
        return testClient != null
            ? testClient
            : playServicesChecker.isGooglePlayServicesAvailable(context)
                ? (LocationClient) googleFusedLocationClientProvider.get()
                : (LocationClient) new AndroidLocationClient(context);
    }

    /** Sets the LocationClient.  For use in tests only. */
    public static void setTestClient(@NonNull LocationClient testClient) {
        LocationClientProvider.testClient = testClient;
    }
}
