package example.com.newapiexample;

import android.app.Application;
import android.content.Context;


import example.com.newapiexample.injection.component.ApplicationComponent;
import example.com.newapiexample.injection.component.DaggerApplicationComponent;
import example.com.newapiexample.injection.module.ApplicationModule;
import timber.log.Timber;


public class NewsApplication extends Application {

    ApplicationComponent mApplicationComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        if (BuildConfig.DEBUG) Timber.plant(new Timber.DebugTree());
        mApplicationComponent = DaggerApplicationComponent.builder()
                .applicationModule(new ApplicationModule(this))
                .build();
    }

    public static NewsApplication get(Context context) {
        return (NewsApplication) context.getApplicationContext();
    }

    public ApplicationComponent getComponent() {
        return mApplicationComponent;
    }

}
