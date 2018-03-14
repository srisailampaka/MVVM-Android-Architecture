package example.com.newapiexample.view.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import example.com.newapiexample.NewsApplication;
import example.com.newapiexample.R;
import example.com.newapiexample.data.DataManager;
import example.com.newapiexample.model.Article;
import example.com.newapiexample.model.NewsResponse;
import example.com.newapiexample.util.DataUtils;
import example.com.newapiexample.util.DialogFactory;
import example.com.newapiexample.view.adapter.NewsAdapter;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.subscriptions.CompositeSubscription;
import timber.log.Timber;

public class NewsFragment extends Fragment implements OnRefreshListener {

    @BindView(R.id.swipe_container)
    SwipeRefreshLayout mSwipeRefreshLayout;

    @BindView(R.id.recycler_new_stories)
    RecyclerView mListPosts;

    @BindView(R.id.layout_offline)
    LinearLayout mOfflineContainer;

    @BindView(R.id.progress_indicator)
    ProgressBar mProgressBar;
/*
    @BindView(R.id.toolbar)
    Toolbar mToolbar;*/

    public void setNewsCategory(String newsCategory) {
        this.newsCategory = newsCategory;
    }

    private String newsCategory;

    private DataManager mDataManager;
    private NewsAdapter mNewsAdapter;
    private CompositeSubscription mSubscriptions;
    private List<Article> mArticles;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mSubscriptions = new CompositeSubscription();
        mArticles = new ArrayList<>();
        mDataManager = NewsApplication.get(getActivity()).getComponent().dataManager();
        mNewsAdapter = new NewsAdapter(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View fragmentView = inflater.inflate(R.layout.fragment_news, container, false);
        ButterKnife.bind(this, fragmentView);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.hn_orange);
        // setupToolbar();
        setupRecyclerView();
        loadStoriesIfNetworkConnected();
        return fragmentView;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mSubscriptions.unsubscribe();
    }

    @Override
    public void onRefresh() {
        //  mSubscriptions.unsubscribe();
        if (mNewsAdapter != null) mNewsAdapter.setItems(new ArrayList<Article>());
        getNewsStories();
    }

    @OnClick(R.id.button_try_again)
    public void onTryAgainClick() {
        loadStoriesIfNetworkConnected();
    }
/*
    private void setupToolbar() {
        ((AppCompatActivity) getActivity()).setSupportActionBar(mToolbar);
        ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayShowTitleEnabled(true);
            if (mUser != null) {
                actionBar.setTitle(mUser);
                actionBar.setDisplayHomeAsUpEnabled(true);
            }
        }
    }*/

    private void setupRecyclerView() {
        mListPosts.setLayoutManager(new LinearLayoutManager(getActivity()));
        mListPosts.setHasFixedSize(true);
        mNewsAdapter.setItems(mArticles);
        mListPosts.setAdapter(mNewsAdapter);
    }

    private void loadStoriesIfNetworkConnected() {
        if (DataUtils.isNetworkAvailable(getActivity())) {
            showHideOfflineLayout(false);
            getNewsStories();
        } else {
            showHideOfflineLayout(true);
        }
    }

    private void getNewsStories() {
        mSubscriptions.add(mDataManager.getNewStories(newsCategory)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(mDataManager.getScheduler())
                .subscribe(new Subscriber<NewsResponse>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        hideLoadingViews();
                        Timber.e("There was a problem loading the top stories " + e);
                        e.printStackTrace();
                        DialogFactory.createSimpleOkErrorDialog(
                                getActivity(),
                                getString(R.string.error_stories)
                        ).show();
                    }

                    @Override
                    public void onNext(NewsResponse newsResponse) {
                        hideLoadingViews();
                        //  mNewsAdapter.addItem(post);
                        mNewsAdapter.setItems(newsResponse.getArticles());
                    }
                }));
    }


    private void hideLoadingViews() {
        mProgressBar.setVisibility(View.GONE);
        mSwipeRefreshLayout.setRefreshing(false);
    }

    private void showHideOfflineLayout(boolean isOffline) {
        mOfflineContainer.setVisibility(isOffline ? View.VISIBLE : View.GONE);
        mListPosts.setVisibility(isOffline ? View.GONE : View.VISIBLE);
        mProgressBar.setVisibility(isOffline ? View.GONE : View.VISIBLE);
    }

}
