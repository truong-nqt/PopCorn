package com.truongnqt.popcorn.viewShowMenuFragment.adapters;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.truongnqt.popcorn.R;
import com.truongnqt.popcorn.databinding.ItemShowInMovieBinding;
import com.truongnqt.popcorn.network.movies.MovieBrief;

import java.util.ArrayList;
import java.util.List;

public class MovieBriefAdapter extends RecyclerView.Adapter<MovieBriefViewHolder> {
    private final List<MovieBrief> moviemovieBriefs;
    private ItemCallback itemCallback;

    public MovieBriefAdapter(List<MovieBrief> items, ItemCallback itemCallback) {
        if (items != null) {
            moviemovieBriefs = items;
        } else {
            moviemovieBriefs = new ArrayList<>();
        }
        this.itemCallback = itemCallback;
    }

    public void setItemCallback(ItemCallback callback) {
        itemCallback = callback;
    }

    public void addItems(List<MovieBrief> movies) {
        if (movies != null) {
            int count = movies.size();
            int start = moviemovieBriefs.size();
            moviemovieBriefs.addAll(movies);
            notifyItemRangeInserted(start, count);
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    public void newList(List<MovieBrief> movies) {
        if (movies != null) {
            moviemovieBriefs.clear();
            moviemovieBriefs.addAll(movies);
            notifyDataSetChanged();
        }
    }

    @NonNull
    @Override
    public MovieBriefViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        ItemShowInMovieBinding binding = DataBindingUtil.inflate(inflater, R.layout.item_show_in_movie, parent, false);
        return new MovieBriefViewHolder(binding, itemCallback);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieBriefViewHolder holder, int position) {
        holder.bind(moviemovieBriefs.get(position), itemCallback);
    }

    @Override
    public int getItemCount() {
        return moviemovieBriefs.size();
    }


    public interface ItemCallback {
        void onItemClick(int position, MovieBrief movieBrief);
    }
}
