package com.truongnqt.popcorn.moviesFragment;

import static android.app.Activity.RESULT_OK;
import static com.truongnqt.popcorn.utils.Constants.START_PAGE;
import static com.truongnqt.popcorn.utils.Constants.TAG;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.speech.RecognizerIntent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.CompositePageTransformer;
import androidx.viewpager2.widget.ViewPager2;

import com.truongnqt.popcorn.R;
import com.truongnqt.popcorn.activities.MainActivity;
import com.truongnqt.popcorn.databinding.FragmentMoviesBinding;
import com.truongnqt.popcorn.movieDetailFragment.MovieDetailFragment;
import com.truongnqt.popcorn.moviesFragment.adapter.MovieBriefsLargeAdapter;
import com.truongnqt.popcorn.moviesFragment.adapter.MovieBriefsSmallAdapter;
import com.truongnqt.popcorn.network.movies.NowShowingMoviesResponse;
import com.truongnqt.popcorn.network.movies.PopularMoviesResponse;
import com.truongnqt.popcorn.network.movies.TopRatedMoviesResponse;
import com.truongnqt.popcorn.network.movies.UpcomingMoviesResponse;
import com.truongnqt.popcorn.searchFragment.SearchFragment;
import com.truongnqt.popcorn.utils.Constants;
import com.truongnqt.popcorn.utils.NetworkConnection;
import com.truongnqt.popcorn.viewAllMoviesFragment.ViewAllMoviesFragment;
import com.truongnqt.popcorn.viewShowMenuFragment.ViewShowMenu;

import java.util.List;

public class MoviesFragment extends Fragment {

    private FragmentMoviesBinding binding;
    private MovieViewModel movieViewModel;
    private Activity activity;

    private final Handler sliderHandler = new Handler();
    private int currentPage = START_PAGE;
    private boolean isCheckValueNowShowing;
    private boolean isCheckValuePopular;
    private boolean isCheckValueUpcoming;
    private boolean isCheckValueTopRated;

    private ActivityResultLauncher<Intent> activityResultLauncher;

    private final MovieBriefsLargeAdapter nowShowingAdapter = new MovieBriefsLargeAdapter(null,
            ((position, movieBrief) -> {
                if (activity instanceof MainActivity) {
                    ((MainActivity) activity).setFragment(MovieDetailFragment.newInstance(movieBrief.getId()), true);
                }
            }));
    private final MovieBriefsSmallAdapter popularAdapter = new MovieBriefsSmallAdapter(null,
            ((position, movieBrief) -> {
                if (activity instanceof MainActivity) {
                    ((MainActivity) activity).setFragment(MovieDetailFragment.newInstance(movieBrief.getId()), true);
                }
            }));

    private final MovieBriefsLargeAdapter upcomingAdapter = new MovieBriefsLargeAdapter(null,
            (position, movieBrief) -> {
                if (activity instanceof MainActivity) {
                    ((MainActivity) activity).setFragment(MovieDetailFragment.newInstance(movieBrief.getId()), true);
                }
            });

    private final MovieBriefsSmallAdapter topRatedAdapter = new MovieBriefsSmallAdapter(null,
            ((position, movieBrief) -> {
                if (activity instanceof MainActivity) {
                    ((MainActivity) activity).setFragment(MovieDetailFragment.newInstance(movieBrief.getId()), true);
                }
            }));

    private final Observer<NowShowingMoviesResponse> nowShowingMoviesResponseObserver = nowShowingMoviesResponse -> {
        Log.d(TAG, "received nowShowingMovies response");
        if(nowShowingMoviesResponse.getResults().size() > 0) {
            nowShowingAdapter.newList(nowShowingMoviesResponse.getResults());
            isCheckValueNowShowing = true;
            checkValueData();
        }
    };

    private final Observer<PopularMoviesResponse> popularMoviesResponseObserver = popularMoviesResponse -> {
        Log.d(TAG, "received popularMovies response");
        if(popularMoviesResponse.getResults().size() > 0) {
            popularAdapter.newList(popularMoviesResponse.getResults());
            isCheckValuePopular = true;
            checkValueData();
        }
    };

    private final Observer<UpcomingMoviesResponse> upcomingMoviesResponseObserver = upcomingMoviesResponse -> {
        Log.d(TAG, "received upcomingMovies response");
        if(upcomingMoviesResponse.getResults().size() > 0) {
            upcomingAdapter.newList(upcomingMoviesResponse.getResults());
            isCheckValueUpcoming = true;
            checkValueData();
        }
    };

