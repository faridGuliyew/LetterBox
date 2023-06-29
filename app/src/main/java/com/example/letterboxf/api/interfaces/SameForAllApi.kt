package com.example.letterboxf.api.interfaces

import com.example.letterboxf.model.apiModels.movies.MovieDetailsResponse
import com.example.letterboxf.model.apiModels.movies.credits.MovieCreditsResponse
import com.example.letterboxf.model.apiModels.people.detail.PersonDetailResponse
import com.example.letterboxf.model.apiModels.people.movies.PersonMoviesResponse
import com.example.letterboxf.model.apiModels.reviews.ReviewsResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface SameForAllApi {

    @GET("movie/{id}")
    suspend fun getMovieDetails(@Path("id") movieId: String, @Query("api_key") apiKey : String) : Response<MovieDetailsResponse>

    @GET("movie/{id}/credits")
    suspend fun getMovieCredits(@Path("id") movieId: String, @Query("api_key") apiKey: String) : Response<MovieCreditsResponse>

    @GET("movie/{movie_id}/reviews")
    suspend fun getReviews(@Path("movie_id") movieId : String, @Query("api_key") apiKey : String) : Response<ReviewsResponse>

    @GET("person/{id}")
    suspend fun getActorDetails(@Path("id") actorId : String, @Query("api_key") apiKey: String) : Response<PersonDetailResponse>

    @GET("person/{id}/combined_credits")
    suspend fun getActorCredits(@Path("id") actorId : String, @Query("api_key") apiKey: String) : Response<PersonMoviesResponse>
}