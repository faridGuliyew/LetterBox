package com.example.letterboxf.model.apiModels.people.movies


import com.google.gson.annotations.SerializedName

data class PersonMoviesResponse(
    @SerializedName("cast")
    val cast: List<Cast>,
    @SerializedName("crew")
    val crew: List<Crew>,
    @SerializedName("id")
    val id: Int
)