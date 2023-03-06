package com.truongnqt.popcorn.favourites;

import android.app.Activity;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.truongnqt.popcorn.activities.MainActivity;
import com.truongnqt.popcorn.databinding.FragmentFavouritesBinding;

public class FavouritesFragment extends Fragment {
    private FragmentFavouritesBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentFavouritesBinding.inflate(inflater, container, false);
        Activity activity = requireActivity();
        binding.viewPagerFav.setAdapter(new FavouritesPagerAdapter(getChildFragmentManager(), getContext()));
        binding.tabViewPagerFav.setViewPager(binding.viewPagerFav);
        ((MainActivity) activity).showBtnNavigationView();
        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
