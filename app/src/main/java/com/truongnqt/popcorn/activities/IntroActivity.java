package com.truongnqt.popcorn.activities;

import android.graphics.Color;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.github.paolorotolo.appintro.AppIntro;
import com.github.paolorotolo.appintro.AppIntroFragment;
import com.github.paolorotolo.appintro.model.SliderPage;
import com.truongnqt.popcorn.R;

public class IntroActivity extends AppIntro {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addSlide(AppIntroFragment.newInstance(sliderPage(
                "Welcome to PopCorn", "Find and Discover your favourite Movies, TV Shows, Actors and more.",
                R.drawable.ic_launcher_large
        )));
        addSlide(AppIntroFragment.newInstance(sliderPage(
                "Favorites", "Mark your Movies and TV Shows favourite.\nSo you never miss them again.",
                R.drawable.heart256
        )));
        addSlide(AppIntroFragment.newInstance(sliderPage(
                "Explore", "Search your loved Movies and TV Shows from vast database.",
                R.drawable.search256
        )));
        addSlide(AppIntroFragment.newInstance(sliderPage(
                "Share", "Share your Movies and TV Shows with friends.",
                R.drawable.share256
        )));

        showStatusBar(false);
        showSkipButton(true);
        setProgressButtonEnabled(true);
    }

    private SliderPage sliderPage(String title, String description, int drawable) {
        SliderPage slider = new SliderPage();
        slider.setTitle(title);
        slider.setDescription(description);
        slider.setImageDrawable(drawable);
        slider.setBgColor(Color.DKGRAY);
        return slider;
    }

    @Override
    public void onSkipPressed(Fragment currentFragment) {
        super.onSkipPressed(currentFragment);
        finish();
    }

    @Override
    public void onDonePressed(Fragment currentFragment) {
        super.onDonePressed(currentFragment);
        finish();
    }
}
