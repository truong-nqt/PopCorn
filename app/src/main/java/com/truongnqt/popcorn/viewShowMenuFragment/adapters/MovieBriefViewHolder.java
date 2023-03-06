package com.truongnqt.popcorn.viewShowMenuFragment.adapters;

import androidx.recyclerview.widget.RecyclerView;

import com.truongnqt.popcorn.databinding.ItemShowInMovieBinding;
import com.truongnqt.popcorn.network.movies.MovieBrief;

public class MovieBriefViewHolder extends RecyclerView.ViewHolder {
    private final ItemShowInMovieBinding binding;
    private MovieBriefAdapter.ItemCallback itemCallback;

    public MovieBriefViewHolder(ItemShowInMovieBinding binding, MovieBriefAdapter.ItemCallback itemCallback) {
        super(binding.getRoot());
        this.binding = binding;
        this.itemCallback = itemCallback;

        binding.getRoot().setOnClickListener(v -> {
            int pos = getBindingAdapterPosition();
            if (pos != RecyclerView.NO_POSITION && itemCallback != null) {
                this.itemCallback.onItemClick(pos, binding.getMovie());
            }
        });
    }

    public void bind(MovieBrief movie, MovieBriefAdapter.ItemCallback itemCallback) {
        binding.setMovie(movie);
        this.itemCallback = itemCallback;
    }
}
