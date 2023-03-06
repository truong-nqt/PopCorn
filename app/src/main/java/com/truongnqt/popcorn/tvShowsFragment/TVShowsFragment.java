package com.truongnqt.popcorn.tvShowsFragment;

import static android.app.Activity.RESULT_OK;
import static com.truongnqt.popcorn.utils.Constants.START_PAGE;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.speech.RecognizerIntent;
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
import com.truongnqt.popcorn.databinding.FragmentTvShowsBinding;
import com.truongnqt.popcorn.network.tvshows.AiringTodayTVShowsResponse;
import com.truongnqt.popcorn.network.tvshows.OnTheAirTVShowsResponse;
import com.truongnqt.popcorn.network.tvshows.PopularTVShowsResponse;
import com.truongnqt.popcorn.network.tvshows.TopRatedTVShowsResponse;
import com.truongnqt.popcorn.searchFragment.SearchFragment;
import com.truongnqt.popcorn.tvShowDetailFragment.TVShowDetailFragment;
import com.truongnqt.popcorn.tvShowsFragment.adapter.TVShowBriefsLargeAdapter;
import com.truongnqt.popcorn.tvShowsFragment.adapter.TVShowBriefsSmallAdapter;
import com.truongnqt.popcorn.utils.Constants;
import com.truongnqt.popcorn.utils.NetworkConnection;
import com.truongnqt.popcorn.viewAllTvShowFragment.ViewAllTVShowsFragment;
import java.util.List;

public class TVShowsFragment extends Fragment {

    private FragmentTvShowsBinding binding;
    private TVShowsViewModel viewModel;
    private Activity activity;

    private final Handler sliderHandler = new Handler();

    private int currentPage = START_PAGE;
    private boolean isCheckValueAiringToday;
    private boolean isCheckValueOnTheAir;
    private boolean isCheckValuePopular;
    private boolean isCheckValueTopRated;

    private final TVShowBriefsLargeAdapter mAiringTodayAdapter = new TVShowBriefsLargeAdapter(null, (position, brief) -> {
        if(activity instanceof MainActivity) {
            ((MainActivity) activity).setFragment(TVShowDetailFragment.newInstance(brief.getId()), true);
        }
    });
    private final TVShowBriefsSmallAdapter mOnTheAirAdapter = new TVShowBriefsSmallAdapter(null, (position, brief) -> {
        if(activity instanceof MainActivity) {
            ((MainActivity) activity).setFragment(TVShowDetailFragment.newInstance(brief.getId()), true);
        }
    });
    private final TVShowBriefsLargeAdapter mPopularAdapter = new TVShowBriefsLargeAdapter(null, (position, brief) -> {
        if(activity instanceof MainActivity) {
            ((MainActivity) activity).setFragment(TVShowDetailFragment.newInstance(brief.getId()), true);
        }
    });
    private final TVShowBriefsSmallAdapter mTopRatedAdapter = new TVShowBriefsSmallAdapter(null, (position, brief) -> {
        if(activity instanceof MainActivity) {
            ((MainActivity) activity).setFragment(TVShowDetailFragment.newInstance(brief.getId()), true);
        }
    });

    private final Observer<AiringTodayTVShowsResponse> airingTodayTVShowsResponseObserver =
            airingTodayTVShowsResponse -> {
                if(airingTodayTVShowsResponse.getResults().size() > 0) {
                    mAiringTodayAdapter.newList(airingTodayTVShowsResponse.getResults());
                    isCheckValueAiringToday = true;
                    checkValueData();
                }
            };

    private final Observer<OnTheAirTVShowsResponse> onTheAirTVShowsResponseObserver =
            onTheAirTVShowsResponse -> {
                if(onTheAirTVShowsResponse.getResults().size() > 0) {
                    mOnTheAirAdapter.newList(onTheAirTVShowsResponse.getResults());
                    isCheckValueOnTheAir = true;
                    checkValueData();
                }
            };

    private final Observer<PopularTVShowsResponse> popularTVShowsResponseObserver =
            popularTVShowsResponse -> {
                if(popularTVShowsResponse.getResults().size() > 0) {
                    mPopularAdapter.newList(popularTVShowsResponse.getResults());
                    isCheckValuePopular = true;
                    checkValueData();
                }
            };

    private final Observer<TopRatedTVShowsResponse> topRatedTVShowsResponseObserver =
            topRatedTVShowsResponse -> {
                if(topRatedTVShowsResponse.getResults().size() > 0) {
                    mTopRatedAdapter.newList(topRatedTVShowsResponse.getResults());
                    isCheckValueTopRated = true;
                    checkValueData();
                }
            };

    public static TVShowsFragment newInstance(int tvShowType) {
        TVShowsFragment fragment = new TVShowsFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(Constants.VIEW_ALL_TV_SHOWS_TYPE, tvShowType);
        fragment.setArguments(bundle);
        return fragment;
    }

    private ActivityResultLauncher<Intent> activityResultLauncher;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        activity = requireActivity();
        viewModel = new ViewModelProvider(this).get(TVShowsViewModel.class);
        viewModel.setApiKey(getString(R.string.MOVIE_DB_API_KEY));
        viewModel.getAiringTodayTVShows(currentPage);
        viewModel.getOnTheAirTVShows(currentPage);
        viewModel.getPopularTVShows(currentPage);
        viewModel.getTopRatedTVShows(currentPage);
        observe();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentTvShowsBinding.inflate(inflater, container, false);

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

