package example.com.newapiexample.injection.module;


import dagger.Module;
import dagger.Provides;

import example.com.newapiexample.data.remote.NewsArticleService;
import example.com.newapiexample.data.remote.RetrofitHelper;
import example.com.newapiexample.injection.scope.PerDataManager;
import rx.Scheduler;
import rx.schedulers.Schedulers;

/**
 * Provide dependencies to the DataManager, mainly Helper classes and Retrofit services.
 */
@Module
public class DataManagerModule {

    public DataManagerModule() {

    }

    @Provides
    @PerDataManager
    NewsArticleService provideHackerNewsService() {
        return new RetrofitHelper().newsArticleService();
    }

    @Provides
    @PerDataManager
    Scheduler provideSubscribeScheduler() {
        return Schedulers.io();
    }
}