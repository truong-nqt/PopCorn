package com.truongnqt.popcorn.personDetailFragment;

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

import com.truongnqt.popcorn.R;
import com.truongnqt.popcorn.activities.MainActivity;
import com.truongnqt.popcorn.databinding.FragmentPersonDetailBinding;
import com.truongnqt.popcorn.movieDetailFragment.MovieDetailFragment;
import com.truongnqt.popcorn.network.movies.MovieCastsOfPersonResponse;
import com.truongnqt.popcorn.network.people.Person;
import com.truongnqt.popcorn.network.tvshows.TVCastsOfPersonResponse;
import com.truongnqt.popcorn.personDetailFragment.adapter.MovieCastsOfPersonAdapter;
import com.truongnqt.popcorn.personDetailFragment.adapter.TVCastsOfPersonAdapter;
import com.truongnqt.popcorn.tvShowDetailFragment.TVShowDetailFragment;
import com.truongnqt.popcorn.utils.Constants;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class PersonDetailFragment extends Fragment {

    private FragmentPersonDetailBinding binding;
    private PersonViewModel viewModel;
    private Activity activity;

    private final List<Person> personList = new ArrayList<>();
    private Bundle receivedIntent;
    private int mPersonId;
    private boolean isCheckValuePerson;
    private boolean isCheckValueMovieCast;
    private boolean isFlagValueMovieCast;
    private boolean isCheckValueTVCast;
    private boolean isFlagValueTVCast;

    private final MovieCastsOfPersonAdapter mMovieCastsOfPersonAdapter = new MovieCastsOfPersonAdapter(null, (position, person) -> {
        if(activity instanceof MainActivity) {
            ((MainActivity) activity).setFragment(MovieDetailFragment.newInstance(person.getId()), true);
        }
    });
    private final TVCastsOfPersonAdapter mTVCastsOfPersonAdapter  = new TVCastsOfPersonAdapter(null,
            ((position, tvCastOfPerson) -> ((MainActivity) activity).setFragment(TVShowDetailFragment.newInstance(tvCastOfPerson.getId()), true)));

    private final Observer<Person> personObserver = personDetails -> {
        personList.add(personDetails);
        isCheckValuePerson = true;
        loadImage();
        setVisibility();
    };
    private final Observer<MovieCastsOfPersonResponse> movieCastsOfPersonObserver = movieCastsOfPersonResponse -> {
        isFlagValueMovieCast = true;
        if(movieCastsOfPersonResponse.getCasts().size() > 0) {
            mMovieCastsOfPersonAdapter.newList(movieCastsOfPersonResponse.getCasts());
            isCheckValueMovieCast = true;
        }
        setVisibility();
    };
    private final Observer<TVCastsOfPersonResponse> tvCastsOfPersonObserver = tvCastsOfPersonResponse -> {
        isFlagValueTVCast = true;
        if(tvCastsOfPersonResponse.getCasts().size() > 0) {
            mTVCastsOfPersonAdapter.newList(tvCastsOfPersonResponse.getCasts());
            isCheckValueTVCast = true;
        }
        setVisibility();
    };

    public static PersonDetailFragment newInstance(int personId) {
        PersonDetailFragment fragment = new PersonDetailFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(Constants.PERSON_ID, personId);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        activity = getActivity();
        FragmentManager fragmentManager = getParentFragmentManager();
        receivedIntent = getArguments();
        assert receivedIntent != null;
        mPersonId = receivedIntent.getInt(Constants.PERSON_ID, -1);
        if (mPersonId == -1) fragmentManager.popBackStack();

        viewModel = new ViewModelProvider(this).get(PersonViewModel.class);
        viewModel.setApiKey(getResources().getString(R.string.MOVIE_DB_API_KEY));
        viewModel.getPerson(mPersonId);
        viewModel.getMovieCasts(mPersonId);
        viewModel.getTVCasts(mPersonId);
        observe();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_person_detail, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        int mCastImageSideSize = (int) (getResources().getDisplayMetrics().widthPixels * 0.33);
        binding.cardViewCastDetail.getLayoutParams().height = mCastImageSideSize;
        binding.cardViewCastDetail.getLayoutParams().width = mCastImageSideSize;
        binding.cardViewCastDetail.setRadius((float) (mCastImageSideSize / 2));
        binding.progressBarCastDetail.setVisibility(View.GONE);
        ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) binding.textViewNameCastDetail.getLayoutParams();
        params.setMargins(params.leftMargin, mCastImageSideSize / 2, params.rightMargin, params.bottomMargin);
        loadImage();
        setUpViews();
    }

    @Override
    public void onPause() {
        super.onPause();
        if(isCheckValuePerson && isFlagValueMovieCast && isFlagValueTVCast)
            mPersonId = -1;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        binding = null;
    }

    @SuppressLint("FragmentLiveDataObserve")
    private void observe() {
        viewModel.getPersonLive().observe(this, personObserver);
        viewModel.getMovieCastsOfPersonLive().observe(this, movieCastsOfPersonObserver);
        viewModel.getTVCastsOfPersonLive().observe(this, tvCastsOfPersonObserver);
    }

    private void setUpViews() {
        binding.contentPersonDetail.recyclerViewMovieCastPersonDetail.setAdapter(mMovieCastsOfPersonAdapter);
        binding.contentPersonDetail.recyclerViewTvCastPersonDetail.setAdapter(mTVCastsOfPersonAdapter);
        setVisibility();
    }

    public void reLoadData() {
        mPersonId = receivedIntent.getInt(Constants.PERSON_ID, -1);
        viewModel.getPerson(mPersonId);
        viewModel.getMovieCasts(mPersonId);
        viewModel.getTVCasts(mPersonId);
        observe();
    }

    private void setVisibility() {
        if(isCheckValuePerson && isFlagValueMovieCast && isFlagValueTVCast) {
            binding.toolbarLayout.setVisibility(View.VISIBLE);
            binding.contentPersonDetail.linearLayoutContentTvShowDetail.setVisibility(View.VISIBLE);
            binding.loadingFrame.setVisibility(View.GONE);
            if(!isCheckValueMovieCast) {
                binding.contentPersonDetail.textViewMovieCastPersonDetail.setVisibility(View.GONE);
                binding.contentPersonDetail.recyclerViewMovieCastPersonDetail.setVisibility(View.GONE);
            }
            if(!isCheckValueTVCast) {
                binding.contentPersonDetail.textViewTvCastPersonDetail.setVisibility(View.GONE);
                binding.contentPersonDetail.recyclerViewTvCastPersonDetail.setVisibility(View.GONE);
            }
        } else
            reLoadData();
    }

    private void loadImage() {
        for(Person person : personList) {
            if(person != null) {

                setAge(person.getDateOfBirth());
                binding.setPerson(person);
                binding.contentPersonDetail.setPerson(person);
            }
        }
        binding.contentPersonDetail.textViewReadMorePersonDetail.setOnClickListener(view -> {
            binding.contentPersonDetail.textViewBioPersonDetail.setMaxLines(Integer.MAX_VALUE);
            binding.contentPersonDetail.textViewReadMorePersonDetail.setVisibility(View.GONE);
        });

    }

    @SuppressLint("SetTextI18n")
    private void setAge(String dateOfBirthString) {
        if (dateOfBirthString != null && !dateOfBirthString.trim().isEmpty()) {
            @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
            @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy");
            try {
                Date releaseDate = sdf1.parse(dateOfBirthString);
                assert releaseDate != null;
                binding.textViewAgeCastDetail.setText((Calendar.getInstance().get(Calendar.YEAR)
                        - Integer.parseInt(sdf2.format(releaseDate))) + "");
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
    }

}
