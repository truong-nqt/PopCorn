package com.truongnqt.popcorn.favourites.fragment;

import android.app.Activity;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.truongnqt.popcorn.R;
import com.truongnqt.popcorn.activities.MainActivity;
import com.truongnqt.popcorn.databinding.FragmentFavouriteTvShowsBinding;
import com.truongnqt.popcorn.favourites.adapter.TVShowBriefsSmallAdapter;
import com.truongnqt.popcorn.network.tvshows.TVShowBrief;
import com.truongnqt.popcorn.tvShowDetailFragment.TVShowDetailFragment;
import com.truongnqt.popcorn.utils.Favourite;

import java.util.List;

public class FavouriteTVShowsFragment extends Fragment {
    private Activity activity;
    private FragmentFavouriteTvShowsBinding binding;

    private final TVShowBriefsSmallAdapter mFavTVShowsAdapter = new TVShowBriefsSmallAdapter(null, ((position, brief) -> {
        Log.d("FavouriteTVShows", ":TVShowBriefsSmallAdapter ");
        if(activity instanceof MainActivity) {
            ((MainActivity) activity).setFragment(TVShowDetailFragment.newInstance(brief.getId()), true);
        }
    }));

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = requireActivity();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_favourite_tv_shows, container, false);
        binding.recyclerViewFavTvShows.setAdapter(mFavTVShowsAdapter);
        loadFavTVShows();
        return binding.getRoot();
    }

    private void loadFavTVShows() {
        List<TVShowBrief> favTVShowBriefs = Favourite.getFavTVShowBriefs(getContext());
        if (favTVShowBriefs.isEmpty())
            binding.layoutRecyclerViewFavTvShowsEmpty.setVisibility(View.VISIBLE);
        mFavTVShowsAdapter.newList(favTVShowBriefs);
    }

}
