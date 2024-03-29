/*
 * Copyright (C) 2017 University of Washington
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */

package com.example.mobiledatacolection;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Environment;
import android.preference.PreferenceManager;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDelegate;
import com.example.mobiledatacolection.dragger.AppDependencyComponent;
import com.example.mobiledatacolection.preferences.GeneralSharedPreferences;
import com.example.mobiledatacolection.tasks.FormController;
import com.example.mobiledatacolection.utils.FileUtils;

import org.javarosa.core.services.PropertyManager;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.util.Locale;

import timber.log.Timber;

import static com.example.mobiledatacolection.preferences.GeneralKeys.KEY_FONT_SIZE;
import static com.example.mobiledatacolection.preferences.GeneralKeys.KEY_USERNAME;
import static com.example.mobiledatacolection.preferences.PropertyManager.PROPMGR_USERNAME;
import static com.example.mobiledatacolection.preferences.PropertyManager.SCHEME_USERNAME;


/**
 * The Open Data Kit Collect application.
 *
 * @author carlhartung
 */
public class MobileDataCollect extends Application {

    // Storage paths
    public static final String ODK_ROOT = Environment.getExternalStorageDirectory()
            + File.separator + "odk";
    public static final String FORMS_PATH = ODK_ROOT + File.separator + "forms";
    public static final String INSTANCES_PATH = ODK_ROOT + File.separator + "instances";
    public static final String CACHE_PATH = ODK_ROOT + File.separator + ".cache";
    public static final String METADATA_PATH = ODK_ROOT + File.separator + "metadata";
    public static final String TMPFILE_PATH = CACHE_PATH + File.separator + "tmp.jpg";
    public static final String TMPDRAWFILE_PATH = CACHE_PATH + File.separator + "tmpDraw.jpg";
    public static final String DEFAULT_FONTSIZE = "21";
    public static final int DEFAULT_FONTSIZE_INT = 21;
    public static final String OFFLINE_LAYERS = ODK_ROOT + File.separator + "layers";
    public static final String SETTINGS = ODK_ROOT + File.separator + "settings";

    public static final int CLICK_DEBOUNCE_MS = 1000;
    public static final String FIELD_LIST = "field-list";

    public static String defaultSysLanguage;
    private static MobileDataCollect singleton;
    private static long lastClickTime;
    private static String lastClickName;

    @Nullable
    private FormController formController;
    // private ExternalDataManager externalDataManager;


    private AppDependencyComponent applicationComponent;

    public static MobileDataCollect getInstance() {
        return singleton;
    }

    public static int getQuestionFontsize() {
        // For testing:
        MobileDataCollect instance = MobileDataCollect.getInstance();
        if (instance == null) {
            return MobileDataCollect.DEFAULT_FONTSIZE_INT;
        }

        return Integer.parseInt(String.valueOf(GeneralSharedPreferences.getInstance().get(KEY_FONT_SIZE)));
    }

    public static boolean isODKTablesInstanceDataDirectory(File directory) {
        /*
         * Special check to prevent deletion of files that
         * could be in use by ODK Tables.
         */
        String dirPath = directory.getAbsolutePath();
        if (dirPath.startsWith(MobileDataCollect.ODK_ROOT)) {
            dirPath = dirPath.substring(MobileDataCollect.ODK_ROOT.length());
            String[] parts = dirPath.split(File.separatorChar == '\\' ? "\\\\" : File.separator);
            // [appName, instances, tableId, instanceId ]
            if (parts.length == 4 && parts[1].equals("instances")) {
                return true;
            }
        }
        return false;
    }

    public static String getCurrentFormIdentifierHash() {
        String formIdentifier = "";
        FormController formController = getInstance().getFormController();
        if (formController != null) {
            if (formController.getFormDef() != null) {
                String formID = formController.getFormDef().getMainInstance()
                        .getRoot().getAttributeValue("", "id");
                formIdentifier = formController.getFormTitle() + " " + formID;
            }
        }

        return FileUtils.getMd5Hash(new ByteArrayInputStream(formIdentifier.getBytes()));
    }

    @Nullable
    public FormController getFormController() {
        return formController;
    }

    public void setFormController(@Nullable FormController controller) {
        formController = controller;
    }

  //  public ExternalDataManager getExternalDataManager() {
      //  return externalDataManager;
    //}

   // public void setExternalDataManager(ExternalDataManager externalDataManager) {
    //    this.externalDataManager = externalDataManager;
   // }

    public String getVersionedAppName() {
        String versionName = BuildConfig.VERSION_NAME;
        versionName = " " + versionName.replaceFirst("-", "\n");
        return getString(R.string.app_name) + versionName;
    }

