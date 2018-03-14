package example.com.newapiexample.injection.component;


import dagger.Component;
import example.com.newapiexample.data.DataManager;
import example.com.newapiexample.injection.module.DataManagerModule;
import example.com.newapiexample.injection.scope.PerDataManager;

@PerDataManager
@Component(dependencies = ApplicationComponent.class, modules = DataManagerModule.class)
public interface DataManagerComponent {

    void inject(DataManager dataManager);
}