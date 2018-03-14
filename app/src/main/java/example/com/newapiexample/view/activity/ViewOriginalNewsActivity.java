package example.com.newapiexample.view.activity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.ShareActionProvider;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import example.com.newapiexample.R;
import example.com.newapiexample.model.Article;
import example.com.newapiexample.util.DataUtils;

public class ViewOriginalNewsActivity extends BaseActivity {

    @BindView(R.id.progress_indicator)
    LinearLayout mProgressContainer;

    @BindView(R.id.layout_offline)
    LinearLayout mOfflineLayout;

    @BindView(R.id.layout_story)
    RelativeLayout mStoryLayout;

    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    @BindView(R.id.web_view)
    WebView mWebView;

    public static final String EXTRA_POST =
            "example.com.newapiexample.view.activity.ViewOriginalNewsActivity.EXTRA_POST";
    private Article mArticle;

    public static Intent getStartIntent(Context context, Article article) {
        Intent intent = new Intent(context, ViewOriginalNewsActivity.class);
        intent.putExtra(EXTRA_POST, article);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_story);
        ButterKnife.bind(this);
        Bundle bundle = getIntent().getExtras();
        if(bundle!=null)
        mArticle = bundle.getParcelable(EXTRA_POST);
        if (mArticle == null)
            throw new IllegalArgumentException("ViewStoryActivity requires a Post object!");
        setupToolbar();
        setupWebView();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.view_story, menu);
        setupShareActionProvider(menu);
        return true;
    }

    @Override
    public void onBackPressed() {
        if (mWebView.canGoBack()) {
            mWebView.goBack();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_browser:
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(mArticle.url)));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @OnClick(R.id.button_try_again)
    public void onTryAgainClick() {
        setupWebView();
    }

    private void setupToolbar() {
        setSupportActionBar(mToolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle(mArticle.title);
        }
    }

    private void setupShareActionProvider(Menu menu) {
        ShareActionProvider shareActionProvider =
                (ShareActionProvider) MenuItemCompat.getActionProvider(menu.findItem(R.id.menu_item_share));
        if (shareActionProvider != null) shareActionProvider.setShareIntent(getShareIntent());
    }

    private void setupWebView() {
        mWebView.setWebChromeClient(new WebChromeClient() {
            public void onProgressChanged(WebView view, int progress) {
                if (progress == 100) mProgressContainer.setVisibility(ProgressBar.GONE);
            }
        });
        mWebView.setWebViewClient(new ProgressWebViewClient());
        mWebView.setInitialScale(1);
        mWebView.getSettings().setBuiltInZoomControls(true);
        mWebView.getSettings().setDisplayZoomControls(true);
        mWebView.getSettings().setLoadsImagesAutomatically(true);
        mWebView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.getSettings().setLoadWithOverviewMode(true);
        mWebView.getSettings().setUseWideViewPort(true);
        mWebView.getSettings().setAllowFileAccess(true);
        mWebView.getSettings().setAppCacheEnabled(true);
        mWebView.getSettings().setCacheMode(WebSettings.LOAD_DEFAULT);

        if (DataUtils.isNetworkAvailable(this)) {
            showHideOfflineLayout(false);
            mWebView.loadUrl(mArticle.url);
        } else {
            showHideOfflineLayout(true);
        }
    }

    private Intent getShareIntent() {
        String shareText = mArticle.title;
        return new Intent()
                .setAction(Intent.ACTION_SEND)
                .setType("text/plain")
                .putExtra(Intent.EXTRA_TEXT, shareText);
    }

    private void showHideOfflineLayout(boolean isOffline) {
        mOfflineLayout.setVisibility(isOffline ? View.VISIBLE : View.GONE);
        mWebView.setVisibility(isOffline ? View.GONE : View.VISIBLE);
        mProgressContainer.setVisibility(isOffline ? View.GONE : View.VISIBLE);
    }

    private class ProgressWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }

        @Override
        public void onPageFinished(WebView view, String page) {
            mProgressContainer.setVisibility(ProgressBar.GONE);
        }
    }

}
