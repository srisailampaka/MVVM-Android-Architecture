package example.com.newapiexample.injection.module;

import android.app.Application;


import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import example.com.newapiexample.data.DataManager;

/**
 * Provide application-level dependencies. Mainly singleton object that can be injected from
 * anywhere in the app.
 */
@Module
public class ApplicationModule {
    protected final Application mApplication;

    public ApplicationModule(Application application) {
        mApplication = application;
    }

    @Provides
    @Singleton
    Application provideApplication() {
        return mApplication;
    }

    @Provides
    @Singleton
    DataManager provideDataManager() {
        return new DataManager(mApplication);
    }

}