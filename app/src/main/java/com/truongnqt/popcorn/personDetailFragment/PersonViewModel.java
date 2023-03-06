package com.truongnqt.popcorn.personDetailFragment;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.MutableLiveData;

import com.truongnqt.popcorn.network.ApiClient;
import com.truongnqt.popcorn.network.PersonService;
import com.truongnqt.popcorn.network.movies.MovieCastsOfPersonResponse;
import com.truongnqt.popcorn.network.people.Person;
import com.truongnqt.popcorn.network.tvshows.TVCastsOfPersonResponse;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.SingleObserver;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class PersonViewModel extends ViewModel {
    private final PersonService personService;
    private final CompositeDisposable compositeDisposable;

    private String apiKey = "";

    private final MutableLiveData<Boolean> loadingLive = new MutableLiveData<>();
    private final MutableLiveData<Person> personLive = new MutableLiveData<>();
    private final MutableLiveData<MovieCastsOfPersonResponse> moviePersonLive = new MutableLiveData<>();
    private final MutableLiveData<TVCastsOfPersonResponse> tvPersonLive = new MutableLiveData<>();

    {
        personService = ApiClient.getClient().create(PersonService.class);
        compositeDisposable = new CompositeDisposable();
    }

    public LiveData<Boolean> getLoadingLive() {
        return loadingLive;
    }

    public LiveData<Person> getPersonLive() {
        return personLive;
    }

    public LiveData<MovieCastsOfPersonResponse> getMovieCastsOfPersonLive() {
        return moviePersonLive;
    }

    public LiveData<TVCastsOfPersonResponse> getTVCastsOfPersonLive() {
        return tvPersonLive;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        compositeDisposable.dispose();
    }

    void getPerson(int id) {
        personService.getPersonDetails(id,
                        apiKey)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<Person>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        compositeDisposable.add(d);
                        loadingLive.setValue(true);
                    }

                    @Override
                    public void onSuccess(@NonNull Person person) {
                        personLive.setValue(person);
                        loadingLive.setValue(false);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        Log.d("PersonViewModel", "onError: " + e);
                        loadingLive.setValue(false);
                    }
                });
    }

    void getMovieCasts(int id) {
        personService.getMovieCastsOfPerson(id,
                        apiKey)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<MovieCastsOfPersonResponse>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        compositeDisposable.add(d);
                        loadingLive.setValue(true);
                    }

                    @Override
                    public void onSuccess(@NonNull MovieCastsOfPersonResponse movieCastsOfPersonResponse) {
                        moviePersonLive.setValue(movieCastsOfPersonResponse);
                        loadingLive.setValue(false);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        Log.d("PersonViewModel", "onError: " + e);
                        loadingLive.setValue(false);
                    }
                });
    }

    void getTVCasts(int id) {
        personService.getTVCastsOfPerson(id,
                        apiKey)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<TVCastsOfPersonResponse>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        compositeDisposable.add(d);
                        loadingLive.setValue(true);
                    }

                    @Override
                    public void onSuccess(@NonNull TVCastsOfPersonResponse tvCastsOfPersonResponse) {
                        tvPersonLive.setValue(tvCastsOfPersonResponse);
                        loadingLive.setValue(false);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        Log.d("PersonViewModel", "onError: " + e);
                        loadingLive.setValue(false);
                    }
                });
    }
}
