package com.truongnqt.popcorn.network;

import com.truongnqt.popcorn.network.tvshows.AiringTodayTVShowsResponse;
import com.truongnqt.popcorn.network.tvshows.OnTheAirTVShowsResponse;
import com.truongnqt.popcorn.network.tvshows.PopularTVShowsResponse;
import com.truongnqt.popcorn.network.tvshows.SimilarTVShowsResponse;
import com.truongnqt.popcorn.network.tvshows.TVShow;
import com.truongnqt.popcorn.network.tvshows.TVShowCreditsResponse;
import com.truongnqt.popcorn.network.tvshows.TopRatedTVShowsResponse;
import com.truongnqt.popcorn.network.videos.VideosResponse;

import io.reactivex.rxjava3.core.Single;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface TVShowService {

    @GET("tv/airing_today")
    Single<AiringTodayTVShowsResponse> getAiringTodayTVShows(@Query("api_key") String apiKey, @Query("page") Integer page);

    @GET("tv/on_the_air")
    Single<OnTheAirTVShowsResponse> getOnTheAirTVShows(@Query("api_key") String apiKey, @Query("page") Integer page);

    @GET("tv/popular")
    Single<PopularTVShowsResponse> getPopularTVShows(@Query("api_key") String apiKey, @Query("page") Integer page);

    @GET("tv/top_rated")
    Single<TopRatedTVShowsResponse> getTopRatedTVShows(@Query("api_key") String apiKey, @Query("page") Integer page);

    @GET("tv/{id}")
    Single<TVShow> getTVShowDetails(@Path("id") Integer tvShowId, @Query("api_key") String apiKey);

    @GET("tv/{id}/videos")
    Single<VideosResponse> getTVShowVideos(@Path("id") Integer movieId, @Query("api_key") String apiKey);

    @GET("tv/{id}/credits")
    Single<TVShowCreditsResponse> getTVShowCredits(@Path("id") Integer movieId, @Query("api_key") String apiKey);

    @GET("tv/{id}/similar")
    Single<SimilarTVShowsResponse> getSimilarTVShows(@Path("id") Integer movieId, @Query("api_key") String apiKey, @Query("page") Integer page);

    @GET("genre/tv/list")
    Single<com.truongnqt.popcorn.network.tvshows.GenresList> getTVShowGenresList(@Query("api_key") String apiKey);
}
