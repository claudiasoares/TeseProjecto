package com.example.mobiledatacolection.dragger;

import android.app.Application;
import android.telephony.SmsManager;

import com.example.mobiledatacolection.MobileDataCollect;
import com.example.mobiledatacolection.preferences.PropertyManager;
import com.example.mobiledatacolection.views.MobileDataCollectView;
import com.example.mobiledatacolection.widget.QuestionWidget;

import org.javarosa.core.reference.ReferenceManager;


import javax.inject.Singleton;

import dagger.BindsInstance;
import dagger.Component;

/**
 * Dagger component for the application. Should include
 * application level Dagger Modules and be built with Application
 * object.
 *
 * Add an `inject(MyClass myClass)` method here for objects you want
 * to inject into so Dagger knows to wire it up.
 *
 * Annotated with @Singleton so modules can include @Singletons that will
 * be retained at an application level (as this an instance of this components
 * is owned by the Application object).
 *
 * If you need to call a provider directly from the component (in a test
 * for example) you can add a method with the type you are looking to fetch
 * (`MyType myType()`) to this interface.
 *
 * To read more about Dagger visit: https://google.github.io/dagger/users-guide
 **/

@Singleton
@Component(modules = {
        AppDependencyModule.class
})
public interface AppDependencyComponent {

    @Component.Builder
    interface Builder {

        @BindsInstance
        Builder application(Application application);

        Builder appDependencyModule(AppDependencyModule testDependencyModule);

        AppDependencyComponent build();
    }

    void inject(MobileDataCollect collect);
    void inject(PropertyManager propertyManager);
    void inject(MobileDataCollectView mobileView);




    // void inject(FormEntryActivity formEntryActivity);

    // void inject(InstanceServerUploaderTask uploader);

    // void inject(CollectServerClient collectClient);

    // void inject(ServerPreferencesFragment serverPreferencesFragment);

    // void inject(FormDownloader formDownloader);

    // void inject(ServerPollingJob serverPollingJob);

    // void inject(AuthDialogUtility authDialogUtility);

    // void inject(FormDownloadList formDownloadList);

    // void inject(InstanceUploaderListActivity activity);

    // void inject(GoogleDriveActivity googleDriveActivity);

    // void inject(GoogleSheetsUploaderActivity googleSheetsUploaderActivity);

    void inject(QuestionWidget questionWidget);

    // void inject(ExStringWidget exStringWidget);

    // RxEventBus rxEventBus();

    // OpenRosaHttpInterface openRosaHttpInterface();

    // DownloadFormListUtils downloadFormListUtils();

    ReferenceManager referenceManager();

    // Analytics analytics();
}
