package com.truongnqt.popcorn.viewShowMenuFragment.adapters;

import androidx.recyclerview.widget.RecyclerView;

import com.truongnqt.popcorn.databinding.ItemShowInTvBinding;
import com.truongnqt.popcorn.network.tvshows.TVShowBrief;

public class TVShowBriefViewHolder extends RecyclerView.ViewHolder {
    private final ItemShowInTvBinding binding;
    private TVShowBriefAdapter.ItemCallback itemCallback;

    public TVShowBriefViewHolder(ItemShowInTvBinding binding, TVShowBriefAdapter.ItemCallback itemCallback) {
        super(binding.getRoot());
        this.binding = binding;
        this.itemCallback = itemCallback;

        binding.getRoot().setOnClickListener(v -> {
            int pos = getBindingAdapterPosition();
            if (pos != RecyclerView.NO_POSITION && itemCallback != null) {
                this.itemCallback.onItemClick(pos, binding.getTv());
            }
        });
    }

    public void bind(TVShowBrief tvShowBrief, TVShowBriefAdapter.ItemCallback itemCallback) {
        binding.setTv(tvShowBrief);
        this.itemCallback = itemCallback;
    }
}
