package com.truongnqt.popcorn.viewAllMoviesFragment;

import static com.truongnqt.popcorn.utils.Constants.START_PAGE;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import com.truongnqt.popcorn.R;
import com.truongnqt.popcorn.activities.MainActivity;
import com.truongnqt.popcorn.databinding.FragmentViewAllMoviesBinding;
import com.truongnqt.popcorn.favourites.adapter.MovieBriefsSmallAdapter;
import com.truongnqt.popcorn.movieDetailFragment.MovieDetailFragment;
import com.truongnqt.popcorn.moviesFragment.MovieViewModel;
import com.truongnqt.popcorn.network.movies.NowShowingMoviesResponse;
import com.truongnqt.popcorn.network.movies.PopularMoviesResponse;
import com.truongnqt.popcorn.network.movies.TopRatedMoviesResponse;
import com.truongnqt.popcorn.network.movies.UpcomingMoviesResponse;
import com.truongnqt.popcorn.utils.Constants;

public class ViewAllMoviesFragment extends Fragment {

    private Activity activity;
    private FragmentViewAllMoviesBinding binding;
    private FragmentManager fragmentManager;
    private MovieViewModel viewModel;

    private int mMovieType;
    private int currentPage = START_PAGE;
    private boolean isCheckValueNowShowing;
    private boolean isCheckValuePopular;
    private boolean isCheckValueUpcoming;
    private boolean isCheckValueTopRated;

    private final MovieBriefsSmallAdapter mMoviesAdapter = new MovieBriefsSmallAdapter(null, ((position, movieDiscover) -> {
        if(activity instanceof MainActivity)
            ((MainActivity) activity).setFragment(MovieDetailFragment.newInstance(movieDiscover.getId()), true);
    }));

    private final Observer<NowShowingMoviesResponse> nowShowingMoviesResponseObserver =
        nowShowingMoviesResponse -> {
            if (nowShowingMoviesResponse.getResults().size() > 0) {
                mMoviesAdapter.newList(nowShowingMoviesResponse.getResults());
                isCheckValueNowShowing = true;
                setVisibility();
            }
    };
    private final Observer<PopularMoviesResponse> popularMoviesResponseObserver =
        popularMoviesResponse -> {
            if (popularMoviesResponse.getResults().size() > 0) {
                mMoviesAdapter.newList(popularMoviesResponse.getResults());
                isCheckValuePopular = true;
                setVisibility();
            }
    };
    private final Observer<UpcomingMoviesResponse> upcomingMoviesResponseObserver =
        upcomingMoviesResponse -> {
            if (upcomingMoviesResponse.getResults().size() > 0) {
                mMoviesAdapter.newList(upcomingMoviesResponse.getResults());
                isCheckValueUpcoming = true;
                setVisibility();
            }
    };
    private final Observer<TopRatedMoviesResponse> topRatedMoviesResponseObserver =
        topRatedMoviesResponse -> {
            if (topRatedMoviesResponse.getResults().size() > 0) {
                mMoviesAdapter.newList(topRatedMoviesResponse.getResults());
                isCheckValueTopRated = true;
                setVisibility();
            }
    };

    public static ViewAllMoviesFragment newInstance(int movieType) {
        ViewAllMoviesFragment fragment = new ViewAllMoviesFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(Constants.VIEW_ALL_MOVIES_TYPE, movieType);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle receivedIntent = getArguments();
        assert receivedIntent != null;
        mMovieType = receivedIntent.getInt(Constants.VIEW_ALL_MOVIES_TYPE, -1);
        activity = requireActivity();
        fragmentManager = getParentFragmentManager();
        if (mMovieType == -1) fragmentManager.popBackStack();

        viewModel = new ViewModelProvider(this).get(MovieViewModel.class);
        viewModel.setApiKey(getResources().getString(R.string.MOVIE_DB_API_KEY));
        switch (mMovieType) {
            case Constants.NOW_SHOWING_MOVIES_TYPE:
                viewModel.getNowShowing(currentPage);
                break;
            case Constants.POPULAR_MOVIES_TYPE:
                viewModel.getPopular(currentPage);
                break;
            case Constants.UPCOMING_MOVIES_TYPE:
                viewModel.getUpcoming(currentPage);
                break;
            case Constants.TOP_RATED_MOVIES_TYPE:
                viewModel.getTopRated(currentPage);
                break;
        }
        observe();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_view_all_movies, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setUpView();
    }

    @Override
    public void onPause() {
        super.onPause();
        if(isCheckValueNowShowing || isCheckValuePopular || isCheckValueUpcoming || isCheckValueTopRated)
            mMovieType = -1;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        binding = null;
    }

    @SuppressLint("FragmentLiveDataObserve")
    private void observe() {
        viewModel.getNowShowingLive().observe(this, nowShowingMoviesResponseObserver);
        viewModel.getPopularLive().observe(this, popularMoviesResponseObserver);
        viewModel.getUpcomingLive().observe(this, upcomingMoviesResponseObserver);
        viewModel.getTopRatedLive().observe(this, topRatedMoviesResponseObserver);
    }

    public void reLoadData() {
        currentPage = START_PAGE;
        switch (mMovieType) {
            case Constants.NOW_SHOWING_MOVIES_TYPE:
                viewModel.getNowShowing(currentPage);
                break;
            case Constants.POPULAR_MOVIES_TYPE:
                viewModel.getPopular(currentPage);
                break;
            case Constants.UPCOMING_MOVIES_TYPE:
                viewModel.getUpcoming(currentPage);
                break;
            case Constants.TOP_RATED_MOVIES_TYPE:
                viewModel.getTopRated(currentPage);
                break;
        }
        observe();
    }

    private void setUpView() {
        boolean pagesOver = false;
        if(pagesOver) return;
        binding.recyclerViewViewAll.setAdapter(mMoviesAdapter);
        setVisibility();
    }

    private void setVisibility() {
        if(isCheckValueNowShowing || isCheckValuePopular || isCheckValueUpcoming || isCheckValueTopRated) {
            binding.recyclerViewViewAll.setVisibility(View.VISIBLE);
            binding.loadingFrame.setVisibility(View.GONE);
        } else reLoadData();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            fragmentManager.popBackStack();
        }
        return super.onOptionsItemSelected(item);
    }

}
