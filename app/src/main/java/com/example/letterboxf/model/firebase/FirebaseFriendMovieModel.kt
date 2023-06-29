package com.example.letterboxf.model.firebase

data class FirebaseFriendMovieModel(
    val id : String = "13",
    val name : String = "Not defined",
    val url : String = "brokenLink",
    val rating : Double = 0.0,
    val friendName : String = "",
    val isLiked : Boolean = false)