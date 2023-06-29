package com.example.letterboxf.model.apiModels.search


import com.google.gson.annotations.SerializedName

data class SearchPersonResult(
    @SerializedName("adult")
    val adult: Boolean,
    @SerializedName("gender")
    val gender: Int,
    @SerializedName("id")
    val id: String,
    @SerializedName("known_for")
    val knownFor: List<KnownFor>,
    @SerializedName("known_for_department")
    val knownForDepartment: String,
    @SerializedName("name")
    val name: String,
    @SerializedName("original_name")
    val originalName: String,
    @SerializedName("popularity")
    val popularity: Double,
    @SerializedName("profile_path")
    val profilePath: String
)