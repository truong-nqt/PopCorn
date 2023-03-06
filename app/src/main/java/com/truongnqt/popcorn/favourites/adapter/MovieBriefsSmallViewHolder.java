package com.truongnqt.popcorn.favourites.adapter;

import androidx.recyclerview.widget.RecyclerView;

import com.truongnqt.popcorn.databinding.ItemShowInMovieBinding;
import com.truongnqt.popcorn.network.movies.MovieBrief;

public class MovieBriefsSmallViewHolder extends RecyclerView.ViewHolder {
    private final ItemShowInMovieBinding binding;
    private MovieBriefsSmallAdapter.ItemCallback itemCallback;

    public MovieBriefsSmallViewHolder(ItemShowInMovieBinding binding, MovieBriefsSmallAdapter.ItemCallback itemCallback) {
        super(binding.getRoot());

        this.binding = binding;
        this.itemCallback = itemCallback;

        binding.getRoot().setOnClickListener(v -> {
            int pos = getBindingAdapterPosition();
            if (pos != RecyclerView.NO_POSITION && itemCallback != null) {
                this.itemCallback.onClickItem(pos, binding.getMovie());
            }
        });
    }

    public void bind(MovieBrief movieBrief, MovieBriefsSmallAdapter.ItemCallback itemCallback) {

        binding.setMovie(movieBrief);
        this.itemCallback = itemCallback;
    }
}
