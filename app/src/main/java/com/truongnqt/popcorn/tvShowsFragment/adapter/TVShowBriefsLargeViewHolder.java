package com.truongnqt.popcorn.tvShowsFragment.adapter;

import androidx.recyclerview.widget.RecyclerView;

import com.truongnqt.popcorn.databinding.ItemShowLargeTvBinding;
import com.truongnqt.popcorn.network.tvshows.TVShowBrief;

public class TVShowBriefsLargeViewHolder extends RecyclerView.ViewHolder {
    private final ItemShowLargeTvBinding binding;
    private TVShowBriefsLargeAdapter.ItemCallback itemCallback;

    public TVShowBriefsLargeViewHolder(ItemShowLargeTvBinding binding, TVShowBriefsLargeAdapter.ItemCallback itemCallback) {
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

    public void bind(TVShowBrief brief, TVShowBriefsLargeAdapter.ItemCallback itemCallback) {
        this.itemCallback = itemCallback;

        binding.setTv(brief);
    }
}
