package example.com.newapiexample.data;

import android.content.Context;


import javax.inject.Inject;

import example.com.newapiexample.NewsApplication;
import example.com.newapiexample.data.remote.NewsArticleService;
import example.com.newapiexample.injection.component.DaggerDataManagerComponent;
import example.com.newapiexample.injection.module.DataManagerModule;
import example.com.newapiexample.model.NewsResponse;
import okhttp3.ResponseBody;
import rx.Observable;
import rx.Scheduler;

public class DataManager {

    private static final String COUNTRY="us";
    private static final String API_KEY="514d788bf2074dfcab26c901a79bcb99";

    @Inject
    protected Scheduler mSubscribeScheduler;
    @Inject
    protected NewsArticleService mNewsArticleService;

    private ResponseBody outputResponse = null;

    public DataManager(Context context) {
        injectDependencies(context);
    }

    /* This constructor is provided so we can set up a DataManager with mocks from unit test.
     * At the moment this is not possible to do with Dagger because the Gradle APT plugin doesn't
     * work for the unit test variant, plus Dagger 2 doesn't provide a nice way of overriding
     * modules */
    public DataManager(Scheduler subscribeScheduler, NewsArticleService newsArticleService) {
        mSubscribeScheduler = subscribeScheduler;
        mNewsArticleService = newsArticleService;
    }

    protected void injectDependencies(Context context) {
        DaggerDataManagerComponent.builder()
                .applicationComponent(NewsApplication.get(context).getComponent())
                .dataManagerModule(new DataManagerModule())
                .build()
                .inject(this);
    }

    public Scheduler getScheduler() {
        return mSubscribeScheduler;
    }


    public Observable<NewsResponse> getNewStories(String category) {

        return mNewsArticleService.getNewsArticleList(COUNTRY, category, API_KEY);
    }

}
