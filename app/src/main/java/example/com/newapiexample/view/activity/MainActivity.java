package example.com.newapiexample.view.activity;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;

import butterknife.BindView;
import butterknife.ButterKnife;
import example.com.newapiexample.R;
import example.com.newapiexample.view.adapter.ViewPagerAdapter;
import example.com.newapiexample.view.fragment.NewsFragment;


public class MainActivity extends BaseActivity {


    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.tabs)
    TabLayout tabLayout;
    @BindView(R.id.viewpager)
    ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        setupActionBar();
        setupViewPager(viewPager);
        tabLayout.setupWithViewPager(viewPager);
    }

    private void setupActionBar() {
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        addPagerViews(adapter);
        viewPager.setAdapter(adapter);
    }

    private void addPagerViews(ViewPagerAdapter adapter) {

        String[] tabNamesList = getResources().getStringArray(R.array.tab_names);
        for (String tabName : tabNamesList) {
            NewsFragment newsFragment = new NewsFragment();
            newsFragment.setNewsCategory(tabName);
            adapter.addFragment(newsFragment, tabName);
        }
    }
}