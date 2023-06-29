package com.example.letterboxf.model.apiModels.people.popularPeople


import com.google.gson.annotations.SerializedName

data class PopularPeopleResponse(
    @SerializedName("page")
    val page: Int,
    @SerializedName("results")
    val results: List<PopularPeopleResult>,
    @SerializedName("total_pages")
    val totalPages: Int,
    @SerializedName("total_results")
    val totalResults: Int
)