package com.example.letterboxf.model.apiModels.search


import com.google.gson.annotations.SerializedName

data class SearchPersonResponse(
    @SerializedName("page")
    val page: Int,
    @SerializedName("results")
    val searchPersonResults: List<SearchPersonResult>,
    @SerializedName("total_pages")
    val totalPages: Int,
    @SerializedName("total_results")
    val totalResults: Int
)