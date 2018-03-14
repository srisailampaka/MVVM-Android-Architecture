package example.com.newapiexample.injection.component;

import android.app.Application;


import javax.inject.Singleton;

import dagger.Component;
import example.com.newapiexample.data.DataManager;
import example.com.newapiexample.injection.module.ApplicationModule;
import example.com.newapiexample.view.activity.MainActivity;

@Singleton
@Component(modules = ApplicationModule.class)
public interface ApplicationComponent {

    void inject(MainActivity mainActivity);

    Application application();
    DataManager dataManager();
}