package com.truongnqt.popcorn.network;

import com.truongnqt.popcorn.network.search.SearchResponse;

import io.reactivex.rxjava3.core.Single;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface SearchService {

    @GET("search/multi")
    Single<SearchResponse> searchMulti(@Query("api_key") String apiKey, @Query("page") Integer page, @Query("query") String query);
}
