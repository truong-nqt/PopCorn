package com.truongnqt.popcorn.searchFragment.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.truongnqt.popcorn.R;
import com.truongnqt.popcorn.databinding.ItemSearchResultBinding;
import com.truongnqt.popcorn.network.search.SearchResult;

import java.util.ArrayList;
import java.util.List;

public class SearchResultsAdapter extends RecyclerView.Adapter<SearchResultsViewHolder> {

    private final List<SearchResult> searchResults;
    private final ItemCallback itemCallback;
    private final Context context;
    private ItemSearchResultBinding binding;

    public SearchResultsAdapter(Context context, List<SearchResult> searchResults, ItemCallback itemCallback) {
        this.searchResults = searchResults != null ? searchResults : new ArrayList<>();
        this.itemCallback = itemCallback;
        this.context = context;
    }

    public void addItems(List<SearchResult> searchResults) {
        if(searchResults != null) {
            int count = searchResults.size();
            int start = this.searchResults.size();
            this.searchResults.addAll(searchResults);
            notifyItemRangeInserted(start, count);
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    public void newList(List<SearchResult> searchResults) {
        if(searchResults != null) {
            this.searchResults.clear();
            this.searchResults.addAll(searchResults);
            notifyDataSetChanged();
        }
    }

    @NonNull
    @Override
    public SearchResultsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        binding = DataBindingUtil.inflate(inflater, R.layout.item_search_result, parent, false);
        return new SearchResultsViewHolder(binding, itemCallback);
    }

    @Override
    public void onBindViewHolder(@NonNull SearchResultsViewHolder holder, int position) {
        binding.imageViewPosterSearch.getLayoutParams().width = (int) (context.getResources().getDisplayMetrics().widthPixels * 0.31);
        binding.imageViewPosterSearch.getLayoutParams().height = (int) ((context.getResources().getDisplayMetrics().widthPixels * 0.31) / 0.66);

        holder.bind(searchResults.get(position), itemCallback);
    }

    @Override
    public int getItemCount() {
        return searchResults.size();
    }

    public interface ItemCallback {
        void OnItemClick(int position, SearchResult searchResult);
    }
}
