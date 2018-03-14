package example.com.newapiexample.view.adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import example.com.newapiexample.R;
import example.com.newapiexample.databinding.ItemNewsBinding;
import example.com.newapiexample.model.Article;
import example.com.newapiexample.viewModel.NewsViewModel;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.BindingHolder> {
    private List<Article> mArticles;
    private Context mContext;

    public NewsAdapter(Context context) {
        mContext = context;
        mArticles = new ArrayList<>();
    }

    @Override
    public BindingHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ItemNewsBinding newsBinding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.getContext()),
                R.layout.item_news,
                parent,
                false);
        return new BindingHolder(newsBinding);
    }

    @Override
    public void onBindViewHolder(BindingHolder holder, int position) {
        ItemNewsBinding newsBinding = holder.binding;
        newsBinding.setViewModel(new NewsViewModel(mContext, mArticles.get(position)));
    }

    @Override
    public int getItemCount() {
        return mArticles.size();
    }

    public void setItems(List<Article> posts) {
        mArticles = posts;
        notifyDataSetChanged();
    }


    public static class BindingHolder extends RecyclerView.ViewHolder {
        private ItemNewsBinding binding;

        BindingHolder(ItemNewsBinding binding) {
            super(binding.cardView);
            this.binding = binding;
        }
    }

}