package com.truongnqt.popcorn.favourites.fragment;

import static com.truongnqt.popcorn.utils.Constants.TAG;

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
import com.truongnqt.popcorn.databinding.FragmentFavouriteMoviesBinding;
import com.truongnqt.popcorn.favourites.adapter.MovieBriefsSmallAdapter;
import com.truongnqt.popcorn.movieDetailFragment.MovieDetailFragment;
import com.truongnqt.popcorn.network.movies.MovieBrief;
import com.truongnqt.popcorn.utils.Favourite;

import java.util.List;

public class FavouriteMoviesFragment extends Fragment {

    private Activity activity;
    private FragmentFavouriteMoviesBinding binding;

    private final MovieBriefsSmallAdapter mFavMoviesAdapter = new MovieBriefsSmallAdapter(null, ((position, movieDiscover) -> {
        Log.d(TAG, "movie discover item clicked " + position);
        if(activity instanceof MainActivity) {
            ((MainActivity) activity).setFragment(MovieDetailFragment.newInstance(movieDiscover.getId()), true);
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
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_favourite_movies, container, false);
        loadFavMovies();
        binding.recyclerViewFavMovies.setAdapter(mFavMoviesAdapter);
        return binding.getRoot();
    }

    private void loadFavMovies() {
        List<MovieBrief> favMovieBriefs = Favourite.getFavMovieBriefs(getContext());
        if (favMovieBriefs.isEmpty())
            binding.layoutRecyclerViewFavMoviesEmpty.setVisibility(View.VISIBLE);
        mFavMoviesAdapter.newList(favMovieBriefs);
    }

}
