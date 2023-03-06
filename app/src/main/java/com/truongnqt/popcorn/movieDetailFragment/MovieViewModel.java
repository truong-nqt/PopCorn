package com.truongnqt.popcorn.movieDetailFragment;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.truongnqt.popcorn.network.ApiClient;
import com.truongnqt.popcorn.network.MovieService;
import com.truongnqt.popcorn.network.movies.Movie;
import com.truongnqt.popcorn.network.movies.MovieCreditsResponse;
import com.truongnqt.popcorn.network.movies.SimilarMoviesResponse;
import com.truongnqt.popcorn.network.videos.VideosResponse;
import com.truongnqt.popcorn.utils.Constants;

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
    private final MutableLiveData<Movie> movieDetailsLive = new MutableLiveData<>();
    private final MutableLiveData<VideosResponse> movieVideoLive = new MutableLiveData<>();
    private final MutableLiveData<MovieCreditsResponse> movieCreditsLive = new MutableLiveData<>();
    private final MutableLiveData<SimilarMoviesResponse> movieSimilarLive = new MutableLiveData<>();

    {
      this.movieService = ApiClient.getClient().create(MovieService.class);
      this.compositeDisposable = new CompositeDisposable();
    }

    public LiveData<Boolean> getLoadingLive() {
        return loadingLive;
    }

    public LiveData<Movie> getMovieDetailsLive() {
        return movieDetailsLive;
    }
    public LiveData<VideosResponse> getMovieVideoLive() {
        return movieVideoLive;
    }
    public LiveData<MovieCreditsResponse> getMovieCreditsLive() {
        return movieCreditsLive;
    }
    public LiveData<SimilarMoviesResponse> getMovieSimilarLive() {
        return movieSimilarLive;
    }

    @Override
    protected void onCleared() {
        compositeDisposable.dispose();
        super.onCleared();
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    void getMovieDetails(int id) {
        movieService.getMovieDetails(id,
                        apiKey)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<Movie>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        compositeDisposable.add(d);
                        loadingLive.setValue(true);
                    }

                    @Override
                    public void onSuccess(@NonNull Movie movie) {
                        movieDetailsLive.setValue(movie);
                        loadingLive.setValue(false);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        loadingLive.setValue(false);
                        Log.d(Constants.TAG, "onError: " + e);
                    }
                });
    }

    void getMovieVideo(int id) {
        movieService.getMovieVideos(id,
                        apiKey)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<VideosResponse>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        compositeDisposable.add(d);
                        loadingLive.setValue(false);
                    }

                    @Override
                    public void onSuccess(@NonNull VideosResponse videosResponse) {
                        movieVideoLive.setValue(videosResponse);
                        loadingLive.setValue(false);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        loadingLive.setValue(false);
                        Log.d(Constants.TAG, "onError: " + e);
                    }
                });
    }

    void getMovieCredits(int id) {
        movieService.getMovieCredits(id,
                        apiKey)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<MovieCreditsResponse>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        compositeDisposable.add(d);
                        loadingLive.setValue(true);
                    }

                    @Override
                    public void onSuccess(@NonNull MovieCreditsResponse movieCreditsResponse) {
                        movieCreditsLive.setValue(movieCreditsResponse);
                        loadingLive.setValue(false);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        loadingLive.setValue(false);
                        Log.d(Constants.TAG, "onError: " + e);
                    }
                });
    }

    void getMovieSimilar(int id, int page) {
        movieService.getSimilarMovies(id,
                        apiKey,
                        page)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<SimilarMoviesResponse>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        compositeDisposable.add(d);
                        loadingLive.setValue(false);
                    }

                    @Override
                    public void onSuccess(@NonNull SimilarMoviesResponse similarMoviesResponse) {
                        movieSimilarLive.setValue(similarMoviesResponse);
                        loadingLive.setValue(false);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        loadingLive.setValue(false);
                        Log.d(Constants.TAG, "onError: " + e);
                    }
                });
    }
}
