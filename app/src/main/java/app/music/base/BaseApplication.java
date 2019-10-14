package app.music.base;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;

import com.squareup.leakcanary.LeakCanary;
import com.squareup.leakcanary.RefWatcher;

import app.music.R;
import app.music.di.component.AppComponent;
import app.music.di.component.DaggerAppComponent;
import app.music.di.module.AppModule;
import timber.log.Timber;

public class BaseApplication extends Application {

    public static final String CHANNEL_ID = "SERVICE_CHANNEL";
    public static BaseApplication application;
    private RefWatcher refWatcher;
    public static AppComponent appComponent;

    public static RefWatcher getRefWatcher(Context context) {
        application = (BaseApplication) context.getApplicationContext();
        return application.refWatcher;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        refWatcher = LeakCanary.install(this);
        Timber.plant(new Timber.DebugTree());
        application = this;
        createNotificationChannel();
    }

    public static BaseApplication getApplication() {
        return application;
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel serviceChannel = new NotificationChannel(
                    CHANNEL_ID,
                    getString(R.string.service_channel),
                    NotificationManager.IMPORTANCE_DEFAULT
            );

            NotificationManager manager = getSystemService(NotificationManager.class);
            if (manager != null) manager.createNotificationChannel(serviceChannel);
        }
    }

    public static AppComponent getAppComponent() {
        if (appComponent == null) {
            appComponent = DaggerAppComponent.builder()
                    .appModule(new AppModule(application))
                    .build();
        }
        return appComponent;
    }
}
