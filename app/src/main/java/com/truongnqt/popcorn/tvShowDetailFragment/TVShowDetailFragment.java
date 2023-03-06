package com.truongnqt.popcorn.tvShowDetailFragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.HapticFeedbackConstants;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.truongnqt.popcorn.R;
import com.truongnqt.popcorn.tvShowDetailFragment.adapter.TVShowCastAdapter;
import com.truongnqt.popcorn.tvShowDetailFragment.adapter.VideoAdapter;
import com.truongnqt.popcorn.tvShowsFragment.adapter.TVShowBriefsSmallAdapter;
import com.truongnqt.popcorn.activities.MainActivity;
import com.truongnqt.popcorn.databinding.FragmentTvshowDetailBinding;
import com.truongnqt.popcorn.network.tvshows.Genre;
import com.truongnqt.popcorn.network.tvshows.Network;
import com.truongnqt.popcorn.network.tvshows.SimilarTVShowsResponse;
import com.truongnqt.popcorn.network.tvshows.TVShow;
import com.truongnqt.popcorn.network.tvshows.TVShowCreditsResponse;
import com.truongnqt.popcorn.network.videos.VideosResponse;
import com.truongnqt.popcorn.personDetailFragment.PersonDetailFragment;
import com.truongnqt.popcorn.utils.Constants;
import com.truongnqt.popcorn.utils.Favourite;
import com.truongnqt.popcorn.utils.NetworkConnection;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TVShowDetailFragment extends Fragment {

    private FragmentTvshowDetailBinding binding;

    private Activity activity;
    private FragmentManager fragmentManager;
    private TVShowViewModel viewModel;

    private final List<TVShow> tvShows = new ArrayList<>();
    private Bundle receivedIntent;
    private int mTVShowId;
    private boolean isCheckValueTVShow;
    private boolean isCheckValueVideo;
    private boolean isFlagValueVideo;
    private boolean isCheckValueCredits;
    private boolean isFlagValueCredits;
    private boolean isCheckValueSimilar;
    private boolean isFlagValueSimilar;

    private final VideoAdapter videoAdapter = new VideoAdapter(null,
            ((position, video) -> {
                Intent youtubeIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(Constants.YOUTUBE_WATCH_BASE_URL + video.getKey()));
                requireContext().startActivity(youtubeIntent);
            }));
    private final TVShowCastAdapter castAdapter = new TVShowCastAdapter(null,
            ((position, brief) -> {
                if(activity instanceof MainActivity) {
                    ((MainActivity) activity).setFragment(PersonDetailFragment.newInstance(brief.getId()), true);
                }
            }));
    private final TVShowBriefsSmallAdapter similarTVShowsAdapter = new TVShowBriefsSmallAdapter(null,
            (position, brief) -> {
                if(activity instanceof MainActivity) {
                    fragmentManager.popBackStackImmediate();
                    ((MainActivity) activity).setFragment(TVShowDetailFragment.newInstance(brief.getId()), true);
                }
            });

    private final Observer<TVShow> tvShowObserver = tvShow -> {
        tvShows.add(tvShow);
        isCheckValueTVShow = true;
        loadTVShowDetails();
        setVisibility();
    };

    private final Observer<VideosResponse> videosResponseObserver = videosResponse -> {
        isFlagValueVideo = true;
        if(videosResponse.getVideos().size() > 0) {
            videoAdapter.newList(videosResponse.getVideos());
            isCheckValueVideo = true;
        }
        setVisibility();
    };

    private final Observer<TVShowCreditsResponse> tvShowCreditsResponseObserver = tvShowCreditsResponse -> {
        isFlagValueCredits = true;
        if(tvShowCreditsResponse.getCasts().size() > 0) {
            castAdapter.newList(tvShowCreditsResponse.getCasts());
            isCheckValueCredits = true;
        }
        setVisibility();
    };

    private final Observer<SimilarTVShowsResponse> similarTVShowsResponseObserver = similarTVShowsResponse -> {
        isFlagValueSimilar = true;
        if(similarTVShowsResponse.getResults().size() > 0) {
            similarTVShowsAdapter.newList(similarTVShowsResponse.getResults());
            isCheckValueSimilar = true;
        }
        setVisibility();
    };

    public static TVShowDetailFragment newInstance(int mTVShowId) {
        TVShowDetailFragment fragment = new TVShowDetailFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(Constants.TV_SHOW_ID, mTVShowId);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        receivedIntent = getArguments();
        assert receivedIntent != null;
        mTVShowId = receivedIntent.getInt(Constants.TV_SHOW_ID, -1);
        activity = getActivity();
        fragmentManager = requireActivity().getSupportFragmentManager();
        if(mTVShowId == -1) fragmentManager.popBackStack();

        viewModel = new ViewModelProvider(this).get(TVShowViewModel.class);
        viewModel.setApiKey(getResources().getString(R.string.MOVIE_DB_API_KEY));
        viewModel.getTVShowDetails(mTVShowId);
        viewModel.getTVShowVideos(mTVShowId);
        viewModel.getTVShowCredits(mTVShowId);
        viewModel.getSimilarTVShows(mTVShowId);
        observe();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_tvshow_detail, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        int mPosterWidth = (int) (getResources().getDisplayMetrics().widthPixels * 0.25);
        int mPosterHeight = (int) (mPosterWidth / 0.66);
        int mBackdropWidth = getResources().getDisplayMetrics().widthPixels;
        int mBackdropHeight = (int) (mBackdropWidth / 1.77);
        binding.layoutToolbarTvShow.getLayoutParams().height = mBackdropHeight + (int) (mPosterHeight * 0.9);
        binding.imageViewPoster.getLayoutParams().width = mPosterWidth;
        binding.imageViewPoster.getLayoutParams().height = mPosterHeight;
        binding.imageViewBackdrop.getLayoutParams().height = mBackdropHeight;
        setUpView();
        if (NetworkConnection.isConnected(requireContext())) {
            loadTVShowDetails();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if(isCheckValueTVShow && isFlagValueVideo && isFlagValueCredits && isFlagValueSimilar)
            mTVShowId = -1;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        binding = null;
    }

    @SuppressLint("FragmentLiveDataObserve")
    private void observe() {
        viewModel.getTVShowLive().observe(this, tvShowObserver);
        viewModel.getVideosLive().observe(this, videosResponseObserver);
        viewModel.getTvShowsCreditsLive().observe(this, tvShowCreditsResponseObserver);
        viewModel.getSimilarTVShowsLive().observe(this, similarTVShowsResponseObserver);
    }

    private void setUpView() {
        binding.contentTvshowDetail.recyclerViewTrailersTvShowDetail.setAdapter(videoAdapter);
        binding.contentTvshowDetail.recyclerViewCastTvShowDetail.setAdapter(castAdapter);
        binding.contentTvshowDetail.recyclerViewSimilarTvShowDetail.setAdapter(similarTVShowsAdapter);
        setVisibility();
    }

    public void reLoadData() {
        mTVShowId = receivedIntent.getInt(Constants.TV_SHOW_ID, -1);
        viewModel.getTVShowDetails(mTVShowId);
        viewModel.getTVShowVideos(mTVShowId);
        viewModel.getTVShowCredits(mTVShowId);
        viewModel.getSimilarTVShows(mTVShowId);
        observe();
    }

    private void setVisibility() {
        if(isCheckValueTVShow && isFlagValueVideo && isFlagValueCredits && isFlagValueSimilar) {
            binding.toolbarLayout.setVisibility(View.VISIBLE);
            binding.contentTvshowDetail.linearLayoutContentTvShowDetail.setVisibility(View.VISIBLE);
            binding.loadingFrame.setVisibility(View.GONE);
            if(!isCheckValueVideo) {
                binding.contentTvshowDetail.recyclerViewTrailersTvShowDetail.setVisibility(View.GONE);
                binding.contentTvshowDetail.textViewTrailerTvShowDetail.setVisibility(View.GONE);
                binding.contentTvshowDetail.lineViewVideoCast.setVisibility(View.GONE);
                Log.d("TAG", "setVisibility: " + isCheckValueVideo);

            }
            if(!isCheckValueCredits) {
                binding.contentTvshowDetail.recyclerViewCastTvShowDetail.setVisibility(View.GONE);
                binding.contentTvshowDetail.textViewCastTvShowDetail.setVisibility(View.GONE);
                binding.contentTvshowDetail.lineViewCastSimilar.setVisibility(View.GONE);
                binding.contentTvshowDetail.lineViewVideoCast.setVisibility(View.GONE);
                Log.d("TAG", "setVisibility: " + isCheckValueCredits);
            }
            if(!isCheckValueSimilar) {
                binding.contentTvshowDetail.recyclerViewSimilarTvShowDetail.setVisibility(View.GONE);
                binding.contentTvshowDetail.textViewSimilarTvShowDetail.setVisibility(View.GONE);
                binding.contentTvshowDetail.lineViewCastSimilar.setVisibility(View.GONE);
                Log.d("TAG", "setVisibility: " + isCheckValueSimilar);
            }
        } else
            reLoadData();
    }

    private void loadTVShowDetails() {
        for(TVShow tvShow : tvShows) {
            if(tvShow != null) {
                binding.setTvShow(tvShow);
                binding.contentTvshowDetail.setTvShow(tvShow);
                setDetails(tvShow.getFirstAirDate(), tvShow.getEpisodeRunTime(), tvShow.getStatus(), tvShow.getOriginCountries(), tvShow.getNetworks());
                setGenres(tvShow.getGenres());
                setYear(tvShow.getFirstAirDate());
                setImageButtons(tvShow.getId(), tvShow.getPosterPath(), tvShow.getName(), tvShow.getHomepage());
            }
        }
        binding.contentTvshowDetail.textViewReadMoreTvShowDetail.setOnClickListener(view -> {
            binding.contentTvshowDetail.textViewOverviewTvShowDetail.setMaxLines(Integer.MAX_VALUE);
            binding.contentTvshowDetail.layoutDetailsTvShowDetail.setVisibility(View.VISIBLE);
            binding.contentTvshowDetail.textViewReadMoreTvShowDetail.setVisibility(View.GONE);
        });
    }

    private void setGenres(List<Genre> genresList) {
        String genres = "";
        if (genresList != null) {
            for (int i = 0; i < genresList.size(); i++) {
                if (genresList.get(i) == null) continue;
                if (i == genresList.size() - 1) {
                    genres = genres.concat(genresList.get(i).getGenreName());
                } else {
                    genres = genres.concat(genresList.get(i).getGenreName() + ", ");
                }
            }
        }
        binding.textViewGenreTvShowDetail.setText(genres);
    }

    private void setYear(String firstAirDateString) {
        if (firstAirDateString != null && !firstAirDateString.trim().isEmpty()) {
            @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
            @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy");
            try {
                Date firstAirDate = sdf1.parse(firstAirDateString);
                if (firstAirDate != null) {
                    binding.textViewYearTvShowDetail.setText(sdf2.format(firstAirDate));
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        } else {
            binding.textViewYearTvShowDetail.setText("");
        }
    }

    private void setImageButtons(final Integer tvShowId, final String posterPath, final String tvShowName, final String homepage) {
        if (tvShowId == null) return;
        if (Favourite.isTVShowFav(getContext(), tvShowId)) {
            binding.contentTvshowDetail.imageButtonFavTvShowDetail.setTag(Constants.TAG_FAV);
            binding.contentTvshowDetail.imageButtonFavTvShowDetail.setImageResource(R.mipmap.ic_favorite_white_24dp);
        } else {
            binding.contentTvshowDetail.imageButtonFavTvShowDetail.setTag(Constants.TAG_NOT_FAV);
            binding.contentTvshowDetail.imageButtonFavTvShowDetail.setImageResource(R.mipmap.ic_favorite_border_white_24dp);
        }
        binding.contentTvshowDetail.imageButtonFavTvShowDetail.setOnClickListener(view -> {
            view.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
            if ((int) binding.contentTvshowDetail.imageButtonFavTvShowDetail.getTag() == Constants.TAG_FAV) {
                Favourite.removeTVShowFromFav(getContext(), tvShowId);
                binding.contentTvshowDetail.imageButtonFavTvShowDetail.setTag(Constants.TAG_NOT_FAV);
                binding.contentTvshowDetail.imageButtonFavTvShowDetail.setImageResource(R.mipmap.ic_favorite_border_white_24dp);
            } else {
                Favourite.addTVShowToFav(getContext(), tvShowId, posterPath, tvShowName);
                binding.contentTvshowDetail.imageButtonFavTvShowDetail.setTag(Constants.TAG_FAV);
                binding.contentTvshowDetail.imageButtonFavTvShowDetail.setImageResource(R.mipmap.ic_favorite_white_24dp);
            }
        });
        binding.contentTvshowDetail.imageButtonShareTvShowDetail.setOnClickListener(view -> {

            view.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
            Intent movieShareIntent = new Intent(Intent.ACTION_SEND);
            movieShareIntent.setType("text/plain");
            String extraText = "";
            if (tvShowName != null) extraText += tvShowName + "\n";
            if (homepage != null) extraText += homepage;
            movieShareIntent.putExtra(Intent.EXTRA_TEXT, extraText);
            startActivity(movieShareIntent);

        });
    }

    private void setDetails(String firstAirDateString, List<Integer> runtime, String status, List<String> originCountries, List<Network> networks) {
        String detailsString = "";

        if (firstAirDateString != null && !firstAirDateString.trim().isEmpty()) {
            @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
            @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf2 = new SimpleDateFormat("MMM d, yyyy");
            try {
                Date releaseDate = sdf1.parse(firstAirDateString);
                if (releaseDate != null) {
                    detailsString += sdf2.format(releaseDate) + "\n";
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        } else {
            detailsString = "-\n";
        }

        if (runtime != null && !runtime.isEmpty() && runtime.get(0) != 0) {
            if (runtime.get(0) < 60) {
                detailsString += runtime.get(0) + " min(s)" + "\n";
            } else {
                detailsString += runtime.get(0) / 60 + " hr " + runtime.get(0) % 60 + " mins" + "\n";
            }
        } else {
            detailsString += "-\n";
        }

        if (status != null && !status.trim().isEmpty()) {
            detailsString += status + "\n";
        } else {
            detailsString += "-\n";
        }

        StringBuilder originCountriesString = new StringBuilder();
        if (originCountries != null && !originCountries.isEmpty()) {
            for (String country : originCountries) {
                if (country == null || country.trim().isEmpty()) continue;
                originCountriesString.append(country).append(", ");
            }
            if (originCountriesString.length() > 0)
                detailsString += originCountriesString.substring(0, originCountriesString.length() - 2) + "\n";
            else
                detailsString += "-\n";
        } else {
            detailsString += "-\n";
        }

        StringBuilder networksString = new StringBuilder();
        if (networks != null && !networks.isEmpty()) {
            for (Network network : networks) {
                if (network == null || network.getName() == null || network.getName().isEmpty())
                    continue;
                networksString.append(network.getName()).append(", ");
            }
            if (networksString.length() > 0)
                detailsString += networksString.substring(0, networksString.length() - 2);
            else
                detailsString += "-\n";
        } else {
            detailsString += "-\n";
        }
        binding.contentTvshowDetail.textViewDetailsTvShowDetail.setText(detailsString);
    }
}