        setUpView();
        setUpMoviesViewPager2(binding.viewpager2AiringToday);
        setUpMoviesViewPager2(binding.viewpager2Popular);
        iClickListener();
    }

    @Override
    public void onResume() {
        super.onResume();

        sliderHandler.postDelayed(slideRunnableNowShowing, 5000);
        sliderHandler.postDelayed(slideRunnableUpcoming, 5000);
    }

    @Override
    public void onPause() {
        super.onPause();

        if(isCheckValueAiringToday && isCheckValueOnTheAir && isCheckValuePopular && isCheckValueTopRated)
            currentPage = -1;
        sliderHandler.removeCallbacks(slideRunnableNowShowing);
        sliderHandler.removeCallbacks(slideRunnableUpcoming);
    }

    private void iClickListener() {

        binding.edtSearch.setOnEditorActionListener((textView, i, keyEvent) -> {
            if(i == EditorInfo.IME_ACTION_SEARCH) {
                return performSearch();
            }
            return false;
        });
        binding.mic.setOnClickListener(v -> displaySpeechRecognizer());
        binding.textViewViewAllAiringToday.setOnClickListener(v -> {
            if(activity instanceof MainActivity) {
                ((MainActivity) activity).setFragment(ViewAllTVShowsFragment.newInstance(Constants.AIRING_TODAY_TV_SHOWS_TYPE), true);
            }
        });
        binding.textViewViewAllOnTheAir.setOnClickListener(v -> {
            if(activity instanceof MainActivity) {
                ((MainActivity) activity).setFragment(ViewAllTVShowsFragment.newInstance(Constants.ON_THE_AIR_TV_SHOWS_TYPE), true);
            }
        });
        binding.textViewViewAllPopular.setOnClickListener(v -> {
            if(activity instanceof MainActivity) {
                ((MainActivity) activity).setFragment(ViewAllTVShowsFragment.newInstance(Constants.POPULAR_TV_SHOWS_TYPE), true);
            }
        });
        binding.textViewViewAllTopRated.setOnClickListener(v -> {
            if(activity instanceof MainActivity) {
                ((MainActivity) activity).setFragment(ViewAllTVShowsFragment.newInstance(Constants.TOP_RATED_TV_SHOWS_TYPE), true);
            }
        });
    }

    private boolean performSearch() {
        if (!NetworkConnection.isConnected(requireContext())) {
            Toast.makeText(requireContext(), R.string.no_network, Toast.LENGTH_SHORT).show();
            return false;
        } else {
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
        viewModel.getAiringTodayTVShowsLive().observe(this, airingTodayTVShowsResponseObserver);
        viewModel.getOnTheAirTVShowsLive().observe(this, onTheAirTVShowsResponseObserver);
        viewModel.getPopularTVShowsLive().observe(this, popularTVShowsResponseObserver);
        viewModel.getTopRatedTVShowsLive().observe(this, topRatedTVShowsResponseObserver);
    }

    public void reLoadData() {
        currentPage = START_PAGE;
        viewModel.getAiringTodayTVShows(currentPage);
        viewModel.getOnTheAirTVShows(currentPage);
        viewModel.getPopularTVShows(currentPage);
        viewModel.getTopRatedTVShows(currentPage);
        observe();
    }

    private void setUpView() {
        checkValueData();
        ((MainActivity) activity).showBtnNavigationView();
        binding.viewpager2AiringToday.setAdapter(mAiringTodayAdapter);
        binding.recyclerViewOnTheAir.setAdapter(mOnTheAirAdapter);
        binding.viewpager2Popular.setAdapter(mPopularAdapter);
        binding.recyclerViewTopRated.setAdapter(mTopRatedAdapter);
    }

    private void checkValueData() {
        if(isCheckValueAiringToday && isCheckValueOnTheAir && isCheckValuePopular && isCheckValueTopRated) {
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

        binding.viewpager2AiringToday.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                binding.viewpager2AiringToday.removeCallbacks(slideRunnableNowShowing);
                binding.viewpager2AiringToday.postDelayed(slideRunnableNowShowing, 5000);
            }
        });

        binding.viewpager2Popular.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                binding.viewpager2Popular.removeCallbacks(slideRunnableUpcoming);
                binding.viewpager2Popular.postDelayed(slideRunnableUpcoming, 5000);
            }
        });
    }

    Runnable slideRunnableNowShowing = new Runnable() {
        @Override
        public void run() {
            if(binding.viewpager2AiringToday.getCurrentItem() == mAiringTodayAdapter.getItemCount() - 1) {
                binding.viewpager2AiringToday.setCurrentItem(0);
            } else
                binding.viewpager2AiringToday.setCurrentItem(binding.viewpager2AiringToday.getCurrentItem() + 1);
        }
    };

    Runnable slideRunnableUpcoming = new Runnable() {
        @Override
        public void run() {
            if (binding.viewpager2Popular.getCurrentItem() == mPopularAdapter.getItemCount() - 1) {
                binding.viewpager2Popular.setCurrentItem(0);
            } else
                binding.viewpager2Popular.setCurrentItem(binding.viewpager2Popular.getCurrentItem() + 1);
        }
    };

}
