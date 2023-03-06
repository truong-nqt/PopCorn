package com.truongnqt.popcorn.tvShowsFragment.adapter;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.truongnqt.popcorn.R;
import com.truongnqt.popcorn.network.tvshows.TVShowBrief;

import java.util.ArrayList;
import java.util.List;

public class TVShowBriefsLargeAdapter extends RecyclerView.Adapter<TVShowBriefsLargeViewHolder> {
    private final List<TVShowBrief> briefs;
    private final ItemCallback itemCallback;

    public TVShowBriefsLargeAdapter(List<TVShowBrief> brief, ItemCallback itemCallback) {
        this.briefs = brief != null ? brief : new ArrayList<>();
        this.itemCallback = itemCallback;
    }

    @SuppressLint("NotifyDataSetChanged")
    public void newList(List<TVShowBrief> briefs) {
        if(briefs != null) {
            this.briefs.clear();
            this.briefs.addAll(briefs);
            notifyDataSetChanged();
        }
    }

    @NonNull
    @Override
    public TVShowBriefsLargeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        com.truongnqt.popcorn.databinding.ItemShowLargeTvBinding binding = DataBindingUtil.inflate(inflater, R.layout.item_show_large_tv, parent, false);
        return new TVShowBriefsLargeViewHolder(binding, itemCallback);
    }

    @Override
    public void onBindViewHolder(@NonNull TVShowBriefsLargeViewHolder holder, int position) {
        holder.bind(briefs.get(position), itemCallback);
    }

    @Override
    public int getItemCount() {
        return briefs.size();
    }

    public interface ItemCallback {
        void OnItemClick(int position, TVShowBrief brief);
    }
}
