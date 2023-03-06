package com.truongnqt.popcorn.network;

import com.truongnqt.popcorn.network.movies.Movie;
import com.truongnqt.popcorn.network.movies.MovieCreditsResponse;
import com.truongnqt.popcorn.network.movies.NowShowingMoviesResponse;
import com.truongnqt.popcorn.network.movies.PopularMoviesResponse;
import com.truongnqt.popcorn.network.movies.SimilarMoviesResponse;
import com.truongnqt.popcorn.network.movies.TopRatedMoviesResponse;
import com.truongnqt.popcorn.network.movies.TrendingMoviesResponse;
import com.truongnqt.popcorn.network.movies.UpcomingMoviesResponse;
import com.truongnqt.popcorn.network.videos.VideosResponse;

import io.reactivex.rxjava3.core.Single;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface MovieService {

    @GET("movie/now_playing")
    Single<NowShowingMoviesResponse> getNowShowingMovies(@Query("api_key") String apiKey,
                                                         @Query("page") Integer page,
                                                         @Query("region") String region);

    @GET("movie/popular")
    Single<PopularMoviesResponse> getPopularMovies(@Query("api_key") String apiKey,
                                                   @Query("page") Integer page,
                                                   @Query("region") String region);

    @GET("movie/upcoming")
    Single<UpcomingMoviesResponse> getUpcomingMovies(@Query("api_key") String apiKey,
                                                     @Query("page") Integer page,
                                                     @Query("region") String region);

    @GET("movie/top_rated")
    Single<TopRatedMoviesResponse> getTopRatedMovies(@Query("api_key") String apiKey,
                                                     @Query("page") Integer page,
                                                     @Query("region") String region);

    @GET("movie/{id}")
    Single<Movie> getMovieDetails(@Path("id") Integer movieId,
                                  @Query("api_key") String apiKey);

    @GET("movie/{id}/videos")
    Single<VideosResponse> getMovieVideos(@Path("id") Integer movieId,
                                          @Query("api_key") String apiKey);

    @GET("movie/{id}/credits")
    Single<MovieCreditsResponse> getMovieCredits(@Path("id") Integer movieId,
                                                 @Query("api_key") String apiKey);

    @GET("movie/{id}/similar")
    Single<SimilarMoviesResponse> getSimilarMovies(@Path("id") Integer movieId,
                                                   @Query("api_key") String apiKey,
                                                   @Query("page") Integer page);

    @GET("genre/movie/list")
    Single<com.truongnqt.popcorn.network.movies.GenresList> getMovieGenresList(
            @Query("api_key") String apiKey);


    @GET("trending/{media_type}/{time_window}")
    Single<TrendingMoviesResponse> getTrendingMovies(@Path("media_type") String type,
                                                     @Path("time_window") String day,
                                                     @Query("api_key") String api);
}
