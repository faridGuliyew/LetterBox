package com.example.letterboxf.api.interfaces

import com.example.letterboxf.model.apiModels.PopularMoviesResponse
import com.example.letterboxf.model.apiModels.people.popularPeople.PopularPeopleResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface PopularApi {

    @GET("movie/popular")
    suspend fun getPopularMovies(@Query("api_key") apiKey : String, @Query("page") page : Int) : Response<PopularMoviesResponse>

    @GET("person/popular")
    suspend fun getPopularPeople (@Query("api_key") apiKey : String, @Query("page") page : Int) : Response<PopularPeopleResponse>
}