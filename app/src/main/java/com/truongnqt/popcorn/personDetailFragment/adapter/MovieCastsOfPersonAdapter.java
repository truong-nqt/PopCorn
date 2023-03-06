package com.truongnqt.popcorn.personDetailFragment.adapter;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.truongnqt.popcorn.R;
import com.truongnqt.popcorn.databinding.ItemShowOfCastBinding;
import com.truongnqt.popcorn.network.movies.MovieCastOfPerson;

import java.util.ArrayList;
import java.util.List;

public class MovieCastsOfPersonAdapter extends RecyclerView.Adapter<MovieCastsOfPersonViewHolder> {
    private final List<MovieCastOfPerson> personList;
    private final ItemCallback itemCallback;

    public MovieCastsOfPersonAdapter(List<MovieCastOfPerson> personList, ItemCallback itemCallback) {
        this.personList = personList != null ? personList : new ArrayList<>();
        this.itemCallback = itemCallback;
    }

    @SuppressLint("NotifyDataSetChanged")
    public void newList(List<MovieCastOfPerson> personList) {
        if(personList != null) {
            this.personList.clear();
            this.personList.addAll(personList);
            notifyDataSetChanged();
        }
    }

    @NonNull
    @Override
    public MovieCastsOfPersonViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        ItemShowOfCastBinding binding = DataBindingUtil.inflate(inflater, R.layout.item_show_of_cast, parent, false);
        return new MovieCastsOfPersonViewHolder(binding, itemCallback);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieCastsOfPersonViewHolder holder, int position) {
        holder.bind(personList.get(position), itemCallback);
    }

    @Override
    public int getItemCount() {
        return personList.size();
    }

    public interface ItemCallback{
        void onItemClick(int position, MovieCastOfPerson person);
    }
}
