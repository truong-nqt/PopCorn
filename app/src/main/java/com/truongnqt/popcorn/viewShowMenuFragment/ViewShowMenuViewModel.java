package com.truongnqt.popcorn.viewShowMenuFragment;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.truongnqt.popcorn.network.ApiClient;
import com.truongnqt.popcorn.network.MovieService;
import com.truongnqt.popcorn.network.TVShowService;
import com.truongnqt.popcorn.network.movies.NowShowingMoviesResponse;
import com.truongnqt.popcorn.network.movies.TopRatedMoviesResponse;
import com.truongnqt.popcorn.network.movies.TrendingMoviesResponse;
import com.truongnqt.popcorn.network.tvshows.TopRatedTVShowsResponse;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.SingleObserver;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class ViewShowMenuViewModel extends ViewModel {
    private final CompositeDisposable compositeDisposable;
    private final MovieService movieService;
    private final TVShowService tvService;

    private String apiKey = "";

    private final MutableLiveData<Boolean> loadingLive = new MutableLiveData<>();
    private final MutableLiveData<NowShowingMoviesResponse> nowShowingMoviesLive = new MutableLiveData<>();
    private final MutableLiveData<TopRatedTVShowsResponse> topRatedTVShowsLive = new MutableLiveData<>();
    private final MutableLiveData<TopRatedMoviesResponse> topRatedMoviesLive = new MutableLiveData<>();
    private final MutableLiveData<TrendingMoviesResponse> trendingMoviesLive = new MutableLiveData<>();

    {
        movieService = ApiClient.getClient().create(MovieService.class);
        tvService = ApiClient.getClient().create(TVShowService.class);
        compositeDisposable = new CompositeDisposable();
    }

    public LiveData<Boolean> getLoadingLive() {
        return loadingLive;
    }

    public LiveData<NowShowingMoviesResponse> getNowShowingMoviesLive() {
        return nowShowingMoviesLive;
    }
    public LiveData<TopRatedTVShowsResponse> getTopRatedTVShowsLive() {
        return topRatedTVShowsLive;
    }
    public LiveData<TopRatedMoviesResponse> getTopRatedMoviesLive() {
        return topRatedMoviesLive;
    }
    public LiveData<TrendingMoviesResponse> getTrendingMoviesLive() {
        return trendingMoviesLive;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    @Override
    protected void onCleared() {
        compositeDisposable.dispose();
        super.onCleared();
    }

    public void getNowShowing(int page) {
        movieService.getNowShowingMovies(apiKey,
                        page,
                        "")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<NowShowingMoviesResponse>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        compositeDisposable.add(d);
                        loadingLive.setValue(true);
                    }

                    @Override
                    public void onSuccess(@NonNull NowShowingMoviesResponse nowShowingMoviesResponse) {
                        nowShowingMoviesLive.setValue(nowShowingMoviesResponse);
                        loadingLive.setValue(false);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        loadingLive.setValue(false);
                        Log.d("MovieViewModel", "onError: " + e);
                    }
                });
    }

    void getTopRatedTVShows(int page) {
        tvService.getTopRatedTVShows(apiKey, page)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<TopRatedTVShowsResponse>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        compositeDisposable.add(d);
                        loadingLive.setValue(true);
                    }

                    @Override
                    public void onSuccess(@NonNull TopRatedTVShowsResponse topRatedTVShowsResponse) {
                        topRatedTVShowsLive.setValue(topRatedTVShowsResponse);
                        loadingLive.setValue(false);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        Log.d("ViewShowMenuViewModel", "onError: " + e);
                        loadingLive.setValue(false);
                    }
                });
    }

    void getTopRatedMovies(int page) {
        movieService.getTopRatedMovies(apiKey, page, "")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<TopRatedMoviesResponse>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        compositeDisposable.add(d);
                        loadingLive.setValue(true);
                    }

                    @Override
                    public void onSuccess(@NonNull TopRatedMoviesResponse topRatedMoviesResponse) {
                        topRatedMoviesLive.setValue(topRatedMoviesResponse);
                        loadingLive.setValue(false);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        Log.d("ViewShowMenuViewModel", "onError: " + e);
                        loadingLive.setValue(false);
                    }
                });
    }

    void getTrending(String movie, String week) {
        movieService.getTrendingMovies(movie, week, apiKey)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<TrendingMoviesResponse>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        compositeDisposable.add(d);
                        loadingLive.setValue(true);
                    }

                    @Override
                    public void onSuccess(@NonNull TrendingMoviesResponse trendingMoviesResponse) {
                        trendingMoviesLive.setValue(trendingMoviesResponse);
                        loadingLive.setValue(false);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        Log.d("ViewShowMenuViewModel", "onError: " + e);
                        loadingLive.setValue(false);
                    }
                });
    }
}
