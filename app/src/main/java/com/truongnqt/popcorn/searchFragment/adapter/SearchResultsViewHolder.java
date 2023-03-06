package com.truongnqt.popcorn.searchFragment.adapter;

import android.annotation.SuppressLint;

import androidx.recyclerview.widget.RecyclerView;

import com.truongnqt.popcorn.R;
import com.truongnqt.popcorn.databinding.ItemSearchResultBinding;
import com.truongnqt.popcorn.network.search.SearchResult;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class SearchResultsViewHolder extends RecyclerView.ViewHolder {
    private final ItemSearchResultBinding binding;
    private SearchResultsAdapter.ItemCallback itemCallback;

    public SearchResultsViewHolder(ItemSearchResultBinding binding, SearchResultsAdapter.ItemCallback itemCallback) {
        super(binding.getRoot());

        this.binding = binding;
        this.itemCallback = itemCallback;
        this.binding.getRoot().setOnClickListener(v -> {
            int position = getBindingAdapterPosition();
            if(RecyclerView.NO_POSITION != position && itemCallback != null) {
                this.itemCallback.OnItemClick(position, binding.getSearchResult());
            }
        });
    }

    public void bind(SearchResult searchResult, SearchResultsAdapter.ItemCallback itemCallback) {
        binding.setSearchResult(searchResult);
        this.itemCallback = itemCallback;

        if (searchResult.getMediaType() != null && searchResult.getMediaType().equals("movie"))
            binding.textViewMediaTypeSearch.setText(R.string.Movie);
        else if (searchResult.getMediaType() != null && searchResult.getMediaType().equals("tv"))
            binding.textViewMediaTypeSearch.setText(R.string.tv_show);
        else if (searchResult.getMediaType() != null && searchResult.getMediaType().equals("person"))
            binding.textViewMediaTypeSearch.setText(R.string.person);
        else
            binding.textViewMediaTypeSearch.setText("");

        if (searchResult.getReleaseDate() != null && !searchResult.getReleaseDate().trim().isEmpty()) {
            @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
            @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy");
            try {
                Date releaseDate = sdf1.parse(searchResult.getReleaseDate());
                assert releaseDate != null;
                binding.textViewYearSearch.setText(sdf2.format(releaseDate));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        } else {
            binding.textViewYearSearch.setText("");
        }
    }
}
