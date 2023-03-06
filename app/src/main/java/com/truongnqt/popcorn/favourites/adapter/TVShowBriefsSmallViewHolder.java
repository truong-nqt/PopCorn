package com.truongnqt.popcorn.favourites.adapter;

import androidx.recyclerview.widget.RecyclerView;

import com.truongnqt.popcorn.databinding.ItemShowInTvBinding;
import com.truongnqt.popcorn.network.tvshows.TVShowBrief;

public class TVShowBriefsSmallViewHolder extends RecyclerView.ViewHolder {
    private final ItemShowInTvBinding binding;
    private TVShowBriefsSmallAdapter.ItemCallback itemCallback;

    public TVShowBriefsSmallViewHolder(ItemShowInTvBinding binding, TVShowBriefsSmallAdapter.ItemCallback itemCallback) {
        super(binding.getRoot());
        this.binding = binding;
        this.itemCallback = itemCallback;

        this.binding.getRoot().setOnClickListener(v -> {
            int position = getBindingAdapterPosition();
            if(RecyclerView.NO_POSITION != position && itemCallback != null) {
                this.itemCallback.OnItemClick(position, binding.getTv());
            }
        });
    }

    public void bind(TVShowBrief tvShowBrief, TVShowBriefsSmallAdapter.ItemCallback itemCallback) {
        this.itemCallback = itemCallback;
        binding.setTv(tvShowBrief);
    }
}