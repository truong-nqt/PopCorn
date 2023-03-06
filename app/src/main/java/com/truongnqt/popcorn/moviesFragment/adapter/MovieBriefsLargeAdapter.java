package com.truongnqt.popcorn.moviesFragment.adapter;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.truongnqt.popcorn.R;
import com.truongnqt.popcorn.databinding.ItemShowLargeMovieBinding;
import com.truongnqt.popcorn.network.movies.MovieBrief;

import java.util.ArrayList;
import java.util.List;

public class MovieBriefsLargeAdapter extends RecyclerView.Adapter<MovieBriefsLargeViewHolder> {

    private final List<MovieBrief> mMovies;
    private ItemCallback itemCallback;

    public MovieBriefsLargeAdapter(List<MovieBrief> movieBriefs, ItemCallback itemCallback) {
        if (movieBriefs != null) {
            this.mMovies = movieBriefs;
        } else {
            this.mMovies = new ArrayList<>();
        }
        this.itemCallback = itemCallback;
    }

    public void setItemCallback(ItemCallback item) {
        itemCallback = item;
    }

    @SuppressLint("NotifyDataSetChanged")
    public void newList(List<MovieBrief> movieBriefs) {
        if(movieBriefs != null) {
            mMovies.clear();
            mMovies.addAll(movieBriefs);
            notifyDataSetChanged();
        }
    }

    @NonNull
    @Override
    public MovieBriefsLargeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        ItemShowLargeMovieBinding binding = DataBindingUtil.inflate(inflater, R.layout.item_show_large_movie, parent, false);
        return new MovieBriefsLargeViewHolder(binding, itemCallback);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieBriefsLargeViewHolder holder, int position) {
        holder.bind(mMovies.get(position), itemCallback);
    }

    @Override
    public int getItemCount() {
        return mMovies.size();
    }

    public interface ItemCallback {
        void onItemClick(int position, MovieBrief movieBrief);
    }
}
