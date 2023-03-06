package com.truongnqt.popcorn.moviesFragment.adapter;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.truongnqt.popcorn.databinding.ItemShowLargeMovieBinding;
import com.truongnqt.popcorn.network.movies.MovieBrief;

public class MovieBriefsLargeViewHolder extends RecyclerView.ViewHolder {
    private ItemShowLargeMovieBinding binding;
    private MovieBriefsLargeAdapter.ItemCallback itemCallback;


    public MovieBriefsLargeViewHolder(@NonNull ItemShowLargeMovieBinding binding, MovieBriefsLargeAdapter.ItemCallback itemCallback) {
        super(binding.getRoot());

        this.binding = binding;
        this.itemCallback = itemCallback;

        binding.getRoot().setOnClickListener(v -> {
            int pos = getBindingAdapterPosition();
            if(pos != RecyclerView.NO_POSITION && itemCallback != null) {
                this.itemCallback.onItemClick(pos, binding.getMovie());
            }
        });
    }

    public void bind(MovieBrief movieBrief, MovieBriefsLargeAdapter.ItemCallback itemCallback) {
        binding.setMovie(movieBrief);
        this.itemCallback = itemCallback;
    }
}
