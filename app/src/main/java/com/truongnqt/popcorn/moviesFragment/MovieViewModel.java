package com.truongnqt.popcorn.moviesFragment;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.truongnqt.popcorn.network.ApiClient;
import com.truongnqt.popcorn.network.MovieService;
import com.truongnqt.popcorn.network.movies.NowShowingMoviesResponse;
import com.truongnqt.popcorn.network.movies.PopularMoviesResponse;
import com.truongnqt.popcorn.network.movies.TopRatedMoviesResponse;
import com.truongnqt.popcorn.network.movies.UpcomingMoviesResponse;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.SingleObserver;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class MovieViewModel extends ViewModel {
    private final MovieService movieService;
    private final CompositeDisposable compositeDisposable;

    private String apiKey = "";

    private final MutableLiveData<Boolean> loadingLive = new MutableLiveData<>();

    private final MutableLiveData<NowShowingMoviesResponse> nowShowingLive = new MutableLiveData<>();
    private final MutableLiveData<PopularMoviesResponse> popularLive = new MutableLiveData<>();
    private final MutableLiveData<UpcomingMoviesResponse> upcomingLive = new MutableLiveData<>();
    private final MutableLiveData<TopRatedMoviesResponse> topRatedLive = new MutableLiveData<>();

    {
        movieService = ApiClient.getClient().create(MovieService.class);
        compositeDisposable = new CompositeDisposable();
    }

    public LiveData<Boolean> getLoadingLive() {
        return loadingLive;
    }


    public LiveData<NowShowingMoviesResponse> getNowShowingLive() {
        return nowShowingLive;
    }

    public LiveData<PopularMoviesResponse> getPopularLive() {
        return popularLive;
    }

    public LiveData<UpcomingMoviesResponse> getUpcomingLive() {
        return upcomingLive;
    }

    public LiveData<TopRatedMoviesResponse> getTopRatedLive() {
        return topRatedLive;
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
                         nowShowingLive.setValue(nowShowingMoviesResponse);
                         loadingLive.setValue(false);
                     }

                     @Override
                     public void onError(@NonNull Throwable e) {
                         loadingLive.setValue(false);
                         Log.d("MovieViewModel", "onError: " + e);
                     }
                 });
    }

    public void getPopular(int page) {
        movieService.getPopularMovies(apiKey,
                        page,
                        "")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<PopularMoviesResponse>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        compositeDisposable.add(d);
                        loadingLive.setValue(true);
                    }

                    @Override
                    public void onSuccess(@NonNull PopularMoviesResponse popularMoviesResponse) {
                        popularLive.setValue(popularMoviesResponse);
                        loadingLive.setValue(false);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        loadingLive.setValue(false);
                        Log.d("MovieViewModel", "onError: " + e);
                    }
                });
    }

    public void getUpcoming(int page) {
        movieService.getUpcomingMovies(apiKey,
                        page,
                        "")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<UpcomingMoviesResponse>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        compositeDisposable.add(d);
                        loadingLive.setValue(true);
                    }

                    @Override
                    public void onSuccess(@NonNull UpcomingMoviesResponse upcomingMoviesResponse) {
                        upcomingLive.setValue(upcomingMoviesResponse);
                        loadingLive.setValue(false);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        loadingLive.setValue(false);
                        Log.d("MovieViewModel", "onError: " + e);
                    }
                });
    }

    public void getTopRated(int page) {
        movieService.getTopRatedMovies(apiKey,
                            page,
                            "")
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
                        topRatedLive.setValue(topRatedMoviesResponse);
                        loadingLive.setValue(false);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        loadingLive.setValue(false);
                        Log.d("MovieViewModel", "onError: " + e);
                    }
                });
    }
}
