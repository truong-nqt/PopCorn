package com.truongnqt.popcorn.tvShowDetailFragment;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.truongnqt.popcorn.network.ApiClient;
import com.truongnqt.popcorn.network.TVShowService;
import com.truongnqt.popcorn.network.tvshows.SimilarTVShowsResponse;
import com.truongnqt.popcorn.network.tvshows.TVShow;
import com.truongnqt.popcorn.network.tvshows.TVShowCreditsResponse;
import com.truongnqt.popcorn.network.videos.VideosResponse;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.SingleObserver;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class TVShowViewModel extends ViewModel {
    private final CompositeDisposable compositeDisposable;
    private final TVShowService service;

    private final MutableLiveData<Boolean> loadingLive = new MutableLiveData<>();
    private final MutableLiveData<TVShow> tvShowLive = new MutableLiveData<>();
    private final MutableLiveData<VideosResponse> videosLive = new MutableLiveData<>();
    private final MutableLiveData<TVShowCreditsResponse> tvShowsCreditsLive = new MutableLiveData<>();
    private final MutableLiveData<SimilarTVShowsResponse> similarTVShowsLive = new MutableLiveData<>();

    private String ApiKey = "";

    {
        compositeDisposable = new CompositeDisposable();
        service = ApiClient.getClient().create(TVShowService.class);
    }

    public void setApiKey(String Api) {
        this.ApiKey = Api;
    }

    public LiveData<TVShow> getTVShowLive() {
        return tvShowLive;
    }

    public LiveData<Boolean> getLoadingLive() {
        return loadingLive;
    }

    public LiveData<VideosResponse> getVideosLive() {
        return videosLive;
    }

    public LiveData<TVShowCreditsResponse> getTvShowsCreditsLive() {
        return tvShowsCreditsLive;
    }

    public LiveData<SimilarTVShowsResponse> getSimilarTVShowsLive() {
        return similarTVShowsLive;
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        compositeDisposable.dispose();
    }

    void getTVShowDetails(int tvShowId) {
        service.getTVShowDetails(tvShowId, ApiKey)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<TVShow>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        compositeDisposable.add(d);
                        loadingLive.setValue(true);
                    }

                    @Override
                    public void onSuccess(@NonNull TVShow tvShow) {
                        tvShowLive.setValue(tvShow);
                        loadingLive.setValue(false);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        Log.d("TVShowViewModel", "onError: " + e);
                        loadingLive.setValue(false);
                    }
                });
    }

    void getTVShowVideos(int id) {
        service.getTVShowVideos(id, ApiKey)
                .observeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<VideosResponse>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        compositeDisposable.add(d);
                        loadingLive.setValue(true);
                    }

                    @Override
                    public void onSuccess(@NonNull VideosResponse videosResponse) {
                        videosLive.setValue(videosResponse);
                        loadingLive.setValue(false);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        loadingLive.setValue(false);
                        Log.d("TVShowViewModel", "onError: " + e);
                    }
                });
    }

    void getTVShowCredits(int id) {
        service.getTVShowCredits(id, ApiKey)
                .observeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<TVShowCreditsResponse>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        compositeDisposable.add(d);
                        loadingLive.setValue(true);
                    }

                    @Override
                    public void onSuccess(@NonNull TVShowCreditsResponse tvShowCreditsResponse) {
                        tvShowsCreditsLive.setValue(tvShowCreditsResponse);
                        loadingLive.setValue(false);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        Log.d("TVShowViewModel", "onError: " + e);
                        loadingLive.setValue(false);
                    }
                });
    }

    void getSimilarTVShows(int id) {
        service.getSimilarTVShows(id, ApiKey, 1)
                .observeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<SimilarTVShowsResponse>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        compositeDisposable.add(d);
                        loadingLive.setValue(true);
                    }

                    @Override
                    public void onSuccess(@NonNull SimilarTVShowsResponse similarTVShowsResponse) {
                        similarTVShowsLive.setValue(similarTVShowsResponse);
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
