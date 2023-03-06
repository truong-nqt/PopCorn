package com.truongnqt.popcorn.tvShowDetailFragment.adapter;

import androidx.recyclerview.widget.RecyclerView;
import com.truongnqt.popcorn.databinding.ItemCastTvBinding;
import com.truongnqt.popcorn.network.tvshows.TVShowCastBrief;

public class TVShowCastViewHolder extends RecyclerView.ViewHolder {
    private final ItemCastTvBinding binding;
    private TVShowCastAdapter.ItemCallback itemCallback;

    public TVShowCastViewHolder(ItemCastTvBinding binding, TVShowCastAdapter.ItemCallback itemCallback) {
        super(binding.getRoot());
        this.binding = binding;
        this.itemCallback = itemCallback;

        this.binding.getRoot().setOnClickListener(v -> {
            int position = getBindingAdapterPosition();

            if (position != RecyclerView.NO_POSITION && itemCallback != null) {
                this.itemCallback.OnItemClick(position, binding.getTvCastBrief());
            }
        });
    }

    public void bind(TVShowCastBrief brief, TVShowCastAdapter.ItemCallback itemCallback) {
        binding.setTvCastBrief(brief);
        this.itemCallback = itemCallback;
    }
}
