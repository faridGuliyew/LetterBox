package com.example.letterboxf.api

import com.example.letterboxf.api.interfaces.PopularApi
import com.example.letterboxf.api.interfaces.SameForAllApi
import com.example.letterboxf.api.interfaces.SearchApi
import retrofit2.Retrofit
import retrofit2.create
import javax.inject.Inject

class ApiUtils @Inject constructor(client : Retrofit){

    val sameForAllApi : SameForAllApi = client.create(SameForAllApi::class.java)
    val popularApi : PopularApi = client.create(PopularApi::class.java)
    val searchApi : SearchApi = client.create(SearchApi::class.java)

}