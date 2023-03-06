package com.truongnqt.popcorn.searchFragment;

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
import com.truongnqt.popcorn.databinding.FragmentSearchBinding;
import com.truongnqt.popcorn.movieDetailFragment.MovieDetailFragment;
import com.truongnqt.popcorn.network.search.SearchResponse;
import com.truongnqt.popcorn.personDetailFragment.PersonDetailFragment;
import com.truongnqt.popcorn.searchFragment.adapter.SearchResultsAdapter;
import com.truongnqt.popcorn.tvShowDetailFragment.TVShowDetailFragment;
import com.truongnqt.popcorn.utils.Constants;
import com.truongnqt.popcorn.utils.NetworkConnection;

public class SearchFragment extends Fragment {

    private FragmentSearchBinding binding;
    private Activity activity;
    private FragmentManager fragmentManager;
    private SearchViewModel viewModel;

    private final int presentPage = 1;
    private String mQuery;

    private SearchResultsAdapter searchResultsAdapter;

    private final Observer<SearchResponse> searchResponseObserver = searchResponse -> {
        if(presentPage == Constants.START_PAGE) {
            searchResultsAdapter.newList(searchResponse.getResults());
        } else{
            searchResultsAdapter.addItems(searchResponse.getResults());
            observe();
        }
    };

    public static SearchFragment newInstance(String query) {
        SearchFragment fragment = new SearchFragment();
        Bundle bundle = new Bundle();
        bundle.putString(Constants.QUERY, query);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        activity = getActivity();
        fragmentManager = getParentFragmentManager();

        Bundle receivedIntent = getArguments();
        assert receivedIntent != null;
        mQuery = receivedIntent.getString(Constants.QUERY);
        if (mQuery == null || mQuery.trim().isEmpty()) fragmentManager.popBackStack();

        searchResultsAdapter = new SearchResultsAdapter(getContext(), null,
                ((position, searchResult) -> {
                    switch (searchResult.getMediaType()) {
                        case "movie":
                            if (activity instanceof MainActivity)
                                ((MainActivity) activity).setFragment(MovieDetailFragment.newInstance(searchResult.getId()), true);
                            break;
                        case "tv":
                            if (activity instanceof MainActivity)
                                ((MainActivity) activity).setFragment(TVShowDetailFragment.newInstance(searchResult.getId()), true);
                            break;
                        case "person":
                            if (activity instanceof MainActivity)
                                ((MainActivity) activity).setFragment(PersonDetailFragment.newInstance(searchResult.getId()), true);
                            break;
                    }
                }));


        viewModel = new ViewModelProvider(this).get(SearchViewModel.class);
        viewModel.setApiKey(getString(R.string.MOVIE_DB_API_KEY));

        viewModel.getSearchMulti(presentPage, mQuery);

        observe();
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_search, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setUpView();

        if(!NetworkConnection.isConnected(requireContext()))
            binding.coordinatorLayout.setVisibility(View.GONE);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        binding = null;
    }

    private void observe() {
        viewModel.getSearchResponseLive().observe(this, searchResponseObserver);
    }

    private void setUpView() {
        binding.layoutContentSearch.recyclerViewSearch.setAdapter(searchResultsAdapter);
        if(searchResultsAdapter == null) {
            binding.layoutContentSearch.textViewEmptySearch.setVisibility(View.VISIBLE);
        }

        binding.contentsSearch.setText("Content search: " + mQuery);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            fragmentManager.popBackStack();
        }
        return super.onOptionsItemSelected(item);
    }

}
