package com.truongnqt.popcorn.viewAllTvShowFragment;

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
import com.truongnqt.popcorn.databinding.FragmentViewAllTvshowsBinding;
import com.truongnqt.popcorn.favourites.adapter.TVShowBriefsSmallAdapter;
import com.truongnqt.popcorn.network.tvshows.AiringTodayTVShowsResponse;
import com.truongnqt.popcorn.network.tvshows.OnTheAirTVShowsResponse;
import com.truongnqt.popcorn.network.tvshows.PopularTVShowsResponse;
import com.truongnqt.popcorn.network.tvshows.TopRatedTVShowsResponse;
import com.truongnqt.popcorn.tvShowDetailFragment.TVShowDetailFragment;
import com.truongnqt.popcorn.utils.Constants;

public class ViewAllTVShowsFragment extends Fragment {

    private Activity activity;
    private FragmentViewAllTvshowsBinding binding;
    private FragmentManager fragmentManager;
    private ViewAllTVShowViewModel viewModel;

    private int mTVShowType;
    private int presentPage = START_PAGE;
    private boolean isCheckValueAiringToday;
    private boolean isCheckValueOnTheAir;
    private boolean isCheckValuePopular;
    private boolean isCheckValueTopRated;

    private final TVShowBriefsSmallAdapter mTVShowsAdapter = new TVShowBriefsSmallAdapter(null, ((position, brief) -> {
        if(activity instanceof MainActivity)
            ((MainActivity) activity).setFragment(TVShowDetailFragment.newInstance(brief.getId()), true);
    }));

    private final Observer<AiringTodayTVShowsResponse> airingTodayTVShowsResponseObserver = airingTodayTVShowsResponse -> {
        if (airingTodayTVShowsResponse.getResults().size() > 0) {
            mTVShowsAdapter.newList(airingTodayTVShowsResponse.getResults());
            isCheckValueAiringToday = true;
            setVisibility();
        }
    };
    private final Observer<OnTheAirTVShowsResponse> onTheAirTVShowsResponseObserver = onTheAirTVShowsResponse -> {
        if (onTheAirTVShowsResponse.getResults().size() > 0) {
            mTVShowsAdapter.newList(onTheAirTVShowsResponse.getResults());
            isCheckValueOnTheAir = true;
            setVisibility();
        }
    };
    private final Observer<PopularTVShowsResponse> popularTVShowsResponseObserver = popularTVShowsResponse -> {
        if (popularTVShowsResponse.getResults().size() > 0) {
            mTVShowsAdapter.newList(popularTVShowsResponse.getResults());
            isCheckValuePopular = true;
            setVisibility();
        }
    };

    private final Observer<TopRatedTVShowsResponse> topRatedTVShowsResponseObserver = topRatedTVShowsResponse -> {
        if (topRatedTVShowsResponse.getResults().size() > 0) {
            mTVShowsAdapter.newList(topRatedTVShowsResponse.getResults());
            isCheckValueTopRated = true;
            setVisibility();
        }
    };

    public static ViewAllTVShowsFragment newInstance(int tvShowType) {
        ViewAllTVShowsFragment fragment = new ViewAllTVShowsFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(Constants.VIEW_ALL_TV_SHOWS_TYPE, tvShowType);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        activity = requireActivity();
        fragmentManager = getParentFragmentManager();
        Bundle receivedIntent = getArguments();
        assert receivedIntent != null;
        mTVShowType = receivedIntent.getInt(Constants.VIEW_ALL_TV_SHOWS_TYPE, -1);
        if (mTVShowType == -1) fragmentManager.popBackStack();

        viewModel = new ViewModelProvider(this).get(ViewAllTVShowViewModel.class);
        viewModel.setApiKey(getString(R.string.MOVIE_DB_API_KEY));
        switch (mTVShowType) {
            case Constants.AIRING_TODAY_TV_SHOWS_TYPE:
                viewModel.getAiringTodayTVShows(presentPage);
                break;
            case Constants.ON_THE_AIR_TV_SHOWS_TYPE:
                viewModel.getOnTheAirTVShows(presentPage);
                break;
            case Constants.POPULAR_TV_SHOWS_TYPE:
                viewModel.getPopularTVShows(presentPage);
                break;
            case Constants.TOP_RATED_TV_SHOWS_TYPE:
                viewModel.getTopRatedTVShows(presentPage);
                break;
        }
        observe();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_view_all_tvshows, container, false);
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
        if(isCheckValueAiringToday || isCheckValueOnTheAir || isCheckValuePopular || isCheckValueTopRated)
            presentPage = -1;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        binding = null;
    }

    @SuppressLint("FragmentLiveDataObserve")
    private void observe() {
        viewModel.getAiringTodayTVShowsLive().observe(this, airingTodayTVShowsResponseObserver);
        viewModel.getOnTheAirTVShowsLive().observe(this, onTheAirTVShowsResponseObserver);
        viewModel.getPopularTVShowsLive().observe(this, popularTVShowsResponseObserver);
        viewModel.getTopRatedTVShowsLive().observe(this, topRatedTVShowsResponseObserver);
    }

    private void setUpView() {
        binding.recyclerViewViewAll.setAdapter(mTVShowsAdapter);
        setVisibility();
    }

    public void reLoadData() {
        presentPage = START_PAGE;
        switch (mTVShowType) {
            case Constants.AIRING_TODAY_TV_SHOWS_TYPE:
                viewModel.getAiringTodayTVShows(presentPage);
                break;
            case Constants.ON_THE_AIR_TV_SHOWS_TYPE:
                viewModel.getOnTheAirTVShows(presentPage);
                break;
            case Constants.POPULAR_TV_SHOWS_TYPE:
                viewModel.getPopularTVShows(presentPage);
                break;
            case Constants.TOP_RATED_TV_SHOWS_TYPE:
                viewModel.getTopRatedTVShows(presentPage);
                break;
        }
        observe();
    }

    private void setVisibility() {
        if(isCheckValueAiringToday || isCheckValueOnTheAir || isCheckValuePopular || isCheckValueTopRated) {
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
