package com.truongnqt.popcorn.viewShowMenuFragment;

import static com.truongnqt.popcorn.R.layout.fragment_view_show_in_menu;
import static com.truongnqt.popcorn.utils.Constants.START_PAGE;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.truongnqt.popcorn.activities.MainActivity;
import com.truongnqt.popcorn.databinding.FragmentViewShowInMenuBinding;
import com.truongnqt.popcorn.movieDetailFragment.MovieDetailFragment;
import com.truongnqt.popcorn.network.movies.NowShowingMoviesResponse;
import com.truongnqt.popcorn.network.movies.TopRatedMoviesResponse;
import com.truongnqt.popcorn.network.movies.TrendingMoviesResponse;
import com.truongnqt.popcorn.network.tvshows.TopRatedTVShowsResponse;
import com.truongnqt.popcorn.tvShowDetailFragment.TVShowDetailFragment;
import com.truongnqt.popcorn.utils.Constants;
import com.truongnqt.popcorn.viewShowMenuFragment.adapters.MovieBriefAdapter;
import com.truongnqt.popcorn.R;
import com.truongnqt.popcorn.viewShowMenuFragment.adapters.TVShowBriefAdapter;

public class ViewShowMenu extends Fragment {

    private Activity activity;
    private FragmentViewShowInMenuBinding binding;
    private ViewShowMenuViewModel viewModel;

    private int typeMenu;
    private int currentPage = START_PAGE;
    private boolean isCheckValueNowShowingMovies;
    private boolean isCheckValueTopRatedMovies;
    private boolean isCheckValueTopRatedTV;
    private boolean isCheckValueTrendingMovies;

    private final MovieBriefAdapter movieBriefAdapter = new MovieBriefAdapter(null,
            ((position, movieDiscover) -> {
                if(activity instanceof MainActivity) {
                    ((MainActivity) activity).setFragment(MovieDetailFragment.newInstance(movieDiscover.getId()), true);
                }
            }));
    private final TVShowBriefAdapter tvShowBriefAdapter = new TVShowBriefAdapter(null, (position, tvShowBrief) -> {
        if(activity instanceof MainActivity) {
            ((MainActivity) activity).setFragment(TVShowDetailFragment.newInstance(tvShowBrief.getId()), true);
        }
    });

    private final Observer<NowShowingMoviesResponse> nowShowingMoviesObserver = nowShowingMoviesResponse -> {
        if (nowShowingMoviesResponse.getResults().size() > 0) {
            movieBriefAdapter.newList(nowShowingMoviesResponse.getResults());
            isCheckValueNowShowingMovies = true;
            setVisibility();
        }
    };
    private final Observer<TopRatedMoviesResponse> topRatedMoviesObserver = topRatedMoviesResponse -> {
        if (topRatedMoviesResponse.getResults().size() > 0) {
            movieBriefAdapter.newList(topRatedMoviesResponse.getResults());
            isCheckValueTopRatedMovies = true;
            setVisibility();
        }
    };
    private final Observer<TopRatedTVShowsResponse> topRatedTVShowsObserver = topRatedTVShowsResponse -> {
        if (topRatedTVShowsResponse.getResults().size() > 0) {
            tvShowBriefAdapter.newList(topRatedTVShowsResponse.getResults());
            isCheckValueTopRatedTV = true;
            setVisibility();
        }
    };
    private final Observer<TrendingMoviesResponse> trendingMoviesObserver = trendingMoviesResponse -> {
        if (trendingMoviesResponse.getResults().size() > 0) {
            movieBriefAdapter.newList(trendingMoviesResponse.getResults());
            isCheckValueTrendingMovies = true;
            setVisibility();
        }
    };

    public static ViewShowMenu newInstance(int typeMenu) {
        ViewShowMenu fragment = new ViewShowMenu();
        Bundle args = new Bundle();
        args.putInt(Constants.MENU_SHOW_ID, typeMenu);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        activity = requireActivity();
        FragmentManager fragmentManager = getParentFragmentManager();
        typeMenu = getArguments().getInt(Constants.MENU_SHOW_ID, -1);
        if (typeMenu == -1)
            fragmentManager.popBackStack();

        viewModel = new ViewModelProvider(this).get(ViewShowMenuViewModel.class);
        viewModel.setApiKey(getResources().getString(R.string.MOVIE_DB_API_KEY));
        switch (typeMenu) {
            case Constants.NOW_SHOWING_MOVIES_TYPE:
                viewModel.getNowShowing(currentPage);
                break;
            case Constants.POPULAR_MOVIES_TYPE:
                viewModel.getTopRatedMovies(currentPage);
                break;
            case Constants.UPCOMING_MOVIES_TYPE:
                viewModel.getTopRatedTVShows(currentPage);
                break;
            case Constants.TOP_RATED_MOVIES_TYPE:
                viewModel.getTrending(Constants.MOVIE, Constants.WEEK);
                break;
        }
        observe();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, fragment_view_show_in_menu, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setUpViews();
    }

    @Override
    public void onPause() {
        super.onPause();
        if(isCheckValueNowShowingMovies || isCheckValueTopRatedMovies
                || isCheckValueTopRatedTV || isCheckValueTrendingMovies)
            currentPage = -1;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        binding = null;
    }

    private void setUpViews() {
        if(typeMenu == Constants.UPCOMING_MOVIES_TYPE) {
            binding.rcvMovieDiscover.setAdapter(tvShowBriefAdapter);
        } else
            binding.rcvMovieDiscover.setAdapter(movieBriefAdapter);

        setVisibility();
    }

    public void reLoadData() {
        currentPage = START_PAGE;
        switch (typeMenu) {
            case Constants.NOW_SHOWING_MOVIES_TYPE:
                viewModel.getNowShowing(currentPage);
                break;
            case Constants.POPULAR_MOVIES_TYPE:
                viewModel.getTopRatedMovies(currentPage);
                break;
            case Constants.UPCOMING_MOVIES_TYPE:
                viewModel.getTopRatedTVShows(currentPage);
                break;
            case Constants.TOP_RATED_MOVIES_TYPE:
                viewModel.getTrending(Constants.MOVIE, Constants.WEEK);
                break;
        }
        observe();
    }

    private void setVisibility() {
        if(isCheckValueNowShowingMovies || isCheckValueTopRatedMovies
                || isCheckValueTopRatedTV || isCheckValueTrendingMovies) {
            binding.rcvMovieDiscover.setVisibility(View.VISIBLE);
            binding.loadingFrame.setVisibility(View.GONE);
        } else reLoadData();
    }

    @SuppressLint("FragmentLiveDataObserve")
    private void observe() {
        viewModel.getNowShowingMoviesLive().observe(this, nowShowingMoviesObserver);
        viewModel.getTopRatedTVShowsLive().observe(this, topRatedTVShowsObserver);
        viewModel.getTopRatedMoviesLive().observe(this, topRatedMoviesObserver);
        viewModel.getTrendingMoviesLive().observe(this, trendingMoviesObserver);
    }
}