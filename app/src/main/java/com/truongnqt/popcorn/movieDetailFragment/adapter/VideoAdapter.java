package com.truongnqt.popcorn.movieDetailFragment.adapter;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.truongnqt.popcorn.R;
import com.truongnqt.popcorn.databinding.ItemVideoBinding;
import com.truongnqt.popcorn.network.videos.Video;

import java.util.ArrayList;
import java.util.List;

public class VideoAdapter extends RecyclerView.Adapter<VideoViewHolder> {

    private final List<Video> mVideos;
    private final ItemCallback itemCallback;

    public VideoAdapter(List<Video> videos, ItemCallback itemCallback) {
        this.mVideos = videos != null ? videos : new ArrayList<>();
        this.itemCallback = itemCallback;
    }

    @SuppressLint("NotifyDataSetChanged")
    public void newList(List<Video> videos) {
        if(videos != null) {
            mVideos.clear();
            mVideos.addAll(videos);
            notifyDataSetChanged();
        }
    }

    @NonNull
    @Override
    public VideoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        ItemVideoBinding binding = DataBindingUtil.inflate(inflater, R.layout.item_video, parent, false);
        return new VideoViewHolder(binding, itemCallback);
    }

    @Override
    public void onBindViewHolder(@NonNull VideoViewHolder holder, int position) {
        holder.bind(mVideos.get(position), itemCallback);
    }

    @Override
    public int getItemCount() {
        return mVideos.size();
    }

    public interface ItemCallback {
        void onItemClick(int position, Video video);
    }
}
