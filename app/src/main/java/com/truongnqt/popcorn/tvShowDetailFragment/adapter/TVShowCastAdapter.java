package com.truongnqt.popcorn.tvShowDetailFragment.adapter;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.truongnqt.popcorn.R;
import com.truongnqt.popcorn.databinding.ItemCastTvBinding;
import com.truongnqt.popcorn.network.tvshows.TVShowCastBrief;

import java.util.ArrayList;
import java.util.List;

public class TVShowCastAdapter extends RecyclerView.Adapter<TVShowCastViewHolder> {
    private final List<TVShowCastBrief> briefs;
    private final ItemCallback itemCallback;

    public TVShowCastAdapter(List<TVShowCastBrief> briefs, ItemCallback itemCallback) {
        this.briefs = briefs != null ? briefs : new ArrayList<>();
        this.itemCallback = itemCallback;
    }

    @SuppressLint("NotifyDataSetChanged")
    public void newList(List<TVShowCastBrief> briefs) {
        if(briefs != null) {
            this.briefs.clear();
            this.briefs.addAll(briefs);
            notifyDataSetChanged();
        }
    }

    @NonNull
    @Override
    public TVShowCastViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        ItemCastTvBinding binding = DataBindingUtil.inflate(inflater, R.layout.item_cast_tv, parent, false);
        return new TVShowCastViewHolder(binding, itemCallback);
    }

    @Override
    public void onBindViewHolder(@NonNull TVShowCastViewHolder holder, int position) {
        holder.bind(briefs.get(position), itemCallback);
    }

    @Override
    public int getItemCount() {
        return briefs.size();
    }

    public interface ItemCallback {
        void OnItemClick(int position, TVShowCastBrief brief);
    }
}
