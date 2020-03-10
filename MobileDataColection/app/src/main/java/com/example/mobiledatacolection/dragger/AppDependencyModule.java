package com.example.mobiledatacolection.dragger;

import android.app.Application;
import android.content.Context;
import android.telephony.SmsManager;

import com.example.mobiledatacolection.dataAccessObject.FormsDao;
import com.example.mobiledatacolection.dataAccessObject.InstancesDao;

import dagger.Module;
import dagger.Provides;

/**
 * Add dependency providers here (annotated with @Provides)
 * for objects you need to inject
 */
@Module
public class AppDependencyModule {

    @Provides
    public SmsManager provideSmsManager() {
        return SmsManager.getDefault();
    }

    /**@Provides
    SmsSubmissionManagerContract provideSmsSubmissionManager(Application application) {
        return new SmsSubmissionManager(application);
    }**/

    @Provides
    Context context(Application application) {
        return application;
    }

    @Provides
    public InstancesDao provideInstancesDao() {
        return new InstancesDao();
    }

    @Provides
    public FormsDao provideFormsDao() {
        return new FormsDao();
    }

    /**@Provides
    @Singleton
    RxEventBus provideRxEventBus() {
        return new RxEventBus();
    }

    @Provides
    MimeTypeMap provideMimeTypeMap() {
        return MimeTypeMap.getSingleton();
    }

    @Provides
    @Singleton
    OpenRosaHttpInterface provideHttpInterface(MimeTypeMap mimeTypeMap) {
        return new OkHttpConnection(
                new OkHttpOpenRosaServerClientProvider(new OkHttpClient()),
                new CollectThenSystemContentTypeMapper(mimeTypeMap),
                Collect.getInstance().getUserAgentString()
        );
    }**/

/**@Provides
    CollectServerClient provideCollectServerClient(OpenRosaHttpInterface httpInterface, WebCredentialsUtils webCredentialsUtils) {
        return new CollectServerClient(httpInterface, webCredentialsUtils);
    }

    @Provides
    WebCredentialsUtils provideWebCredentials() {
        return new WebCredentialsUtils();
    }

    @Provides
    DownloadFormListUtils provideDownloadFormListUtils(
            Application application,
            CollectServerClient collectServerClient,
            WebCredentialsUtils webCredentialsUtils,
            FormsDao formsDao) {
        return new DownloadFormListUtils(
                application,
                collectServerClient,
                webCredentialsUtils,
                formsDao
        );
    }

    @Provides
    @Singleton
    public Analytics providesAnalytics(Application application) {
        com.google.android.gms.analytics.GoogleAnalytics analytics = com.google.android.gms.analytics.GoogleAnalytics.getInstance(application);
        Tracker tracker = analytics.newTracker(R.xml.global_tracker);
        GoogleAnalytics googleAnalytics = new GoogleAnalytics(tracker);

        com.google.firebase.analytics.FirebaseAnalytics firebaseAnalyticsInstance = com.google.firebase.analytics.FirebaseAnalytics.getInstance(application);
        FirebaseAnalytics firebaseAnalytics = new FirebaseAnalytics(firebaseAnalyticsInstance);

        return new Analytics() {
            @Override
            public void logEvent(String category, String action) {
                googleAnalytics.logEvent(category, action);
                firebaseAnalytics.logEvent(category, action);
            }

            @Override
            public void logEvent(String category, String action, String label) {
                googleAnalytics.logEvent(category, action, label);
                firebaseAnalytics.logEvent(category, action, label);
            }
        };
    }

    @Provides
    public PermissionUtils providesPermissionUtils() {
        return new PermissionUtils();
    }

    @Provides
    public ReferenceManager providesReferenceManager() {
        return ReferenceManager.instance();
    }

    @Provides
    public AudioHelperFactory providesAudioHelperFactory() {
        return new ScreenContextAudioHelperFactory();
    }

    @Provides
    public ActivityAvailability providesActivityAvailability(Context context) {
        return new ActivityAvailability(context);
    }**/
}
