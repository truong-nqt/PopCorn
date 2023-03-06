package com.truongnqt.popcorn.tvShowDetailFragment.adapter;

import androidx.recyclerview.widget.RecyclerView;

import com.truongnqt.popcorn.databinding.ItemVideoBinding;
import com.truongnqt.popcorn.network.videos.Video;

public class VideoViewHolder extends RecyclerView.ViewHolder {
    private final ItemVideoBinding binding;
    private VideoAdapter.ItemCallback itemCallback;

    public VideoViewHolder(ItemVideoBinding binding, VideoAdapter.ItemCallback itemCallback) {
        super(binding.getRoot());
        this.binding = binding;
        this.itemCallback = itemCallback;

        this.binding.getRoot().setOnClickListener(v -> {
            int position = getBindingAdapterPosition();
            if(position != RecyclerView.NO_POSITION && itemCallback != null) {
                this.itemCallback.OnItemClick(position, binding.getVideos());
            }
        });
    }

    public void bind(Video video, VideoAdapter.ItemCallback itemCallback) {
        binding.setVideos(video);
        this.itemCallback = itemCallback;
    }
}
