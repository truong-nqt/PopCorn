package com.truongnqt.popcorn.personDetailFragment.adapter;

import androidx.recyclerview.widget.RecyclerView;

import com.truongnqt.popcorn.databinding.ItemShowOfCastTvBinding;
import com.truongnqt.popcorn.network.tvshows.TVCastOfPerson;

public class TVCastsOfPersonViewHolder extends RecyclerView.ViewHolder {
    private final ItemShowOfCastTvBinding binding;
    private TVCastsOfPersonAdapter.ItemCallback itemCallback;

    public TVCastsOfPersonViewHolder(ItemShowOfCastTvBinding binding, TVCastsOfPersonAdapter.ItemCallback itemCallback) {
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

    public void bind(TVCastOfPerson tvCastOfPerson, TVCastsOfPersonAdapter.ItemCallback itemCallback) {
        binding.setPerson(tvCastOfPerson);
        this.itemCallback = itemCallback;

    }
}
