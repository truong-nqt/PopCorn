package com.truongnqt.popcorn.tvShowsFragment;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.truongnqt.popcorn.network.ApiClient;
import com.truongnqt.popcorn.network.TVShowService;
import com.truongnqt.popcorn.network.tvshows.AiringTodayTVShowsResponse;
import com.truongnqt.popcorn.network.tvshows.GenresList;
import com.truongnqt.popcorn.network.tvshows.OnTheAirTVShowsResponse;
import com.truongnqt.popcorn.network.tvshows.PopularTVShowsResponse;
import com.truongnqt.popcorn.network.tvshows.TopRatedTVShowsResponse;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.SingleObserver;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class TVShowsViewModel extends ViewModel {
    private final CompositeDisposable compositeDisposable;
    private final TVShowService service;

    private final MutableLiveData<Boolean> loadingLive = new MutableLiveData<>();

    private final MutableLiveData<AiringTodayTVShowsResponse> airingTodayTVShowsLive = new MutableLiveData<>();
    private final MutableLiveData<OnTheAirTVShowsResponse> onTheAirTVShowsLive = new MutableLiveData<>();
    private final MutableLiveData<PopularTVShowsResponse> popularTVShowsLive = new MutableLiveData<>();
    private final MutableLiveData<TopRatedTVShowsResponse> topRatedTVShowsLive = new MutableLiveData<>();
    private final MutableLiveData<GenresList> genresListLive = new MutableLiveData<>();

    private String ApiKey = "";

    {
        compositeDisposable = new CompositeDisposable();
        service = ApiClient.getClient().create(TVShowService.class);
    }

    public void setApiKey(String Api) {
        this.ApiKey = Api;
    }

    public LiveData<AiringTodayTVShowsResponse> getAiringTodayTVShowsLive() {
        return airingTodayTVShowsLive;
    }

    public LiveData<OnTheAirTVShowsResponse> getOnTheAirTVShowsLive() {
        return onTheAirTVShowsLive;
    }

    public LiveData<PopularTVShowsResponse> getPopularTVShowsLive() {
        return popularTVShowsLive;
    }

    public LiveData<TopRatedTVShowsResponse> getTopRatedTVShowsLive() {
        return topRatedTVShowsLive;
    }

    public LiveData<GenresList> getGenresListLive() {
        return genresListLive;
    }

    public LiveData<Boolean> getLoadingLive() {
        return loadingLive;
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        compositeDisposable.dispose();
    }

    void getAiringTodayTVShows(int page) {
        service.getAiringTodayTVShows(ApiKey, page)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<AiringTodayTVShowsResponse>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        compositeDisposable.add(d);
                        loadingLive.setValue(true);
                    }

                    @Override
                    public void onSuccess(@NonNull AiringTodayTVShowsResponse airingTodayTVShowsResponse) {
                        airingTodayTVShowsLive.setValue(airingTodayTVShowsResponse);
                        loadingLive.setValue(false);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        Log.d("TVShowViewModel", "onError: " + e);
                        loadingLive.setValue(false);
                    }
                });
    }

    void getOnTheAirTVShows(int page) {
        service.getOnTheAirTVShows(ApiKey, page)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<OnTheAirTVShowsResponse>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        compositeDisposable.add(d);
                        loadingLive.setValue(true);
                    }

                    @Override
                    public void onSuccess(@NonNull OnTheAirTVShowsResponse onTheAirTVShowsResponse) {
                        onTheAirTVShowsLive.setValue(onTheAirTVShowsResponse);
                        loadingLive.setValue(false);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        Log.d("TVShowViewModel", "onError: " + e);
                        loadingLive.setValue(false);
                    }
                });
    }

    void getPopularTVShows(int page) {
        service.getPopularTVShows(ApiKey, page)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<PopularTVShowsResponse>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        compositeDisposable.add(d);
                        loadingLive.setValue(true);
                    }

                    @Override
                    public void onSuccess(@NonNull PopularTVShowsResponse popularTVShowsResponse) {
                        popularTVShowsLive.setValue(popularTVShowsResponse);
                        loadingLive.setValue(false);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        Log.d("TVShowViewModel", "onError: " + e);
                        loadingLive.setValue(false);
                    }
                });
    }

    void getTopRatedTVShows(int page) {
        service.getTopRatedTVShows(ApiKey, page)
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
                        Log.d("TVShowViewModel", "onError: " + e);
                        loadingLive.setValue(false);
                    }
                });
    }

    void getTVShowGenresList() {
        service.getTVShowGenresList(ApiKey)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<GenresList>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        compositeDisposable.add(d);
                        loadingLive.setValue(true);
                    }

                    @Override
                    public void onSuccess(@NonNull GenresList genresList) {
                        genresListLive.setValue(genresList);
                        loadingLive.setValue(false);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        Log.d("TVShowViewModel", "onError: " + e);
                        loadingLive.setValue(false);
                    }
                });
    }
}
