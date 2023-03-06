package com.truongnqt.popcorn.movieDetailFragment.adapter;

import androidx.recyclerview.widget.RecyclerView;

import com.truongnqt.popcorn.databinding.ItemCastBinding;
import com.truongnqt.popcorn.network.movies.MovieCastBrief;

public class MovieCastsViewHolder extends RecyclerView.ViewHolder {
    private final ItemCastBinding binding;
    private MovieCastsAdapter.ItemCallback itemCallback;

    public MovieCastsViewHolder(ItemCastBinding binding, MovieCastsAdapter.ItemCallback itemCallback){
        super(binding.getRoot());
        this.binding = binding;
        this.itemCallback = itemCallback;

        binding.getRoot().setOnClickListener(view -> {
            int position = getBindingAdapterPosition();
            if(RecyclerView.NO_POSITION != position && itemCallback != null) {
                this.itemCallback.onItemClick(position, binding.getMovieCastBrief());
            }
        });
    }

    public void bind(MovieCastBrief movieCastBrief, MovieCastsAdapter.ItemCallback itemCallback) {
        binding.setMovieCastBrief(movieCastBrief);
        this.itemCallback = itemCallback;
    }
}
