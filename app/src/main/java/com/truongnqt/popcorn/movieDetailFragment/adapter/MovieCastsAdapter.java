package com.truongnqt.popcorn.movieDetailFragment.adapter;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.truongnqt.popcorn.R;
import com.truongnqt.popcorn.databinding.ItemCastBinding;
import com.truongnqt.popcorn.network.movies.MovieCastBrief;

import java.util.ArrayList;
import java.util.List;

public class MovieCastsAdapter extends RecyclerView.Adapter<MovieCastsViewHolder> {
    private final List<MovieCastBrief> mCasts;
    private final ItemCallback itemCallback;

    public MovieCastsAdapter(List<MovieCastBrief> casts, ItemCallback itemCallback) {
        this.mCasts = casts != null ? casts : new ArrayList<>();
        this.itemCallback = itemCallback;
    }

    @SuppressLint("NotifyDataSetChanged")
    public void newList(List<MovieCastBrief> casts) {
        if(casts != null) {
            mCasts.clear();
            mCasts.addAll(casts);
            notifyDataSetChanged();
        }
    }

    @NonNull
    @Override
    public MovieCastsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        ItemCastBinding binding = DataBindingUtil.inflate(inflater, R.layout.item_cast, parent, false);
        return new MovieCastsViewHolder(binding, itemCallback);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieCastsViewHolder holder, int position) {
        holder.bind(mCasts.get(position), itemCallback);
    }

    @Override
    public int getItemCount() {
        return mCasts.size();
    }

    public interface ItemCallback {
        void onItemClick(int position, MovieCastBrief movieCastBrief);
    }
}
