package com.maluta.popularmovies.utils;

import com.maluta.popularmovies.model.MoviesResponse;
import com.maluta.popularmovies.model.ReviewsResponse;
import com.maluta.popularmovies.model.TrailersResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by admin on 6/11/2018.
 */

public interface MovieClient {

    @GET("movie/popular")
    Call<MoviesResponse> getPopularMovies (@Query("api_key") String apiKey );

    @GET("movie/top_rated")
    Call<MoviesResponse> getTopRatedMovies(@Query("api_key") String apiKey);

    @GET("movie/{id}/videos")
    Call<TrailersResponse> getTrailers(@Path("id") Integer id, @Query("api_key") String apiKey);

    @GET("movie/{id}/reviews")
    Call<ReviewsResponse> getReviews(@Path("id") Integer id, @Query("api_key") String apiKey);
}
