package example.com.newapiexample.data.remote;

import example.com.newapiexample.model.NewsResponse;

import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;


public interface NewsArticleService {

    String ENDPOINT_URL = "https://newsapi.org/v2/";
    /**
     * Return NewsArticle list with status.
     */
    @GET("top-headlines")
    Observable<NewsResponse> getNewsArticleList(@Query("country") String country, @Query("category") String category, @Query("apiKey") String apiKey);

}
