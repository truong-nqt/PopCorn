package com.truongnqt.popcorn.movieDetailFragment;

import static com.truongnqt.popcorn.utils.Constants.START_PAGE;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import android.view.HapticFeedbackConstants;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import com.truongnqt.popcorn.R;
import com.truongnqt.popcorn.activities.MainActivity;
import com.truongnqt.popcorn.databinding.FragmentMovieDetailBinding;
import com.truongnqt.popcorn.moviesFragment.adapter.MovieBriefsSmallAdapter;
import com.truongnqt.popcorn.personDetailFragment.PersonDetailFragment;
import com.truongnqt.popcorn.movieDetailFragment.adapter.MovieCastsAdapter;
import com.truongnqt.popcorn.movieDetailFragment.adapter.VideoAdapter;
import com.truongnqt.popcorn.network.movies.Movie;
import com.truongnqt.popcorn.network.movies.MovieCreditsResponse;
import com.truongnqt.popcorn.network.movies.SimilarMoviesResponse;
import com.truongnqt.popcorn.network.videos.VideosResponse;
import com.truongnqt.popcorn.utils.Constants;
import com.truongnqt.popcorn.utils.Favourite;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MovieDetailFragment extends Fragment {

    private FragmentMovieDetailBinding binding;
    private MovieViewModel moviesViewModel;
    private Activity activity;
    private FragmentManager fragmentManager;

    private final List<Movie> mMovies = new ArrayList<>();
    private Bundle receivedIntent;
    private int mMovieId;
    private boolean isCheckValueMovie;
    private boolean isCheckValueVideo;
    private boolean isFlagValueVideo;
    private boolean isCheckValueCast;
    private boolean isFlagValueCast;
    private boolean isCheckValueBriefs;
    private boolean isFlagValueBriefs;

    private final VideoAdapter videoAdapter = new VideoAdapter(null,
            ((position, video) -> {
                Intent youtubeIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(Constants.YOUTUBE_WATCH_BASE_URL + video.getKey()));
                requireContext().startActivity(youtubeIntent);
            }));
    private final MovieCastsAdapter movieCastsAdapter = new MovieCastsAdapter(null,
            ((position, movieCastBrief) -> {
                if (activity instanceof MainActivity) {
                    ((MainActivity) activity).setFragment(PersonDetailFragment.newInstance(movieCastBrief.getId()), true);
                }
            }));
    private final MovieBriefsSmallAdapter movieBriefsSmallAdapter = new MovieBriefsSmallAdapter(null,
            ((position, movieBrief) -> {
                if (activity instanceof MainActivity) {
                    fragmentManager.popBackStackImmediate();
                    ((MainActivity) activity).setFragment(MovieDetailFragment.newInstance(movieBrief.getId()), true);
                }
            }));

    private final Observer<Movie> movieObserver = movieResponse -> {
        isCheckValueMovie = true;
        mMovies.add(movieResponse);
        loadMovie();
        setVisibility();
    };
    private final Observer<VideosResponse> videosObserver = videosResponse -> {
        isFlagValueVideo = true;
        if(videosResponse.getVideos().size() > 0) {
            videoAdapter.newList(videosResponse.getVideos());
            isCheckValueVideo = true;
        }
        setVisibility();
    };
    private final Observer<MovieCreditsResponse> movieCreditsObserver = movieCreditsResponse -> {
        isFlagValueCast = true;
        if(movieCreditsResponse.getCasts().size() > 0) {
            movieCastsAdapter.newList(movieCreditsResponse.getCasts());
            isCheckValueCast = true;
        }
        setVisibility();
    };
    private final Observer<SimilarMoviesResponse> similarMoviesObserver = similarMoviesResponse -> {
        isFlagValueBriefs = true;
        if(similarMoviesResponse.getResults().size() > 0) {
            movieBriefsSmallAdapter.newList(similarMoviesResponse.getResults());
            isCheckValueBriefs = true;
        }
        setVisibility();
    };

    public static MovieDetailFragment newInstance(int movieBrief) {
        MovieDetailFragment fragment = new MovieDetailFragment();
        Bundle args = new Bundle();
        args.putInt(Constants.MOVIE_ID, movieBrief);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {

        receivedIntent = getArguments();
        assert receivedIntent != null;
        mMovieId = receivedIntent.getInt(Constants.MOVIE_ID, -1);
        activity = requireActivity();
        fragmentManager = getParentFragmentManager();
        if (mMovieId == -1) fragmentManager.popBackStack();

        moviesViewModel = new ViewModelProvider(this).get(MovieViewModel.class);
        moviesViewModel.setApiKey(getResources().getString(R.string.MOVIE_DB_API_KEY));
        moviesViewModel.getMovieDetails(mMovieId);
        moviesViewModel.getMovieVideo(mMovieId);
        moviesViewModel.getMovieCredits(mMovieId);
        moviesViewModel.getMovieSimilar(mMovieId, START_PAGE);
        observe();
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_movie_detail, container, false);

        int mPosterWidth = (int) (getResources().getDisplayMetrics().widthPixels * 0.25);
        int mPosterHeight = (int) (mPosterWidth / 0.66);
        int mBackdropWidth = getResources().getDisplayMetrics().widthPixels;
        int mBackdropHeight = (int) (mBackdropWidth / 1.77);
        binding.layoutToolbarMovie.getLayoutParams().height = mBackdropHeight + (int) (mPosterHeight * 0.9);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setUpViews();

        BottomSheetBehavior<LinearLayout> bottomSheetBehavior = BottomSheetBehavior.from(binding.bottomSheetMovieDetail.bottomSheet);
        // change the state of the bottom sheet
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
        // set hideable or not
        bottomSheetBehavior.setHideable(false);
    }

    @Override
    public void onPause() {
        super.onPause();
        if(isCheckValueMovie && isFlagValueVideo && isFlagValueCast && isFlagValueBriefs)
            mMovieId = -1;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        binding = null;
    }

    private void setUpViews() {
        binding.bottomSheetMovieDetail.recyclerViewTrailersMovieDetail.setAdapter(videoAdapter);
        binding.bottomSheetMovieDetail.recyclerViewCastMovieDetail.setAdapter(movieCastsAdapter);
        binding.bottomSheetMovieDetail.recyclerViewSimilarMovieDetail.setAdapter(movieBriefsSmallAdapter);
        loadMovie();
        setVisibility();
    }

    public void reLoadData() {
        mMovieId = receivedIntent.getInt(Constants.MOVIE_ID, -1);
        moviesViewModel.getMovieDetails(mMovieId);
        moviesViewModel.getMovieVideo(mMovieId);
        moviesViewModel.getMovieCredits(mMovieId);
        moviesViewModel.getMovieSimilar(mMovieId, START_PAGE);
        observe();
    }

    private void setVisibility() {
        if(isCheckValueMovie && isFlagValueVideo && isFlagValueCast && isFlagValueBriefs) {
            binding.layoutToolbarMovie.setVisibility(View.VISIBLE);
            binding.bottomSheetMovieDetail.bottomSheet.setVisibility(View.VISIBLE);
            binding.loadingFrame.setVisibility(View.GONE);
            if(!isCheckValueVideo) {
                binding.bottomSheetMovieDetail.recyclerViewCastMovieDetail.setVisibility(View.GONE);
                binding.bottomSheetMovieDetail.textViewCastMovieDetail.setVisibility(View.GONE);
            }
            if(!isCheckValueCast) {
                binding.bottomSheetMovieDetail.recyclerViewTrailersMovieDetail.setVisibility(View.GONE);
                binding.bottomSheetMovieDetail.textViewTrailerMovieDetail.setVisibility(View.GONE);
                binding.bottomSheetMovieDetail.viewHorizontalLine.setVisibility(View.GONE);
            }
            if(!isCheckValueBriefs) {
                binding.bottomSheetMovieDetail.recyclerViewSimilarMovieDetail.setVisibility(View.GONE);
                binding.bottomSheetMovieDetail.textViewSimilarMovieDetail.setVisibility(View.GONE);
                binding.bottomSheetMovieDetail.viewHorizontalLine.setVisibility(View.GONE);
            }
        } else
            reLoadData();
    }

    @SuppressLint("FragmentLiveDataObserve")
    private void observe() {
        moviesViewModel.getMovieDetailsLive().observe(this, movieObserver);
        moviesViewModel.getMovieVideoLive().observe(this, videosObserver);
        moviesViewModel.getMovieCreditsLive().observe(this, movieCreditsObserver);
        moviesViewModel.getMovieSimilarLive().observe(this, similarMoviesObserver);
    }

    @SuppressLint("SetTextI18n")
    private void loadMovie() {
        for (Movie movie : mMovies) {
            if(movie == null) {
                binding.bottomSheetMovieDetail.noDataTextView.setVisibility(View.VISIBLE);
                binding.bottomSheetMovieDetail.noDataTextView.setText("The movie is broken");
            }
            binding.setMovie(movie);
            binding.bottomSheetMovieDetail.setMovie(movie);
            binding.bottomSheetMovieDetail.textViewReadMoreMovieDetail.setOnClickListener(view -> {
                binding.bottomSheetMovieDetail.layoutDetailsMovieDetail.setVisibility(View.VISIBLE);
                binding.bottomSheetMovieDetail.textViewOverviewMovieDetailBtnSheet.setMaxLines(10);
                binding.bottomSheetMovieDetail.textViewReadMoreMovieDetail.setVisibility(View.GONE);
            });
            assert movie != null;
            setImageButtons(movie.getId(), movie.getPosterPath(), movie.getTitle(),
                    movie.getTagline(), movie.getImdbId(), movie.getHomepage());
            setDetails(movie.getReleaseDate(), movie.getRuntime());
        }
    }

    private void setImageButtons(final Integer movieId, final String posterPath, final String movieTitle, final String movieTagline, final String imdbId, final String homepage) {
        if (movieId == null) return;
        if (Favourite.isMovieFav(getContext(), movieId)) {
            binding.bottomSheetMovieDetail.imageButtonFavMovieDetail.setTag(Constants.TAG_FAV);
            binding.bottomSheetMovieDetail.imageButtonFavMovieDetail.setImageResource(R.mipmap.ic_favorite_white_24dp);
        } else {
            binding.bottomSheetMovieDetail.imageButtonFavMovieDetail.setTag(Constants.TAG_NOT_FAV);
            binding.bottomSheetMovieDetail.imageButtonFavMovieDetail.setImageResource(R.mipmap.ic_favorite_border_white_24dp);
        }
        binding.bottomSheetMovieDetail.imageButtonFavMovieDetail.setOnClickListener(view -> {
            view.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
            if ((int) binding.bottomSheetMovieDetail.imageButtonFavMovieDetail.getTag() == Constants.TAG_FAV) {
                Favourite.removeMovieFromFav(getContext(), movieId);
                binding.bottomSheetMovieDetail.imageButtonFavMovieDetail.setTag(Constants.TAG_NOT_FAV);
                binding.bottomSheetMovieDetail.imageButtonFavMovieDetail.setImageResource(R.mipmap.ic_favorite_border_white_24dp);
            } else {
                Favourite.addMovieToFav(getContext(), movieId, posterPath, movieTitle);
                binding.bottomSheetMovieDetail.imageButtonFavMovieDetail.setTag(Constants.TAG_FAV);
                binding.bottomSheetMovieDetail.imageButtonFavMovieDetail.setImageResource(R.mipmap.ic_favorite_white_24dp);
            }
        });
        binding.bottomSheetMovieDetail.imageButtonShareMovieDetail.setOnClickListener(view -> {
            view.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
            Intent movieShareIntent = new Intent(Intent.ACTION_SEND);
            movieShareIntent.setType("text/plain");
            String extraText = "";
            if (movieTitle != null) extraText += movieTitle + "\n";
            if (movieTagline != null) extraText += movieTagline + "\n";
            if (imdbId != null) extraText += Constants.IMDB_BASE_URL + imdbId + "\n";
            if (homepage != null) extraText += homepage;
            movieShareIntent.putExtra(Intent.EXTRA_TEXT, extraText);
            startActivity(movieShareIntent);
        });
    }

    private void setDetails(String releaseString, Integer runtime) {
        String detailsString = "";

        if (releaseString != null && !releaseString.trim().isEmpty()) {
            @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
            @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf2 = new SimpleDateFormat("MMM d, yyyy");
            try {
                Date releaseDate = sdf1.parse(releaseString);
                if(releaseDate != null) {
                    detailsString += sdf2.format(releaseDate) + "\n";
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        } else {
            detailsString = "-\n";
        }

        if (runtime != null && runtime != 0) {
            if (runtime < 60) {
                detailsString += runtime + " min(s)";
            } else {
                detailsString += runtime / 60 + " hr " + runtime % 60 + " mins";
            }
        } else {
            detailsString += "-";
        }
        binding.bottomSheetMovieDetail.textViewDetailsMovieDetail.setText(detailsString);
    }
}
