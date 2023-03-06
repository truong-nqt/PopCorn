package com.truongnqt.popcorn.viewShowMenuFragment.adapters;

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

public class TVShowBriefAdapter extends RecyclerView.Adapter<TVShowBriefViewHolder> {
    private final List<TVShowBrief> tvShowBriefs;
    private ItemCallback itemCallback;

    public TVShowBriefAdapter(List<TVShowBrief> items, ItemCallback itemCallback) {
        if (items != null) {
            tvShowBriefs = items;
        } else {
            tvShowBriefs = new ArrayList<>();
        }
        this.itemCallback = itemCallback;
    }

    public void setItemCallback(ItemCallback callback) {
        itemCallback = callback;
    }

    public void addItems(List<TVShowBrief> tvShowBriefList) {
        if (tvShowBriefList != null) {
            int count = tvShowBriefList.size();
            int start = tvShowBriefs.size();
            tvShowBriefs.addAll(tvShowBriefList);
            notifyItemRangeInserted(start, count);
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    public void newList(List<TVShowBrief> tvShowBriefs) {
        if (tvShowBriefs != null) {
            this.tvShowBriefs.clear();
            this.tvShowBriefs.addAll(tvShowBriefs);
            notifyDataSetChanged();
        }
    }

    @NonNull
    @Override
    public TVShowBriefViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        ItemShowInTvBinding binding = DataBindingUtil.inflate(inflater, R.layout.item_show_in_tv, parent, false);
        return new TVShowBriefViewHolder(binding, itemCallback);
    }

    @Override
    public void onBindViewHolder(@NonNull TVShowBriefViewHolder holder, int position) {
        holder.bind(tvShowBriefs.get(position), itemCallback);
    }

    @Override
    public int getItemCount() {
        return tvShowBriefs.size();
    }


    public interface ItemCallback {
        void onItemClick(int position, TVShowBrief movieDiscover);
    }
}