    private final Observer<TopRatedMoviesResponse> topRatedMoviesResponseObserver = topRatedMoviesResponse -> {
        Log.d(TAG, "received topRatedMovies response");
        if(topRatedMoviesResponse.getResults().size() > 0) {
            topRatedAdapter.newList(topRatedMoviesResponse.getResults());
            isCheckValueTopRated = true;
            checkValueData();
        }
    };

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        activity = requireActivity();
        movieViewModel = new ViewModelProvider(this).get(MovieViewModel.class);
        movieViewModel.setApiKey(getResources().getString(R.string.MOVIE_DB_API_KEY));
        movieViewModel.getNowShowing(currentPage);
        movieViewModel.getPopular(currentPage);
        movieViewModel.getUpcoming(currentPage);
        movieViewModel.getTopRated(currentPage);
        observe();
        Log.d("MoviesFragment", "onCreate: ");
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentMoviesBinding.inflate(inflater, container, false);

        activityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    Intent intent = result.getData();
                    List<String> results = intent.getStringArrayListExtra(
                            RecognizerIntent.EXTRA_RESULTS);
                    String spokenText = results.get(0);
                    if(spokenText.isEmpty()) {
                        Toast.makeText(requireContext(), "Please try again", Toast.LENGTH_SHORT).show();
                    }
                    if (!NetworkConnection.isConnected(requireContext())) {
                        Toast.makeText(requireContext(), R.string.no_network, Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if(activity instanceof MainActivity) {
                        ((MainActivity) activity).setFragment(SearchFragment.newInstance(spokenText), true);
                    }
                }
            });

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setUpViews();
        setUpMoviesViewPager2(binding.viewPageNowShowing);
        setUpMoviesViewPager2(binding.viewPageUpcoming);
        clickListener();
        Log.d("MoviesFragment", "onViewCreated: ");
    }

    @Override
    public void onResume() {
        super.onResume();
        sliderHandler.postDelayed(slideRunnableNowShowing, 5000);
        sliderHandler.postDelayed(slideRunnableUpcoming, 5000);
        Log.d("MoviesFragment", "onResume: ");
    }

    @Override
    public void onPause() {
        super.onPause();

        if(isCheckValueNowShowing && isCheckValuePopular && isCheckValueUpcoming && isCheckValueTopRated)
            currentPage = -1;
        sliderHandler.removeCallbacks(slideRunnableNowShowing);
        sliderHandler.removeCallbacks(slideRunnableUpcoming);
        Log.d("MoviesFragment", "onPause: ");
    }

    private void clickListener() {

        binding.genresLayoutTextView.setOnClickListener(v -> {
            if(activity instanceof MainActivity) {
                ((MainActivity) activity).setFragment(ViewShowMenu
                        .newInstance(Constants.NOW_SHOWING_MOVIES_TYPE), true);
            }
        });
        binding.TVLayoutTextView.setOnClickListener(v -> {
            if(activity instanceof MainActivity) {
                ((MainActivity) activity).setFragment(ViewShowMenu
                        .newInstance(Constants.POPULAR_MOVIES_TYPE), true);
            }
        });
        binding.moviesLayoutTextView.setOnClickListener(v -> {
            if(activity instanceof MainActivity) {
                ((MainActivity) activity).setFragment(ViewShowMenu
                        .newInstance(Constants.UPCOMING_MOVIES_TYPE), true);
            }
        });
        binding.inTheatreLayoutTextView.setOnClickListener(v -> {
            if(activity instanceof MainActivity) {
                ((MainActivity) activity).setFragment(ViewShowMenu
                        .newInstance(Constants.TOP_RATED_MOVIES_TYPE), true);
            }
        });
        binding.edtSearch.setOnEditorActionListener((textView, actionId, keyEvent) -> {
            if(actionId == EditorInfo.IME_ACTION_SEARCH) {
                return performSearch();
            }
            return false;
        });
        binding.mic.setOnClickListener(v -> displaySpeechRecognizer());
        binding.textViewViewAllNowShowing.setOnClickListener(v -> {
            if(activity instanceof MainActivity) {
                ((MainActivity) activity).setFragment(ViewAllMoviesFragment.newInstance(Constants.NOW_SHOWING_MOVIES_TYPE), true);
            }
        });
        binding.textViewViewAllPopular.setOnClickListener(v -> {
            if(activity instanceof MainActivity) {
                ((MainActivity) activity).setFragment(ViewAllMoviesFragment.newInstance(Constants.POPULAR_MOVIES_TYPE), true);
            }
        });
        binding.textViewViewAllUpcoming.setOnClickListener(v -> {
            if(activity instanceof MainActivity) {
                ((MainActivity) activity).setFragment(ViewAllMoviesFragment.newInstance(Constants.UPCOMING_MOVIES_TYPE), true);
            }
        });
        binding.textViewViewAllTopRated.setOnClickListener(v -> {
            if(activity instanceof MainActivity) {
                ((MainActivity) activity).setFragment(ViewAllMoviesFragment.newInstance(Constants.TOP_RATED_MOVIES_TYPE), true);
            }
        });
    }

    private boolean performSearch() {
        if (!NetworkConnection.isConnected(requireContext())) {
            Toast.makeText(requireContext(), R.string.no_network, Toast.LENGTH_SHORT).show();
            return false;
        }
        else {
            if(activity instanceof MainActivity) {
                ((MainActivity) activity).setFragment(SearchFragment.newInstance(binding.edtSearch.getText().toString()), true);
                binding.edtSearch.clearFocus();
                binding.edtSearch.setText("");
            }
        }
        return true;
    }

    private void displaySpeechRecognizer() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);

        activityResultLauncher.launch(intent);
    }

    @SuppressLint("FragmentLiveDataObserve")
    private void observe() {
        movieViewModel.getNowShowingLive().observe(this, nowShowingMoviesResponseObserver);
        movieViewModel.getPopularLive().observe(this, popularMoviesResponseObserver);
        movieViewModel.getUpcomingLive().observe(this, upcomingMoviesResponseObserver);
        movieViewModel.getTopRatedLive().observe(this, topRatedMoviesResponseObserver);
    }

    public void reLoadData() {
        currentPage = START_PAGE;
        movieViewModel.getNowShowing(currentPage);
        movieViewModel.getPopular(currentPage);
        movieViewModel.getUpcoming(currentPage);
        movieViewModel.getTopRated(currentPage);
        observe();
    }

    private void setUpViews() {
        checkValueData();
        ((MainActivity) activity).showBtnNavigationView();
        binding.viewPageNowShowing.setAdapter(nowShowingAdapter);
        binding.recyclerViewPopular.setAdapter(popularAdapter);
        binding.viewPageUpcoming.setAdapter(upcomingAdapter);
        binding.recyclerViewTopRated.setAdapter(topRatedAdapter);
    }

    private void checkValueData() {
        if(isCheckValueNowShowing && isCheckValuePopular && isCheckValueUpcoming && isCheckValueTopRated) {
            binding.loadingFrame.setVisibility(View.GONE);
            binding.scrollView.setVisibility(View.VISIBLE);
        }
        else reLoadData();
    }

    private void setUpMoviesViewPager2(ViewPager2 viewPager2) {
        viewPager2.setClipToPadding(false);
        viewPager2.setClipChildren(false);
        viewPager2.setOffscreenPageLimit(3);

        viewPager2.getChildAt(0).setOverScrollMode(RecyclerView.OVER_SCROLL_NEVER);
        CompositePageTransformer compositePageTransformer = new CompositePageTransformer();
        compositePageTransformer.addTransformer((page, position) -> {
            float r = 1 - Math.abs(position);
            page.setScaleY(0.85f + r * 0.15f);
        });

        viewPager2.setPageTransformer(compositePageTransformer);
        binding.viewPageNowShowing.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                binding.viewPageNowShowing.removeCallbacks(slideRunnableNowShowing);
                binding.viewPageNowShowing.postDelayed(slideRunnableNowShowing, 5000);
            }
        });
        binding.viewPageUpcoming.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                binding.viewPageUpcoming.removeCallbacks(slideRunnableUpcoming);
                binding.viewPageUpcoming.postDelayed(slideRunnableUpcoming, 5000);
            }
        });
    }

    Runnable slideRunnableNowShowing = new Runnable() {
        @Override
        public void run() {
            if(binding.viewPageNowShowing.getCurrentItem() == nowShowingAdapter.getItemCount() - 1) {
                binding.viewPageNowShowing.setCurrentItem(0);
            } else
                binding.viewPageNowShowing.setCurrentItem(binding.viewPageNowShowing.getCurrentItem() + 1);

        }
    };

    Runnable slideRunnableUpcoming = new Runnable() {
        @Override
        public void run() {
            if (binding.viewPageUpcoming.getCurrentItem() == upcomingAdapter.getItemCount() - 1) {
                binding.viewPageUpcoming.setCurrentItem(0);
            } else
                binding.viewPageUpcoming.setCurrentItem(binding.viewPageUpcoming.getCurrentItem() + 1);
        }
    };
}
