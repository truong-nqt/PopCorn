package com.truongnqt.popcorn.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.snackbar.Snackbar;
import com.truongnqt.popcorn.R;
import com.truongnqt.popcorn.broadcastreceivers.ConnectivityBroadcastReceiver;
import com.truongnqt.popcorn.movieDetailFragment.MovieDetailFragment;
import com.truongnqt.popcorn.personDetailFragment.PersonDetailFragment;
import com.truongnqt.popcorn.tvShowDetailFragment.TVShowDetailFragment;
import com.truongnqt.popcorn.tvShowsFragment.TVShowsFragment;
import com.truongnqt.popcorn.databinding.ActivityMainBinding;
import com.truongnqt.popcorn.favourites.FavouritesFragment;
import com.truongnqt.popcorn.moviesFragment.MoviesFragment;
import com.truongnqt.popcorn.utils.Constants;
import com.truongnqt.popcorn.utils.NetworkConnection;
import com.truongnqt.popcorn.viewAllMoviesFragment.ViewAllMoviesFragment;
import com.truongnqt.popcorn.viewAllTvShowFragment.ViewAllTVShowsFragment;
import com.truongnqt.popcorn.viewShowMenuFragment.ViewShowMenu;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private FragmentManager fragmentManager;

    private ConnectivityBroadcastReceiver connectivityBroadcast;
    private MoviesFragment moviesFragment;
    private TVShowsFragment tvShowsFragment;
    private FavouritesFragment favouritesFragment;
    private Fragment fragment;

    private int itemId;
    private int checkItemId;
    private int iClick;
    private long time;
    private boolean doubleBackToExitPressedOnce;
    private Snackbar mConnectivitySnackBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
        if (sharedPreferences.getBoolean(Constants.FIRST_TIME_LAUNCH, true)) {
            startActivity(new Intent(MainActivity.this, IntroActivity.class));
            SharedPreferences.Editor sharedPreferencesEditor = sharedPreferences.edit();
            sharedPreferencesEditor.putBoolean(Constants.FIRST_TIME_LAUNCH, false);
            sharedPreferencesEditor.apply();
        }

        initListener();
    }

    @Override
    public void onBackPressed() {
        if(fragmentManager.getBackStackEntryCount() == 0) {
            if (doubleBackToExitPressedOnce) {
                super.onBackPressed();
                return;
            }
            doubleBackToExitPressedOnce = true;
            Toast.makeText(this, R.string.press_again_to_exit, Toast.LENGTH_SHORT).show();
            new Handler().postDelayed(() -> doubleBackToExitPressedOnce = false, 2000);
        }

        if(fragmentManager.getBackStackEntryCount() > 0) {
            fragmentManager.popBackStack();
        }
        Log.d("TAG", "onBackPressed: " + fragmentManager.getBackStackEntryCount());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(connectivityBroadcast);
        binding = null;
    }

    private void initListener() {
        binding.navView.getMenu().findItem(R.id.nav_movies).setChecked(true);
        binding.navView.setOnItemSelectedListener(item -> {
            itemId =  item.getItemId();
            return menuItem(true);
        });

        mConnectivitySnackBar = Snackbar.make(binding.layoutContentMain.mainActivityFragmentContainer,
                R.string.no_network, Snackbar.LENGTH_INDEFINITE);
        fragmentManager = getSupportFragmentManager();
        moviesFragment = new MoviesFragment();
        tvShowsFragment = new TVShowsFragment();
        favouritesFragment = new FavouritesFragment();
        setFragment(moviesFragment, false);

        connectivityBroadcast = new ConnectivityBroadcastReceiver(() -> {
            binding.layoutContentMain.mainActivityFragmentContainer.setVisibility(View.VISIBLE);
            mConnectivitySnackBar.dismiss();
            reloadData();
        });
        IntentFilter intentFilter = new IntentFilter("android.net.conn.CONNECTIVITY_CHANGE");
        registerReceiver(connectivityBroadcast, intentFilter);
    }

    @SuppressLint("NonConstantResourceId")
    public boolean menuItem(boolean menuItem) {

        switch (itemId) {
            case R.id.nav_movies:
                checkReloadFragment(moviesFragment, R.id.nav_movies);
                return true;
            case R.id.nav_tv_shows:
                checkReloadFragment(tvShowsFragment, R.id.nav_tv_shows);
                return true;
            case R.id.nav_favorites:
                checkReloadFragment(favouritesFragment, R.id.nav_favorites);
                return true;
            case R.id.nav_about:
                Intent intent = new Intent(MainActivity.this, AboutActivity.class);
                startActivity(intent);
                return false;
        }
        return menuItem;
    }

    public void setFragment(Fragment fragment, Boolean addToBackStack) {
        this.fragment = fragment;
        fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.main_activity_fragment_container, fragment, "myFragmentTag");
        if(addToBackStack) {
            fragmentTransaction.addToBackStack(null);
            hideBtnNavigationView();
        }
        fragmentTransaction.commit();
        checkInternet();
    }

    private void checkInternet() {
        if(!NetworkConnection.isConnected(this)) {
            binding.layoutContentMain.mainActivityFragmentContainer.setVisibility(View.GONE);
            mConnectivitySnackBar.show();
            if(fragment instanceof FavouritesFragment) {
                binding.layoutContentMain.mainActivityFragmentContainer.setVisibility(View.VISIBLE);
                mConnectivitySnackBar.dismiss();
            }
        }
        else if(NetworkConnection.isConnected(this))
            mConnectivitySnackBar.dismiss();
    }

    private void reloadData() {
        if(fragment instanceof MoviesFragment)
            ((MoviesFragment) fragment).reLoadData();
        else if(fragment instanceof TVShowsFragment)
            ((TVShowsFragment) fragment).reLoadData();
        else if(fragment instanceof MovieDetailFragment)
            ((MovieDetailFragment) fragment).reLoadData();
        else if(fragment instanceof TVShowDetailFragment)
            ((TVShowDetailFragment) fragment).reLoadData();
        else if(fragment instanceof PersonDetailFragment)
            ((PersonDetailFragment) fragment).reLoadData();
        else if(fragment instanceof ViewAllMoviesFragment)
            ((ViewAllMoviesFragment) fragment).reLoadData();
        else if(fragment instanceof ViewAllTVShowsFragment)
            ((ViewAllTVShowsFragment) fragment).reLoadData();
        else if(fragment instanceof ViewShowMenu)
            ((ViewShowMenu) fragment).reLoadData();
    }

    private void checkReloadFragment(Fragment fragment, int itemId) {
        if(!NetworkConnection.isConnected(this)) setFragment(fragment, false);
        else {
            if(checkItemId != itemId)
                iClick = Constants.clickFirst;
            if(checkItemId == itemId && iClick == Constants.clickSecond) {
                if(((System.currentTimeMillis() - time) <= Constants.timeEnough)) {
                    if(fragment instanceof MoviesFragment) {
                        moviesFragment = new MoviesFragment();
                        fragment = moviesFragment;
                    }
                    if(fragment instanceof TVShowsFragment) {
                        tvShowsFragment = new TVShowsFragment();
                        fragment = tvShowsFragment;
                    }
                    if(fragment instanceof FavouritesFragment) {
                        favouritesFragment = new FavouritesFragment();
                        fragment = favouritesFragment;
                    }
                }
                iClick = Constants.clickFirst;
                time = Constants.timeFirst;
            }
            ++iClick;
            if (iClick == Constants.checkClick) {
                setFragment(fragment, false);
            }
            if (iClick == Constants.clickSecond)
                time = System.currentTimeMillis();
            checkItemId = itemId;
        }
    }

    private void hideBtnNavigationView() {
        binding.navView.setVisibility(View.GONE);
    }

    public void showBtnNavigationView() {
        binding.navView.setVisibility(View.VISIBLE);
    }

}
