package com.truongnqt.popcorn.personDetailFragment.adapter;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.truongnqt.popcorn.R;
import com.truongnqt.popcorn.databinding.ItemShowOfCastTvBinding;
import com.truongnqt.popcorn.network.tvshows.TVCastOfPerson;

import java.util.ArrayList;
import java.util.List;

public class TVCastsOfPersonAdapter extends RecyclerView.Adapter<TVCastsOfPersonViewHolder> {

    private final List<TVCastOfPerson> mTVCasts;
    private final ItemCallback itemCallback;

    public TVCastsOfPersonAdapter(List<TVCastOfPerson> tVCasts, ItemCallback itemCallback) {
        this.mTVCasts = tVCasts != null ? tVCasts : new ArrayList<>();
        this.itemCallback = itemCallback;
    }

    @SuppressLint("NotifyDataSetChanged")
    public void newList(List<TVCastOfPerson> tVCasts) {
        if (tVCasts != null) {
            mTVCasts.clear();
            mTVCasts.addAll(tVCasts);
            notifyDataSetChanged();
        }
    }

    @NonNull
    @Override
    public TVCastsOfPersonViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        ItemShowOfCastTvBinding binding = DataBindingUtil.inflate(inflater, R.layout.item_show_of_cast_tv, parent, false);
        return new TVCastsOfPersonViewHolder(binding, itemCallback);
    }

    @Override
    public void onBindViewHolder(@NonNull TVCastsOfPersonViewHolder holder, int position) {
        holder.bind(mTVCasts.get(position), itemCallback);
    }

    @Override
    public int getItemCount() {
        return mTVCasts.size();
    }

    public interface ItemCallback {
        void onItemClick(int position, TVCastOfPerson tvCastOfPerson);
    }
}
