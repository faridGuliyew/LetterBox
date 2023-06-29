package com.example.letterboxf.api.interfaces

import com.example.letterboxf.model.apiModels.PopularMoviesResponse
import com.example.letterboxf.model.apiModels.search.SearchPersonResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface SearchApi {

    @GET("search/movie")
    suspend fun searchMovie(@Query("api_key") apiKey : String, @Query("query") query : String) : Response<PopularMoviesResponse>

    @GET("search/person")
    suspend fun searchPerson(@Query("api_key") apiKey : String, @Query("query") query : String) : Response<SearchPersonResponse>
}