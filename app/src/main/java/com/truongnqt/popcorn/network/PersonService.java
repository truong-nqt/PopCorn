package com.truongnqt.popcorn.network;

import com.truongnqt.popcorn.network.movies.MovieCastsOfPersonResponse;
import com.truongnqt.popcorn.network.people.Person;
import com.truongnqt.popcorn.network.tvshows.TVCastsOfPersonResponse;

import io.reactivex.rxjava3.core.Single;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface PersonService {

    @GET("person/{id}")
    Single<Person> getPersonDetails(@Path("id") Integer personId,
                                    @Query("api_key") String apiKey);

    @GET("person/{id}/movie_credits")
    Single<MovieCastsOfPersonResponse> getMovieCastsOfPerson(@Path("id") Integer personId,
                                                             @Query("api_key") String apiKey);

    @GET("person/{id}/tv_credits")
    Single<TVCastsOfPersonResponse> getTVCastsOfPerson(@Path("id") Integer personId,
                                                       @Query("api_key") String apiKey);
}
