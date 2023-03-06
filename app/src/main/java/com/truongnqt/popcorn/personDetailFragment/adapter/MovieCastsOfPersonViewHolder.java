package com.truongnqt.popcorn.personDetailFragment.adapter;

import androidx.recyclerview.widget.RecyclerView;

import com.truongnqt.popcorn.databinding.ItemShowOfCastBinding;
import com.truongnqt.popcorn.network.movies.MovieCastOfPerson;

public class MovieCastsOfPersonViewHolder extends RecyclerView.ViewHolder {
    private final ItemShowOfCastBinding binding;
    private MovieCastsOfPersonAdapter.ItemCallback itemCallback;

    public MovieCastsOfPersonViewHolder(ItemShowOfCastBinding binding, MovieCastsOfPersonAdapter.ItemCallback itemCallback) {
        super(binding.getRoot());

        this.binding = binding;
        this.itemCallback = itemCallback;

        this.binding.getRoot().setOnClickListener(v -> {
            int position = getBindingAdapterPosition();
            if (RecyclerView.NO_POSITION != position && itemCallback != null) {
                this.itemCallback.onItemClick(position, binding.getPerson());
            }
        });
    }

    public void bind(MovieCastOfPerson person, MovieCastsOfPersonAdapter.ItemCallback itemCallback) {
        this.itemCallback = itemCallback;
        binding.setPerson(person);
    }

}
