package example.com.newapiexample.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;


public class NewsResponse {

    @SerializedName("articles")
    private List<Article> articles = null;

    public List<Article> getArticles() {
        return articles;
    }
}
