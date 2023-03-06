package com.truongnqt.popcorn.searchFragment;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.truongnqt.popcorn.network.ApiClient;
import com.truongnqt.popcorn.network.SearchService;
import com.truongnqt.popcorn.network.search.SearchResponse;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.SingleObserver;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class SearchViewModel extends ViewModel {

    private final CompositeDisposable compositeDisposable;
    private final SearchService service;

    private String apiKey = "";

    private final MutableLiveData<Boolean> loadingLive = new MutableLiveData<>();
    private final MutableLiveData<SearchResponse> searchResponseLive = new MutableLiveData<>();

    {
        compositeDisposable = new CompositeDisposable();
        service = ApiClient.getClient().create(SearchService.class);
    }

    public LiveData<Boolean> getLoadingLive() {
        return loadingLive;
    }

    public LiveData<SearchResponse> getSearchResponseLive() {
        return searchResponseLive;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    @Override
    protected void onCleared() {
        super.onCleared();
    }

    void getSearchMulti(int page, String query) {
        service.searchMulti(apiKey,
                    page,
                    query)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<SearchResponse>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        compositeDisposable.add(d);
                        loadingLive.setValue(true);
                    }

                    @Override
                    public void onSuccess(@NonNull SearchResponse searchResponse) {
                        searchResponseLive.setValue(searchResponse);
                        loadingLive.setValue(false);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        Log.d("SearchViewModel", "onError: " + e);
                        loadingLive.setValue(false);
                    }
                });
    }
}