    /**
     * Get a User-Agent string that provides the platform details followed by the application ID
     * and application version name: {@code Dalvik/<version> (platform info) org.odk.MobileDataCollect.android/v<version>}.
     *
     * This deviates from the recommended format as described in https://github.com/opendatakit/MobileDataCollect/issues/3253.
     */
    public String getUserAgentString() {
        return String.format("%s %s/%s",
                System.getProperty("http.agent"),
                BuildConfig.APPLICATION_ID,
                BuildConfig.VERSION_NAME);
    }

    public boolean isNetworkAvailable() {
        ConnectivityManager cm =
                (ConnectivityManager)this.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();
        return isConnected;
    }

    /*
        Adds support for multidex support library. For more info check out the link below,
        https://developer.android.com/studio/build/multidex.html
    */
    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
       // MultiDex.install(this);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        singleton = this;

        // installTls12();
        // setupDagger();

        // NotificationUtils.createNotificationChannel(singleton);

        // registerReceiver(new SmsSentBroadcastReceiver(), new IntentFilter(SMS_SEND_ACTION));
        // registerReceiver(new SmsNotificationReceiver(), new IntentFilter(SMS_NOTIFICATION_ACTION));

        /*try {
            JobManager
                    .create(this)
                    .addJobCreator(new MobileDataCollectJobCreator());
        } catch (JobManagerCreateException e) {
            Timber.e(e);
        }*/

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        FormMetadataMigrator.migrate(prefs);
        //PrefMigrator.migrateSharedPrefs();
        //AutoSendPreferenceMigrator.migrate();

        //reloadSharedPreferences();

       // PRNGFixes.apply();
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
       // JodaTimeAndroid.init(this);

        defaultSysLanguage = Locale.getDefault().getLanguage();
        //new LocaleHelper().updateLocale(this);

        initializeJavaRosa();

        if (BuildConfig.BUILD_TYPE.equals("odkMobileDataCollectRelease")) {

        } else {
            Timber.plant(new Timber.DebugTree());
        }

        // setupLeakCanary();
        setupOSMDroid();
    }

    protected void setupOSMDroid() {
       // org.osmdroid.config.Configuration.getInstance().setUserAgentValue(getUserAgentString());
    }

   /* private void setupDagger() {
        applicationComponent = DaggerAppDependencyComponent.builder()
                .application(this)
                .build();

        applicationComponent.inject(this);
    }

    /*private void installTls12() {
        if (Build.VERSION.SDK_INT <= 20) {
            ProviderInstaller.installIfNeededAsync(getApplicationContext(), new ProviderInstaller.ProviderInstallListener() {
                @Override
                public void onProviderInstalled() {
                }

                @Override
                public void onProviderInstallFailed(int i, Intent intent) {
                    GoogleApiAvailability
                            .getInstance()
                            .showErrorNotification(getApplicationContext(), i);
                }
            });
        }
    }*/

   /**
    * Detecta Leaks de memória
    * protected RefWatcher setupLeakCanary() {
        if (LeakCanary.isInAnalyzerProcess(this)) {
            return RefWatcher.DISABLED;
        }
        return LeakCanary.install(this);
    }**/

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        //noinspection deprecation
       /* defaultSysLanguage = newConfig.locale.getLanguage();
        boolean isUsingSysLanguage = GeneralSharedPreferences.getInstance().get(KEY_APP_LANGUAGE).equals("");
        if (!isUsingSysLanguage) {
            new LocaleHelper().updateLocale(this);
        }*/
    }


    /*public synchronized Tracker getDefaultTracker() {
        /*if (tracker == null) {
            GoogleAnalytics analytics = GoogleAnalytics.getInstance(this);
            tracker = analytics.newTracker(R.xml.global_tracker);
        }
        return tracker;
    }*/

    public void logRemoteAnalytics(String event, String action, String label) {
        // Google Analytics
       /* MobileDataCollect.getInstance()
                .getDefaultTracker()
                .send(new HitBuilders.EventBuilder()
                        .setCategory(event)
                        .setAction(action)
                        .setLabel(label)
                        .build());*/

    }

    public void setAnalyticsMobileDataCollectionEnabled(boolean isAnalyticsEnabled) {

    }

    public void initializeJavaRosa() {
        PropertyManager mgr = new PropertyManager();

        // Use the server username by default if the metadata username is not defined
        if (mgr.getSingularProperty(PROPMGR_USERNAME) == null || mgr.getSingularProperty(PROPMGR_USERNAME).isEmpty()) {
            mgr.putProperty(PROPMGR_USERNAME, SCHEME_USERNAME, (String) GeneralSharedPreferences.getInstance().get(KEY_USERNAME));
        }

        FormController.initializeJavaRosa(mgr);
    }

    public AppDependencyComponent getComponent() {
        return applicationComponent;
    }
}
