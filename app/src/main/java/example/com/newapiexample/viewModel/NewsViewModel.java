package example.com.newapiexample.viewModel;

import android.content.Context;
import android.content.Intent;
import android.databinding.BaseObservable;
import android.databinding.BindingAdapter;
import android.net.Uri;
import android.support.customtabs.CustomTabsIntent;

import android.text.TextUtils;

import android.view.View;
import android.widget.ImageView;


import com.squareup.picasso.Picasso;

import example.com.newapiexample.R;
import example.com.newapiexample.model.Article;
import example.com.newapiexample.util.CustomTabsHelper;

import example.com.newapiexample.view.activity.VideoViewActivity;
import example.com.newapiexample.view.activity.ViewOriginalNewsActivity;


public class NewsViewModel extends BaseObservable {

    private Context context;
    private Article article;

    public NewsViewModel(Context context, Article article) {
        this.context = context;
        this.article = article;
    }

    public String getArticleDescription() {
        String articleDescription = "Content : Not Available";
        if (!TextUtils.isEmpty(article.description))
            articleDescription = article.description;

        return articleDescription;
    }

    public String getArticleTitle() {
        return article.title;
    }

    public String getArticleAuthor() {
        String authorName = "Not Available";
        if (!TextUtils.isEmpty(article.author))
            authorName = article.author;
        return "Author : " + authorName;
    }

    public String getPublishedDate() {
        return "PublishedDate : " + article.publishedAt;
    }

    public View.OnClickListener onClickPost() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!TextUtils.isEmpty(article.url)) {
                    launchOriginalNewsActivity();
                }
            }
        };
    }

    public View.OnClickListener watchNews() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Load some generic video

                Intent myIntent = new Intent(context, VideoViewActivity.class);
                context.startActivity(myIntent);
            }
        };
    }

    @BindingAdapter({"bind:imageUrl"})
    public static void loadImage(ImageView view, String url) {
        Picasso.with(view.getContext())
                .load(url)
                .placeholder(R.drawable.loading)
                .into(view);
    }

    @BindingAdapter({"bind:isVisible"})
    public static void setVisibility(View view, boolean visible) {
        view.setVisibility(visible ? View.VISIBLE : View.GONE);
    }


    public boolean setVisibility() {
        return TextUtils.isEmpty(article.description) || TextUtils.isEmpty(article.description);
    }

    public String imageURL() {
        return article.urlToImage;
    }

    private void launchOriginalNewsActivity() {
        String packageName = CustomTabsHelper.getPackageNameToUse(context);

        if (packageName == null) {
            context.startActivity(ViewOriginalNewsActivity.getStartIntent(context, article));
        } else {
            CustomTabsIntent.Builder intentBuilder = new CustomTabsIntent.Builder();
            Intent intent = intentBuilder.build().intent;
            intent.setData(Uri.parse(article.url));
            context.startActivity(intent);
        }
    }

}
