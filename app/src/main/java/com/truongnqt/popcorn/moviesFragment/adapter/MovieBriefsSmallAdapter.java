package com.truongnqt.popcorn.moviesFragment.adapter;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.truongnqt.popcorn.R;
import com.truongnqt.popcorn.databinding.ItemShowSmallMovieBinding;
import com.truongnqt.popcorn.network.movies.MovieBrief;

import java.util.ArrayList;
import java.util.List;

public class MovieBriefsSmallAdapter extends RecyclerView.Adapter<MovieBriefsSmallViewHolder> {
    private final List<MovieBrief> movieBriefs;
    private final ItemCallback itemCallback;

    public MovieBriefsSmallAdapter(List<MovieBrief> movieBriefs, ItemCallback itemCallback) {
        this.movieBriefs = movieBriefs != null ? movieBriefs : new ArrayList<>();
        this.itemCallback = itemCallback;
    }

    public void addItem(List<MovieBrief> movieBriefs) {
        if(movieBriefs != null) {
            int count = movieBriefs.size();
            int start = this.movieBriefs.size();
            this.movieBriefs.addAll(movieBriefs);
            notifyItemRangeInserted(start, count);
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    public void newList(List<MovieBrief> movieBriefs) {
        if(movieBriefs != null) {
            this.movieBriefs.clear();
            this.movieBriefs.addAll(movieBriefs);
            notifyDataSetChanged();
        }
    }

    @NonNull
    @Override
    public MovieBriefsSmallViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        ItemShowSmallMovieBinding binding = DataBindingUtil.inflate(inflater, R.layout.item_show_small_movie, parent,false);
        return new MovieBriefsSmallViewHolder(binding, this.itemCallback);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieBriefsSmallViewHolder holder, int position) {
        holder.bind(movieBriefs.get(position), this.itemCallback);
    }

    @Override
    public int getItemCount() {
        return movieBriefs.size();
    }

    public interface ItemCallback {
        void onClickItem(int position, MovieBrief movieBrief);
    }
}
