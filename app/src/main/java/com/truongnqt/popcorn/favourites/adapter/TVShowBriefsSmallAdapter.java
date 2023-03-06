package com.truongnqt.popcorn.favourites.adapter;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.truongnqt.popcorn.R;
import com.truongnqt.popcorn.databinding.ItemShowInTvBinding;
import com.truongnqt.popcorn.network.tvshows.TVShowBrief;

import java.util.ArrayList;
import java.util.List;

public class TVShowBriefsSmallAdapter extends RecyclerView.Adapter<TVShowBriefsSmallViewHolder> {
    private final List<TVShowBrief> briefs;
    private final ItemCallback itemCallback;

    public TVShowBriefsSmallAdapter(List<TVShowBrief> brief, ItemCallback itemCallback) {
        this.briefs = brief != null ? brief : new ArrayList<>();
        this.itemCallback = itemCallback;
    }

    public void addItem(List<TVShowBrief> briefs) {
        if(briefs != null) {
            int count = briefs.size();
            int start = this.briefs.size();
            this.briefs.addAll(briefs);
            notifyItemRangeInserted(start, count);
        }
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
    public TVShowBriefsSmallViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        ItemShowInTvBinding binding = DataBindingUtil.inflate(inflater, R.layout.item_show_in_tv, parent,false);
        return new TVShowBriefsSmallViewHolder(binding, itemCallback);
    }

    @Override
    public void onBindViewHolder(@NonNull TVShowBriefsSmallViewHolder holder, int position) {
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