package example.com.newapiexample.data.remote;


import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;


public class RetrofitHelper {

    public NewsArticleService newsArticleService() {
        Retrofit.Builder builder = new Retrofit.Builder();
        builder.baseUrl(NewsArticleService.ENDPOINT_URL);
        builder.addConverterFactory(GsonConverterFactory.create());
        builder.addCallAdapterFactory(RxJavaCallAdapterFactory.create());
        return builder.build().create(NewsArticleService.class);
    }

}
